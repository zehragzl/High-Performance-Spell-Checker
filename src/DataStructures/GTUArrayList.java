package DataStructures;

/**
 * A generic dynamic array implementation that automatically resizes as elements are added.
 *
 * <p>This class provides a resizable array similar to {@code java.util.ArrayList},
 * implemented from scratch without using Java Collections. The array doubles in
 * capacity when full, providing O(1) amortized insertion time.</p>
 *
 * <h3>Complexity Summary</h3>
 * <ul>
 *   <li>{@code add}    — O(1) amortized</li>
 *   <li>{@code get}    — O(1)</li>
 *   <li>{@code remove} — O(n) due to element shifting</li>
 *   <li>{@code clear}  — O(n)</li>
 * </ul>
 *
 * @param <E> the type of elements stored in this list
 *
 * @author Zehra Güzel
 * @version 1.0
 */
public class GTUArrayList<E> {
    private E[] data;
    private int size;
    private int capacity;

    /**
     * Constructs an empty list with a default initial capacity of 10.
     */
    @SuppressWarnings("unchecked")
    public GTUArrayList() {
        capacity = 10;
        data = (E[]) new Object[capacity];
        size = 0;
    }

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list (defaults to 10 if ≤ 0)
     */
    @SuppressWarnings("unchecked")
    public GTUArrayList(int initialCapacity) {
        capacity = initialCapacity > 0 ? initialCapacity : 10;
        data = (E[]) new Object[capacity];
        size = 0;
    }

    /**
     * Appends the specified element to the end of this list.
     * If the internal array is full, it is automatically resized.
     *
     * <p><b>Time Complexity:</b> O(1) amortized</p>
     *
     * @param element the element to add
     */
    public void add(E element) {
        if (size >= capacity) {
            resize();
        }
        data[size++] = element;
    }

    /**
     * Returns the element at the specified index.
     *
     * <p><b>Time Complexity:</b> O(1)</p>
     *
     * @param index the index of the element to return (0-based)
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public E get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return data[index];
    }

    /**
     * Removes the element at the specified index and shifts subsequent
     * elements to the left using {@code System.arraycopy}.
     *
     * <p><b>Time Complexity:</b> O(n)</p>
     *
     * @param index the index of the element to remove (0-based)
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void remove(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        // Shift only the elements after the removed element
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        data[--size] = null; // Null the last element to avoid loitering
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements
     */
    public int size() {
        return size;
    }

    /**
     * Removes all elements from this list and nullifies references
     * to allow garbage collection.
     *
     * <p><b>Time Complexity:</b> O(n)</p>
     */
    public void clear() {
        // Null all elements for better GC performance
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    /**
     * Doubles the capacity of the internal array and copies existing
     * elements to the new array using {@code System.arraycopy}.
     *
     * <p><b>Growth Factor:</b> 2x</p>
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        capacity = (int) (capacity * 2);
        E[] newData = (E[]) new Object[capacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }
}
