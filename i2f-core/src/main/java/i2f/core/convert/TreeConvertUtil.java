package i2f.core.convert;

import i2f.core.annotations.remark.Author;

import java.util.*;

/**
 * @author ltb
 * @date 2022/3/19 15:22
 * @desc
 */
@Author("i2f")
public class TreeConvertUtil {
    /**
     * 适用于数据不能一次性获取，需要多次获取，典型的就是获取一棵树中的某棵子树
     * @param list
     * @param provider
     * @param <T>
     * @return
     */
    public static <T extends ITreeNode> List<T> list2Tree(List<T> list,INextLevelDataProvider<T> provider){
        List<T> flat=list2TreeFlat(list,provider);
        return list2Tree(flat);
    }
    public static <T extends ITreeNode> List<T> list2TreeFlat(List<T> list,INextLevelDataProvider<T> provider){
        List<T> ret=new LinkedList<>();
        if(list==null || list.isEmpty()){
            return ret;
        }
        ret.addAll(list);
        List<T> next= provider.getNextLevel(list);
        if(next!=null && next.size()>0) {
            ret.addAll(next);
            list2TreeFlat(next, provider);
        }
        return ret;
    }

    /**
     * 适用于数据能一次性获取，典型的就是获取整棵树
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends ITreeNode> List<T> list2Tree(List<T> list){
        int size= list.size();
        List<T> root=new ArrayList<>(size);
        List<T> children=new ArrayList<>(size);
        for(T item : list){
            boolean isRoot=true;
            for(T mit : list){
                if (item.isMyParent(mit)) {
                    isRoot=false;
                    break;
                }
            }
            if(isRoot){
                root.add(item);
            }else{
                children.add(item);
            }
        }


        if(children.size()>0){
            list2TreeNext(root,children);
        }
        return root;
    }

    private static<T extends ITreeNode> void list2TreeNext(List<T> root, List<T> children){
        for(T item : root){
            List<T> curRoot=new ArrayList<>(root.size());
            List<T> curChildren=new ArrayList<>(children.size());
            for(T mit : children){
                if(item.isMyChild(mit)){
                    item.asMyChild(mit);
                    curRoot.add(mit);
                }else{
                    curChildren.add(mit);
                }
            }
            if(curChildren.size()>0) {
                list2TreeNext(curRoot, curChildren);
            }
        }
    }

    public static<T extends IChildren> List<T> tree2List(Collection<T> tree){
        List<T> ret=new ArrayList<>();
        int i=0;
        Iterator<T> it=tree.iterator();
        while(it.hasNext()){
            ret.add(it.next());
        }
        while(true){
            if(i==ret.size()){
                break;
            }
            T item=ret.get(i);
            Collection<T> col=item.getChildren();
            if(col==null){
                i++;
                continue;
            }
            Iterator<T> nit=tree.iterator();
            while(nit.hasNext()){
                ret.add(nit.next());
            }
            i++;
        }
        return ret;
    }
}
