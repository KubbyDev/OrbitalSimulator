package tools.test;

import java.math.BigDecimal;

public class Test {

    public static void main(String... args) {

        long start = System.nanoTime();

        double number = 3.0;
        for(int i = 0; i < 3000000; i++)
            number += 0.0001;

        System.out.println("Time with doubles: " + (double)(System.nanoTime() - start)/1e9);
        start = System.nanoTime();

        float number3 = 3.0f;
        for(int i = 0; i < 3000000; i++)
            number3 += 0.0001f;

        System.out.println("Time with floats: " + (double)(System.nanoTime() - start)/1e9);
        start = System.nanoTime();

        BigDecimal number4 = new BigDecimal(0.0001);
        BigDecimal number2 = new BigDecimal(3.0);
        for(int i = 0; i < 3000000; i++)
            number2 = number4.add(number2);

        System.out.println("Time with BigDecimals: " + (double)(System.nanoTime() - start)/1e9);
    }
}
