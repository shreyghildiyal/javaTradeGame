package utils;

public class Time {
    
    public static float timeStarted = System.nanoTime();

    public static float getTimeNanoSeconds() {
        return System.nanoTime() - timeStarted;
    }

    public static float getTimeSeconds() {
        return getTimeNanoSeconds() / 1E9f;
    }
}
