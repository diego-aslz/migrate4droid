package migrate4droid.test.generators;

import java.sql.Types;

import migrate4droid.generators.GeneratorHelper;
import migrate4droid.schema.Column;
import android.test.AndroidTestCase;

public class GeneratorHelperTest extends AndroidTestCase {

	public void testGetSqlName() {
		assertEquals("BIGINT", GeneratorHelper.getSqlName(Types.BIGINT));
		assertEquals("BOOL", GeneratorHelper.getSqlName(Types.BOOLEAN));
		assertEquals("CHAR", GeneratorHelper.getSqlName(Types.CHAR));
		assertEquals("DATE", GeneratorHelper.getSqlName(Types.DATE));
		assertEquals("DECIMAL", GeneratorHelper.getSqlName(Types.DECIMAL));
		assertEquals("DOUBLE", GeneratorHelper.getSqlName(Types.DOUBLE));
		assertEquals("FLOAT", GeneratorHelper.getSqlName(Types.FLOAT));
		assertEquals("INT", GeneratorHelper.getSqlName(Types.INTEGER));
		assertEquals("NUMERIC", GeneratorHelper.getSqlName(Types.NUMERIC));
		assertEquals("SMALLINT", GeneratorHelper.getSqlName(Types.SMALLINT));
		assertEquals("TIME", GeneratorHelper.getSqlName(Types.TIME));
		assertEquals("TIMESTAMP", GeneratorHelper.getSqlName(Types.TIMESTAMP));
		assertEquals("TINYINT", GeneratorHelper.getSqlName(Types.TINYINT));
		assertEquals("VARCHAR", GeneratorHelper.getSqlName(Types.VARCHAR));
		
		assertNull(GeneratorHelper.getSqlName(Integer.MAX_VALUE));
	}
	
	public void testNeedsLength() {
		assertTrue(GeneratorHelper.needsLength(Types.CHAR));
		assertTrue(GeneratorHelper.needsLength(Types.VARCHAR));
		
		assertFalse(GeneratorHelper.needsLength(Types.DATE));
		assertFalse(GeneratorHelper.needsLength(Types.TIMESTAMP));
	}
	
	public void testNeedsQuotes() {
		assertTrue(GeneratorHelper.needsQuotes(Types.CHAR));
		assertTrue(GeneratorHelper.needsQuotes(Types.VARCHAR));
		
		assertFalse(GeneratorHelper.needsQuotes(Types.DATE));
		assertFalse(GeneratorHelper.needsQuotes(Types.TIMESTAMP));
	}
	
	public void testCountPrimaryKeyColumns() {
		Column[] columns = new Column[3];
		columns[0] = new Column("primary1", Types.INTEGER, -1, true, false, null, true);
		columns[1] = new Column("column1", Types.INTEGER);
		columns[2] = new Column("column2", Types.INTEGER);
		
		assertEquals(1, GeneratorHelper.countPrimaryKeyColumns(columns));
		
		columns[2] = new Column("primary2", Types.INTEGER, -1, true, true, "NA", false);
		
		assertEquals(2, GeneratorHelper.countPrimaryKeyColumns(columns));
		
	}
}
