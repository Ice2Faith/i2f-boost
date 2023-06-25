package i2f.core.database.jdbc.core;

import i2f.core.annotations.remark.Author;

import java.sql.Connection;

@Author("i2f")
public interface ITransaction{
    //这个返回值将会是执行directDoTransaction时的返回值
    void doTrans(Connection conn, Object... params) throws Exception;
}
