package i2f.core.data;

import i2f.core.annotations.remark.Author;
import lombok.Data;
import lombok.NoArgsConstructor;

@Author("i2f")
@Data
@NoArgsConstructor
public class Triple<K, E, V> {
    public K fst;
    public E sec;
    public V trd;
    public Object tag;

    public Triple(K fst, E sec, V trd) {
        this.fst = fst;
        this.sec = sec;
        this.trd = trd;
    }

    public Triple(K fst, E sec, V trd, Object tag) {
        this.fst = fst;
        this.sec = sec;
        this.trd = trd;
        this.tag = tag;
    }
}
