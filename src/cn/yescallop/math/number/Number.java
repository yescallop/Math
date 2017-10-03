package cn.yescallop.math.number;

public abstract class Number implements Cloneable {

    public abstract Number add(Number val);

    public abstract Number subtract(Number val);

    public abstract Number multiply(Number val);

    public abstract Number divide(Number val);

    public abstract Number pow(int exp);

    @Override
    public Number clone() {
        try {
            return (Number) super.clone();
        } catch (CloneNotSupportedException e){
            return null;
        }
    }
}
