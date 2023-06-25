package i2f.core.serialize.xml.parser;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/6/13 13:55
 * @desc
 */
@Data
@NoArgsConstructor
public class XmlNode {
    private String name;
    private Map<String,String> attrs;
    private List<XmlNode> nodes;
    private String content;
    public XmlNode(String name){
        this.name=name;
    }
    public XmlNode attrs(Map<String, String> attrs){
        this.attrs=attrs;
        return this;
    }
    public XmlNode attr(String name,String val){
        if(attrs==null){
            attrs=new HashMap<>();
        }
        attrs.put(name,val);
        return this;
    }
    public XmlNode nodes(List<XmlNode> nodes){
        this.nodes=nodes;
        return this;
    }
    public XmlNode add(XmlNode node){
        if(nodes==null){
            nodes=new ArrayList<>();
        }
        nodes.add(node);
        return this;
    }
}
