package net.intelie.challenges.impl;

import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.EventStore;
import net.intelie.challenges.database.MongoDBConnection;

public class EventStoreImpl implements EventStore {
	
	private MongoCollection<Event> events;
	
	@SuppressWarnings("unchecked")
	public EventStoreImpl() {
		events = (MongoCollection<Event>) MongoDBConnection.getInstance().getCollection("events", Event.class);
	}

	@Override
	public void insert(Event event) {
		events.insertOne(event);
	}

	@Override
	public void removeAll(String type) {
		events.deleteMany(Filters.all("type", type));
	}

	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		Bson typeEq = Filters.eq("type", type);
		Bson timestampGte = Filters.gte("timestamp", startTime);
		Bson timestampLt = Filters.lt("timestamp", endTime);
		
		MongoCursor<Event> eventsList = events.find(Filters.and(typeEq, timestampGte, timestampLt)).iterator();
		
		return new EventList(eventsList).iterator();
	}
}
