package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
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
    
    public ArrayHeap() {
        this.heap = makeArrayOfT(STARTING_SIZE);
        this.size = 0;
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
        if (this.isEmpty()) {
            throw new EmptyContainerException();
        }
        // percolating happens here
        
        // store top value as it's own thing.
        // empty bottom slot, put bottom at top
        // PERCOLATE THAT BITCH
        throw new NotYetImplementedException();
    }

    @Override
    public T peekMin() {
        if (this.isEmpty()) {
            throw new EmptyContainerException();
        }
        
        T result = this.heap[0];
        this.heap[0] = this.heap[this.size - 1];
        this.heap[this.size - 1] = null;
        
        // percolate method from top
        
        this.size--;
        
        return result;
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        
        // reorganizing happens here
        // increment size
        this.size++;
        if (this.size > this.heap.length) {
            // resize
            this.reSize();
        }
        
        if(item.compareTo(heap[0]) < 0) {
            //rearrange
        }else {
            insertHelper(item, 0);
        }
        
        //traverse to correct place, relocate shit as needed
        // put that bitch/bicboi down
        
        throw new NotYetImplementedException();
    }
    
    private void insertHelper(T item, int i) {
        if ((ArrayHeap.NUM_CHILDREN * i) + 4 > this.heap.length) {
            this.reSize();
        }
        
        if(item.compareTo(this.heap[i]) > 0) {
            if(this.heap[(ArrayHeap.NUM_CHILDREN * i) + 1] == null) {
                this.heap[(ArrayHeap.NUM_CHILDREN * i) + 1] = item;
            }else if(this.heap[(ArrayHeap.NUM_CHILDREN * i) + 2] == null) {
                this.heap[(ArrayHeap.NUM_CHILDREN * i) - 1] = item;
            }else if(this.heap[(ArrayHeap.NUM_CHILDREN * i) + 3] == null) {
                this.heap[(ArrayHeap.NUM_CHILDREN * i) + 3] = item;   
            }else if(this.heap[(ArrayHeap.NUM_CHILDREN * i) + 4] == null) {
                this.heap[(ArrayHeap.NUM_CHILDREN * i) + 4] = item;
            }else {
                this.insertHelper(item, i + 1);
            }
        }else {
            //rearrange
            int newIndex = this.percolate(item, i);
            // then do insertHelper(item, newIndex);??
        }
    }
    
    private int percolate(T item, int i) {
        // NOTE: always percolate down, and replace with SMALLEST child
        
        // what to do...
        
        // first check
        if (this.heap[i].compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 1]) > 0) {
            
        } else if (this.heap[i].compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 2]) > 0) {
            
        } else if (this.heap[i].compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 3]) > 0) {
            
        } else if (this.heap[i].compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 4]) > 0) {
            
        } else {
            return i;
        }
        // this is a placeholder until rest of code is written
        return 0;
    }
    
    private void reSize() {
        T[] newHeap = this.makeArrayOfT(this.heap.length * (ArrayHeap.NUM_CHILDREN + 1));
        for (int i = 0; i < this.heap.length; i++) {
            newHeap[i] = this.heap[i];
        }
        this.heap = newHeap;
    }

    @Override
    public int size() {
        return this.size;
    }
}
