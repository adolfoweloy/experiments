package au.com.aeloy;

import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;

public class Application {

    private static final int SIZE = 10000;

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);

        foo(sc);

        // wait the user to type exit to stop the execution
        waitOrExit(sc, "main");
    }

    private static void foo(Scanner sc) throws InterruptedException {

        waitOrExit(sc, "begin");

        // big list of big integers
        List<BigInteger> codes = LongStream.range(0, SIZE)
                .mapToObj(BigInteger::valueOf)
                .collect(toList());

        // creates a big object composed of lots of list of BigIntegers
        ComplexBigObject big = new ComplexBigObject();
        int multiple = 10;
        for (int i =0; i<SIZE; i++) {
            if (i % multiple == 0) {
                Thread.sleep(TimeUnit.MILLISECONDS.toSeconds(5));
                multiple = multiple * 10;
            }
        }

        IntStream.range(0, SIZE).forEach(i -> big.add(new Brick(codes)));

        // wait within the stack
        waitOrExit(sc, "foo");
    }

    private static void waitOrExit(Scanner sc, String functionName) {
        System.out.println("Type 'q' to exit from " + functionName);
        String result = "";
        while (!"q".equals(result)) {
            result = sc.next();
        }
    }

}
