package test.condition;

import i2f.core.lambda.column.Column;
import i2f.core.lambda.column.Columns;
import i2f.core.lambda.condition.Condition;
import i2f.core.lambda.condition.Conditions;

/**
 * @author ltb
 * @date 2022/9/1 9:40
 * @desc
 */
public class TestConditions {
    public static void main(String[] args) {
        Object val = null;
        Conditions conds = Conditions.where()
                .and()
                .prefix("a.")
                .eq("name", "zhang")
                .gte("age", 12)
                .lte("age", 24)
                .between("grade", 60, 80)
                .in("level", 1, 2, 3)
                .prefix("b.")
                .eq("course", 1)
                .noPrefix()
                .like("teacher", "li")
                .neq(val != null, "score", 8)
                .eq(Condition::getColumn, "id")
                .eq(Condition::getOper, "=");

        System.out.println(conds);

        Columns cols = Columns.select()
                .prefix("a.")
                .col(Column::getColumn)
                .cols(Column::getAlias, Column::getPrefix)
                .col(val != null, Column::getColumn);

        System.out.println(cols);

    }
}
