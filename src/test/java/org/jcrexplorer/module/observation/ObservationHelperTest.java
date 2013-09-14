package org.jcrexplorer.module.observation;

import javax.jcr.observation.Event;

import junit.framework.TestCase;

public class ObservationHelperTest extends TestCase {

	public void testGetEventType() {
		assertEquals(0, ObservationHelper.getEventType(null));
		assertEquals(0, ObservationHelper.getEventType(new Integer[0]));

		assertEquals(Event.NODE_ADDED, ObservationHelper
				.getEventType(new Integer[] { Event.NODE_ADDED }));
		assertEquals(Event.NODE_REMOVED, ObservationHelper
				.getEventType(new Integer[] { Event.NODE_REMOVED }));

		assertEquals(Event.NODE_ADDED | Event.NODE_REMOVED, ObservationHelper
				.getEventType(new Integer[] { Event.NODE_ADDED,
						Event.NODE_REMOVED }));
		assertEquals(Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED
				| Event.PROPERTY_REMOVED, ObservationHelper
				.getEventType(new Integer[] { Event.PROPERTY_ADDED,
						Event.PROPERTY_CHANGED, Event.PROPERTY_REMOVED }));
	}	

}
