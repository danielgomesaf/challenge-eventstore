package net.intelie.challenges.impl;


import com.mongodb.client.MongoCursor;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;

public class EventList {
	
	private final MongoCursor<Event> events;
	
	public EventList(MongoCursor<Event> events) {
		this.events = events;
		
	}
	
	public EventIterator iterator() {
		return (EventIterator) new EventIteratorImpl();
	}
	
	class EventIteratorImpl implements EventIterator {

		@Override
		public void remove() {
			events.remove();
		}

		@Override
		public void close() throws Exception {
			events.close();
		}

		@Override
		public boolean moveNext() {
			return events.hasNext();
		}

		@Override
		public Event current() {
			return events.next();
		}
	}
}
