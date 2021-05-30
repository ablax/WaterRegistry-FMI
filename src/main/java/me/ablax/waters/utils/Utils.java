package me.ablax.waters.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Murad Hamza on 29.5.2021 г.
 */
public final class Utils {
    private Utils() {
        //utils
    }


    private static final Executor executor = Executors.newFixedThreadPool(4);


    public static void runAsync(final Runnable runnabble) {
        executor.execute(runnabble);
    }
}
