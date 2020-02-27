package com.wsq.sponge.policy;

import com.wsq.sponge.Sponge;
import lombok.Getter;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: sqwang
 * @Date:
 */
public class Cache extends Sponge {
    private final ReferenceQueue queue = new ReferenceQueue();

    private Map<Integer, Entry> repository = new ConcurrentHashMap<>();

    public void set(String key, Object value) {
        cleanCache();
        Entry entry = new Entry(key, value, queue);
        repository.put(key.hashCode(), entry);
    }

    public Object get(String key) {
        cleanCache();
        Entry entry = repository.get(key.hashCode());
        return entry == null ? null : entry.getValue();
    }

    private void cleanCache() {
        Entry entry;
        while ((entry = (Entry) queue.poll()) != null) {
            repository.remove(entry.getHash());
        }
    }

    @Getter
    static class Entry extends WeakReference<String> {
        Object value;
        Integer hash;

        Entry(String key, Object value, ReferenceQueue<String> queue) {
            super(key, queue);
            this.hash = key.hashCode();
            this.value = value;
        }
    }
}