package cn.yescallop.math.number;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class Rational extends Number implements Comparable<Rational> {

    public static final Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);
    public static final Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);

    private final BigInteger numerator;
    private final BigInteger denominator;

    private Rational(BigInteger numerator, BigInteger denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static Rational of(BigInteger numerator, BigInteger denominator) {
        if (denominator.signum() == 0)
            throw new ArithmeticException("zero denominator");

        if (numerator.signum() == 0)
            return ZERO;

        if (numerator.equals(denominator))
            return ONE;

        if (denominator.signum() < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }

        if (!numerator.abs().equals(BigInteger.ONE) && !denominator.equals(BigInteger.ONE)) {
            BigInteger gcd = numerator.gcd(denominator);
            if (!gcd.equals(BigInteger.ONE)) {
                numerator = numerator.divide(gcd);
                denominator = denominator.divide(gcd);
            }
        }
        return new Rational(numerator, denominator);
    }

    public static Rational of(BigInteger val) {
        return new Rational(val, BigInteger.ONE);
    }

    public static Rational of(long val) {
        return of(BigInteger.valueOf(val));
    }

    public static Rational of(BigDecimal val) {
        return of(val.unscaledValue(), BigInteger.TEN.pow(val.scale()));
    }

    public static Rational of(double val) {
        return of(BigDecimal.valueOf(val));
    }

    public static Rational of(String val) {
        String[] s = val.split("/");
        if (s.length == 1)
            return of(new BigDecimal(val));

        if (s.length == 2)
            return of(new BigInteger(s[0]), new BigInteger(s[1]));

        throw new NumberFormatException("invalid rational");
    }

    @Override
    public Number add(Number val) {
        if (!(val instanceof Rational))
            return val.add(this);

        if (((Rational) val).signum() == 0)
            return this;

        BigInteger gcd = denominator.gcd(((Rational) val).denominator);
        return of(
                numerator.multiply(((Rational) val).denominator.divide(gcd))
                        .add(((Rational) val).numerator.multiply(denominator.divide(gcd))),
                denominator.multiply(((Rational) val).denominator)
                        .divide(gcd)
        );
    }

    @Override
    public Number subtract(Number val) {
        if (!(val instanceof Rational))
            return val.subtract(this);

        if (((Rational) val).signum() == 0)
            return this;

        if (signum() == 0)
            return ((Rational) val).negate();

        BigInteger gcd = denominator.gcd(((Rational) val).denominator);
        return of(
                numerator.multiply(((Rational) val).denominator.divide(gcd))
                        .subtract(((Rational) val).numerator.multiply(denominator.divide(gcd))),
                denominator.multiply(((Rational) val).denominator)
                        .divide(gcd)
        );
    }

    @Override
    public Number multiply(Number val) {
        if (!(val instanceof Rational))
            return val.multiply(this);

        if (signum() == 0 || ((Rational) val).signum() == 0)
            return ZERO;

        return of(
                numerator.multiply(((Rational) val).numerator),
                denominator.multiply(((Rational) val).denominator)
        );
    }

    @Override
    public Number divide(Number val) {
        if (!(val instanceof Rational))
            return val.divide(this);

        if (((Rational) val).signum() == 0)
            throw new ArithmeticException("Division by zero");

        if (this.signum() == 0)
            return ZERO;

        return of(
                numerator.multiply(((Rational) val).denominator),
                denominator.multiply(((Rational) val).numerator)
        );
    }

    @Override
    public Number pow(int exp) {
        return new Rational(numerator.pow(exp), denominator.pow(exp));
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    public boolean isInteger() {
        return denominator.equals(BigInteger.ONE);
    }

    public int signum() {
        return numerator.signum();
    }

    public Rational negate() {
        return new Rational(numerator.negate(), denominator);
    }

    public Rational abs() {
        return signum() < 0 ? new Rational(numerator.negate(), denominator) : this;
    }

    public Rational reverse() {
        if (signum() == 0)
            return ZERO;
        return new Rational(signum() > 0 ? denominator : denominator.negate(), numerator.abs());
    }

    @Override
    public int compareTo(Rational o) {
        if (signum() == o.signum()) {
            BigInteger gcd = denominator.gcd(o.denominator);
            return numerator.multiply(o.denominator.divide(gcd))
                    .compareTo(o.numerator.multiply(denominator.divide(gcd)));
        }
        return signum() > o.signum() ? 1 : -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Rational))
            return false;

        return ((Rational) obj).numerator.equals(numerator) &&
                ((Rational) obj).denominator.equals(denominator);
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
