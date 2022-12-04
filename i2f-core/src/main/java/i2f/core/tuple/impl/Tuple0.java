package i2f.core.tuple.impl;

import i2f.core.collection.Collections;
import i2f.core.tuple.ITuple;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class Tuple0 implements ITuple {

    @Override
    public List<Object> toList() {
        return Collections.arrayList();
    }
}
