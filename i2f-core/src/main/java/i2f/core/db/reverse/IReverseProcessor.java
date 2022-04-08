package i2f.core.db.reverse;

import i2f.core.annotations.remark.Author;
import i2f.core.db.data.TableColumnMeta;
import i2f.core.db.data.TableMeta;

import java.util.List;

/**
 * @author ltb
 * @date 2022/3/23 23:25
 * @desc
 */
@Author("i2f")
public interface IReverseProcessor<T> {
    void onBegin(TableMeta meta);

    void onBeginColumns(TableMeta meta, List<TableColumnMeta> columns);

    boolean onColumn(TableColumnMeta column,TableMeta meta,int index,int size,boolean isFirst,boolean isEnd,boolean isRenderFirst,int renderIndex);

    void onEndColumns(TableMeta meta, List<TableColumnMeta> columns);

    void onEnd(TableMeta meta);

    T onResult();
}
