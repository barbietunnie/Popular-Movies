package com.xuxperience.popularmovies.listeners;

/**
 * Callback mechanism that enables the abstraction of our AsyncTasks
 * out into separate, re-usable and testable classes, and yet still retain a hook
 * back into the calling activity.
 *
 * Created by badeyemi on 5/3/17.
 */

public interface AsyncTaskCompleteListener<T> {
    /**
     * Invoked when the AsyncTask has completed its execution.
     *
     * @param result The resulting object from the AsyncTask
     */
    public void onTaskComplete(T result);
}
