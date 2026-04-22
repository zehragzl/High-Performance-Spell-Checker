package DataStructures;

/**
 * A generic hash map implementation using open addressing with quadratic probing.
 *
 * <p>This hash map is built entirely from scratch without relying on
 * {@code java.util} collections. It uses a custom hash function with bit
 * manipulation to minimize clustering, prime-sized table capacities for
 * uniform distribution, and lazy deletion (tombstone markers) for efficient
 * removal operations.</p>
 *
 * <h3>Collision Resolution Strategy</h3>
 * <p>Quadratic probing with a {@code k += 2} increment pattern is used to
 * resolve collisions. Combined with prime-sized tables, this ensures all
 * positions in the table are reachable before any position is revisited.</p>
 *
 * <h3>Load Factor &amp; Rehashing</h3>
 * <p>The table maintains a maximum load factor of 0.5. When exceeded, the
 * table is rehashed to the next prime number greater than double the current
 * capacity, ensuring O(1) amortized insertion.</p>
 *
 * <h3>Complexity Summary</h3>
 * <ul>
 *   <li>{@code put}    — O(1) amortized, O(n) worst case</li>
 *   <li>{@code get}    — O(1) average, O(n) worst case</li>
 *   <li>{@code remove} — O(1) average, O(n) worst case</li>
 *   <li>{@code rehash} — O(n)</li>
 * </ul>
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author Zehra Güzel
 * @version 1.0
 * @see Entry
 * @see GTUIterator
 */
public class GTUHashMap<K, V> {
    private Entry<K, V>[] table;
    private int size;
    private int capacity;
    private int collisionCount = 0;
    private final double LOAD_FACTOR = 0.5;

    /**
     * Constructs an empty hash map with an initial prime capacity of 101.
     */
    @SuppressWarnings("unchecked")
    public GTUHashMap() {
        this.capacity = 101;
        this.table = new Entry[capacity];
        this.size = 0;
    }

    /**
     * Computes the hash index for the given key using bit manipulation.
     *
     * <p>The hash function applies two rounds of XOR-shift mixing combined
     * with multiplication by the prime constant {@code 0x45d9f3b} to achieve
     * an avalanche effect, ensuring that small differences in keys produce
     * significantly different hash values.</p>
     *
     * @param key the key to hash (must not be {@code null})
     * @return the table index in the range [0, capacity)
     */
    private int hash(K key) {
        int h = key.hashCode();
        h ^= (h >>> 16);
        h *= 0x45d9f3b;
        h ^= (h >>> 16);
        return (h & 0x7fffffff) % capacity;
    }

    /**
     * Inserts or updates a key-value pair in the map.
     *
     * <p>If the key already exists, the associated value is updated.
     * If the load factor is exceeded before insertion, the table is
     * rehashed to maintain O(1) average-case performance.</p>
     *
     * <p><b>Time Complexity:</b> O(1) amortized</p>
     *
     * @param key   the key to insert (must not be {@code null})
     * @param value the value to associate with the key
     * @throws IllegalArgumentException if the key is {@code null}
     */
    public void put(K key, V value) {
    if (key == null) throw new IllegalArgumentException("Key cannot be null");

    if ((double) size / capacity >= LOAD_FACTOR) {
        rehash();
    }

    int index = hash(key);
    int k = 1;

    while (true) {
        Entry<K, V> entry = table[index];

        if (entry == null || entry.isDeleted) {
            table[index] = new Entry<>(key, value);
            size++;
            return;
        }

        if (entry.key.equals(key)) {
            entry.value = value;
            return;
        }

        collisionCount++;
        k += 2;
        index = (index + k) % capacity;
    }
}

    /**
     * Retrieves the value associated with the given key.
     *
     * <p><b>Time Complexity:</b> O(1) average case</p>
     *
     * @param key the key whose value is to be retrieved
     * @return the value associated with the key, or {@code null} if not found
     */
    public V get(K key) {
    if (key == null) return null;

    int index = hash(key);
    int k = 1;

    while (true) {
        Entry<K, V> entry = table[index];

        if (entry == null) return null;
        if (!entry.isDeleted && entry.key.equals(key)) return entry.value;

        k += 2;
        index = (index + k) % capacity;
    }
}


    /**
     * Removes the entry with the specified key using lazy deletion.
     *
     * <p>The entry is not physically removed from the table. Instead, its
     * {@code isDeleted} flag is set to {@code true}, allowing the probing
     * sequence to continue past this slot during future lookups.</p>
     *
     * <p><b>Time Complexity:</b> O(1) average case</p>
     *
     * @param key the key of the entry to remove
     */
   public void remove(K key) {
    if (key == null) return;

    int index = hash(key);
    int k = 1;

    while (true) {
        Entry<K, V> entry = table[index];

        if (entry == null) return;
        if (!entry.isDeleted && entry.key.equals(key)) {
            entry.isDeleted = true;
            size--;
            return;
        }

        k += 2;
        index = (index + k) % capacity;
    }
}


    /**
     * Checks if the map contains the specified key.
     *
     * <p><b>Time Complexity:</b> O(1) average case</p>
     *
     * @param key the key to check for
     * @return {@code true} if the key exists in the map
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Returns the number of active (non-deleted) entries in the map.
     *
     * @return the number of key-value pairs stored in the map
     */
    public int size() {
        return size;
    }

    /**
     * Returns the total number of collisions that occurred during insertions.
     *
     * <p>This metric is useful for evaluating the quality of the hash function
     * and the effectiveness of the probing strategy.</p>
     *
     * @return the cumulative collision count
     */
    public int getCollisionCount() {
        return collisionCount;
    }

    /**
     * Resizes the table to the next prime number greater than double the
     * current capacity, and reinserts all active entries.
     *
     * <p>This operation resets the collision counter and is triggered
     * automatically when the load factor exceeds 0.5.</p>
     *
     * <p><b>Time Complexity:</b> O(n)</p>
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        Entry<K, V>[] oldTable = table;
        capacity = nextPrime(capacity * 2);
        table = new Entry[capacity];
        size = 0;
        collisionCount = 0;

        for (Entry<K, V> entry : oldTable) {
            if (entry != null && !entry.isDeleted) {
                put(entry.key, entry.value);
            }
        }
    }

    /**
     * Finds the smallest prime number greater than or equal to {@code n}.
     *
     * @param n the starting number
     * @return the next prime number ≥ n
     */
    private int nextPrime(int n) {
        while (!isPrime(n)) n++;
        return n;
    }

    /**
     * Tests whether the given number is prime.
     *
     * @param num the number to test
     * @return {@code true} if {@code num} is a prime number
     */
    private boolean isPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    /**
     * Returns an iterator over the keys in this map, skipping deleted entries.
     *
     * <p>The iterator traverses the underlying table array sequentially and
     * returns only active (non-tombstoned) keys.</p>
     *
     * @return a {@link GTUIterator} over the keys in this map
     */
    public GTUIterator<K> keyIterator() {
        return new GTUIterator<K>() {
            private int index = 0;
            private int returned = 0;

            @Override
            public boolean hasNext() {
                return returned < size;
            }

            @Override
            public K next() {
                while (index < table.length) {
                    if (table[index] != null && !table[index].isDeleted) {
                        returned++;
                        return table[index++].key;
                    }
                    index++;
                }
                return null;
            }
        };
    }
}
