package org.jcrexplorer.util;

import java.io.ByteArrayInputStream;
import java.util.Calendar;

import javax.jcr.PropertyType;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class provides some general utils. 
 */
public class JcrUtils {

	private Log logger = LogFactory.getLog(this.getClass());

	/**
	 * Returns an empty value for single-valued properties
	 * @param session
	 * @param type
	 * @return
	 * @throws IllegalArgumentException
	 */
	public Value getEmptyValue(Session session, int type)  {

		try {
			ValueFactory vf = session.getValueFactory();

			switch (type) {
			case PropertyType.STRING:
				return vf.createValue("");
			case PropertyType.BOOLEAN:
				return vf.createValue(false);
			case PropertyType.BINARY:
				return vf.createValue(new ByteArrayInputStream(new byte[0]));
			case PropertyType.DATE:
				return vf.createValue(Calendar.getInstance());
			case PropertyType.DOUBLE:
				return vf.createValue(0);
			case PropertyType.LONG:
				return vf.createValue(0);
			case PropertyType.REFERENCE:
				// return vf.createValue((Node) null);
				throw new Exception("Empty value is not available for REFERENCE properties.");
			case PropertyType.PATH:
				return vf.createValue("/");
			case PropertyType.NAME:
				return vf.createValue("nt:base");
			}

		}
		catch (Exception ex) {
			logger.error("Error occured creating empty single value for type " + PropertyType.nameFromValue(type), ex);
		}

		return null;

	}

	/**
	 * Returns an empty value for multi-valued properties
	 * @param session
	 * @param type
	 * @return
	 */
	public Value[] getEmptyMultiValue(Session session, int type) {

		try {
			ValueFactory vf = session.getValueFactory();

			switch (type) {
			case PropertyType.STRING:
				return new Value[] { vf.createValue("") };
			case PropertyType.BOOLEAN:
				return new Value[] { vf.createValue(false) };
			case PropertyType.BINARY:
				return new Value[] { vf.createValue(new ByteArrayInputStream(new byte[0])) };
			case PropertyType.DATE:
				return new Value[] { vf.createValue(Calendar.getInstance()) };
			case PropertyType.DOUBLE:
				return new Value[] { vf.createValue(0) };
			case PropertyType.LONG:
				return new Value[] { vf.createValue(0) };
			case PropertyType.REFERENCE:
				// return new Value[] { vf.createValue((Node) null) };
				throw new Exception("Empty value is not available for REFERENCE properties.");
			case PropertyType.PATH:
				return new Value[] { vf.createValue("/") };
			case PropertyType.NAME:
				return new Value[] { vf.createValue("nt:base") };
			}

		}
		catch (Exception ex) {
			logger.error("Error occured creating empty single value for type " + PropertyType.nameFromValue(type), ex);
		}

		return null;

	}
}
