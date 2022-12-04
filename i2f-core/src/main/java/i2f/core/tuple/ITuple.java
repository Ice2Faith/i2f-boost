package i2f.core.tuple;

import java.io.Serializable;
import java.util.List;


public interface ITuple extends Serializable {
    List<Object> toList();
}
