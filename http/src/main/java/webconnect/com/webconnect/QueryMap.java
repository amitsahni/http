package webconnect.com.webconnect;

import android.util.SparseArray;
import android.util.SparseBooleanArray;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Allow duplicate keys
 *
 * @param <K>
 * @param <V>
 */
public class QueryMap<K, V> extends LinkedHashMap<K, V> {

    private SparseArray<K> key = new SparseArray<>();
    private SparseArray<V> value = new SparseArray<>();
    private AtomicInteger autoIncrement = new AtomicInteger();

    @Override
    public V put(K key, V value) {
        int auto = autoIncrement.getAndIncrement();
        this.key.put(auto, key);
        this.value.put(auto, value);
        return super.put(key, value);
    }

    public SparseArray<K> getKey() {
        return key;
    }

    public SparseArray<V> getValue() {
        return value;
    }
}
