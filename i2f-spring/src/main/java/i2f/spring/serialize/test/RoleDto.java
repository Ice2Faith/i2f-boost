package i2f.spring.serialize.test;

import lombok.Data;

import java.util.Random;

@Data
public class RoleDto {
    private Long roleId;
    private String roleKey;
    private String roleName;
    private Boolean enable;

    public static RoleDto makeRandom() {
        Random random = new Random();
        RoleDto r = new RoleDto();
        r.setRoleId((long) random.nextInt());
        r.setRoleKey("rk_" + (char) (random.nextInt(26) + 'A'));
        r.setRoleName("角色" + (char) (random.nextInt(26) + 'A'));
        r.setEnable(random.nextBoolean());
        return r;
    }
}
