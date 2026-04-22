package DataStructures;

/**
 * A generic hash set implementation backed by {@link GTUHashMap}.
 *
 * <p>This set stores unique elements by delegating to an internal
 * {@code GTUHashMap<E, Object>}, where each element is stored as a key
 * mapped to a shared sentinel value. This design leverages the O(1)
 * average-case performance of the underlying hash map for all core
 * set operations.</p>
 *
 * <h3>Complexity Summary</h3>
 * <ul>
 *   <li>{@code add}      — O(1) amortized</li>
 *   <li>{@code remove}   — O(1) average</li>
 *   <li>{@code contains} — O(1) average</li>
 * </ul>
 *
 * @param <E> the type of elements maintained by this set
 *
 * @author Zehra Güzel
 * @version 1.0
 * @see GTUHashMap
 */
public class GTUHashSet<E> { 
    /** Shared sentinel value used for all keys in the backing map. */
    private static final Object WORD = new Object(); 

    /** The backing hash map that stores set elements as keys. */
    private GTUHashMap<E, Object> map; 

    /**
     * Constructs an empty hash set.
     */
    public GTUHashSet(){ 
        map = new GTUHashMap<>(); 
    } 

    /**
     * Adds the specified element to the set if it is not already present.
     *
     * <p><b>Time Complexity:</b> O(1) amortized</p>
     *
     * @param element the element to add
     */
    public void add(E element){
        map.put(element, WORD); 
    } 

    /**
     * Removes the specified element from the set if present.
     *
     * <p><b>Time Complexity:</b> O(1) average case</p>
     *
     * @param element the element to remove
     */
    public void remove(E element){ 
        map.remove(element); 
    } 

    /**
     * Returns {@code true} if the set contains the specified element.
     *
     * <p><b>Time Complexity:</b> O(1) average case</p>
     *
     * @param element the element to check for
     * @return {@code true} if the element exists in the set
     */
    public boolean contains(E element){ 
        return map.containsKey(element); 
    } 
    
    /**
     * Returns the number of elements in the set.
     *
     * @return the number of elements
     */
    public int size(){ 
        return map.size(); 
    } 

    /**
     * Returns an iterator over the elements in this set.
     *
     * @return a {@link GTUIterator} over the set elements
     */
    public GTUIterator<E> keyIterator() {
        return map.keyIterator(); 
    }

    /**
     * Returns the total number of hash collisions in the underlying map.
     *
     * <p>Useful for performance analysis and hash function evaluation.</p>
     *
     * @return the cumulative collision count
     */
    public int getCollisionCount() {
        return map.getCollisionCount();
    }
} 