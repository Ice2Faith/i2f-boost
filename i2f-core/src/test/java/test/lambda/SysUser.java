package test.lambda;

import lombok.Data;

import java.util.Date;

/**
* <p>
* 管理用户实体类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
@Data
public class SysUser {

    private static final long serialVersionUID = 1L;

    public SysUser buildSysUser(int a){
        return this;
    }
    public SysUser testArgs2(int a,double b){
        return this;
    }
    public SysUser testArgs3(int a,double b,char c){
        return this;
    }

    public SysUser testVarArgs(Integer... args){
        return this;
    }
    public void testVoidVarArgs(Object... args){

    }
    /**
     * ID
     */
    private String id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码盐
     */
    private String salt;

    /**
     * 角色列表
     */
    private String roleIds;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态
     */
    private Integer state;


}
