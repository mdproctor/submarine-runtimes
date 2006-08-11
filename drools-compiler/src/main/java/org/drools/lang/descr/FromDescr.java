package org.drools.lang.descr;

public class FromDescr extends PatternDescr {

	private ColumnDescr column;
	private DeclarativeInvokerDescr dataSource;
	
	public int getLine() {
		return column.getLine();
	}
	public void setColumn(ColumnDescr column) {
		this.column = column;
	}
	public DeclarativeInvokerDescr getDataSource() {
		return dataSource;
	}
	public void setDataSource(DeclarativeInvokerDescr dataSource) {
		this.dataSource = dataSource;
	}
	
}
