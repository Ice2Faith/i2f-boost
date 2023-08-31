package i2f.core.serialize.str.xml.impl.parser;

import i2f.core.generate.RegexGenerator;
import i2f.core.match.regex.data.RegexMatchItem;
import i2f.core.type.str.Strings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/6/13 13:53
 * @desc
 */
public class XmlParser {
    public static void main(String[] args){
        String str="<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
                "<Painter>\n" +
                "  <Edges>\n" +
                "    <Line id=\"e10001\" formId=\"s1000\" toId=\"s1001\" text=\"\" />\n" +
                "    <Line id=\"e10002\" formId=\"s1000\" toId=\"s1002\" text=\"\" />\n" +
                "  </Edges>\n" +
                "  <Shapes>\n" +
                "    <Rectangle  id=\"s1000\" posX=\"10\" posY=\"10\" width=\"120\" height=\"80\" text=\"考试\" />\n" +
                "    <Ellipse    id=\"s1001\" posX=\"20\" posY=\"20\" width=\"120\" height=\"80\" text=\"ID\" />\n" +
                "  </Shapes>\n" +
                "</Painter>";
        parse(str);
    }
    public static XmlNode parse(String str){
        XmlNode root=new XmlNode();
        str=trimComments(str);
        parseNext(str,root);
        return root;
    }
    public static String trimComments(String str){
        StringBuilder builder=new StringBuilder();
        int i=0;
        while(i<str.length()){
            if(str.charAt(i)=='<'){
                if(i+4<str.length()){
                    String pstr = str.substring(i,i+4);
                    if(pstr.equals("<!--")){
                        i+=4;
                        while(i<str.length()){
                            if(str.charAt(i)=='-'){
                                if(i+3<str.length()){
                                    String nstr = str.substring(i,i+3);
                                    if(nstr.equals("-->")){
                                        i+=nstr.length();
                                        break;
                                    }else{
                                        i+=nstr.length();
                                    }
                                }else{
                                    i++;
                                }
                            }else{
                                i++;
                            }
                        }
                    }else{
                        builder.append(pstr);
                        i+=pstr.length();
                    }
                }else{
                    builder.append(str.charAt(i));
                    i++;
                }
            }else{
                builder.append(str.charAt(i));
                i++;
            }
        }
        return builder.toString();
    }

    /**
     * 用来匹配一个key等于一个value的正则表达式
     * 例如：
     * aaa="bbb"
     * _ab2="ccc-+/*+..*\""
     */
    public static final String REG_KEY_EQUAL_VALUE= RegexGenerator.DECLARE_NAME_REGEX+"\\s*=\\s*"+RegexGenerator.QUOTE_STRING_REGEX;
    public static void parseNext(String str,XmlNode root){
        int offset=0;
        while(offset<str.length()){
            XmlCtx ctx=readOneTag(str,offset);
            if(ctx.getName()==null && ( ctx.getXml()==null || "".equals(ctx.getXml().trim()))){

            }else{
                Map<String,String> attrs=null;
                String attrLine = ctx.getAttr();
                if(attrLine!=null && !"".equals(attrLine)){
                    attrs=new HashMap<>();
                    List<RegexMatchItem> items = Strings.regexFinds(attrLine, REG_KEY_EQUAL_VALUE);
                    for(RegexMatchItem item : items){
                        String pstr = item.matchStr;
                        String[] arr2=pstr.split("=",2);
                        String key=arr2[0].trim();
                        String val=arr2[1].trim();
                        val=val.substring(1,val.length()-1);
                        attrs.put(key,val);
                    }
                }
                XmlNode node=new XmlNode();
                node.setName(ctx.getName());
                node.setContent(ctx.getXml());
                node.setAttrs(attrs);
                root.add(node);
                System.out.println(ctx);
                if(ctx.getContent()!=null && !"".equals(ctx.getContent())){
                    parseNext(ctx.getContent(),node);
                }
            }
            offset=ctx.getTo();
        }
    }
    public static XmlCtx readOneTag(String str,int idx){
        int i=idx;
        int sidx=i;
        int bodyBegin=i;
        int bodyEnd=i;
        String attr=null;
        String curTag=null;
        String curTagName=null;
        while(i<str.length()){
            if(str.charAt(i)=='<'){
                if(i+1<str.length()){
                    String pstr = str.substring(i,i+2);
                    if(pstr.startsWith("</")){
                        if(curTagName==null){
                            i+=pstr.length();
                        }else{
                            int j=0;
                            while((i+j)<str.length()){
                                if(str.charAt(i+j)=='>'){
                                    break;
                                }
                                j++;
                            }
                            String tag = str.substring(i+2,i+j);
                            String tagName = tag.split("\\s+",2)[0];

                            i+=j+1;
                            if(tagName.equals(curTagName)){
                                bodyEnd=i-j-1;
                                break;
                            }
                        }
                    }else if(pstr.startsWith("<")){
                        if(curTagName==null){
                            sidx=i;
                        }
                        int j=0;
                        while((i+j)<str.length()){
                            if(str.charAt(i+j)=='>'){
                                break;
                            }
                            j++;
                        }
                        boolean closeTag=false;
                        String tag = str.substring(i+1,i+j);
                        if(tag.endsWith("/")){
                            closeTag=true;
                            tag=tag.substring(0,tag.length()-1);
                        }
                        String[] tagArr=tag.split("\\s+",2);
                        String tagName = tagArr[0];

                        i+=j+1;
                        if(curTagName==null){
                            bodyBegin=i;
                            curTagName=tagName;
                            curTag=tag;
                            if(tagArr.length>1){
                                attr=tagArr[1];
                            }
                        }
                        if(closeTag){
                            bodyEnd=i;
                            if(tagName.equals(curTagName)) {
                                break;
                            }
                        }
                    }else{
                        i+=pstr.length();
                    }
                }else{
                    i++;
                }
            }else{
                i++;
            }
        }
        String content=str.substring(sidx,i);
        XmlCtx ctx=new XmlCtx();
        ctx.setName(curTagName);
        ctx.setTag(curTag);
        ctx.setAttr(attr);
        ctx.setContent(str.substring(bodyBegin,bodyEnd));
        ctx.setXml(content);
        ctx.setForm(sidx);
        ctx.setTo(i);

        return ctx;
    }
}
