package i2f.annotations.doc.api;

public interface PackageInfo {
    String INFO="SpringMVC使用指导建议\n" +
            "Controller上使用@Module注解说明模块\n" +
            "Mapping方法上使用@Operation注解说明操作\n" +
            "至于@Method则可以不用，又@GetMapping系列得出即可\n" +
            "至于@Label，可以添加在Mapping上，也可以在Controller上表示一个分类\n" +
            "至于@Protocol,直接不用，因为就是HTTP请求\n" +
            "至于@System,除非是进行外部调用，否则一般一个项目就是一个System，没必要添加";
}



