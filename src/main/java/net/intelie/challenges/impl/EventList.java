package net.intelie.challenges.impl;

import java.util.List;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.IEventList;

public class EventList implements IEventList {
	
	public List<Event> events;
	
	public EventList(List<Event> events) {
		this.events = events;
	}

	@Override
	public EventIterator getIterator() {
		return new EventIteratorImpl();
	}
	
	public void addEvent(Event event) {
		events.add(event);
	}
	
	private class EventIteratorImpl implements EventIterator {

		int index = 0;
		
		@Override
		public void close() throws Exception {
			events = null;
		}

		@Override
		public boolean moveNext() {
			if (index < events.size())
				return true;
			
			return false;
		}

		@Override
		public Event current() {
			if (this.moveNext())
				return events.get(index++);
			
			return null;
		}

		@Override
		public void remove() {
			int toRemove = index - 1;
			events.remove(toRemove);
			index = 0;
		}
	}
}