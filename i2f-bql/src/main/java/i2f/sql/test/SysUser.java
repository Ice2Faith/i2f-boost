package i2f.sql.test;

import i2f.sql.annotations.DbIgnore;
import i2f.sql.annotations.DbName;
import i2f.sql.annotations.DbPrimary;

import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2024/4/9 13:55
 * @desc
 */
@DbName("sys_user")
public class SysUser {
    @DbPrimary
    private Long id;

    @DbName("username")
    private String userName;
    private String password;

    private Long roleId;

    @DbName("nickname")
    private String nickName;
    private Integer age;
    private Integer grade;

    private Integer status;

    private Date createTime;
    private Date updateTime;

    private Integer delFlag;

    @DbIgnore
    private String statusDesc;

    @DbIgnore
    private String roleName;

    @DbIgnore
    private String delFlagDesc;

    public SysUser buildAge(int age) throws Exception {
        return this;
    }

    public SysUser withAge(int age) {
        return this;
    }

    public void updateAge() {

    }

    /////////////////////////////////////////////////


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDelFlagDesc() {
        return delFlagDesc;
    }

    public void setDelFlagDesc(String delFlagDesc) {
        this.delFlagDesc = delFlagDesc;
    }
}
