package carcassonne.view.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

/**
 * Utility class for threading with AWT/Swing user interfaces.
 * @author Timur Saglam
 */
public final class ThreadingUtil {
    private ThreadingUtil() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Helps view classes to execute a task in the background where there is no result to be returned.
     * @param task is the task to execute.
     */
    public static void runInBackground(Runnable task) {
        SwingWorker<?, ?> worker = new SwingWorker<Boolean, Object>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                task.run();
                return true;
            }
        };
        worker.execute();
    }

    /**
     * Helps view classes to execute a task in the background and call-back on completion.
     * @param <T> is the return type of the task.
     * @param task is the task to be executed.
     * @param callback is the callback with the result on completion.
     */
    public static <T> void runAndCallback(Callable<T> task, Consumer<T> callback) {
        SwingWorker<T, ?> worker = new SwingWorker<T, Object>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.call();
            }

            @Override
            protected void done() {
                try {
                    callback.accept(get());
                } catch (InterruptedException | ExecutionException exception) {
                    GameMessage.showError(exception.getCause().getMessage());
                }
            }
        };
        worker.execute();
    }

    /**
     * Helps view classes to execute a task in the background and call-back on completion.
     * @param task is the task to be executed.
     * @param callback is the callback on completion.
     */
    public static void runAndCallback(Runnable task, Runnable callback) {
        runAndCallback(() -> {
            task.run();
            return true; // no return value required, true as default
        }, it -> callback.run());
    }
}
