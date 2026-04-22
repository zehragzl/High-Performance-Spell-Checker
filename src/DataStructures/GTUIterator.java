package DataStructures;

/**
 * A custom iterator interface for traversing elements in custom data structures.
 *
 * <p>This interface provides a simplified iterator contract similar to
 * {@code java.util.Iterator}, designed to work with the custom collection
 * classes in this project without relying on the Java Collections Framework.</p>
 *
 * @param <T> the type of elements returned by this iterator
 *
 * @author Zehra Güzel
 * @version 1.0
 */
public interface GTUIterator<T> {

    /**
     * Returns {@code true} if the iteration has more elements.
     *
     * @return {@code true} if there are remaining elements to iterate over
     */
    boolean hasNext();

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element, or {@code null} if no more elements exist
     */
    T next();
}