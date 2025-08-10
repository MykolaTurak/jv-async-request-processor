package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> userDataMap = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        UserData cached = userDataMap.get(userId);
        if (userDataMap.containsKey(userId)) {
            return CompletableFuture.completedFuture(cached);
        }

        return CompletableFuture
                .supplyAsync(
                    () -> new UserData(userId, "Details for" + userId), executor)
                    .thenApply(userData -> {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        userDataMap.put(userId, userData);
                        CompletableFuture.delayedExecutor(400, TimeUnit.MILLISECONDS);
                        return userData;
                    });
    }
}
