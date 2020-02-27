package com.wsq.sponge;

import com.wsq.sponge.policy.Cache;
import com.wsq.sponge.policy.Lru;

/**
 * @Description:
 * @Author: sqwang
 * @Date:
 */
public abstract class Sponge {

    public abstract Object get(String key);

    public abstract void set(String key, Object value);

    public static Sponge builderLru(int maxNum) {
        return new Lru(maxNum);
    }

    public static Sponge builderCache() {
        return new Cache();
    }
}