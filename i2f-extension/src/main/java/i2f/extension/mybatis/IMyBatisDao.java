package i2f.extension.mybatis;

public interface IMyBatisDao<T,E>{
    E toDo(T dao,Object ... params);
}
