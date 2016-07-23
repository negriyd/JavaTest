package org.javatest.cachemap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by negriyd on 23.07.2016.
 */
public class CacheMapImpl<KeyType, ValueType> implements CacheMap<KeyType, ValueType> {

    private Map<KeyType, ExpirableValue<ValueType>> map = new HashMap();
    private long timeToLive;

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public long getTimeToLive() {
        return this.timeToLive;
    }

    public ValueType put(KeyType key, ValueType value) {
        clearExpired();
        ExpirableValue<ValueType> expirableValue = new ExpirableValue<ValueType>(value);
        ExpirableValue<ValueType> result = map.put(key, expirableValue);
        return result != null?result.getValue():null;
    }

    public void clearExpired() {
        List<KeyType> expiredKeys = new LinkedList<KeyType>();
        map.forEach((k,v) -> {
            if (isExpired(v.getCreateTime()))
                expiredKeys.add(k);
        });
        expiredKeys.forEach(key -> map.remove(key));
    }

    public void clear() {
        map.clear();
    }
    public boolean containsKey(Object key) {
        return getNotExpiredMap().containsKey(key);
    }

    public boolean containsValue(Object value) {
        return getNotExpiredMap().containsValue(value);
    }

    public ValueType get(Object key) {
        return getNotExpiredMap().get(key);
    }

    public boolean isEmpty() {
        return getNotExpiredMap().isEmpty();
    }

    public ValueType remove(Object key) {
        ExpirableValue<ValueType> result = map.remove(key);
        return result == null?null:result.getValue();
    }

    public int size() {
        return getNotExpiredMap().size();
    }

    private Map<KeyType, ValueType> getNotExpiredMap() {
        return map.entrySet().stream().filter(p -> !isExpired(p.getValue().getCreateTime()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue().getValue()));
    }

    private boolean isExpired(long time) {
        return Clock.getTime() - time > getTimeToLive();
    }
}
