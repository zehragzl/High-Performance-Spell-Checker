package DataStructures;

public class GTUHashMap<K, V> {
    private Entry<K, V>[] table;
    private int size;
    private int capacity;
    private int collisionCount = 0;
    private final double LOAD_FACTOR = 0.5;

    @SuppressWarnings("unchecked")
    public GTUHashMap() {
        this.capacity = 101;
        this.table = new Entry[capacity];
        this.size = 0;
    }

    // Improved hash function using bit manipulation to reduce clustering
    private int hash(K key) {
        int h = key.hashCode();
        h ^= (h >>> 16);
        h *= 0x45d9f3b;
        h ^= (h >>> 16);
        return (h & 0x7fffffff) % capacity;
    }

    // Inserts or updates a key-value pair using quadratic probing
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

    // Retrieves the value associated with the given key
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


    // Checks if the key exists in the map
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    // Returns the number of active entries
    public int size() {
        return size;
    }

    // Returns the number of collisions occurred during insertions
    public int getCollisionCount() {
        return collisionCount;
    }

    // Resizes and rehashes the table to a larger prime-sized capacity
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

    // Finds the next prime number greater than or equal to n
    private int nextPrime(int n) {
        while (!isPrime(n)) n++;
        return n;
    }

    // Checks if a number is prime
    private boolean isPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    // Returns an iterator over keys in the hash map (excluding deleted entries)
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
