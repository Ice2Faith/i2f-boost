package i2f.workflow;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * @author Ice2Faith
 * @date 2024/3/22 15:55
 * @desc
 */
public class WorkFlow {
    public static final BiPredicate<Throwable,FlowNode> DEFAULT_EXCEPTION_HANDLER=(e,n)->{
        System.err.println(String.format("flow node(%s:%s) throw exception, %s : %s",n.getId(),n.getName(),
                e.getClass().getName(),e.getMessage()));
        return false;
    };

    protected ConcurrentMap<String,FlowNode> nodeMap=new ConcurrentHashMap<>();
    protected ConcurrentHashMap<String,Set<String>> nextMap=new ConcurrentHashMap<>();
    protected ExecutorService pool= ForkJoinPool.commonPool();
    protected Set<String> activeIds=new HashSet<>();
    protected BiPredicate<Throwable,FlowNode> exceptionHandler=DEFAULT_EXCEPTION_HANDLER;


    public static WorkFlow flow(){
        return new WorkFlow();
    }

    public WorkFlow pool(ExecutorService pool){
        this.pool=pool;
        return this;
    }

    public WorkFlow activeIds(Collection<String> activeIds){
        if(activeIds!=null){
            this.activeIds=new HashSet<>(activeIds);
        }
        return this;
    }

    public Set<String> findChildrenIds(Set<String> searchIds){
        Set<String> ret=new HashSet<>();
        findChildrenIdsNext(nextMap,searchIds,ret);
        return ret;
    }

    public Set<String> filterIds(Collection<String> rawIds){
        Set<String> ret=new HashSet<>();
        for (String rawId : rawIds) {
            if(nodeMap.containsKey(rawId)){
                ret.add(rawId);
            }
        }
        return ret;
    }

    public static void findChildrenIdsNext(Map<String, Set<String>> nextMap, Set<String> searchIds, Set<String> result) {
        if(nextMap==null || searchIds==null || result==null){
            return;
        }
        for (String searchId : searchIds) {
            if(nextMap.containsKey(searchId)){
                result.add(searchId);
                findChildrenIdsNext(nextMap,nextMap.get(searchId),result);
            }
        }
    }

    public WorkFlow add(String id, String name, FlowTask task, Collection<String> prevIds){
        FlowNode node=new FlowNode();
        node.setId(id);
        node.setName(name);
        node.setPrev(new HashSet<>());
        if(prevIds!=null){
            node.getPrev().addAll(prevIds);
        }
        node.setNext(new HashSet<>());
        node.setTask(task);

        node.setDone(new ConcurrentHashMap<>());
        node.setFlow(this);
        nodeMap.put(node.getId(),node);
        return this;
    }

    public WorkFlow done(){
        nextMap.clear();
        for (Map.Entry<String, FlowNode> entry : nodeMap.entrySet()) {
            FlowNode node = entry.getValue();
            Set<String> prev = node.getPrev();
            Set<String> tmp=new HashSet<>(prev);
            for (String id : tmp) {
                if(!nodeMap.containsKey(id)){
                    prev.remove(id);
                }
            }
            for (String id : prev) {
                if(!nextMap.containsKey(id)){
                    nextMap.put(id,new HashSet<>());
                }
                nextMap.get(id).add(entry.getKey());
            }
        }

        for (Map.Entry<String, FlowNode> entry : nodeMap.entrySet()) {
            Set<String> next = nextMap.get(entry.getKey());
            if(next!=null){
                entry.getValue().setNext(next);
            }
        }

        this.activeIds=filterIds(this.activeIds);

        return this;
    }

    public CountDownLatch run(){
        for (Map.Entry<String, FlowNode> entry : nodeMap.entrySet()) {
            entry.getValue().getDone().clear();
        }

        ConcurrentHashMap<String,FlowNode> runMap=new ConcurrentHashMap<>();

        Set<String> waitList=new HashSet<>();
        if(this.activeIds.isEmpty()){
            waitList.addAll(nodeMap.keySet());
            runMap.putAll(nodeMap);
        }else{
            waitList.addAll(findChildrenIds(this.activeIds));
            for (String id : waitList) {
                FlowNode node = nodeMap.get(id);
                FlowNode item=new FlowNode();
                item.setId(node.getId());
                item.setName(node.getName());
                item.setPrev(new HashSet<>(node.getPrev()));
                item.setNext(new HashSet<>(node.getNext()));
                item.setTask(node.getTask());
                item.setFlow(node.getFlow());
                item.setDone(new ConcurrentHashMap<>());
                runMap.put(item.getId(),item);
            }
            for (String id : this.activeIds) {
                runMap.get(id).setPrev(new HashSet<>());
            }
        }


        CountDownLatch latch=new CountDownLatch(waitList.size());

        Runnable monitorTask=()->{
            Set<String> removeIds=new HashSet<>();
            double sleepTs=1;
            while(!waitList.isEmpty()){
                removeIds.clear();
                for (String id : waitList) {
                    FlowNode node = runMap.get(id);
                    FlowTask task = node.getTask();
                    if(task.trigger(node)){
                        removeIds.add(id);
                        Runnable runTask=()->{
                            Throwable ex=null;
                            try{
                                task.run(node);
                            }catch(Throwable e){
                                ex=e;
                                boolean broke=false;
                                if(exceptionHandler!=null){
                                    broke=exceptionHandler.test(e,node);
                                }else{
                                    broke=DEFAULT_EXCEPTION_HANDLER.test(e,node);
                                }
                                if(!broke){
                                    synchronized (waitList){
                                        Set<String> subIds = findChildrenIds(new HashSet<>(Arrays.asList(id)));
                                        for (String failId : subIds) {
                                            waitList.remove(failId);
                                        }
                                    }
                                }
                            }finally {
                                for (String nextId : node.getNext()) {
                                    Map<String, Optional<Throwable>> done = runMap.get(nextId).getDone();
                                    if(!done.containsKey(id)){
                                        done.put(id,Optional.ofNullable(ex));
                                    }
                                }
                                latch.countDown();
                            }
                        };
                        if(pool!=null){
                            pool.submit(runTask);
                        }else{
                            runTask.run();
                        }
                    }
                }
                boolean isTrigger=false;
                for (String removeId : removeIds) {
                    waitList.remove(removeId);
                    isTrigger=true;
                }
                if(isTrigger){
                    sleepTs=1;
                    continue;
                }else{
                    sleepTs=sleepTs*1.1;
                    sleepTs=Math.max(sleepTs,1);
                    sleepTs=Math.min(sleepTs,3000);
                }
                try{
//                    System.out.println(String.format("monitor:%.2f",sleepTs));
                    Thread.sleep((int)sleepTs);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        if(pool!=null){
            pool.submit(monitorTask);
        }else{
            monitorTask.run();
        }

        return latch;
    }
}
