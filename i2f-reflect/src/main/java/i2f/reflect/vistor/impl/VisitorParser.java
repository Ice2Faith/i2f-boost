package i2f.reflect.vistor.impl;

import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author Ice2Faith
 * @date 2024/3/1 8:54
 * @desc
 * 一个支持上下文参数的简易表达式访问器
 * 目的：
 * 提供一个类似js的访问方式，访问多层级嵌套的对象
 * 例如：
 * $root.user.name
 * $root.user.roles[0].name
 * user.name
 * user.roles.@toString()
 * 定义：
 * 需要三个基本量
 *      expression 表达式
 *      rootObj 根元素，也就是表达式中的值，默认从根元素开始查找
 *      paramObj 根参数，也就是表达式中，可能会使用到其他的参数，则以此为根元素开始查找
 * 每个层级使用.英文句号分隔
 *      也就是访问此对象的方法或者属性
 *      比如：user.name
 *         ：user.age
 *      这些表达式的值，将会从根元素开始查找
 *      最终结果分别对应：rootObj.user.name ; rootObj.user.age
 * 使用[]包含一个索引或者键
 *      比如：[0]
 *         ：[name]
 *      适用于Object访问，Array访问，List访问，Map访问
 *      对于Map访问时，就是键
 * 使用#{}包含一个根参数的值
 *      比如：#{gender}
 *          ：#{cond.minAge}
 *      这些表达式的值，将从根参数开始查找
 *      最终结果分别对应：paramObj.gender;paramObj.cond.minAge
 * 使用@引导一个符号
 *      比如：@isEmpty()
 *          ：@age
 *          ：@Math.PI
 *          ：@String.valueOf()
 *          ：@String.valueOf(user.age)
 *          ：@isValid(user.age,#{cond.minAge})
 *          ：@java.util.Date.from()
 *      也就是@之后跟一个方法isEmpty()、一个属性age、一个全限定方法String.valueOf()、一个静态变量Math.PI都是可以的
 *      而且，针对常用的Java包的类可以不用指定全限定名称，例如：String.valueOf()就不用写成java.lang.String.valueOf()
 *      怎么区别方法和属性？通过是否带有参数括号进行区分，@Math.PI不带括号，认为是属性，@String.valueOf()带括号，认为是方法
 *      同时方法支持传入参数
 *          例如：@String.valueOf(user.age) 就传入了一个参数，这个参数user.name就是从根元素中获取的
 *          ：@isValid(user.age,#{cond.minAge}) 就传入了两个参数，第二个参数则是从参数中获取的
 * 其他的一些特殊常量
 *      $true 就是 true
 *      $false 就是 false
 *      $root 就是入参 rootObj
 *      $param 就是入参 paramObj
 *      $node 就是当前节点的值
 *          例如表达式： user.role.@addKey($node.@getKey()).size()
 *          当识别到@addKey函数调用时，此时的$node的值就是user.role
 *          因此可以理解为调用者的调用方
 *     数值组
 *          $引导 $123 就是 Integer 类型 123
 *          $l引导 $l123 就是 Long 类型 123
 *          $12.125 就是 Double 类型 12.125
 *          $f引导 $f12.125 就是 Float 类型 12.125
 *          $0x引导 $0x123 就是 Integer 的 16 进制 123
 *          $0b引导 $0b101 就是 Integer 的 2 进制 101
 *          $0引导(0开头) $0123 就是 Integer 的 8 进制 123
 *          $l 0x 引导 $l0x123 就是 Long 的 16 进制 123
 *          $l 0b 引导 $l0b101 就是 Long 的 2 进制 101
 *          $l 0(0开头) 引导 $l0123 就是 Long 的 8 进制 123
 * 知道这些后，下面给出几个示例
 *
 * ---------------------------------
 * paramObj={
 *      name: '超级管理员',
 *      perm: [{
 *          perms: ['add','delete'],
 *          get(index){
 *              return this.perms[index]
 *          }
 *      }],
 *      category:{
 *          charIdx: 0
 *      },
 *      requires:{
 *          permIdx: 1
 *      }
 * }
 *
 * rootObj={
 *      user:{
 *          roles: [{
 *              name: '超级管理员',
 *              key: 'root',
 *              perms: ['add','delete','edit'],
 *              getKeys(name,perm){
 *                  if(name!=this.name){
 *                      return ['view']
 *                  }
 *                  if(!this.perms.contains(perm)){
 *                      return ['view']
 *                  }
 *                  return [perm,'view']
 *              }
 *          },{
 *              name: '管理员',
 *              key: 'admin',
 *              perms: ['add','edit']
 *          }]
 *      }
 * }
 *
 * expression= user.roles[0].@getKeys(#{name},#{age}[0].@get(#{current.key}))[0].@charAt(#{category.charIdx})
 * ret= 'd'
 *
 * expression= user.roles[0].@getKeys(#{name},#{perm}[0].@get(#{requires.permIdx}))[0].@isEmpty()
 * ret= false
 *
 * expression= @String.valueOf($23.565)
 * ret= "23.565"
 *
 * expression= @Math.PI
 * ret= 3.14159...
 */
public class VisitorParser {
    public static Visitor visit(String expression, Object rootObj){
        return visit(expression,rootObj,rootObj,rootObj);
    }

    public static Visitor visit(String expression,Object rootObj,Object paramObj) {
        return visit(expression, rootObj, rootObj, paramObj);
    }

    private static Visitor visit(String expression,Object rootObj,Object nodeObj,Object paramObj){
        Visitor ret=new ReadonlyVisitor(nodeObj,nodeObj);
        List<String> tokens = splitTokens(expression);

        for (String token : tokens) {
            Object currRet=ret.get();
            if(token.startsWith("[")){
                String nextExpression=token.substring(1,token.length()-1);
                ret=visit(nextExpression,rootObj,currRet,paramObj);
            }else if(token.startsWith("@")){
                String nextExpression = token.substring(1);
                String methodName = nextFullNaming(nextExpression, new AtomicInteger(0));
                String paramExpression = token.substring(1 + methodName.length());
                Class<?> staticClass=null;

                int dotIdx=methodName.lastIndexOf(".");
                if(dotIdx>=0){
                    String className=methodName.substring(0,dotIdx);
                    methodName=methodName.substring(dotIdx+1);
                    staticClass = ReflectResolver.loadClassWithJdk(className);
                }

                if(!"".equals(paramExpression)){
                    paramExpression=paramExpression.substring(1,paramExpression.length()-1);
                    List<String> parameters=splitParameters(paramExpression);
                    Object[] args=new Object[parameters.size()];
                    int i=0;
                    for (String parameter : parameters) {
                        if(parameter.startsWith("#{")){
                            args[i]=visit(parameter,rootObj,paramObj,paramObj).get();
                        }else{
                            args[i]=visit(parameter,rootObj,currRet,paramObj).get();
                        }
                        i++;
                    }

                    if(staticClass==null){
                        try{
                            Object val=ReflectResolver.invokeMethod(currRet,methodName,args);
                            ret=new ReadonlyVisitor(val,currRet);
                        }catch(Throwable e){
                            throw new IllegalStateException("invoke method ["+methodName+"] error in class ["+currRet.getClass().getName()+"] at token : "+token,e);
                        }
                    }else{
                        try{
                            Object val=ReflectResolver.invokeStaticMethod(staticClass,methodName,args);
                            ret=new ReadonlyVisitor(val,staticClass);
                        }catch(Throwable e){
                            throw new IllegalStateException("invoke static method ["+methodName+"] error in class ["+staticClass+"] at token : "+token,e);
                        }
                    }
                }else{
                    if(staticClass==null){
                        ret=new FieldVisitor(currRet,methodName);
                    }else{
                        ret=new StaticFieldVisitor(staticClass,methodName);
                    }
                }



            }else if(token.startsWith("#")){
                String nextExpression=token.substring(2,token.length()-1);
                ret=visit(nextExpression,rootObj,paramObj,paramObj);
            }else{
                if("$root".equals(token)){
                    ret=new ConstVisitor(rootObj);
                }else if("$param".equals(token)){
                    ret=new ConstVisitor(paramObj);
                }else if("$node".equals(token)){
                    ret=new ConstVisitor(nodeObj);
                }else if("$true".equals(token)){
                    ret=new ConstVisitor(true);
                }else if("$false".equals(token)){
                    ret=new ConstVisitor(false);
                }else if(token.matches("\\$\\d+\\.\\d+")){
                    ret=new ConstVisitor(Double.parseDouble(token.substring(1)));
                }else if(token.matches("\\$f\\d+\\.\\d+")){
                    ret=new ConstVisitor(Float.parseFloat(token.substring(2)));
                } else if(token.matches("\\$[1-9]([0-9]+)?")){
                    ret=new ConstVisitor(Integer.parseInt(token.substring(1),10));
                }else if(token.matches("\\$(0x|0X)[a-fA-F0-9]+")){
                    ret=new ConstVisitor(Integer.parseInt(token.substring(3),16));
                } else if(token.matches("\\$0([0-9]+)?")){
                    ret=new ConstVisitor(Integer.parseInt(token.substring(2),8));
                } else if(token.matches("\\$(0b|0B)[0-1]+")){
                    ret=new ConstVisitor(Integer.parseInt(token.substring(3),2));
                }else if(token.matches("\\$l[1-9]([0-9]+)?")){
                    ret=new ConstVisitor(Long.parseLong(token.substring(2),10));
                }else if(token.matches("\\$l(0x|0X)[a-fA-F0-9]+")){
                    ret=new ConstVisitor(Long.parseLong(token.substring(4),16));
                } else if(token.matches("\\$l0([0-9]+)?")){
                    ret=new ConstVisitor(Long.parseLong(token.substring(3),8));
                } else if(token.matches("\\$l(0b|0B)[0-1]+")){
                    ret=new ConstVisitor(Long.parseLong(token.substring(4),2));
                } else if(currRet instanceof Map){
                    ret=new MapVisitor((Map)currRet,token);
                }else if(ReflectResolver.isArray(currRet)){
                    Integer idx = Integer.valueOf(token);
                    return new ArrayVisitor(currRet,idx);
                } else if(currRet instanceof List){
                    Integer idx = Integer.valueOf(token);
                    return new ListVisitor((List)currRet,idx);
                }else if(currRet instanceof Collection){
                    Collection col=(Collection)ret;
                    Iterator iterator = col.iterator();
                    Integer idx = Integer.valueOf(token);
                    int i=0;
                    while(iterator.hasNext()){
                        Object next = iterator.next();
                        if(idx==i){
                            ret=new ReadonlyVisitor(next,col);
                            break;
                        }
                        i++;
                    }
                }else{
                    try{
                        ReflectResolver.valueGet(currRet,token);
                        ret=new FieldVisitor(currRet,token);
                    }catch(Throwable e){
                        throw new IllegalStateException("bean field ["+token+"] in class ["+ret.getClass().getName()+"] get value error",e);
                    }
                }
            }
        }

        return ret;
    }

    public static List<String> splitParameters(String expression){
        AtomicInteger index=new AtomicInteger(0);
        Stack<Character> stack=new Stack<>();
        List<String> tokens=new ArrayList<>();
        String curr="";
        while(index.get()<expression.length()) {
            char ch = expression.charAt(index.get());
            if(ch=='('){
                curr+=ch;
                stack.push(ch);
            }else if(ch==')'){
                curr+=ch;
                stack.pop();
            }else if(ch==','){
                if(stack.isEmpty()){
                    tokens.add(curr);
                    curr="";
                }
            }else{
                curr+=ch;
            }
            index.incrementAndGet();
        }

        if(!"".equals(curr)){
            tokens.add(curr);
        }

        return tokens;
    }

    public static List<String> splitTokens(String expression){
        AtomicInteger index=new AtomicInteger(0);
        List<String> tokens=new ArrayList<>();
        String curr="";
        while(index.get()<expression.length()){
            char ch = expression.charAt(index.get());
            if((ch>='a' && ch<='z')
                    ||(ch>='A' && ch<='Z'
                    || (ch>='0' && ch<='9')
                    || ch=='_'
                    || ch=='$')){
                curr+=ch;
                index.incrementAndGet();
            }else if(ch=='['){
                AtomicInteger nextIndex=new AtomicInteger(index.get());
                String str = nextEnclose(expression, nextIndex, '[', ']');
                if(str!=null && !"".equals(str)){
                    if(!"".equals(curr)){
                        tokens.add(curr);
                        curr="";
                    }

                    curr+=str;
                    index.set(nextIndex.get());
                }else{
                    throw new IllegalStateException("enclose ["+ch+"] not compared at index is "+index.get());
                }
            }else if(ch=='@'){
                AtomicInteger nextIndex=new AtomicInteger(index.get()+1);
                String str=nextFullNaming(expression,nextIndex);
                if(str!=null && !"".equals(str)){
                    curr+='@';
                    curr+=str;
                    index.set(nextIndex.get());
                    if(index.get()>=expression.length()){
                        continue;
                    }
                    ch=expression.charAt(index.get());
                    if(ch=='('){
                        nextIndex=new AtomicInteger(index.get());
                        str = nextEnclose(expression, nextIndex, '(', ')');
                        if(str!=null && !"".equals(str)){
                            curr+=str;
                            tokens.add(curr);
                            curr="";
                            index.set(nextIndex.get());
                        }else{
                            throw new IllegalStateException("enclose ["+ch+"] not compared at index is "+index.get());
                        }
                    }else{
                        if(!"".equals(curr)){
                            tokens.add(curr);
                            curr="";
                        }
                    }
                }else{
                    throw new IllegalStateException("method naming ["+ch+"] not compared at index is "+index.get());
                }
            }else if(ch=='#'){
                index.incrementAndGet();
                ch=expression.charAt(index.get());
                if(ch=='{'){
                    curr+='#';
                    AtomicInteger nextIndex=new AtomicInteger(index.get());
                    String str = nextEnclose(expression, nextIndex, '{', '}');
                    if(str!=null && !"".equals(str)){
                        curr+=str;
                        tokens.add(curr);
                        curr="";
                        index.set(nextIndex.get());
                    }else{
                        throw new IllegalStateException("enclose ["+ch+"] not compared at index is "+index.get());
                    }
                }else{
                    throw new IllegalStateException("context argument enclose ["+ch+"] not compared at index is "+index.get());
                }
            }else if(ch=='.'){
                String tmp = expression.substring(index.get() - 1, index.get() + 2);
                if(tmp.matches("\\d\\.\\d") && !curr.contains(".")){
                    curr+=".";
                }else{
                    if(!"".equals(curr)){
                        tokens.add(curr);
                        curr="";
                    }
                }
                index.incrementAndGet();
            }else{
                throw new IllegalStateException("char ["+ch+"] not recognized at index is "+index.get());
            }
        }
        if(!"".equals(curr)){
            tokens.add(curr);
        }
        return tokens;
    }

    public static String nextFullNaming(String expression,AtomicInteger index){
        AtomicInteger tmp=new AtomicInteger(index.get());
        String ret="";
        String str = nextNaming(expression, tmp);
        if(str!=null && !"".equals(str)){
            ret+=str;
            while(tmp.get()<expression.length() && expression.charAt(tmp.get())=='.'){
                tmp.incrementAndGet();
                str = nextNaming(expression, tmp);
                if(str!=null && !"".equals(str)) {
                    ret+=".";
                    ret += str;
                }else{
                    break;
                }
            }
        }

        index.set(tmp.get());

        return ret;
    }

    public static String nextNaming(String expression,AtomicInteger index){
        String ret="";
        while(index.get()<expression.length()) {
            char ch = expression.charAt(index.get());
            if(ch>='0' && ch<='9'){
                if("".equals(ret)){
                    return ret;
                }
            }else if((ch>='a' && ch<='z')
                    ||(ch>='A' && ch<='Z'
                    || (ch>='0' && ch<='9')
                    || ch=='_'
                    || ch=='$')){
                ret+=ch;
            }else{
                return ret;
            }
            index.incrementAndGet();
        }
        return ret;
    }

    public static String nextEnclose(String expression, AtomicInteger index, char left, char right){
        Stack<Character> stack=new Stack<>();
        String ret="";
        while(index.get()<expression.length()){
            char ch = expression.charAt(index.get());
            if(ch==left){
                stack.push(ch);
            }else if(ch==right){
                if(stack.isEmpty()){
                    return "";
                }
                stack.pop();
                if(stack.isEmpty()){
                    ret+=ch;
                    index.incrementAndGet();
                    return ret;
                }
            }
            ret+=ch;
            index.incrementAndGet();
        }
        return "";
    }

}
