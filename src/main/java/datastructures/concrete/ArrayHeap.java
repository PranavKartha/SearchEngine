package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.NotYetImplementedException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Feel free to add more fields and constants.
    private static final int STARTING_SIZE = 5;
    private int size;
    private int bottom;
    
    public ArrayHeap() {
        this.heap = makeArrayOfT(STARTING_SIZE);
        this.size = 0;
        bottom = -1;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
        // percolating happens here
        
        // store top value as it's own thing.
        // empty bottom slot, put bottom at top
        // PERCOLATE THAT BITCH
        throw new NotYetImplementedException();
    }

    @Override
    public T peekMin() {
        // return the T at the top bicBoi
        throw new NotYetImplementedException();
    }

    @Override
    public void insert(T item) {
        // reorganizing happens here
        this.size++;
        if (this.size > this.heap.length) {
            //resize method
        }
        
        //traverse to correct place, relocate shit as needed
        // put that bitch/bicboi down
        throw new NotYetImplementedException();
    }

    @Override
    public int size() {
        return this.size;
    }
}
