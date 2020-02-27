package com.wsq.sponge.policy;

import com.wsq.sponge.Sponge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;

public class Lru extends Sponge {
    private int maxNum;

    //用来记录节点，使得遍历链表复杂度降低到O(1)
    private ConcurrentHashMap<String, Link.Entry> tmpMap;
    private Link link;

    public Lru(int maxNum) {
        this.maxNum = maxNum;
        tmpMap = new ConcurrentHashMap<>(maxNum);
        link = new Link();
    }

    public Object get(String key) {
        Link.Entry entry = tmpMap.get(key);
        link.remove(entry);
        link.insertFirst(entry);

        return entry.value;
    }

    public void set(String key, Object value) {
        if (tmpMap.containsKey(key)) {
            link.remove(tmpMap.get(key));
            link.insertFirst(tmpMap.get(key));
            return;
        }

        if (tmpMap.size() >= maxNum) {
            link.remove(link.tail.before);
        }

        Link.Entry entry = link.new Entry(null, null, key, value);
        link.insertFirst(entry);
        tmpMap.put(key, entry);
    }
}

//双向链表
class Link {
    Entry head;
    Entry tail;

    public Link() {
        head = new Entry();
        tail = new Entry();
        head.setAfter(tail);
        tail.setBefore(head);
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Entry {
        Entry before;
        Entry after;
        String key;
        Object value;
    }

    //此部分比较麻烦，由于是双向链表所以，修改指针时候要注意前后节点都要修改
    public void insertFirst(Entry entry) {
        entry.after = head.after;
        entry.after.before = entry;
        entry.before = head;
        head.after = entry;
        if (tail.before == head) {
            tail.before = entry;
        }
    }

    public void remove(Entry entry) {
        entry.before.after = entry.after;
        entry.after.before = entry.before;
    }
}