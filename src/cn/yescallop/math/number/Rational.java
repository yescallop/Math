package cn.yescallop.math.number;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Rational implements Number {

    public static final Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);

    private final BigInteger numerator;
    private final BigInteger denominator;

    public Rational(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO))
            throw new ArithmeticException("zero denominator");

        if (numerator.equals(denominator)) {
            numerator = denominator = BigInteger.ONE;
        } else {
            BigInteger gcd = numerator.gcd(denominator);
            if (gcd.equals(BigInteger.ONE)) {
                numerator = numerator;
                denominator = denominator;
            } else {
                numerator = numerator.divide(gcd);
                denominator = denominator.divide(gcd);
            }
            if (denominator.signum() < 0) {
                numerator = numerator.negate();
                denominator = denominator.negate();
            }
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static Rational of(BigInteger val) {
        return new Rational(val, BigInteger.ONE);
    }

    public static Rational of(long val) {
        return of(BigInteger.valueOf(val));
    }

    public static Rational of(BigDecimal val) {
        return new Rational(val.unscaledValue(), BigInteger.valueOf(val.scale() * 10));
    }

    public static Rational of(double val) {
        return of(BigDecimal.valueOf(val));
    }

    public static Rational of(String val) {
        return of(new BigDecimal(val));
    }

    @Override
    public Number add(Number val) {
        if (!(val instanceof Rational))
            return val.add(this);
        BigInteger gcd = denominator.gcd(((Rational) val).denominator);
        return new Rational(
                numerator.multiply(((Rational) val).denominator.divide(gcd)).add(
                        ((Rational) val).numerator.multiply(denominator.divide(gcd))),
                denominator.multiply(((Rational) val).denominator)
                        .divide(gcd)
        );
    }

    @Override
    public Number subtract(Number val) {
        if (!(val instanceof Rational))
            return val.subtract(this);
        BigInteger gcd = denominator.gcd(((Rational) val).denominator);
        return new Rational(
                numerator.multiply(((Rational) val).denominator.divide(gcd)).subtract(
                        ((Rational) val).numerator.multiply(denominator.divide(gcd))),
                denominator.multiply(((Rational) val).denominator)
                        .divide(gcd)
        );
    }

    @Override
    public Number multiply(Number val) {
        if (!(val instanceof Rational))
            return val.multiply(this);
        return new Rational(
                numerator.multiply(((Rational) val).numerator),
                denominator.multiply(((Rational) val).denominator)
        );
    }

    @Override
    public Number divide(Number val) {
        if (!(val instanceof Rational))
            return val.divide(this);
        return new Rational(
                numerator.multiply(((Rational) val).denominator),
                denominator.multiply(((Rational) val).numerator)
        );
    }

    @Override
    public String toString() {
        if (denominator.equals(BigInteger.ONE)) {
            return numerator.toString();
        } else {
            return numerator.toString() + "/" + denominator.toString();
        }
    }
}
