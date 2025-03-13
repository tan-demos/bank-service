package com.bank.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

public class ObjectPool<T> {
    private static final int DEFAULT_INIT_SIZE = 1024;
    private final int maxSize;
    private final LinkedBlockingQueue<T> blockingQueue;
    private final Supplier<T> factory;

    public ObjectPool(Supplier<T> factory) {
        this(factory, 0);
    }
    public ObjectPool(Supplier<T> factory, int size) {
        int n = DEFAULT_INIT_SIZE;
        if (size > 0) {
            this.maxSize = size;
            n = size;
        } else {
            this.maxSize = 0;
        }

        this.factory = factory;

        blockingQueue = new LinkedBlockingQueue<>();
        for (int i = 0; i < n; i++) {
            blockingQueue.offer(factory.get());
        }
    }

    public T borrowObject() {
        if (blockingQueue.isEmpty()) {
            if (this.maxSize <= 0) {
                blockingQueue.offer(factory.get());
            }
        }
        return blockingQueue.poll();
    }

    public void returnObject(T e) {
        blockingQueue.offer(e);
    }

    public int size() {
        return blockingQueue.size();
    }
}
