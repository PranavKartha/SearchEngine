package datastructures.sorting;

import misc.BaseTest;
import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

/**
 * See spec for details on what kinds of tests this class should include. 
 */
public class TestSortingStress extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout=10*SECOND)
    public void testHeapInsertPeekRemoveStress() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertTrue(heap.isEmpty());
        int cap = 200000;
        for (int i = 0; i < cap; i++) {
            heap.insert(i);
            assertEquals(i, heap.peekMin());
        }
        
        assertTrue(!heap.isEmpty());
        
        for (int i = cap - 1; i >= 0; i--) {
            assertEquals(i, heap.removeMin());
        }
        
        assertTrue(heap.isEmpty());
    }
}
