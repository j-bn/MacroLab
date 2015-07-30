package uk.me.jamesthornton.macrolab;

public class Vector2 {
    public double x, y;

    //Constructors
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    //Creates a zero vector
    public Vector2() {
        this.x = 0;
        this.y = 0;
    }
    //Copy constructor
    public Vector2(Vector2 source) {
        this.x = source.x;
        this.y = source.y;
    }

    //Mutable methods
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void set(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    //Mutable vector arithmetic methods
    //[TODO] Should use static methods?
    public Vector2 add(Vector2 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }
    public Vector2 subtract(Vector2 v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }
    public Vector2 multiply(Vector2 v) {
        this.x *= v.x;
        this.y *= v.y;
        return this;
    }
    public Vector2 divide(Vector2 v) {
        this.x /= v.x;
        this.y /= v.y;
        return this;
    }

    //Mutable scalar arithmetic methods
    public Vector2 add(double a) {
        this.x += a;
        this.y += a;
        return this;
    }
    public Vector2 subtract(double a) {
        this.x -= a;
        this.y -= a;
        return this;
    }
    public Vector2 multiply(double a) {
        this.x *= a;
        this.y *= a;
        return this;
    }
    public Vector2 divide(double a) {
        this.x /= a;
        this.y /= a;
        return this;
    }

    //Static methods
    public static Vector2 add(Vector2 a, Vector2 b) {
        return new Vector2(a.x + b.x, a.y + b.y);
    }
    public static Vector2 subtract(Vector2 a, Vector2 b) {
        return new Vector2(a.x - b.x, a.y - b.y);
    }
    public static Vector2 multiply(Vector2 a, Vector2 b) {
        return new Vector2(a.x * b.x, a.y * b.y);
    }
    public static Vector2 divide(Vector2 a, Vector2 b) {
        return new Vector2(a.x / b.x, a.y / b.y);
    }

    public static double distance(Vector2 a, Vector2 b) {
        return subtract(a, b).getMagnitude();
    }

    //Getters
    public int getIntX() {
        return (int) Math.round(x);
    }
    public int getIntY() {
        return (int) Math.round(y);
    }
    public double getMagnitude() {
        return Math.sqrt(x * x + y * y);
    }
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    //Named static constructors
    public static Vector2 unitAngle(double a) {
        //[TODO]
        return new Vector2((double) Math.cos(a), (double) Math.sin(a));
    }

    //Clone
    public Vector2 clone() {
        return new Vector2(this);
    }

}
