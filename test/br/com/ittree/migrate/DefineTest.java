package br.com.ittree.migrate;

import static br.com.ittree.migrate.Define.*;
import static br.com.ittree.migrate.Define.DataTypes.*;

import java.sql.Types;

import junit.framework.TestCase;
import br.com.ittree.migrate.Define.ColumnOption;
import br.com.ittree.migrate.Define.DataTypes;
import br.com.ittree.migrate.schema.Column;

public class DefineTest extends TestCase {

	public void testDefine_passColumnNameAndType() {
		Column column = Define.column("column_name", DataTypes.INTEGER);
		assertEquals("column_name", column.getColumnName());
		assertEquals(Types.INTEGER, column.getColumnType());
	}

	public void testDefine_createIntegerColumn() {
		Column column = Define.column("id", INTEGER);
		assertEquals("id", column.getColumnName());
		assertEquals(Types.INTEGER, column.getColumnType());
		assertFalse(column.isAutoincrement());
		assertFalse(column.isPrimaryKey());
		assertFalse(column.isUnicode());
		assertTrue(column.isNullable()); // nullable by default
	}	

	public void testDefine_createIntegerWithPrimary() {
		Column column = Define.column("id", INTEGER, primarykey());
		assertEquals("id", column.getColumnName());
		assertEquals(Types.INTEGER, column.getColumnType());
		assertTrue(column.isPrimaryKey());
		assertFalse(column.isUnicode());
		assertTrue(column.isNullable());
	}

	public void testDefine_createIntegerWithPrimaryAndAutoIncrement() {
		Column column = Define.column("id", INTEGER, primarykey(), autoincrement());
		assertEquals("id", column.getColumnName());
		assertEquals(Types.INTEGER, column.getColumnType());
		assertTrue(column.isAutoincrement());
		assertTrue(column.isPrimaryKey());
		assertTrue(column.isNullable());
		assertFalse(column.isUnicode());
	}

	public void testDefine_createVarcharWithNotNullAndUnicode() {
		Column column = Define.column("id", VARCHAR, length(255), notnull(), unicode());
		assertEquals("id", column.getColumnName());
		assertEquals(Types.VARCHAR, column.getColumnType());
		assertEquals(255, column.getLength());
		assertFalse(column.isNullable());
		assertTrue(column.isUnicode());
	}

	public void testDefine_createVarcharSizeDefaultPrimaryKey() {
		Column column = Define.column("id", VARCHAR, length(255), defaultValue("default_id"), primarykey());
		assertEquals("id", column.getColumnName());
		assertEquals(Types.VARCHAR, column.getColumnType());
		assertEquals(column.getLength(), 255);
		assertEquals(column.getDefaultValue(), "default_id");
		assertTrue(column.isPrimaryKey());
	}

	public void testDefine_createVarcharSizeDefaultValueWithExplicitNullable() {
		Column column = Define.column("id", VARCHAR, length(255), defaultValue("default_id"), notnull(false));
		assertEquals("id", column.getColumnName());
		assertEquals(Types.VARCHAR, column.getColumnType());
		assertEquals(255, column.getLength());
		assertEquals(column.getDefaultValue(), "default_id");
		assertTrue(column.isNullable());
	}

	public void testDefine_createVarcharNull() {
		Column column = Define.column("nullOptions", VARCHAR, (ColumnOption<?>)null);
		assertEquals("nullOptions", column.getColumnName());
		assertEquals(Types.VARCHAR, column.getColumnType());
		// defaults
		assertEquals(column.getLength(), -1);
		assertTrue(column.isNullable());
		assertFalse(column.isUnicode());
		assertFalse(column.isPrimaryKey());
		assertFalse(column.isAutoincrement());
		assertEquals(column.getDefaultValue(), null);
	}
}
