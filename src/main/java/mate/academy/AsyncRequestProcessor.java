package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> userDataMap = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (userDataMap.containsKey(userId)) {
            return CompletableFuture.completedFuture(userDataMap.get(userId));
        }

        return CompletableFuture.supplyAsync(
                () -> new UserData(userId, "Details for" + userId), executor)
                .thenApply(userData -> {
                    userDataMap.put(userId, userData);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return userData;
                });
    }
}
