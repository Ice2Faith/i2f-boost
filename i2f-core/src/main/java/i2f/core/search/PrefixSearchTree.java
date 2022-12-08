package i2f.core.search;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 最简单的前缀索引树
 *
 * @param <T> 索引键类型
 * @param <D> 数据类型
 */
public class PrefixSearchTree<T, D> {
    private ConcurrentSkipListMap<T, PrefixSearchTree<T, D>> tree = new ConcurrentSkipListMap<>();

    private T node;
    private D data;

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    public void add(Iterator<T> iter, D data) {
        List<T> col = new LinkedList<>();
        PrefixSearchTree<T, D> node = this;
        while (iter.hasNext()) {
            T item = iter.next();
            col.add(item);
            if (!node.tree.containsKey(item)) {
                PrefixSearchTree<T, D> sub = new PrefixSearchTree<>();
                sub.node = item;
                node.tree.put(item, sub);
            }
            node = node.tree.get(item);
        }
        node.data = data;
    }

    public void remove(Iterator<T> iter) {
        PrefixSearchTree<T, D> node = this;
        while (iter.hasNext()) {
            T item = iter.next();
            node = node.tree.get(item);
            if (node == null) {
                return;
            }
        }
        node.clear();
    }

    public void clear() {
        this.tree.clear();
    }

    public PrefixSearchTree<T, D> tree(Iterator<T> iter) {
        PrefixSearchTree<T, D> node = this;
        while (iter.hasNext()) {
            T item = iter.next();
            node = node.tree.get(item);
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    public D find(Iterator<T> iter) {
        PrefixSearchTree<T, D> node = this;
        while (iter.hasNext()) {
            T item = iter.next();
            node = node.tree.get(item);
            if (node == null) {
                return null;
            }
        }
        return node.data;
    }

    public List<D> prefix(Iterator<T> iter) {
        PrefixSearchTree<T, D> node = this;
        while (iter.hasNext()) {
            T item = iter.next();
            node = node.tree.get(item);
            if (node == null) {
                return null;
            }
        }
        List<D> ret = new LinkedList<>();
        collect(node, ret);
        return ret;
    }

    public List<D> collect() {
        List<D> ret = new LinkedList<>();
        collect(this, ret);
        return ret;
    }

    public void collect(PrefixSearchTree<T, D> node, List<D> list) {
        if (node.data != null) {
            list.add(node.data);
        }
        for (Map.Entry<T, PrefixSearchTree<T, D>> item : node.tree.entrySet()) {
            collect(item.getValue(), list);
        }
    }

    public void printTree(PrintStream out) {
        printTreeNext(this, out, 0);
    }

    private void printTreeNext(PrefixSearchTree<T, D> tree, PrintStream out, int level) {
        String prefix = "";
        for (int i = 0; i < level; i++) {
            prefix += "|-";
        }
        out.println(prefix + tree.node);
        if (tree.data != null) {
            out.print(prefix + ">/ ");
            out.print(tree.data);
            out.println();
        }
        for (Map.Entry<T, PrefixSearchTree<T, D>> item : tree.tree.entrySet()) {
            printTreeNext(item.getValue(), out, level + 1);
        }
    }
}
