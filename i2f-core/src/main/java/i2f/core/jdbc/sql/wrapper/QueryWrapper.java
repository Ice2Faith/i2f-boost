package i2f.core.jdbc.sql.wrapper;

import i2f.core.jdbc.sql.wrapper.core.*;

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

    @Override
    public BindSql prepare() {
        return null;
    }
}
