# 集合工具包
- 提供一些集合的简化操作

## 详情
- Arrays
    - 提供对数组的常见操作
```java
Object obj=new String[]{"a"};
boolean rs=Arrays.isArray(obj);

RefArray<String> refArr=Arrays.refArray(obj);
refArr.isArray();
```
- Collections
    - 提供对集合的便捷操作
```java
List<Integer> list1=Collections.arrayList(1,2,3,4,5);

Set<String> list2=Collections.collect(new HashSet<>(),"1","2","3","3");
```
- Maps
    - 提供对映射的便捷操作
```java
HashMap<String, Integer> map1 = Maps.hashMap(Tuples.of("a", 1),
                Tuples.of("b", 2));

TreeMap<String, Integer> map2 = Maps.collect(new TreeMap<String, Integer>(), "a", 1, "b", 2);
```
- RefArray
    - 提供对Object类型的数组的便捷操作
```java
Object array =new String[]{"a","b"};
RefArray<String> refArray = Arrays.refArray(array);

boolean isArray = refArray.isArray();
int length = refArray.length();
String str = refArray.get(0);
refArray.set(1,"c");
for(String item : refArray){
    System.out.println(item);
}
```