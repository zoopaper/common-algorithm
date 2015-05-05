package net.snails.common.algorithm.cache.random;

import java.util.Random;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/5
 * Time: 14:09
 */
public class RandomUtil {


    public static long getRandomLong() {

        Random random = new Random();
        Long l = random.nextLong();

        return l;
    }


    public static int getRandomInt() {
        Random random = new Random();

        return random.nextInt();
    }

    public static float getRandomFloat() {
        Random random = new Random();

        return random.nextFloat();
    }


    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            System.out.println( ( getRandomFloat()));
        }

    }
}
