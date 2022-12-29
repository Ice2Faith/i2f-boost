package test;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ltb
 * @date 2022/3/28 16:00
 * @desc
 */
public class TestRegex {
    public static void main(String[] args) {
        List<Integer> collect = Stream.of(1, 2, 3)
                .collect(Collectors.toList());
    }

}
