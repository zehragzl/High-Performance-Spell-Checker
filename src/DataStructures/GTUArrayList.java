package DataStructures;

public class GTUArrayList<E> {
    private E[] data;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public GTUArrayList() {
        capacity = 10;
        data = (E[]) new Object[capacity];
        size = 0;
    }

    // Allow users to specify an initial capacity
    @SuppressWarnings("unchecked")
    public GTUArrayList(int initialCapacity) {
        capacity = initialCapacity > 0 ? initialCapacity : 10;
        data = (E[]) new Object[capacity];
        size = 0;
    }

    public void add(E element) {
        if (size >= capacity) {
            resize();
        }
        data[size++] = element;
    }

    public E get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return data[index];
    }

    public void remove(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        // Shift only the elements after the removed element
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        data[--size] = null; // Null the last element to avoid loitering
    }

    public int size() {
        return size;
    }

    public void clear() {
        // Null all elements for better GC performance
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity = (int) (capacity * 2); // Use a growth factor of 1.5
        E[] newData = (E[]) new Object[capacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }
}
