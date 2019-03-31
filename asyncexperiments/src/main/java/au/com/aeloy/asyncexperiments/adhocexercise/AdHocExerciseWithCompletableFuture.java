package au.com.aeloy.asyncexperiments.adhocexercise;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;

public class AdHocExerciseWithCompletableFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        System.out.println("Main thread: " + currentThread().getName());

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try {
                return calculate();
            } catch (InterruptedException e) {
                return CompletableFuture.completedFuture(0);
            }
        }).thenAcceptAsync((result) -> {
            System.out.println("Using the result " + result + " in " + currentThread().getName());
        });

        System.out.println("the end: " + currentThread().getName());
        future.get();
    }

    private static CompletableFuture<Integer> calculate() throws InterruptedException {
        System.out.println("calculate is running on " + currentThread().getName());

        CompletableFuture<Integer> result = new CompletableFuture<>();
        sleep(SECONDS.toMillis(10));
        result.complete(10);

        return result;
    }

}
