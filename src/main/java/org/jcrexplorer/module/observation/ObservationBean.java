package org.jcrexplorer.module.observation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcrexplorer.module.ModuleBean;

public class ObservationBean extends ModuleBean implements EventListener {

	private Log logger = LogFactory.getLog(this.getClass());

	private TreeSet<EventWrapper> eventList = new TreeSet<EventWrapper>(
			new EventListComparator());

	private Integer[] eventTypes;

	private String path = "/";

	private boolean isDeep = true;

	private String[] uuids = null;

	private String[] nodeTypeNames = null;

	private boolean noLocal = false;

	private boolean isRegistered = false;

	public void onEvent(EventIterator events) {
		while (events.hasNext()) {
			Event event = events.nextEvent();
			eventList.add(new EventWrapper(event));
		}
	}

	public TreeSet<EventWrapper> getEventList() {
		return eventList;
	}

	public void clear(ActionEvent event) {
		eventList.clear();
		addInfoMessage("Cleared event list.");
	}

	public void register(ActionEvent event) {
		renewRegistration();
	}

	public void unregister(ActionEvent event) {
		try {
			contentBean.getSession().getWorkspace().getObservationManager()
					.removeEventListener(this);
			isRegistered = false;
			addInfoMessage("Sucessfully unregistered this observation console.");
		} catch (Exception ex) {
			addErrorMessage(ex);
		}
	}

	private void renewRegistration() {
		try {
			contentBean.getSession().getWorkspace().getObservationManager()
					.removeEventListener(this);
			contentBean.getSession().getWorkspace().getObservationManager()
					.addEventListener(this,
							ObservationHelper.getEventType(eventTypes), path,
							isDeep, uuids, nodeTypeNames, noLocal);
			isRegistered = true;
			addInfoMessage("Sucessfully registered observation settings.");
		} catch (Exception e) {
			addErrorMessage(e);
		}
	}

	public Collection<SelectItem> getEventTypeList() {
		ArrayList<SelectItem> result = new ArrayList<SelectItem>();
		result.add(new SelectItem(Event.NODE_ADDED, "NODE_ADDED"));
		result.add(new SelectItem(Event.NODE_REMOVED, "NODE_REMOVED"));
		result.add(new SelectItem(Event.PROPERTY_ADDED, "PROPERTY_ADDED"));
		result.add(new SelectItem(Event.PROPERTY_CHANGED, "PROPERTY_CHANGED"));
		result.add(new SelectItem(Event.PROPERTY_REMOVED, "PROPERTY_REMOVED"));
		return result;
	}

	public Integer[] getEventTypes() {
		return eventTypes;
	}

	public void setEventTypes(Integer[] eventTypes) {
		this.eventTypes = eventTypes;
	}

	public boolean getIsDeep() {
		return isDeep;
	}

	public void setIsDeep(boolean isDeep) {
		this.isDeep = isDeep;
	}

	public boolean getNoLocal() {
		return noLocal;
	}

	public void setNoLocal(boolean noLocal) {
		this.noLocal = noLocal;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean getIsRegistered() {
		return isRegistered;
	}

	private class EventListComparator implements Comparator {

		public int compare(Object event0, Object event1) {
			final int AFTER = -1;
			final int EQUAL = 0;
			final int BEFORE = 1;

			if (event0 == event1)
				return EQUAL;

			if (event0 == null)
				return AFTER;
			if (event1 == null)
				return BEFORE;

			return ((EventWrapper) event0).getTimestamp().compareTo(
					((EventWrapper) event1).getTimestamp())
					* -1;
		}

	}

}
