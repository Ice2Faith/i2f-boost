package i2f.annotations.doc.db;

import i2f.annotations.doc.base.Comment;

public interface PackageInfo {
    String INFO="Bean与Table-DDL进行双向生成的解释\n" +
            "下面直接使用DDL形式讲解\n" +
            "create table @Table.value\n" +
            "(\n" +
            "   -- 主键\n" +
            "   @Column.value @DataType.value @Primary @AutoIncrement @Restrict.value @Comment.value ,\n" +
            "   -- 外键\n" +
            "   @Column.value @DataType.value foreign key reference @Foreign.table (@Foreign.column) @Restrict.value @Comment.value ,\n" +
            "   -- 一般\n" +
            "   @Column.value @DataType.value @Restrict.value @Comment.value ,\n" +
            ") @Table.restricts @Comment.value ;";
}




