package i2f.core.reflection.pkg.data;

import i2f.core.annotations.remark.Author;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/9/24
 */

@Author("i2f")
public class ClassMetaTreeNode {
    public ClassMetaTreeNode parent;
    public Map<String, ClassMetaTreeNode> children;
    public String path;
    public Map<String, ClassMetaData> data;
    public List<ClassMetaData> treeData;
    public ClassMetaTreeNode(String path){
        this.path=path;
    }

    public void asChild(ClassMetaTreeNode node){
        node.parent=this;
        if(this.children==null){
            this.children=new HashMap<>(32);
        }
        this.children.put(node.path,node);
    }
}