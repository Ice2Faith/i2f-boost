package i2f.core.codec.str.html;

import i2f.core.codec.str.html.cer.HtmlCerCodec;
import i2f.core.codec.str.html.ncr.HtmlNcrCodec;

/**
 * @author Ice2Faith
 * @date 2023/8/31 9:24
 * @desc
 */
public class HtmlStringStringCodec {
    public static final HtmlStringStringCodec INSTANCE = new HtmlStringStringCodec();
    public static final HtmlStringStringCodec NCR_CER_INSTANCE = new HtmlStringStringCodec(true);

    private boolean ncrEncode = false;

    private HtmlNcrCodec ncrCodec = HtmlNcrCodec.CER_FOR_INSTANCE;
    private HtmlCerCodec cerCodec = HtmlCerCodec.INSTANCE;


    public HtmlStringStringCodec() {
    }

    public HtmlStringStringCodec(boolean ncrEncode) {
        this.ncrEncode = ncrEncode;
    }

    public HtmlStringStringCodec(boolean ncrEncode, HtmlNcrCodec ncrCodec) {
        this.ncrEncode = ncrEncode;
        this.ncrCodec = ncrCodec;
    }

    public HtmlStringStringCodec(boolean ncrEncode, HtmlCerCodec cerCodec) {
        this.ncrEncode = ncrEncode;
        this.cerCodec = cerCodec;
    }

    public HtmlStringStringCodec(boolean ncrEncode, HtmlNcrCodec ncrCodec, HtmlCerCodec cerCodec) {
        this.ncrEncode = ncrEncode;
        this.ncrCodec = ncrCodec;
        this.cerCodec = cerCodec;
    }

    public String encode(String data) {
        if (data == null) {
            return null;
        }
        String ret = cerCodec.encode(data);
        if (ncrEncode) {
            ret = ncrCodec.encode(ret);
        }
        return ret;
    }

    public String decode(String enc) {
        if (enc == null) {
            return null;
        }
        String ret = ncrCodec.decode(enc);
        ret = cerCodec.decode(ret);
        return ret;
    }
}
