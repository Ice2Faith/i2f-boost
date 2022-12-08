package i2f.core.search;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 最简单的前序索引树
 * 基于最常用的String类型实现
 */
public class StringSearchTree<D> {
    private ConcurrentSkipListMap<Character, StringSearchTree<D>> tree = new ConcurrentSkipListMap<>();

    private char node;
    private D data;

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    public void add(String str, D data) {
        StringSearchTree<D> node = this;
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
            if (!node.tree.containsKey(item)) {
                StringSearchTree<D> sub = new StringSearchTree<D>();
                sub.node = item;
                node.tree.put(item, sub);
            }
            node = node.tree.get(item);
        }
        node.data = data;
    }

    public void remove(String str) {
        StringSearchTree<D> node = this;
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
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

    public StringSearchTree<D> tree(String str) {
        StringSearchTree<D> node = this;
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
            node = node.tree.get(item);
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    public D find(String str) {
        StringSearchTree<D> node = this;
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
            node = node.tree.get(item);
            if (node == null) {
                return null;
            }
        }
        return node.data;
    }

    public List<D> prefix(String str) {
        StringSearchTree<D> node = this;
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
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

    public void collect(StringSearchTree<D> node, List<D> list) {
        if (node.data != null) {
            list.add(node.data);
        }
        for (Map.Entry<Character, StringSearchTree<D>> item : node.tree.entrySet()) {
            collect(item.getValue(), list);
        }
    }

    public void printTree(PrintStream out) {
        printTreeNext(this, out, 0);
    }

    private void printTreeNext(StringSearchTree<D> tree, PrintStream out, int level) {
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
        for (Map.Entry<Character, StringSearchTree<D>> item : tree.tree.entrySet()) {
            printTreeNext(item.getValue(), out, level + 1);
        }
    }
}
