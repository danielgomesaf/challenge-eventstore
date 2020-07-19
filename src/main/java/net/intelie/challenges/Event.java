package net.intelie.challenges;

/**
 * This is just an event stub, feel free to expand it if needed.
 */
public class Event {
    
	private String type;
    private long timestamp;

    public Event(String type, long timestamp) {
    	this.type = type;
    	this.timestamp = timestamp;
    }

	public String getType() {
		return type;
	}

	/*
	 * public void setType(String type) { this.type = type; }
	 */

	public long getTimestamp() {
		return timestamp;
	}

	/*
	 * public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
	 */
}
