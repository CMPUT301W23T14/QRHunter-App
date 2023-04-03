package com.example.qrhunter.data.repository;

/**
 * Interface containing a callback method used in methods in repository classes.
 * Read more about it here https://stackoverflow.com/questions/48499310/how-to-return-a-documentsnapshot-as-a-result-of-a-method?noredirect=1&lq=1.
 * Author: Alex Mamo
 *
 * @param <T> The type of the result you're expecting
 */
public interface RepositoryCallback<T> {
    /**
     * The callback method to be called
     *
     * @param result The result of the callback method
     */
    void onSuccess(T result);
}
