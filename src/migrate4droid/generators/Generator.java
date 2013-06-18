package migrate4droid.generators;

import migrate4droid.schema.Column;
import migrate4droid.schema.ForeignKey;
import migrate4droid.schema.Index;
import migrate4droid.schema.Table;


/**
 * Responsible for creating SQL DDL statements for a specific database.
 *
 */
public interface Generator {

	public String createTableStatement(Table table);
	
	public String createTableStatement(Table table, String options);

	public String dropTableStatement(String tableName);
	
	public String addColumnStatement(Column column, String tableName);
	
	public String dropColumnStatement(String columnName, String tableName);
	
	public String addIndex(Index index);
	
	public String dropIndex(Index index);
	
	public String dropIndex(String indexName, String tableName);
	
	public String addForeignKey(ForeignKey foreignKey);
	
	public String dropForeignKey(ForeignKey foreignKey);
	
	public String dropForeignKey(String foreignKeyName, String childTable);
	
	public String renameColumn(String newColumnName, String oldColumnName, String tableName);
	
	public String wrapName(String name);
}
