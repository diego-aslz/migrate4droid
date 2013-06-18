package migrate4droid.schema;

import migrate4droid.misc.Validator;


public class Table {

	private String tableName;
	private Column[] columns;

	public Table(String tableName, Column[] columns){
		
		Validator.notNull(tableName, "String tableName cannot be null");
		Validator.notNull(columns, "Columns can not be null");
		Validator.isTrue(columns.length > 0, "Must include at least one column");
		
		this.tableName = tableName;
		this.columns = columns;
	}

	public String getTableName() {
		return tableName;
	}

	public Column[] getColumns() {
		return columns;
	}

}
