package i2f.reflect;

import i2f.reflect.vistor.Visitor;
import i2f.reflect.vistor.impl.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2024/3/1 8:54
 * @desc
 * context={
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
 * root={
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
 */

class TestRole{
    private String name;
    private String key;
    private List<String> perms;

    public List<String> getKeys(String name,String perm){
        List<String> ret=new ArrayList<>();
        if(!this.name.equals(name)){
            ret.add("view");
            return ret;
        }
        if(!this.perms.contains(perm)){
            ret.add("view");
            return ret;
        }
        ret.add(perm);
        ret.add("view");
        return ret;
    }

    public String getName() {
        return name;
    }

    public TestRole setName(String name) {
        this.name = name;
        return this;
    }

    public String getKey() {
        return key;
    }

    public TestRole setKey(String key) {
        this.key = key;
        return this;
    }

    public List<String> getPerms() {
        return perms;
    }

    public TestRole setPerms(List<String> perms) {
        this.perms = perms;
        return this;
    }
}

class TestUser{
    private List<TestRole> roles;

    public List<TestRole> getRoles() {
        return roles;
    }

    public TestUser setRoles(List<TestRole> roles) {
        this.roles = roles;
        return this;
    }
}

class TestContextPerm{
    private List<String> perms;
    public String get(int index){
        return this.perms.get(index);
    }

    public List<String> getPerms() {
        return perms;
    }

    public TestContextPerm setPerms(List<String> perms) {
        this.perms = perms;
        return this;
    }
}
class TestContextCategory{
    private int charIdx;

    public int getCharIdx() {
        return charIdx;
    }

    public TestContextCategory setCharIdx(int charIdx) {
        this.charIdx = charIdx;
        return this;
    }
}
class TestContextRequires{
    private int permIdx;

    public int getPermIdx() {
        return permIdx;
    }

    public TestContextRequires setPermIdx(int permIdx) {
        this.permIdx = permIdx;
        return this;
    }
}
class TestContext{
    private String name;
    private List<TestContextPerm> perm;
    private TestContextCategory category;
    private TestContextRequires requires;

    public String getName() {
        return name;
    }

    public TestContext setName(String name) {
        this.name = name;
        return this;
    }

    public List<TestContextPerm> getPerm() {
        return perm;
    }

    public TestContext setPerm(List<TestContextPerm> perm) {
        this.perm = perm;
        return this;
    }

    public TestContextCategory getCategory() {
        return category;
    }

    public TestContext setCategory(TestContextCategory category) {
        this.category = category;
        return this;
    }

    public TestContextRequires getRequires() {
        return requires;
    }

    public TestContext setRequires(TestContextRequires requires) {
        this.requires = requires;
        return this;
    }
}

public class TestVisitor {

    public static void main(String[] args){
        String expression="user.roles[0].@getKeys(#{name},#{perm}[0].@get(#{requires.permIdx}))[0].@charAt(#{category.charIdx})";
//        expression="user.roles[0].@getKeys(#{name},#{perm}[0].@get(#{requires.permIdx}))[0].@isEmpty()";
//        expression="@String.valueOf($23.565)";
        expression="@Math.PI";
        Map<String,Object> rootObj=new HashMap<>();

        List<TestRole> userRoles=new ArrayList<>();
        TestRole role1=new TestRole();
        role1.setName("超级管理员");
        role1.setKey("root");
        role1.setPerms(Arrays.asList("add","delete","edit"));
        userRoles.add(role1);
        TestRole role2=new TestRole();
        role2.setName("管理员");
        role2.setKey("admin");
        role2.setPerms(Arrays.asList("add","edit"));
        userRoles.add(role2);

        TestUser user=new TestUser();
        user.setRoles(userRoles);
        rootObj.put("user",user);


        TestContext paramObj=new TestContext();
        paramObj.setName("超级管理员");
        TestContextPerm contextPerm=new TestContextPerm();
        contextPerm.setPerms(Arrays.asList("add","delete"));
        paramObj.setPerm(Arrays.asList(contextPerm));
        TestContextCategory contextCategory=new TestContextCategory();
        contextCategory.setCharIdx(0);
        paramObj.setCategory(contextCategory);
        TestContextRequires contextRequires=new TestContextRequires();
        contextRequires.setPermIdx(1);
        paramObj.setRequires(contextRequires);


        Visitor ret = Visitor.visit(expression, rootObj, paramObj);

        System.out.println(ret.getClass().getName());
        System.out.println(ret.get());

        ret.set(666);
        System.out.println(ret.get());
    }


}
