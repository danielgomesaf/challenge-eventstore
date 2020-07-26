package net.intelie.challenges.impl;

import java.util.ArrayList;
import java.util.List;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.EventStore;

public class EventStoreImpl implements EventStore {
	
	private EventList eventList;
	
	public EventStoreImpl(List<Event> events) {
		eventList = new EventList(events);
	}
	
	@Override
	public synchronized void insert(Event event) {
		eventList.addEvent(event);
	}

	@Override
	public synchronized void removeAll(String type) {
		EventIterator it = eventList.getIterator();
		while (it.moveNext()) {
			Event event = it.current();
			if (event.type().equals(type))
				it.remove();
		}
	}

	@Override
	public synchronized EventIterator query(String type, long startTime, long endTime) {
		List<Event> queryEvents = new ArrayList<Event>();
		
		EventIterator it = eventList.getIterator();
		while( it.moveNext()) {
			Event event = it.current();
			String eventType = event.type();
			long eventTime = event.timestamp();
			
			if (eventType.equals(type) && (eventTime >= startTime && eventTime < endTime))
				queryEvents.add(event);
		}
		
		return new EventList(queryEvents).getIterator();
	}

	@Override
	public synchronized EventIterator query(String type) {
		List<Event> queryEvents = new ArrayList<Event>();
		
		EventIterator it = eventList.getIterator();
		while (it.moveNext()) {
			Event event = it.current();
			String eventType = event.type();
			
			if (eventType.equals(type))
				queryEvents.add(event);
		}
		
		return new EventList(queryEvents).getIterator();
	}
}
