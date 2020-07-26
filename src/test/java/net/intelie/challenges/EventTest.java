package net.intelie.challenges;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.intelie.challenges.impl.EventStoreImpl;

public class EventTest {
	
	private EventStore es;
	
	@Before
	public void init() {
		List<Event> events = new ArrayList<>();
    	events.add(new Event("type_one", 1000L));
    	events.add(new Event("type_one", 2000L));
    	events.add(new Event("type_two", 1500L));
    	events.add(new Event("type_three", 3000L));
    	events.add(new Event("type_one", 1750L));
    	
    	es = new EventStoreImpl(events);
	}
	
	@After
    public void finalize() {
        es = null;
    }
	
    @Test
    public void insert() {
    	es.insert(new Event("type_five", 5000L));
    	
    	EventIterator eventIterator = es.query("type_five", 5000L, 7000L);
    	
    	List<Event> el = new ArrayList<>();
    	for (EventIterator it = eventIterator; eventIterator.moveNext();) {
    		Event e = it.current();
    		
    		if (e.type().equals("type_five"))
				el.add(e);
    	}
    	
    	assertTrue(el.size() == 1);
    }
    
    @Test
    public void removeAll() {
    	es.removeAll("type_one");
    	
    	EventIterator eventIterator = es.query("type_one");
    	
    	List<Event> el = new ArrayList<>();
		for (EventIterator it = eventIterator; eventIterator.moveNext();) {
			Event e = it.current();
			
			if (e.type().equals("type_one"))
				el.add(e);
		}
    	
    	assertTrue(el.isEmpty());
    }
    
    @Test
    public void query() {
    	EventIterator eventIterator = es.query("type_one", 1000L, 2001L);
    	
    	List<Event> el = new ArrayList<>();
    	for (EventIterator it = eventIterator; eventIterator.moveNext();)
    		el.add(it.current());
    	
    	assertTrue(el.size() == 3);	
    }
    
    @Test
    public void testThreadSafety() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() -> {
                es.insert(new Event("type_five", 5000L));
                latch.countDown();
            });
        }
        latch.await();
        
        List<Event> el = new ArrayList<>();
        
        EventIterator eventIterator = es.query("type_five", 5000L, 7000L);
        
    	for (EventIterator it = eventIterator; eventIterator.moveNext();) {
    		Event e = it.current();
    		
    		if (e.type().equals("type_five"))
				el.add(e);
    	}
        assertEquals(numberOfThreads, el.size());
    }
}