package br.com.ittree.migrate;

import java.sql.SQLException;

import android.database.sqlite.SQLiteDatabase;
import br.com.ittree.migrate.generators.Generator;
import br.com.ittree.migrate.generators.GeneratorFactory;
import br.com.ittree.migrate.log.Logger;
import br.com.ittree.migrate.misc.SchemaMigrationException;
import br.com.ittree.migrate.misc.Validator;
import br.com.ittree.migrate.schema.Column;
import br.com.ittree.migrate.schema.ForeignKey;
import br.com.ittree.migrate.schema.Index;
import br.com.ittree.migrate.schema.Table;

/**
 * Contains commands that can be called from within Migration classes.
 *
 */
public class Execute {

	/**
	 * Create a table
	 * 
	 * @param table
	 */
	public static void createTable(Table table) {
		createTable(table, null);
	}

	/**
	 * Create a table with database specific options.
	 * This allows, for example, passing an engine type
	 * to MySQL.  While the <code>tableOptions</code>
	 * may be ignored for database products that do not
	 * accept such things, be aware that using this 
	 * argument may make your migrations no longer cross
	 * product compatible.
	 * 
	 * @param table
	 * @param tableOptions
	 */
	public static void createTable(Table table, String tableOptions){
		Validator.notNull(table, "Table can not be null");

		try {
			Generator generator = GeneratorFactory.getGenerator();

			String query;
			if (tableOptions != null) {
				query = generator.createTableStatement(table, tableOptions);
			} else {
				query = generator.createTableStatement(table);
			}

			executeStatement(query);
		} catch (SQLException e) {
			Logger.e("Unable to create table " + table.getTableName(), e);
			throw new SchemaMigrationException("Unable to create table " + table.getTableName(), e);
		} 
	}

	/**
	 * Drop a table
	 * 
	 * @param tableName
	 */
	public static void dropTable(String tableName) {
		Validator.notNull(tableName, "Table name can not be null");

		try {
			Generator generator = GeneratorFactory.getGenerator();
			String query = generator.dropTableStatement(tableName);
			executeStatement(query);
		} catch (SQLException e) {
			Logger.e("Unable to drop table " + tableName, e);
			throw new SchemaMigrationException("Unable to drop table " + tableName, e);
		} 
	}

	/**
	 * Add a column to a table
	 * 
	 * @param column
	 * @param table
	 * @param afterColumn
	 */
	public static void addColumn(Column column, String table) {
		Validator.notNull(column, "Column can not be null");
		Validator.notNull(table, "Table can not be null");

		try {
			Generator generator = GeneratorFactory.getGenerator();
			String query = generator.addColumnStatement(column, table);
			executeStatement(query);
		} catch (SQLException e) {
			Logger.e("Unable to alter table " + table + " and add column " + column.getColumnName(), e);
			throw new SchemaMigrationException("Unable to alter table " + table + " and add column " + column.getColumnName(), e);
		}
	}

	/**
	 * Drop a column from a table
	 * 
	 * @param columnName
	 * @param tableName
	 */
	public static void dropColumn(String columnName, String tableName) {
		Validator.notNull(columnName, "Column can not be null");
		Validator.notNull(tableName, "Table can not be null");

		try {
			Generator generator = GeneratorFactory.getGenerator();
			String query = generator.dropColumnStatement(columnName, tableName);
			executeStatement(query);
		} catch (SQLException e) {
			Logger.e("Unable to alter table " + tableName + " and drop column " + columnName, e);
			throw new SchemaMigrationException("Unable to alter table " + tableName + " and drop column " + columnName, e);
		}

	}

	/**
	 * Add an index
	 * 
	 * @param index
	 */
	public static void addIndex(Index index) {
		Validator.notNull(index, "Index can not be null");

		try {
			Generator generator = GeneratorFactory.getGenerator();
			String query = generator.addIndex(index);
			executeStatement(query);
		} catch (SQLException e) {
			Logger.e("Unable to add index " + index.getName() + " on table " + index.getTableName(), e);
			throw new SchemaMigrationException("Unable to add index " + index.getName() + " on table " + index.getTableName(), e);
		}
	}

	/**
	 * Drop an index
	 * 
	 * @param indexName
	 * @param tableName
	 */
	public static void dropIndex(String indexName, String tableName) {
		Validator.notNull(indexName, "Index can not be null");

		try {
			Generator generator = GeneratorFactory.getGenerator();
			String query = generator.dropIndex(indexName, tableName);
			executeStatement(query);
		} catch (SQLException e) {
			Logger.e("Unable to drop index " + indexName + " from table " + tableName, e);
			throw new SchemaMigrationException("Unable to drop index " + indexName + " from table " + tableName, e);
		}
	}

	/**
	 * Add a foreign key
	 * 
	 * @param foreignKey
	 */
	public static void addForeignKey(ForeignKey foreignKey) {
		Validator.notNull(foreignKey, "ForeignKey can not be null");

		try {
			Generator generator = GeneratorFactory.getGenerator();
			String query = generator.addForeignKey(foreignKey);
			executeStatement(query);
		} catch (SQLException e) {
			Logger.e("Unable to add foreign key " + foreignKey.getName() + " on table " + foreignKey.getParentTable(), e);
			throw new SchemaMigrationException("Unable to add foreign key " + foreignKey.getName() + " on table " + foreignKey.getParentTable(), e);
		}
	}

	/**
	 * Drop a foreign key
	 * 
	 * @param foreignKey
	 */
	public static void dropForeignKey(ForeignKey foreignKey) {
		Validator.notNull(foreignKey, "ForeignKey can not be null");

		try {
			Generator generator = GeneratorFactory.getGenerator();
			String query = generator.dropForeignKey(foreignKey);
			executeStatement(query);
		} catch (SQLException e) {
			Logger.e("Unable to drop foreign key " + foreignKey.getName() + " from table " + foreignKey.getParentTable(), e);
			throw new SchemaMigrationException("Unable to drop foreign key " + foreignKey.getName() + " from table " + foreignKey.getParentTable(), e);
		}
	}

	/**
	 * Drop a foreign key
	 * 
	 * @param foreignKeyName
	 * @param childTableName
	 */
	public static void dropForeignKey(String foreignKeyName, String childTableName) {
		Validator.notNull(foreignKeyName, "ForeignKey can not be null");

		try {
			Generator generator = GeneratorFactory.getGenerator();
			String query = generator.dropForeignKey(foreignKeyName, childTableName);
			executeStatement(query);
		} catch (SQLException e) {
			Logger.e("Unable to drop foreign key " + foreignKeyName + " from table " + childTableName, e);
			throw new SchemaMigrationException("Unable to drop foreign key " + foreignKeyName + " from table " + childTableName, e);
		}

	}

	/**
	 * Rename a column
	 * 
	 * @param newColumnName
	 * @param oldColumnName
	 * @param tableName
	 */
	public static void renameColumn(String newColumnName, String oldColumnName, String tableName) {
		Validator.notNull(newColumnName, "New column name can not be null");
		Validator.notNull(oldColumnName, "Old column name can not be null");
		Validator.notNull(tableName, "Table name can not be null");

		try {
			Generator generator = GeneratorFactory.getGenerator();
			String query = generator.renameColumn(newColumnName, oldColumnName, tableName);
			executeStatement(query);
		} catch (SQLException e) {
			String message = "Unable to rename column " + oldColumnName + " to " + newColumnName + " on table " + tableName;
			Logger.e(message, e);
			throw new SchemaMigrationException(message, e);
		}
	}

	public static void execute(String query) {
		try {
			executeStatement(query);
		} catch (SQLException e) {
			String message = "Unable to execute " + query;
			Logger.e(message, e);
			throw new SchemaMigrationException(message, e);
		}
	}

	private static void executeStatement(String query) throws SQLException {
		SQLiteDatabase db = Migrate4Droid.getConfiguration().getDatabase();
		db.execSQL(query);
	}

	public static void execute(String query, Object ... args) {
		try {
			executeStatement(query, args);
		} catch (SQLException e) {
			String message = "Unable to execute with parameters " + query;
			Logger.e(message, e);
			throw new SchemaMigrationException(message, e);
		}
	}

	private static void executeStatement(String query, Object ... args) throws SQLException {
		SQLiteDatabase db = Migrate4Droid.getConfiguration().getDatabase();
		db.execSQL(query, args);
	}
}
