package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
//
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
    private int count;
    
    public ArrayHeap() {
        this.heap = makeArrayOfT(STARTING_SIZE);
        this.count = 0;
    }

    /**
     * This method will return a new, empty array of the given count
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
        T result = this.heap[0];
        this.heap[0] = this.heap[this.count - 1];
        this.heap[this.count - 1] = null;

        this.count--;
        
        percolate(heap[0], 0);
        
        return result;
    }

    @Override
    public T peekMin() {
        if (this.isEmpty()) {
            throw new EmptyContainerException();
        }
        
        return this.heap[0];
    }
    


    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        
        // reorganizing happens here
        // increment count
        this.count++;
        
        
        // insertion
        if (this.isEmpty()) {
            this.heap[0] = item;
        } else {
            insertHelper(item, 0);
        }
        
        //traverse to correct place, relocate shit as needed
        // put that boi down
    }
    
    private void insertHelper(T item, int i) {
        if (heap[0] == null) {
            heap[0] = item;
        } else {
            if ((ArrayHeap.NUM_CHILDREN * i) + 4 > this.heap.length) {
                this.recount();
            }
            if (this.heap[(ArrayHeap.NUM_CHILDREN * i) + 1] == null) {
                childCheck(item, i, 1);
            } else if (this.heap[(ArrayHeap.NUM_CHILDREN * i) + 2] == null) {
                childCheck(item, i, 2);
            } else if (this.heap[(ArrayHeap.NUM_CHILDREN * i) + 3] == null) {
                childCheck(item, i, 3);
            } else if (this.heap[(ArrayHeap.NUM_CHILDREN * i) + 4] == null) {
                childCheck(item, i, 4);
            } else {
                this.insertHelper(item, i + 1);
            }
        }    
    }
    
    public void childCheck(T item, int i, int childNum) {
        if (item.compareTo(this.heap[i]) > 0) {
            this.heap[(ArrayHeap.NUM_CHILDREN * i) + childNum] = item;
        } else {
            relocate(item, (ArrayHeap.NUM_CHILDREN * i) + childNum);
            //relocate
        }
    }
    
    
    private void relocate(T item, int i) {
        T parentData = this.heap[(i - 1) / ArrayHeap.NUM_CHILDREN];
        if (item.compareTo(parentData) < 0) {
            heap[(i - 1) / ArrayHeap.NUM_CHILDREN] = item;
            heap[i] = parentData;
            relocate(item, (i - 1) / ArrayHeap.NUM_CHILDREN);
        }
    }
    
    private void percolate(T item, int i) {
        // NOTE: always percolate down, and replace with SMALLEST child
        
        // first check
        // base: not all kids are there
        // aka the kids arent all right
        if (count <= ArrayHeap.NUM_CHILDREN + 1) {
            for (int i1 = 0; i1 < count; i1++) {
                if (heap[0].compareTo(heap[i1]) > 0) {
                    T top = heap[0];
                    heap[0] = heap[i1];
                    heap[i1] = top;        
                }
            }
        } else {
       
            if (this.hasMaxKids(i)) {
                // #hail4Kids
            
                // case where we do shit
                if ((item.compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 1])
                        > 0 || item.compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 2]) > 0)
                        ||(item.compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 3]) > 0
                                || item.compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 4]) > 0)){
                      // pick smallest child
                      // infiltrate them... wait, no, um.. i mean-- percolate them
                      T smallest = this.heap[ArrayHeap.NUM_CHILDREN * i + 1];
                      int smallIndex = ArrayHeap.NUM_CHILDREN * i + 1;
                  
                      if (smallest.compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 2]) > 0) {
                          smallest = this.heap[ArrayHeap.NUM_CHILDREN * i + 2];
                          smallIndex = ArrayHeap.NUM_CHILDREN * i + 2;
                      }
                      if (smallest.compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 3]) > 0) {
                            smallest = this.heap[ArrayHeap.NUM_CHILDREN * i + 3];
                          smallIndex = ArrayHeap.NUM_CHILDREN * i + 3;
                      }
                      if (smallest.compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 4]) > 0) {
                          smallest = this.heap[ArrayHeap.NUM_CHILDREN * i + 4];
                          smallIndex = ArrayHeap.NUM_CHILDREN * i + 4;
                      }
                  
                      this.heap[i] = smallest;
                      this.heap[smallIndex] = item;
                      percolate(item, smallIndex);
                }
            // if that don't happen we don't swayp
            } else {
                // less eww base cases... you heard me
                if (!this.hasNoKids(i)) {
                    // we good
                    
                    // need to do another last percolation
                    int smallIndex = i;
                    T small = this.heap[i];
                
                    if ((ArrayHeap.NUM_CHILDREN * i + 1) < this.count) {
                        if (small.compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 1]) > 0) {
                            smallIndex = ArrayHeap.NUM_CHILDREN * i + 1;
                            small = this.heap[smallIndex];
                        }
                    }
                
                    if ((ArrayHeap.NUM_CHILDREN * i + 2) < this.count) {
                        if (small.compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 2]) > 0) {
                            smallIndex = ArrayHeap.NUM_CHILDREN * i + 2;
                            small = this.heap[smallIndex];
                        }
                    }
                    
                    if ((ArrayHeap.NUM_CHILDREN * i + 3) < this.count) {
                        if (small.compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 3]) > 0) {
                            smallIndex = ArrayHeap.NUM_CHILDREN * i + 3;
                            small = this.heap[smallIndex];
                        }
                    }
                    
                    if (i != smallIndex) {
                        this.heap[i] = small;
                        this.heap[smallIndex] = item;
                    }
                }
            }
        }
    }    
    
    private boolean hasNoKids(int i) {
        return ArrayHeap.NUM_CHILDREN * i + 1 < this.count;
    }
    
    private boolean hasMaxKids(int i) {
        return ArrayHeap.NUM_CHILDREN * i + 4 < this.count;
    }
    
    private void recount() {
        T[] newHeap = this.makeArrayOfT(this.heap.length * (ArrayHeap.NUM_CHILDREN + 1));
        for (int i = 0; i < this.heap.length; i++) {
            newHeap[i] = this.heap[i];
        }
        this.heap = newHeap;
    }

    @Override
    public int size() {
        return this.count;
    }
}
