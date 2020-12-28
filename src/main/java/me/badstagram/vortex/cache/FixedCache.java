/*
 MIT License

 Copyright (c) 2020 badstagram

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.

 */

package me.badstagram.vortex.cache;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FixedCache<K, V> {

    private final Map<K, V> map;
    private final K[] keys;
    private int currIndex = 0;

    @SuppressWarnings("unchecked")
    public FixedCache(int size) {
        this.map = new HashMap<>();
        if (size < 1) {
            throw new IllegalArgumentException("Cache size must be at least 1");
        }
        this.keys = (K[]) new Object[size];
    }

    public V put(K key, V value) {
        if (map.containsKey(key)) {
            return map.put(key, value);
        }
        if (keys[currIndex] != null) {
            map.remove(keys[currIndex]);
        }
        keys[currIndex] = key;
        currIndex = (currIndex + 1) % keys.length;
        return map.put(key, value);
    }

    public V pull(K key) {
        return map.remove(key);
    }

    public V get(K key) {
        return map.get(key);
    }

    public boolean contains(K key) {
        return map.containsKey(key);
    }

    public Collection<V> getValues() {
        return map.values();
    }

}
