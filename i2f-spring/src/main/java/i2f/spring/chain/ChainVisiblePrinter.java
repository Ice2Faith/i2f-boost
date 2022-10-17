package i2f.spring.chain;

import i2f.spring.context.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/10/17 11:30
 * @desc 打印chain链结构
 */
public class ChainVisiblePrinter {
    public static String getChainTreeAsString() {
        return getChainTreeAsString(null);
    }

    public static <T extends IChainResolver> String getChainTreeAsString(Class<T> rootClass) {
        ApplicationContext context = SpringUtil.getApplicationContext();
        List<TreeNode<Class>> tree = getChainTree(context, rootClass);
        StringBuilder sb = new StringBuilder();
        getChainTreeAsString(sb, tree);
        return sb.toString();
    }

    public static void getChainTreeAsString(StringBuilder sb, List<TreeNode<Class>> tree) {
        if (tree == null) {
            return;
        }
        for (TreeNode<Class> node : tree) {
            sb.append("-");
            for (int i = 0; i < node.level; i++) {
                sb.append("|---");
            }
            sb.append(node.data == null ? "*" : node.data.getSimpleName());
            sb.append("\n");
            getChainTreeAsString(sb, node.children);
        }
    }

    public static class TreeNode<T> {
        public T data;
        public int level;
        public List<TreeNode<Class>> children;
    }

    public static List<TreeNode<Class>> getChainTree(ApplicationContext context) {
        return getChainTree(context, null);
    }

    public static <T extends IChainResolver> List<TreeNode<Class>> getChainTree(ApplicationContext context, Class<T> rootClass) {
        Map<String, IChainResolver> beans = context.getBeansOfType(IChainResolver.class);
        TreeNode<Class> node = new TreeNode<>();
        node.data = rootClass;
        node.level = 0;
        node.children = getChainTreeNext(node.data, node.level + 1, beans);

        List<TreeNode<Class>> children = new ArrayList<>();
        children.add(node);
        return children;
    }

    private static List<TreeNode<Class>> getChainTreeNext(Class<? extends IChainResolver> root, int level, Map<String, IChainResolver> beans) {
        List<TreeNode<Class>> ret = new ArrayList<>();
        for (Map.Entry<String, IChainResolver> item : beans.entrySet()) {
            IChainResolver val = item.getValue();
            Set<Class<? extends IChainResolver>> attach = val.attach();
            boolean isTarget = false;
            if (attach == null || attach.isEmpty()) {
                if (root == null) {
                    isTarget = true;
                }
            } else {
                for (Class<? extends IChainResolver> clazz : attach) {
                    if (clazz.equals(root)) {
                        isTarget = true;
                        break;
                    }
                }
            }
            if (isTarget) {
                TreeNode<Class> node = new TreeNode<>();
                node.data = val.getClass();
                node.level = level;
                node.children = getChainTreeNext(node.data, node.level + 1, beans);
                ret.add(node);
            }
        }
        return ret;
    }

}
