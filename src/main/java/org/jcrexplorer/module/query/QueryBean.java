package org.jcrexplorer.module.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcrexplorer.Constants;
import org.jcrexplorer.module.ModuleBean;

public class QueryBean extends ModuleBean {

	private Log logger = LogFactory.getLog(this.getClass());

	private String queryString;

	private DataModel results;

	private DataModel columnNames;

	private String queryLanguage = Query.XPATH;

	public String getQueryLanguage() {
		return queryLanguage;
	}

	public void setQueryLanguage(String queryType) {
		this.queryLanguage = queryType;
	}

	public DataModel getResults() {
		return results;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * Executes the actual query using the given <code>queryString</code> and
	 * <code>queryLanguage</code>. The results will be available in
	 * <code>results</code> and <code>columnNames</code>. It will be
	 * written into DataModels to be accesible via Tomahawk dataTable
	 * components.
	 */
	public String doQuery() {
		try {
			QueryManager qm = contentBean.getSession().getWorkspace()
					.getQueryManager();
			Query q = qm.createQuery(queryString, queryLanguage);
			QueryResult qr = q.execute();
			RowIterator ri = qr.getRows();
			logger.info("Finished query with " + ri.getSize() + " rows.");
			ArrayList<Row> result = new ArrayList<Row>();
			while (ri.hasNext()) {
				result.add(ri.nextRow());
			}

			List<List> rowList = new ArrayList<List>();
			Iterator it = result.iterator();
			while (it.hasNext()) {
				List<Value> colList = new ArrayList<Value>();
				Row row = (Row) it.next();
				for (Value value : row.getValues()) {
					colList.add(value);
				}
				rowList.add(colList);
			}
			results = new ListDataModel(rowList);

			columnNames = new ListDataModel(Arrays.asList(qr.getColumnNames()));

			addInfoMessage("Finished query with " + results.getRowCount()
					+ " rows and " + columnNames.getRowCount() + " columns.");

			return Constants.OUTCOME_SUCCESS;
		} catch (RepositoryException e) {
			addErrorMessage(e);
			results = new ListDataModel();
			columnNames = new ListDataModel();
			return Constants.OUTCOME_FAILURE;
		}
	}

	public Object getColumnValue() {
		Object columnValue = null;
		if (results.isRowAvailable() && columnNames.isRowAvailable()) {
			columnValue = ((List) results.getRowData()).get(columnNames
					.getRowIndex());
		}
		return columnValue;
	}

	/**
	 * Returns the list of query languages that are available for the current
	 * repository implementation as SelectItems.
	 * 
	 * @return
	 */
	public Collection<SelectItem> getQueryLanguageList() {
		String[] s = new String[0];
		try {
			s = contentBean.getSession().getWorkspace().getQueryManager()
					.getSupportedQueryLanguages();
		} catch (RepositoryException e) {
			addErrorMessage(e);
		}

		Collection<SelectItem> result = new ArrayList<SelectItem>();
		for (String string : s) {
			result.add(new SelectItem(string));
		}

		return result;
	}

	public DataModel getColumnNames() {
		return columnNames;
	}

}
