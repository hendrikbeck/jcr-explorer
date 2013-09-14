package org.jcrexplorer.jsf.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class DateTimeConverter implements Converter {

	private static DateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd  hh:mm:ss");

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2)
			throws ConverterException {
		// Do nothing here
		return null;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ConverterException {
		try {
			return formatter.format((Date) arg2);
		} catch (Exception ex) {
			return "";
		}
	}

}
