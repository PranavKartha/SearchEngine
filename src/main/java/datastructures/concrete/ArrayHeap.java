package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

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
        T result = this.heap[0];
        this.heap[0] = this.heap[this.size - 1];
        this.heap[this.size - 1] = null;
        
        // percolate method from top
        // percolate(item, 0);
        
        this.size--;
        
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
        // increment size
        this.size++;
        
        if (this.size > this.heap.length) {
            // resize
            this.reSize();
        }
        
        // insertion
        if (this.isEmpty()) {
            this.heap[0] = item;
        } else {
            insertHelper(item, 0);
        }
        
        //traverse to correct place, relocate shit as needed
        // put that bitch/bicboi down
    }
    
    private void insertHelper(T item, int i) {
        if ((ArrayHeap.NUM_CHILDREN * i) + 4 > this.heap.length) {
            this.reSize();
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
//        if(item.compareTo(this.heap[i]) > 0) {
//            if(this.heap[(ArrayHeap.NUM_CHILDREN * i) + 1] == null) {
//                this.heap[(ArrayHeap.NUM_CHILDREN * i) + 1] = item;
//            }else if(this.heap[(ArrayHeap.NUM_CHILDREN * i) + 2] == null) {
//                this.heap[(ArrayHeap.NUM_CHILDREN * i) + 2] = item;
//            }else if(this.heap[(ArrayHeap.NUM_CHILDREN * i) + 3] == null) {
//                this.heap[(ArrayHeap.NUM_CHILDREN * i) + 3] = item;   
//            }else if(this.heap[(ArrayHeap.NUM_CHILDREN * i) + 4] == null) {
//                this.heap[(ArrayHeap.NUM_CHILDREN * i) + 4] = item;
//            }else {
//                this.insertHelper(item, i + 1);
//            }
//        }else {
//            //rearrange
//            int newIndex = this.percolate(item, i);
//            // then do insertHelper(item, newIndex);??
//        }
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
        if (item.compareTo(parentData) > 0) {
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
        
        if (this.hasMaxKids(i)) {
            // #hail4Kids
            
            // pick smallest child
            // penetrate them... wait, no, um.. i mean-- percolate them
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
            
        } else {
            // less pedophilic base cases... you heard me
            if (!this.hasNoKids(i)) {
                int smallIndex = ArrayHeap.NUM_CHILDREN * i + 1;
                T small = this.heap[smallIndex];
                
                if (this.heap[ArrayHeap.NUM_CHILDREN * i + 2] != null) {
                    if (small.compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 2]) > 0) {
                        smallIndex = ArrayHeap.NUM_CHILDREN * i + 2;
                        small = this.heap[smallIndex];
                    }
                }
                
                if (this.heap[ArrayHeap.NUM_CHILDREN * i + 3] != null) {
                    if (small.compareTo(this.heap[ArrayHeap.NUM_CHILDREN * i + 3]) > 0) {
                        smallIndex = ArrayHeap.NUM_CHILDREN * i + 3;
                        small = this.heap[smallIndex];
                    }
                }
                
                this.heap[i] = small;
                this.heap[smallIndex] = item;
            }
        }
    }
    
    private boolean hasNoKids(int i) {
        return (this.heap[ArrayHeap.NUM_CHILDREN * i + 1] == null && this.heap[ArrayHeap.NUM_CHILDREN * i + 2] == null)
                && (this.heap[ArrayHeap.NUM_CHILDREN * i + 3] == null && this.heap[ArrayHeap.NUM_CHILDREN * i + 4] == null);
    }
    
    private boolean hasMaxKids(int i) {
        return (this.heap[ArrayHeap.NUM_CHILDREN * i + 1] != null && this.heap[ArrayHeap.NUM_CHILDREN * i + 2] != null) 
                && (this.heap[ArrayHeap.NUM_CHILDREN * i + 3] != null && this.heap[ArrayHeap.NUM_CHILDREN * i + 4] != null);
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
