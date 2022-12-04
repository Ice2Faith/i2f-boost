package i2f.core.functional.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.functional.common.IFilter;

import java.util.Random;

/**
 * @author ltb
 * @date 2022/3/25 14:07
 * @desc
 */
@Author("i2f")
public class RandomFilter<T> implements IFilter<T> {
    private Random rand;
    public RandomFilter(){
        rand=new Random(System.currentTimeMillis());
    }
    public RandomFilter(long seed){
        rand=new Random(seed);
    }

    public RandomFilter(Random rand) {
        this.rand = rand;
    }

    @Override
    public boolean test(T val) {
        return rand.nextBoolean();
    }
}
