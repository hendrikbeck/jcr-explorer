package org.jcrexplorer.module.observation;

import java.util.Date;

import javax.jcr.observation.Event;

public class EventWrapper {

	private Event event;

	private Date timestamp;

	public EventWrapper(Event event) {
		this.event = event;
		this.timestamp = new Date();
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getTypeAsString() {
		return ObservationHelper.eventTypeName(event.getType());
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getTimestampAsString() {
		return ObservationHelper.getFormattedDate(this.timestamp);
	}

}
