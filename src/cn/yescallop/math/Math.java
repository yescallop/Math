package cn.yescallop.math;

import java.math.BigInteger;

public class Math {

    private Math() {
        //no instance
    }

    public static final BigInteger BI_TWO = BigInteger.valueOf(2);
    public static final BigInteger BI_THREE = BigInteger.valueOf(3);
    public static final BigInteger BI_FOUR = BigInteger.valueOf(4);
    public static final BigInteger BI_SIX = BigInteger.valueOf(6);
    public static final BigInteger BI_NINE = BigInteger.valueOf(9);

    private static final double LOG2 = java.lang.Math.log(2);
    private static final double LOG3 = java.lang.Math.log(3);

    /**
     * Returns the value a^n, the nth power of a.
     * A recursive hack for BigInteger exponent.
     */
    public static BigInteger pow(BigInteger a, BigInteger n) {
        if (n.equals(BigInteger.ONE))
            return a;
        BigInteger tmp = pow(a, n.shiftRight(1));
        tmp = tmp.multiply(tmp);
        if (n.testBit(0))
            tmp = tmp.multiply(a);
        return tmp;
    }

    /**
     * Returns the value φ(n), the Euler's totient function value of n.
     */
    public static int euler(int n) {
        if (n == 1)
            return 1;
        int res = 1;
        for (int i = 2; i < n; i++) {
            if (gcd(n, i) == 1)
                res++;
        }
        return res;
    }

    /**
     * Returns the greatest common divisor of a, b.
     * Uses the Euclidean algorithm.
     */
    public static int gcd(int a, int b) {
        while (b != 0) {
            int r = a;
            a = b;
            b = r % b;
        }
        return a;
    }

    /**
     * Returns the multiplicative order of a modulo n.
     */
    public static int ord(BigInteger a, int n) {
        BigInteger _n = BigInteger.valueOf(n);
        for (int k = 0; ; k++) {
            if (a.pow(k).mod(_n).equals(BigInteger.ONE))
                return k;
        }
    }

    /**
     * Returns if integer a (odd) is a perfect power.
     */
    public static boolean isPerfectPower(BigInteger a) {
        if (intSqrt(a) != null)
            return true;
        double maxi = log2(a);
        for (int i = 3; i <= maxi; i += 2) {
            if (intNthRootOdd(a, i) != null)
                return true;
        }
        return false;
    }

    /**
     * Returns the integer nth (n > 1 and n is odd) root
     * of a (odd) if it has one, or else returns null.
     * Uses 2-adic Newton iteration.
     */
    public static BigInteger intNthRootOdd(BigInteger a, int n) {
        int k = (int) java.lang.Math.ceil(log2(a) / n);
        int r = (int) java.lang.Math.ceil(log2(k));
        if (r < 1) return null;
        BigInteger g = BigInteger.ONE;
        BigInteger s = BigInteger.ONE;
        BigInteger m = BI_FOUR;
        BigInteger t = BigInteger.ONE;
        BigInteger _n = BigInteger.valueOf(n);
        for (int i = 1; i < r; i++) {
            g = g.subtract(
                    g.multiply(t)
                            .subtract(a)
                            .multiply(s)
            ).mod(m);
            BigInteger mSquare = m.pow(2);
            t = g.pow(n - 1).remainder(mSquare);
            s = s.shiftLeft(1)
                    .subtract(
                            _n.multiply(t).multiply(s.pow(2))
                    ).mod(m);
            m = mSquare;
        }
        g = g.subtract(
                g.multiply(t)
                        .subtract(a)
                        .multiply(s)
        ).mod(BI_TWO.pow(k));
        return g.pow(n).equals(a) ? g : null;
    }

    /**
     * Returns the integer square root of a if it has one, or else returns null.
     * Uses 3-adic Newton iteration.
     */
    public static BigInteger intSqrt(BigInteger a) {
        if (a.mod(BI_THREE).equals(BI_TWO))
            return null;
        BigInteger[] dr;
        while ((dr = a.divideAndRemainder(BI_NINE))[1].equals(BigInteger.ZERO)) {
            a = dr[0];
        }
        if (dr[1].equals(BI_THREE) || dr[1].equals(BI_SIX))
            return null;
        int k = (int) java.lang.Math.ceil(log3(a) / 2);
        int r = (int) java.lang.Math.ceil(log2(k));
        if (r < 1) return null;
        BigInteger g = BigInteger.ONE;
        BigInteger s = BI_TWO;
        BigInteger m = BI_NINE;
        for (int i = 1; i < r; i++) {
            g = g.subtract(g.pow(2).subtract(a).multiply(s))
                    .mod(m);
            s = s.subtract(g.multiply(s.pow(2)))
                    .shiftLeft(1)
                    .mod(m);
            m = m.pow(2);
        }
        m = BI_THREE.pow(k);
        g = g.subtract(
                g.pow(2)
                        .subtract(a)
                        .multiply(s)
        ).mod(m);
        return g.pow(2).equals(a) || (g = m.subtract(g)).pow(2).equals(a) ? g : null;
    }

    /**
     * Returns the the base 2 logarithm of n.
     */
    public static double log2(BigInteger n) {
        return log(n) / LOG2;
    }

    /**
     * Returns the the base 2 logarithm of n.
     */
    private static double log2(double n) {
        return java.lang.Math.log(n) / LOG2;
    }

    /**
     * Returns the the base 3 logarithm of n.
     */
    public static double log3(BigInteger n) {
        return log(n) / LOG3;
    }

    /**
     * Returns the value ln n, the natural logarithm of n.
     */
    public static double log(BigInteger n) {
        int b = n.bitLength() - 1022;
        if (b > 0)
            n = n.shiftRight(b);
        double res = java.lang.Math.log(n.doubleValue());
        return b > 0 ? res + b * LOG2 : res;
    }

    /**
     * Returns the value C(n, k), the binomial coefficient of n, k.
     */
    public static BigInteger binomialCoef(int n, int k) {
        return factorial(n).divide(factorial(k))
                .divide(factorial(n - k));
    }

    /**
     * Returns the value n!, the factorial of n.
     */
    public static BigInteger factorial(int n) {
        BigInteger res = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            res = res.multiply(BigInteger.valueOf(i));
        }
        return res;
    }
}
