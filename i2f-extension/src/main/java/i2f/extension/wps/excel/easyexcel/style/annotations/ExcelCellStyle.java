package i2f.extension.wps.excel.easyexcel.style.annotations;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2022/10/12 11:52
 * @desc
 */
@Target({
        ElementType.FIELD,
        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelCellStyle {
    // 当SpEL表达式值为true时，应用这里的配置，SpEL表达式广泛应用于Spring配置中
    // 在Spring配置中，默认是spring上下文
    // 但是在本实例中，上下文为ExcelStyleCallbackMeta
    // 因此，在spel表达式中，可以直接使用ExcelStyleCallbackMeta中的属性
    String spel() default "#{true}";

    String fontName() default "";

    IndexedColors fontColor() default IndexedColors.BLACK;

    boolean fontBold() default false;

    boolean fontItalic() default false;

    boolean fontUnderline() default false;

    short fontHeight() default 11;

    IndexedColors backgroundColor() default IndexedColors.WHITE;

    BorderStyle borderBottom() default BorderStyle.HAIR;

    BorderStyle borderLeft() default BorderStyle.HAIR;

    BorderStyle borderRight() default BorderStyle.HAIR;

    BorderStyle borderTop() default BorderStyle.HAIR;

    HorizontalAlignment alignHorizontal() default HorizontalAlignment.LEFT;

    VerticalAlignment alignVertical() default VerticalAlignment.CENTER;
}
