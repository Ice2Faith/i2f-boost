package i2f.core.jce.md.md;

/**
 * @author ltb
 * @date 2022/6/8 8:52
 * @desc
 */
public enum MdType {
    MD2("MD2"),
    MD5("MD5");


    private String type;

    private MdType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
