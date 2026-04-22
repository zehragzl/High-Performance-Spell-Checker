package DataStructures;

/**
 * A generic key-value pair entry used internally by {@link GTUHashMap}.
 * Supports lazy deletion through a tombstone flag ({@code isDeleted}).
 *
 * <p>When an entry is "deleted" from the hash map, it is not physically removed
 * from the table. Instead, the {@code isDeleted} flag is set to {@code true},
 * allowing the quadratic probing sequence to continue past deleted slots
 * during lookups.</p>
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 *
 * @author Zehra Güzel
 * @version 1.0
 */
public class Entry<K, V> { 
    /** The key associated with this entry. */
    public K key; 

    /** The value associated with this entry. */
    public V value; 

    /** Tombstone flag: {@code true} if this entry has been logically deleted. */
    public boolean isDeleted; 

    /**
     * Constructs a new entry with the specified key and value.
     * The entry is initially marked as active (not deleted).
     *
     * @param key   the key for this entry (must not be {@code null})
     * @param value the value for this entry
     */
    public Entry(K key, V value) { 
        this.key = key; 
        this.value = value; 
        this.isDeleted = false; 
    } 
}