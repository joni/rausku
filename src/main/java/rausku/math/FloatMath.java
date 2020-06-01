package rausku.math;

public class FloatMath {

    public static float PI = (float) Math.PI;

    public static float toRadians(float degrees) {
        return degrees * PI / 180;
    }

    public static float toDegrees(float radians) {
        return radians * 180 / PI;
    }

    public static float sin(float angle) {
        return ((float) Math.sin(angle));
    }

    public static float cos(float angle) {
        return ((float) Math.cos(angle));
    }

    public static float tan(float angle) {
        return ((float) Math.tan(angle));
    }

    public static float sqrt(float x) {
        return ((float) Math.sqrt(x));
    }

    public static float exp(float x) {
        return ((float) Math.exp(x));
    }

    public static float pow(float x, float y) {
        return ((float) Math.pow(x, y));
    }

    public static float abs(float x) {
        return Math.abs(x);
    }

    public static int ceil(float value) {
        return (int) Math.ceil(value);
    }

    public static int floor(float value) {
        return (int) Math.floor(value);
    }

    public static float hypot(float x, float y) {
        return (float) Math.hypot(x, y);
    }

    public static float lerp(float start, float end, float x) {
        return start * (1 - x) + end * x;
    }

    public static float smoothstep3(float start, float end, float x) {
        float blend = x * x * (3 - x * 2);
        return lerp(start, end, blend);
    }

    public static float smoothstep5(float start, float end, float x) {
        float blend = x * x * x * (10 - x * (15 - x * 6));
        return lerp(start, end, blend);
    }
}
