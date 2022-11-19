package utils;

import java.math.BigInteger;

public class PrimeGenerator {

    public static void main(String[] args) {

        System.out.println(
                BigInteger.ZERO + " compare " + BigInteger.ONE + " " + BigInteger.ZERO.compareTo(BigInteger.ONE));
        ;

        for (BigInteger a : BigInteger.TEN.divideAndRemainder(BigInteger.TEN)) {
            System.out.println(a);
        }

        BigInteger multi = BigInteger.valueOf(1);
        for (int i = 1; i < 200; i++) {
            multi = multi.multiply(BigInteger.valueOf(2));
            BigInteger num = multi.add(BigInteger.valueOf(1));
            if (isPrime(num)) {
                System.out.println(num);
            }
        }
    }

    private static boolean isPrime(BigInteger num) {
        BigInteger[] res;
        for (BigInteger i = BigInteger.valueOf(2); i.compareTo(num.divide(BigInteger.valueOf(2))) < 0; i = i
                .add(BigInteger.ONE)) {
            res = num.divideAndRemainder(i);
            if (res[1].compareTo(BigInteger.ZERO) == 0) {
                return false;
            }
        }
        return true;
    }
}
