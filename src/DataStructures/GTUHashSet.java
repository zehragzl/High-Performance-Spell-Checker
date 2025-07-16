package DataStructures;


public class GTUHashSet<E> { 
    private static final Object WORD = new Object(); 
    private GTUHashMap<E, Object> map; 


    public GTUHashSet(){ 
        map = new GTUHashMap<>(); 
    } 

    public void add(E element){
        map.put(element, WORD); 
    } 

    public void remove(E element){ 
        map.remove(element); 
    } 

    public boolean contains(E element){ 
        return map.containsKey(element); 
    } 
    
    public int size(){ 
        return map.size(); 
    } 

    // Returns an iterator for iterating over the keys (elements in the set)
    public GTUIterator<E> keyIterator() {
        return map.keyIterator(); 
    }

    // Returns the number of collisions during operations in the underlying map
    public int getCollisionCount() {
        return map.getCollisionCount();
    }


} 