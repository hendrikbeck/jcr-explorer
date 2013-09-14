package org.jcrexplorer.module.observation;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jcr.observation.Event;

public class ObservationHelper {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static int getEventType(Integer[] eventTypes) {
		if ((eventTypes == null) || (eventTypes.length == 0)) {
			return 0;
		}

		int result = 0;
		for (int i : eventTypes) {
			result = result | i;
		}
		
		return result;
	}
	
	public static String eventTypeName(int type) {
		switch (type) {
		case Event.NODE_ADDED:
			return "Node added";
		case Event.NODE_REMOVED:
			return "Node removed";
		case Event.PROPERTY_ADDED:
			return "Property added";
		case Event.PROPERTY_CHANGED:
			return "Property changed";
		case Event.PROPERTY_REMOVED:
			return "Property removed";
		default:
			return "Unknown event type";
		}
	}
	
	public static String getFormattedDate(Date date) {
		return sdf.format(date);
	}

}
