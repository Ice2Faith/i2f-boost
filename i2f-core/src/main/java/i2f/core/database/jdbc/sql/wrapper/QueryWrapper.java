package i2f.core.database.jdbc.sql.wrapper;

import i2f.core.database.jdbc.sql.consts.Sql;
import i2f.core.database.jdbc.sql.wrapper.core.*;
import i2f.core.type.str.Appender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 14:26
 * @desc
 */
public class QueryWrapper
        implements IWhereWrapper<ConditionWrapper<QueryWrapper>>,
        ITableWrapper<TableWrapper<QueryWrapper>>,
        IBuildWrapper<QueryWrapper>,
        IPreparable {
    protected SelectWrapper<QueryWrapper> columns=new SelectWrapper<>(this);
    protected TableWrapper<QueryWrapper> table=new TableWrapper<>(this);
    protected JoinWrapper<QueryWrapper> join=new JoinWrapper<>(this);
    protected ConditionWrapper<QueryWrapper> condition=new ConditionWrapper<QueryWrapper>(this);

    protected ColumnWrapper<QueryWrapper> groups=new ColumnWrapper<>(this);
    protected ConditionWrapper<QueryWrapper> having=new ConditionWrapper<QueryWrapper>(this);

    protected OrderWrapper<QueryWrapper> orders=new OrderWrapper<>(this);

    protected PageWrapper<QueryWrapper> page=new PageWrapper<>(this);

    private QueryWrapper(){

    }
    public static QueryWrapper builder(){
        return new QueryWrapper();
    }

    public SelectWrapper<QueryWrapper> select() {
        return columns;
    }

    @Override
    public TableWrapper<QueryWrapper> table() {
        return table;
    }

    public JoinWrapper<QueryWrapper> joins() {
        return join;
    }

    @Override
    public ConditionWrapper<QueryWrapper> where() {
        return condition;
    }

    public ColumnWrapper<QueryWrapper> group(){
        return groups;
    }

    public ConditionWrapper<QueryWrapper> having(){
        return having;
    }

    public OrderWrapper<QueryWrapper> order(){
        return orders;
    }

    @Override
    public QueryWrapper done() {
        return this;
    }

    public PageWrapper<QueryWrapper> page(){
        return page;
    }

    @Override
    public BindSql prepare() {
        List<Object> params=new ArrayList<>();
        Appender<StringBuilder> builder = Appender.builder()
                .add(Sql.SELECT)
                .line()
                .add(columns.selectColumns())
                .line()
                .addsSep(" ", Sql.FROM, table.aliasTable())
                .line()
                .add(join.joinTables());
        BindSql condBindSql=condition.prepare();
        if(condBindSql.sql!=null && !"".equals(condBindSql.sql)){
            builder.line()
                    .add(Sql.WHERE)
                    .line()
                    .add(condBindSql.sql);
            params.addAll(condBindSql.params);
        }

        String groupSql=groups.groupColumns();
        if(groupSql!=null && !"".equals(groupSql)){
            builder.line()
                    .addsSep(" ",Sql.GROUP_BY,groupSql);

            BindSql havingBindSql=having.prepare();
            if(havingBindSql.sql!=null && !"".equals(havingBindSql.sql)){
                builder.line()
                        .add(Sql.HAVING)
                        .line()
                        .add(havingBindSql.sql);
                params.addAll(havingBindSql.params);
            }
        }

        String orderSql=orders.orderColumns();
        if(orderSql!=null && !"".equals(orderSql)){
            builder.line()
                    .addsSep(" ",Sql.ORDER_BY,orderSql);
        }

        return new PageBindSql(builder.get(),params).page(page.index, page.size);
    }
}
