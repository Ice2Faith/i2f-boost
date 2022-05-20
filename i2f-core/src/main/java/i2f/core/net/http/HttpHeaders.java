package i2f.core.net.http;

/**
 * @author ltb
 * @date 2022/5/20 11:29
 * @desc
 */
public interface HttpHeaders {
    String ContentType="Content-Type";
    String ContentDisposition="Content-Disposition";
    String ContentLength="Content-Length";

    String AccessControlExposeHeaders="Access-Control-Expose-Headers";

    String CacheControl="Cache-Control";
    String Expires="Expires";
    String Pragma="Pragma";

    String AcceptRanges="Accept-Ranges";

}
