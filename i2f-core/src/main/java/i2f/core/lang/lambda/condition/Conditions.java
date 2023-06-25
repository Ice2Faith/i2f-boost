package i2f.core.lang.lambda.condition;

import i2f.core.lang.functional.common.IGetter;
import i2f.core.lang.lambda.Lambdas;

import java.util.*;

/**
 * @author ltb
 * @date 2022/9/1 8:42
 * @desc
 */
public class Conditions {
    public static Conditions where() {
        return new Conditions();
    }

    //////////////////////////////////////////////////////////////////////////////
    protected Object link = CondLink.AND;
    protected Object oper = CondOper.EQ;
    protected Object prefix = null;
    protected List<Condition> conditions = new LinkedList<>();

    //////////////////////////////////////////////////////////////////////////////
    public Conditions() {

    }

    //////////////////////////////////////////////////////////////////////////////
    public Conditions and() {
        this.link = CondLink.AND;
        return this;
    }

    public Conditions or() {
        this.link = CondLink.OR;
        return this;
    }

    public Conditions link(Object link) {
        this.link = link;
        return this;
    }

    public Conditions and(Conditions conds) {
        return cond(true, CondLink.AND, null, null, conds);
    }

    public Conditions or(Conditions conds) {
        return cond(true, CondLink.OR, null, null, conds);
    }

    public Conditions prefix(Object prefix) {
        this.prefix = prefix;
        return this;
    }

    public Conditions noPrefix() {
        this.prefix = null;
        return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    public Conditions eq() {
        this.oper = CondOper.EQ;
        return this;
    }

    public Conditions neq() {
        this.oper = CondOper.NEQ;
        return this;
    }

    public Conditions gt() {
        this.oper = CondOper.GT;
        return this;
    }

    public Conditions lt() {
        this.oper = CondOper.LT;
        return this;
    }

    public Conditions gte() {
        this.oper = CondOper.GTE;
        return this;
    }

    public Conditions lte() {
        this.oper = CondOper.LTE;
        return this;
    }

    public Conditions in() {
        this.oper = CondOper.IN;
        return this;
    }

    public Conditions like() {
        this.oper = CondOper.LIKE;
        return this;
    }

    public Conditions between() {
        this.oper = CondOper.BETWEEN;
        return this;
    }

    public Conditions exists() {
        this.oper = CondOper.EXISTS;
        return this;
    }

    public Conditions nin() {
        this.oper = CondOper.NOT_IN;
        return this;
    }

    public Conditions nlike() {
        this.oper = CondOper.NOT_LIKE;
        return this;
    }

    public Conditions nexists() {
        this.oper = CondOper.NOT_EXISTS;
        return this;
    }

    public Conditions oper(Object oper) {
        this.oper = oper;
        return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    public Conditions cond(boolean decide, Condition cond) {
        if (cond == null) {
            return this;
        }
        if (decide) {
            this.conditions.add(cond);
        }
        return this;
    }

    public Conditions cond(boolean decide, Object link, Object prefix, Object column, Object oper, Object value) {
        if (decide) {
            Condition one = new Condition(link, prefix, column, oper, value);
            this.conditions.add(one);
        }
        return this;
    }

    public Conditions cond(boolean decide, Object link, Object column, Object oper, Object value) {
        return cond(decide, link, this.prefix, column, oper, value);
    }

    public Conditions cond(boolean decide, Object column, Object oper, Object value) {
        return cond(decide, this.link, this.prefix, column, oper, value);
    }

    public Conditions cond(boolean decide, Object column, Object value) {
        return cond(decide, this.link, this.prefix, column, this.oper, value);
    }

    public <R, T> Conditions cond(boolean decide, Object link, Object prefix, IGetter<R, T> column, Object oper, Object value) {
        String columnName = Lambdas.fieldName(column);
        return cond(decide, link, prefix, columnName, oper, value);
    }

    public <R, T> Conditions cond(boolean decide, Object link, IGetter<R, T> column, Object oper, Object value) {
        String columnName = Lambdas.fieldName(column);
        return cond(decide, link, columnName, oper, value);
    }

    public <R, T> Conditions cond(boolean decide, IGetter<R, T> column, Object oper, Object value) {
        String columnName = Lambdas.fieldName(column);
        return cond(decide, columnName, oper, value);
    }

    public <R, T> Conditions cond(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return cond(decide, columnName, value);
    }

    //////////////////////////////////////////////////////////////////////////////
    public Conditions eq(boolean decide, Object column, Object value) {
        return cond(decide, column, CondOper.EQ, value);
    }

    public Conditions neq(boolean decide, Object column, Object value) {
        return cond(decide, column, CondOper.NEQ, value);
    }

    public Conditions gt(boolean decide, Object column, Object value) {
        return cond(decide, column, CondOper.GT, value);
    }

    public Conditions lt(boolean decide, Object column, Object value) {
        return cond(decide, column, CondOper.LT, value);
    }

    public Conditions gte(boolean decide, Object column, Object value) {
        return cond(decide, column, CondOper.GTE, value);
    }

    public Conditions lte(boolean decide, Object column, Object value) {
        return cond(decide, column, CondOper.LTE, value);
    }

    public Conditions in(boolean decide, Object column, Object value) {
        return cond(decide, column, CondOper.IN, value);
    }

    public Conditions in(boolean decide, Object column, Object... values) {
        List<Object> list = new ArrayList<>(Math.max(values.length, 10));
        for (Object item : values) {
            list.add(item);
        }
        return cond(decide, column, CondOper.IN, list);
    }

    public Conditions in(boolean decide, Object column, Collection<?> values) {
        return cond(decide, column, CondOper.IN, values);
    }

    public Conditions in(boolean decide, Object column, Iterable<?> iterable) {
        List<Object> list = new LinkedList<>();
        Iterator<?> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return cond(decide, column, CondOper.IN, list);
    }

    public Conditions in(boolean decide, Object column, Enumeration<?> enumeration) {
        List<Object> list = new LinkedList<>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return cond(decide, column, CondOper.IN, list);
    }

    public Conditions like(boolean decide, Object column, Object value) {
        return cond(decide, column, CondOper.LIKE, value);
    }

    public Conditions between(boolean decide, Object column, Object value) {
        return cond(decide, column, CondOper.BETWEEN, value);
    }

    public Conditions between(boolean decide, Object column, Object low, Object high) {
        Object[] arr = {low, high};
        return cond(decide, column, CondOper.BETWEEN, arr);
    }

    public Conditions exists(boolean decide, Object column, Object value) {
        return cond(decide, column, CondOper.EXISTS, value);
    }

    public Conditions nin(boolean decide, Object column, Object value) {
        return cond(decide, column, CondOper.NOT_IN, value);
    }

    public Conditions nin(boolean decide, Object column, Object... values) {
        List<Object> list = new ArrayList<>(Math.max(values.length, 10));
        for (Object item : values) {
            list.add(item);
        }
        return cond(decide, column, CondOper.NOT_IN, list);
    }

    public Conditions nin(boolean decide, Object column, Collection<?> values) {
        return cond(decide, column, CondOper.NOT_IN, values);
    }

    public Conditions nin(boolean decide, Object column, Iterable<?> iterable) {
        List<Object> list = new LinkedList<>();
        Iterator<?> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return cond(decide, column, CondOper.NOT_IN, list);
    }

    public Conditions nin(boolean decide, Object column, Enumeration<?> enumeration) {
        List<Object> list = new LinkedList<>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return cond(decide, column, CondOper.NOT_IN, list);
    }

    public Conditions nlike(boolean decide, Object column, Object value) {
        return cond(decide, column, CondOper.NOT_LIKE, value);
    }

    public Conditions nexists(boolean decide, Object column, Object value) {
        return cond(decide, column, CondOper.NOT_EXISTS, value);
    }

    public <R, T> Conditions eq(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return eq(decide, columnName, value);
    }

    public <R, T> Conditions neq(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return neq(decide, columnName, value);
    }

    public <R, T> Conditions gt(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return gt(decide, columnName, value);
    }

    public <R, T> Conditions lt(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return lt(decide, columnName, value);
    }

    public <R, T> Conditions gte(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return gte(decide, columnName, value);
    }

    public <R, T> Conditions lte(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return lte(decide, columnName, value);
    }

    public <R, T> Conditions in(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return in(decide, columnName, value);
    }

    public <R, T> Conditions in(boolean decide, IGetter<R, T> column, Object... values) {
        String columnName = Lambdas.fieldName(column);
        return in(decide, columnName, values);
    }

    public <R, T> Conditions in(boolean decide, IGetter<R, T> column, Collection<?> values) {
        String columnName = Lambdas.fieldName(column);
        return in(decide, columnName, values);
    }

    public <R, T> Conditions in(boolean decide, IGetter<R, T> column, Iterable<?> iterable) {
        String columnName = Lambdas.fieldName(column);
        return in(decide, columnName, iterable);
    }

    public <R, T> Conditions in(boolean decide, IGetter<R, T> column, Enumeration<?> enumeration) {
        String columnName = Lambdas.fieldName(column);
        return in(decide, columnName, enumeration);
    }

    public <R, T> Conditions like(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return like(decide, columnName, value);
    }

    public <R, T> Conditions between(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return between(decide, columnName, value);
    }

    public <R, T> Conditions between(boolean decide, IGetter<R, T> column, Object low, Object high) {
        String columnName = Lambdas.fieldName(column);
        return between(decide, columnName, low, high);
    }

    public <R, T> Conditions exists(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return exists(decide, columnName, value);
    }

    public <R, T> Conditions nin(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return nin(decide, columnName, value);
    }

    public <R, T> Conditions nin(boolean decide, IGetter<R, T> column, Object... values) {
        String columnName = Lambdas.fieldName(column);
        return nin(decide, columnName, values);
    }

    public <R, T> Conditions nin(boolean decide, IGetter<R, T> column, Collection<?> values) {
        String columnName = Lambdas.fieldName(column);
        return nin(decide, columnName, values);
    }

    public <R, T> Conditions nin(boolean decide, IGetter<R, T> column, Iterable<?> iterable) {
        String columnName = Lambdas.fieldName(column);
        return nin(decide, columnName, iterable);
    }

    public <R, T> Conditions nin(boolean decide, IGetter<R, T> column, Enumeration<?> enumeration) {
        String columnName = Lambdas.fieldName(column);
        return nin(decide, columnName, enumeration);
    }

    public <R, T> Conditions nlike(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return nlike(decide, columnName, value);
    }

    public <R, T> Conditions nexists(boolean decide, IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return nexists(decide, columnName, value);
    }

    //////////////////////////////////////////////////////////////////////////////
    public Conditions cond(Condition cond) {
        return cond(true, cond);
    }

    public Conditions cond(Object link, Object column, Object oper, Object value) {
        return cond(true, link, column, oper, value);
    }

    public Conditions cond(Object column, Object oper, Object value) {
        return cond(true, column, oper, value);
    }

    public Conditions cond(Object column, Object value) {
        return cond(true, column, value);
    }

    public <R, T> Conditions cond(Object link, IGetter<R, T> column, Object oper, Object value) {
        String columnName = Lambdas.fieldName(column);
        return cond(link, columnName, oper, value);
    }

    public <R, T> Conditions cond(IGetter<R, T> column, Object oper, Object value) {
        String columnName = Lambdas.fieldName(column);
        return cond(columnName, oper, value);
    }

    public <R, T> Conditions cond(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return cond(columnName, value);
    }

    //////////////////////////////////////////////////////////////////////////////
    public Conditions eq(Object column, Object value) {
        return eq(true, column, value);
    }

    public Conditions neq(Object column, Object value) {
        return neq(true, column, value);
    }

    public Conditions gt(Object column, Object value) {
        return gt(true, column, value);
    }

    public Conditions lt(Object column, Object value) {
        return lt(true, column, value);
    }

    public Conditions gte(Object column, Object value) {
        return gte(true, column, value);
    }

    public Conditions lte(Object column, Object value) {
        return lte(true, column, value);
    }

    public Conditions in(Object column, Object value) {
        return in(true, column, value);
    }

    public Conditions in(Object column, Object... values) {
        return in(true, column, values);
    }

    public Conditions in(Object column, Collection<?> values) {
        return in(true, column, values);
    }

    public Conditions in(Object column, Iterable<?> iterable) {
        return in(true, column, iterable);
    }

    public Conditions in(Object column, Enumeration<?> enumeration) {
        return in(true, column, enumeration);
    }

    public Conditions like(Object column, Object value) {
        return like(true, column, value);
    }

    public Conditions between(Object column, Object value) {
        return between(true, column, value);
    }

    public Conditions between(Object column, Object low, Object high) {
        return between(true, column, low, high);
    }

    public Conditions exists(Object column, Object value) {
        return exists(true, column, value);
    }

    public Conditions nin(Object column, Object value) {
        return nin(true, column, value);
    }

    public Conditions nin(Object column, Object... values) {
        return nin(true, column, values);
    }

    public Conditions nin(Object column, Collection<?> values) {
        return nin(true, column, values);
    }

    public Conditions nin(Object column, Iterable<?> iterable) {
        return nin(true, column, iterable);
    }

    public Conditions nin(Object column, Enumeration<?> enumeration) {
        return nin(true, column, enumeration);
    }

    public Conditions nlike(Object column, Object value) {
        return nlike(true, column, value);
    }

    public Conditions nexists(Object column, Object value) {
        return nexists(true, column, value);
    }


    public <R, T> Conditions eq(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return eq(columnName, value);
    }

    public <R, T> Conditions neq(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return neq(columnName, value);
    }

    public <R, T> Conditions gt(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return gt(columnName, value);
    }

    public <R, T> Conditions lt(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return lt(columnName, value);
    }

    public <R, T> Conditions gte(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return gte(columnName, value);
    }

    public <R, T> Conditions lte(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return lte(columnName, value);
    }

    public <R, T> Conditions in(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return in(columnName, value);
    }

    public <R, T> Conditions in(IGetter<R, T> column, Object... values) {
        String columnName = Lambdas.fieldName(column);
        return in(columnName, values);
    }

    public <R, T> Conditions in(IGetter<R, T> column, Collection<?> values) {
        String columnName = Lambdas.fieldName(column);
        return in(columnName, values);
    }

    public <R, T> Conditions in(IGetter<R, T> column, Iterable<?> iterable) {
        String columnName = Lambdas.fieldName(column);
        return in(columnName, iterable);
    }

    public <R, T> Conditions in(IGetter<R, T> column, Enumeration<?> enumeration) {
        String columnName = Lambdas.fieldName(column);
        return in(columnName, enumeration);
    }

    public <R, T> Conditions like(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return like(columnName, value);
    }

    public <R, T> Conditions between(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return between(columnName, value);
    }

    public <R, T> Conditions between(IGetter<R, T> column, Object low, Object high) {
        String columnName = Lambdas.fieldName(column);
        return between(columnName, low, high);
    }

    public <R, T> Conditions exists(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return exists(columnName, value);
    }

    public <R, T> Conditions nin(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return nin(columnName, value);
    }

    public <R, T> Conditions nin(IGetter<R, T> column, Object... values) {
        String columnName = Lambdas.fieldName(column);
        return nin(columnName, values);
    }

    public <R, T> Conditions nin(IGetter<R, T> column, Collection<?> values) {
        String columnName = Lambdas.fieldName(column);
        return nin(columnName, values);
    }

    public <R, T> Conditions nin(IGetter<R, T> column, Iterable<?> iterable) {
        String columnName = Lambdas.fieldName(column);
        return nin(columnName, iterable);
    }

    public <R, T> Conditions nin(IGetter<R, T> column, Enumeration<?> enumeration) {
        String columnName = Lambdas.fieldName(column);
        return nin(columnName, enumeration);
    }

    public <R, T> Conditions nlike(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return nlike(columnName, value);
    }

    public <R, T> Conditions nexists(IGetter<R, T> column, Object value) {
        String columnName = Lambdas.fieldName(column);
        return nexists(columnName, value);
    }
}
