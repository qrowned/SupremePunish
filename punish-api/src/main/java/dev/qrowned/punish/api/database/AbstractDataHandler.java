package dev.qrowned.punish.api.database;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractDataHandler<K, V> {

    protected final AbstractDataSource abstractDataSource;
    protected final AsyncLoadingCache<K, V> asyncLoadingCache;

    protected AbstractDataHandler(@NotNull AbstractDataSource abstractDataSource,
                                  @NotNull AsyncCacheLoader<K, V> asyncCacheLoader) {
        this.abstractDataSource = abstractDataSource;
        this.asyncLoadingCache = Caffeine.newBuilder()
                .buildAsync(asyncCacheLoader);
    }

    protected AbstractDataHandler(@NotNull AbstractDataSource abstractDataSource,
                                  @NotNull AsyncLoadingCache<K, V> asyncLoadingCache) {
        this.abstractDataSource = abstractDataSource;
        this.asyncLoadingCache = asyncLoadingCache;
    }

    public CompletableFuture<V> getData(@NotNull K k) {
        return this.asyncLoadingCache.get(k);
    }

    public void invalidate(@NotNull K k) {
        this.asyncLoadingCache.synchronous().invalidate(k);
    }

    public abstract void updateData(@NotNull K k, @NotNull V v);

    protected void insertData(@NotNull K k, @NotNull V v) {
        this.updateData(k, v);
    }

}
