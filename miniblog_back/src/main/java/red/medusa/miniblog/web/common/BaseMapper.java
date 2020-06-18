package red.medusa.miniblog.web.common;

import java.io.Serializable;
import java.util.List;

public interface BaseMapper<T,K extends Serializable> {
    int insert(T t);
    int update(T t);
    int delete(K k);
    T findById(K k);
    List<T> findAll();
}
