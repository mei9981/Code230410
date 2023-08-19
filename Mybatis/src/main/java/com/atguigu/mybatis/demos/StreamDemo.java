package com.atguigu.mybatis.demos;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Smexy on 2023/8/19
 */
public class StreamDemo
{
    public static void main(String[] args) {

        //把一个集合的数字 加10，再挑选出偶数
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);

        Stream<Integer> s3 = Stream.of(1, 2, 3, 4, 5, 6);

        int [] nums = {1,2,3,4,5,6};
        IntStream s2 = Arrays.stream(nums);
        //有一个Stream对象。 只要是Collection类型，提供方法可以直接转换为Stream
        Stream<Integer> s1 = list.stream();

        //只留下返回true的元素
        List<Integer> result = s1
            .map(x -> x + 10)
            .filter(x -> x % 2 == 0)
            .collect(Collectors.toList());

        System.out.println(result);

    }
}
