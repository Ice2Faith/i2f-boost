# Mybatis 语法集锦
---

# 查询
- where 语句
```xml
<where>
    <if test="userId!=null and userId!=''">
        and a.user_id=#{userId}
    </if>

    <if test="userName!=null and userName!=''">
        and a.user_name like concat(concat('%',#{userName}),'%')
    </if>

    <if test="status!=null and status!=''">
        and a.status in
        <foreach collection="status.split(',')" item="item" open="(" separator="," close=")">
            #{item}
        </foreach>
    </if>
</where>
```
- 查询列
```sql
a.status status,
case
    when a.status=1 then '启用'
    when a.status=2 then '禁用'
    else ''
end statusDesc,
```
