package i2f.core.type.tuple.impl;

import i2f.core.container.collection.CollectionUtil;
import i2f.core.type.tuple.ITuple;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class Tuple0 implements ITuple {

    @Override
    public List<Object> toList() {
        return CollectionUtil.arrayList();
    }
}
