package me.ablax.waters.utils;

/**
 * @author Murad Hamza on 29.5.2021 г.
 */
public class Pair<A, B> {
    private A a;
    private B b;

    public Pair(final A a, final B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public void setA(final A a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public void setB(final B b) {
        this.b = b;
    }
}
