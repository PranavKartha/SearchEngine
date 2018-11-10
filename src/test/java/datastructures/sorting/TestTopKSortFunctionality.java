package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testNegativeKInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        try {
            IList<Integer> topNegativeThings = Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // guud jahb broi 
        }
    }
    
    @Test//(timeout=SECOND)
    public void testKGreaterThanN() {
        IList<Integer> list = new DoubleLinkedList<>();
        IList<Integer> topK;
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        topK = Searcher.topKSort(10, list);
        assertEquals(list.size(), topK.size());
    }
    
    @Test(timeout=SECOND)
    public void testNullInputs() {
        IList<String> list = new DoubleLinkedList<>();
        IList<String> topK;
        list.add("");
        list.add("oogiboogi");
        list.add(null);
        try {
            topK = Searcher.topKSort(3, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // yaay
        }
    }
}
