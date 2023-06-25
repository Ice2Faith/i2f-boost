package i2f.generator.drawio.er;

import i2f.core.database.db.data.TableColumnMeta;
import i2f.core.database.db.data.TableMeta;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2023/5/13 17:16
 * @desc
 */
public class DrawioAdapter {

    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static List<DrawioErElem> parseEr(List<TableMeta> tables) {

        int y = 0;
        List<DrawioErElem> elems = new LinkedList<>();
        for (TableMeta table : tables) {
            int x = 0;
            List<TableColumnMeta> columns = table.getColumns();
            DrawioErElem entity = new DrawioErElem();
            entity.setId(getId());
            entity.setText(table.getRemark());
            entity.setType("entity");
            entity.setX(x);
            entity.setY(y);
            entity.setWidth(120);
            entity.setHeight(60);
            elems.add(entity);

            y += 200;
            for (TableColumnMeta column : columns) {
                DrawioErElem attribute = new DrawioErElem();
                attribute.setId(getId());
                attribute.setText(column.getRemark());
                attribute.setType("attribute");
                attribute.setX(x);
                attribute.setY(y);
                attribute.setWidth(120);
                attribute.setHeight(60);
                elems.add(attribute);

                DrawioErElem link = new DrawioErElem();
                link.setId(getId());
                link.setText("");
                link.setType("link");
                link.setWidth(50);
                link.setHeight(50);
                link.setStartId(entity.getId());
                link.setEndId(attribute.getId());
                elems.add(link);

                x += 100;
            }

            y += 200;
        }

        return elems;
    }
}
