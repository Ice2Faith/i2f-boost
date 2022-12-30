package i2f.spring.serialize.test;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@JacksonXmlRootElement(localName = "root")
public class UserDto {
    private Long userId;
    private String userName;
    private Integer age;
    private Double high;
    private BigDecimal money;
    @JacksonXmlElementWrapper(localName = "roles")
    @JacksonXmlProperty(localName = "item")
    private List<RoleDto> roles;

    public static UserDto makeRandom() {
        UserDto ret = new UserDto();
        Random random = new Random();
        ret.setUserId((long) random.nextInt());
        ret.setUserName("用户" + (char) (random.nextInt(26) + 'A'));
        ret.setAge(random.nextInt(15) + 20);
        ret.setHigh(random.nextDouble() * 0.5 + 1.5);
        ret.setMoney(BigDecimal.valueOf(random.nextDouble() * 5000));
        List<RoleDto> roles = new ArrayList<>();
        int rcnt = random.nextInt(5);
        for (int i = 0; i < rcnt; i++) {
            roles.add(RoleDto.makeRandom());
        }
        ret.setRoles(roles);
        return ret;
    }
}
