package i2f.core.dict.test;


import i2f.core.dict.annotations.Dict;
import i2f.core.dict.annotations.DictDecode;
import i2f.core.dict.annotations.DictEncode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2023/2/21 13:45
 * @desc
 */
@Data
@NoArgsConstructor
public class TestBean {

    @DictEncode("sexDesc")
    private Integer sex;

    @DictDecode("sex")
    @Dict(code = "1", text = "男")
    @Dict(code = "2", text = "女")
    @Dict(code = "", text = "未知")
    private String sexDesc;

    @DictDecode
    @DictEncode
    @Dict(code = "1", text = "一级")
    @Dict(code = "2", text = "二级")
    @Dict(code = "3", text = "三级")
    @Dict(code = "", text = "未知")
    private String grade;
}
