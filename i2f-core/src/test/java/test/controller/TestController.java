package test.controller;

import i2f.core.db.annotations.DbCatalog;
import i2f.core.db.annotations.DbComment;
import i2f.core.db.annotations.DbName;
import i2f.core.id.IdNumberUtil;
import i2f.core.id.data.IdNumberData;
import i2f.core.pkg.PackageUtil;

import java.io.IOException;
import java.util.List;

/**
 * @author ltb
 * @date 2022/3/22 22:42
 * @desc
 */
@DbName
@DbComment
public class TestController {

    public static void main(String[] args) throws IOException {
        IdNumberData data=IdNumberUtil.parse("522401199902260139");
        List<Class> all= PackageUtil.getIncludeAnnotationClasses("test", DbName.class);
        List<Class> controller= PackageUtil.getIncludeAnnotationClasses("test", DbComment.class);
        List<Class> service= PackageUtil.getIncludeAnnotationClasses("test", DbCatalog.class);

    }
}
