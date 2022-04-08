package i2f.core.thread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * @author ltb
 * @date 2022/3/26 12:47
 * @desc
 */
public class ForkJoinUtil {
    private static volatile ForkJoinPool defaultPool=new ForkJoinPool();

    public static<T> T submit(RecursiveTask<T> task,ForkJoinPool pool){
        return pool.invoke(task);
    }

    public static<T> T submit(RecursiveTask<T> task){
        return submit(task,defaultPool);
    }

    public static void execute(RecursiveAction action,ForkJoinPool pool){
        pool.invoke(action);
    }

    public static void execute(RecursiveAction action){
        execute(action,defaultPool);
    }
}
