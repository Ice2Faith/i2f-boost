package i2f.workflow;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ice2Faith
 * @date 2024/3/22 15:56
 * @desc
 */
public class FlowNode {
    protected String id;
    protected String name;
    protected Set<String> prev;
    protected Set<String> next;
    protected FlowTask task;

    protected ConcurrentHashMap<String, Optional<Throwable>> done;
    protected WorkFlow flow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getPrev() {
        return prev;
    }

    public void setPrev(Set<String> prev) {
        this.prev = prev;
    }

    public Set<String> getNext() {
        return next;
    }

    public void setNext(Set<String> next) {
        this.next = next;
    }

    public FlowTask getTask() {
        return task;
    }

    public void setTask(FlowTask task) {
        this.task = task;
    }

    public ConcurrentHashMap<String, Optional<Throwable>> getDone() {
        return done;
    }

    public void setDone(ConcurrentHashMap<String,  Optional<Throwable>> done) {
        this.done = done;
    }

    public WorkFlow getFlow() {
        return flow;
    }

    public void setFlow(WorkFlow flow) {
        this.flow = flow;
    }

}
