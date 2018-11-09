package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
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
    
    
    @Test(timeout = SECOND)
    public void testInsertBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 20; i++) {
            heap.insert(i);
            assertEquals(i + 1, heap.size());
        }
    }
    
    @Test(timeout=SECOND)
    public void testRemovePeekMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 20; i++) {
            heap.insert(i);
        }
        for (int i = 0; i < 20; i++) {
            assertEquals(heap.peekMin(), heap.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void testNegativeInputBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i > -10; i--) {
            heap.insert(i);
            assertEquals(i, heap.peekMin());
        }
        
        for (int i = -9; i <= 0; i++) {
            assertEquals(i, heap.removeMin());
        }
        
        assertTrue(heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testNullInput() {
        IPriorityQueue<String> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Don't do anything, as a wise boi once said, "Esketwo"
        }
    }
    
    @Test(timeout=SECOND)
    public void insertOutOfOrder() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(5);
        assertEquals(5, heap.peekMin());
        heap.insert(72);
        heap.insert(27);
        heap.insert(3);
        heap.insert(4);
        assertEquals(3, heap.peekMin());
        assertEquals(5, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testEmptyRemoveMin() {
        IPriorityQueue<String> heap = this.makeInstance();
        
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException e) {
            // this is some good, tasty shit
        }
    }
    
    @Test(timeout=SECOND)
    public void testEmptyPeekMin() {
        IPriorityQueue<Double> heap = this.makeInstance();
        
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException e) {
            // twas a night like this one when the good shit was found
            // nothing had stirred, for all were passed out on the ground
            // the drugs in their blood, they were higher than kites
            // yet they lay like in storage, lost without light
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertAndPeekMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 20; i < 0; i--){
            heap.insert(i);
            assertEquals(i, heap.peekMin());           
        }      
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 20; i++) {
            heap.insert(i);
        }
        
        
        
        for (int i = 0; i < 19; i++) {
            System.out.println(i);
            
            System.out.println(heap.peekMin());
            assertEquals(i, heap.removeMin());
        }
        System.out.println(heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testIsEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertTrue(heap.isEmpty());
        for (int i = 0; i < 20; i++) {
            heap.insert(i);
        }
        assertTrue(!heap.isEmpty());
        for (int i = 0; i < 20; i++) {
            heap.removeMin();
        }
        assertTrue(heap.isEmpty());
    
    }
}
