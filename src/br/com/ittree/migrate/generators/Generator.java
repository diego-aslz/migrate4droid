package br.com.ittree.migrate.generators;

import br.com.ittree.migrate.schema.Column;
import br.com.ittree.migrate.schema.ForeignKey;
import br.com.ittree.migrate.schema.Index;
import br.com.ittree.migrate.schema.Table;


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
