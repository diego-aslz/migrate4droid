package migrate4droid.generators;

import migrate4droid.log.Logger;
import migrate4droid.misc.SchemaMigrationException;
import migrate4droid.misc.Validator;
import migrate4droid.schema.Column;
import migrate4droid.schema.ForeignKey;
import migrate4droid.schema.Index;
import migrate4droid.schema.Table;

/**
 * Default Generator for migrate4j.  Designed to work specifically for
 * H2 databases.
 *
 */
public class GenericGenerator implements Generator {

	/**
	 * ALTER TABLE <tableName> ADD <column.name> <column.type>[(<column.length>)] 
	 * 		[NOT] NULL [AUTO_INCREMENT] [PRIMARY KEY] [DEFAULT <column.default>]
	 * 		[BEFORE <existingcolumn>]
	 * 
	 * <p>Determines column placement based on existing columns.  Uses JDBC to find 
	 * the <code>afterColumn</code>, then selects the next column to be used in the
	 * <code>BEFORE</code> clause.  Obviously, Generators for databases that support 
	 * an actual <code>AFTER</code> clause must override this method.
	 */
	public String addColumnStatement(Column column, String tableName) {

		Validator.notNull(column, "Column can not be null");
		Validator.notNull(tableName, "Table name can not be null");

		StringBuffer retVal = new StringBuffer();

		retVal.append("ALTER TABLE ")
		.append(wrapName(tableName))
		.append(" ADD ")
		.append(makeColumnString(column));

		return retVal.toString();
	}

	/**
	 * ALTER TABLE <foreignKey.childTable> ADD CONSTRAINT <foreignKey.name>
	 * 		FOREIGN KEY (foreignKey.childColumn[,...]) REFERENCES 
	 * 		<foreignKey.parentTable> (foreignKey.parentColumn[,...])
	 */
	public String addForeignKey(ForeignKey foreignKey) {
		Validator.notNull(foreignKey, "Foreign key can not be null");

		StringBuffer retVal = new StringBuffer();

		String[] childColumns = wrapStrings(foreignKey.getChildColumns());
		String[] parentColumns = wrapStrings(foreignKey.getParentColumns());


		retVal.append("ALTER TABLE ")
		.append(wrapName(foreignKey.getChildTable()))
		.append(" ADD CONSTRAINT ")
		.append(wrapName(foreignKey.getName()))
		.append(" FOREIGN KEY  (")
		.append(GeneratorHelper.makeStringList(childColumns))
		.append(") REFERENCES ")
		.append(wrapName(foreignKey.getParentTable()))
		.append(" (")
		.append(GeneratorHelper.makeStringList(parentColumns))
		.append(")");

		return retVal.toString();
	}	

	/**
	 * CREATE [UNIQUE] INDEX <name> [PRIMARY KEY] ON <index.table>(<index.columnName>[,...])
	 */
	public String addIndex(Index index) {
		Validator.notNull(index, "Index can not be null");

		StringBuffer query = new StringBuffer("CREATE ");

		if (index.isUnique()) {
			query.append("UNIQUE ");
		}

		query.append("INDEX ")
		.append(wrapName(index.getName()))
		.append(" ");

		if (index.isPrimaryKey()) {
			query.append("PRIMARY KEY ");
		}

		query.append("ON ")
		.append(wrapName(index.getTableName()))
		.append("(");

		String[] columns = index.getColumnNames();
		String comma = "";
		for (int x = 0 ; x < columns.length ; x++) {	
			query.append(comma)
			.append(wrapName(columns[x]));

			comma = ", ";

		}

		query.append(")");

		return query.toString();
	}


	public String createTableStatement(Table table, String options) {
		return createTableStatement(table);
	}


	/**
	 * CREATE TABLE <table.name> (<column.name> <column.type>[(<column.length>)] 
	 * 		[NOT] NULL [AUTO_INCREMENT] [PRIMARY KEY] [DEFAULT <column.default>][,...])
	 */
	public String createTableStatement(Table table) {

		StringBuffer retVal = new StringBuffer();

		Validator.notNull(table, "Table can not be null");		

		Column[] columns = table.getColumns();

		Validator.notNull(columns, "Columns can not be null");
		Validator.isTrue(columns.length > 0, "At least one column must exist");

		int numberOfAutoIncrementColumns = GeneratorHelper.countAutoIncrementColumns(columns);

		Validator.isTrue(numberOfAutoIncrementColumns <=1, "Can not have more than one autoincrement key");

		retVal.append("CREATE TABLE ")
		.append(wrapName(table.getTableName()))
		.append(" (");

		try {
			for (int x = 0 ; x < columns.length ; x++ ){
				Column column = (Column)columns[x];

				if (x > 0) {
					retVal.append(", ");
				}

				retVal.append(makeColumnString(column));

			}
		} catch (ClassCastException e) {
			Logger.e("A table column couldn't be cast to a column: " + e.getMessage());
			throw new SchemaMigrationException("A table column couldn't be cast to a column: " + e.getMessage());
		}

		return retVal.toString().trim() + ");";
	}

	/**
	 * ALTER TABLE <tableName> drop <columnName>
	 */
	public String dropColumnStatement(String columnName, String tableName) {

		Validator.notNull(columnName, "Column name can not be null");
		Validator.notNull(tableName, "Table name can not be null");

		StringBuffer query = new StringBuffer();

		query.append("ALTER TABLE ")
		.append(wrapName(tableName))
		.append(" DROP ")
		.append(wrapName(columnName));

		return query.toString();
	}

	/**
	 * DROP INDEX <indexName>
	 */
	public String dropIndex(String indexName, String tableName) {

		Validator.notNull(indexName, "Index name can not be null");

		StringBuffer query = new StringBuffer();

		query.append("DROP INDEX ")
		.append(wrapName(indexName));

		return query.toString();
	}

	/**
	 * DROP INDEX <indexName>
	 */
	public String dropIndex(Index index) {
		Validator.notNull(index, "Index can not be null");

		return dropIndex(index.getName(), index.getTableName());

	}

	/**
	 * DROP TABLE <tableName>
	 */
	public String dropTableStatement(String tableName) {
		Validator.notNull(tableName, "Table name must not be null");

		StringBuffer retVal = new StringBuffer();
		retVal.append("DROP TABLE ")
		.append(wrapName(tableName));

		return retVal.toString();
	}

	/**
	 * ALTER TABLE <childTable> DROP CONSTRAINT <foreignKeyName>
	 */
	public String dropForeignKey(ForeignKey foreignKey) {
		Validator.notNull(foreignKey, "Foreign key can not be null");		

		return dropForeignKey(foreignKey.getName(), foreignKey.getChildTable());
	}

	/**
	 * ALTER TABLE <childTable> DROP CONSTRAINT <foreignKeyName>
	 */
	public String dropForeignKey(String foreignKeyName, String childTable) {
		Validator.notNull(foreignKeyName, "Foreign key name can not be null");
		Validator.notNull(childTable, "Child table name can not be null");

		StringBuffer retVal = new StringBuffer();

		retVal.append("ALTER TABLE ")
		.append(wrapName(childTable))
		.append(" DROP CONSTRAINT ")
		.append(wrapName(foreignKeyName));

		return retVal.toString();
	}

	public String wrapName(String name) {
		StringBuffer wrap = new StringBuffer();

		wrap.append(getIdentifier())
		.append(name)
		.append(getIdentifier());

		return wrap.toString();
	}

	public String[] wrapStrings(String[] strings) {

		String[] wrapped = new String[strings.length];

		for (int x = 0 ; x < strings.length ; x++ ) {
			wrapped[x] = wrapName(strings[x]);
		}

		return wrapped;
	}

	protected String getIdentifier() {
		return "\"";
	}

	public String makeColumnString(Column column) {
		StringBuffer retVal = new StringBuffer();

		retVal.append(wrapName(column.getColumnName()))
		.append(" ");		

		int type = column.getColumnType();

		retVal.append(GeneratorHelper.getSqlName(type));
		if (GeneratorHelper.needsLength(type)) {

			retVal.append("(")
			.append(column.getLength())
			.append(")");

		}
		retVal.append(" ");

		if (!column.isNullable()) {
			retVal.append("NOT ");;
		}
		retVal.append("NULL ");

		if (column.isAutoincrement()) {
			retVal.append("AUTO_INCREMENT ");
		}

		if (column.isPrimaryKey()) {
			retVal.append("PRIMARY KEY ");
		}

		if (column.getDefaultValue() != null) {
			retVal.append("DEFAULT '")
			.append(column.getDefaultValue())
			.append("' ");
		}

		return retVal.toString().trim();
	}

	public String renameColumn(String newColumnName, String oldColumnName,
			String tableName) {

		Validator.notNull(newColumnName, "New column name can not be null");
		Validator.notNull(oldColumnName, "Old column name can not be null");
		Validator.notNull(tableName, "Table name can not be null");

		StringBuffer query = new StringBuffer();

		query.append("ALTER TABLE ")
		.append(wrapName(tableName))
		.append(" ALTER COLUMN ")
		.append(wrapName(oldColumnName))
		.append(" RENAME TO ")
		.append(wrapName(newColumnName));

		return query.toString();
	}
}
