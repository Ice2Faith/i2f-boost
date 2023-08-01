package excel;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2023/8/1 14:35
 * @desc
 */
@Data
public class TestDeptVo extends TestDomVo {
    private String deptKey;
    private String deptName;
    private TestDeptVo parent;
}
