package net.intelie.challenges.database;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

	private static MongoDBConnection INSTANCE = null;
	
	private MongoClient client;
	
	private CodecRegistry registry;
	
	private MongoDBConnection() {
		client = MongoClients.create(System.getProperty("mongodb.uri"));
		
		registry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	}
	
	public static synchronized MongoDBConnection getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MongoDBConnection();
		}
		
		return INSTANCE;
	}
	
	public MongoDatabase getDatabase() {
		return client.getDatabase(System.getProperty("mongodb.db")).withCodecRegistry(registry);
	}
	
	public MongoCollection<?> getCollection(String name, Class<?> clazz) {
		return MongoDBConnection.getInstance().getDatabase().getCollection(name, clazz);
	}
}
