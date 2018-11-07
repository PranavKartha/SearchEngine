package datastructures.sorting;

import static org.junit.Assert.assertTrue;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testInsertAndPeekMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for(int i = 20; i < 0; i--){
            heap.insert(i);
            assertEquals(i, heap.peekMin());           
        }      
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for(int i = 0; i < 20; i++) {
            heap.insert(i);
        }
        for(int i = 0; i < 20; i++) {
            assertEquals(i, heap.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void testIsEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertTrue(heap.isEmpty());
        for(int i = 0; i < 20; i++) {
            heap.insert(i);
        }
        for(int i = 0; i < 20; i++) {
            heap.removeMin();
        }
        assertTrue(heap.isEmpty());
    
    }
    
}
