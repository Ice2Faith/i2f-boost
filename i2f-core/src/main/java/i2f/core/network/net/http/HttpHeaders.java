package i2f.core.network.net.http;

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

    String UserAgent="User-Agent";

    String Connection="Connection";

    String ContentEncoding="Content-Encoding";
    String Server="Server";
    String Date="Date";
    String SetCookie="Set-Cookie";

    String XFrameOptions="X-Frame-Options";
    String XUaCompatible="X-Ua-Compatible";

    String AcceptEncoding="Accept-Encoding";
    String AcceptLanguage="Accept-Language";

    String Cookie="Cookie";
    String Host="Host";

}
