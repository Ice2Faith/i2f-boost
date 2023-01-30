package i2f.springboot.sensible.test;


import i2f.springboot.sensible.Sensible;
import i2f.springboot.sensible.SensibleType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2023/1/30 15:52
 * @desc
 */
@Data
@NoArgsConstructor
public class TestBean {

    @Sensible(type = SensibleType.PHONE)
    private String phone;

    @Sensible(type = SensibleType.EMAIL)
    private String email;

    @Sensible(type = SensibleType.ID_CARD)
    private String idCard;

    @Sensible(type = SensibleType.PASSWORD)
    private String password;

    @Sensible(type = SensibleType.TRUNC, prefix = 1, suffix = 0, fill = "#")
    private String realname;

    @Sensible(type = SensibleType.DICT, param = "country")
    private Long country;

}
