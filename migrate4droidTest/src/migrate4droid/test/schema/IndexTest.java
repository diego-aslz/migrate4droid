package migrate4droid.test.schema;

import migrate4droid.misc.SchemaMigrationException;
import migrate4droid.schema.Index;
import android.test.AndroidTestCase;

public class IndexTest extends AndroidTestCase {

	private String tableName;
	private String[] columnNames;
	
	protected void setUp() throws Exception {
		tableName = "tablename";
		
		super.setUp();
	}
	
	public void testConstructor_ThrowsExceptionIfMissingTableName() {
		columnNames = new String[1];
		columnNames[0] = "onecolumn";
		
		try {
			new Index(null, columnNames);
			fail("Should have thrown a SchemaMigrationException");
		} catch (SchemaMigrationException expected) {
		} catch (Exception exception) {
			fail("Should have thrown a SchemaMigrationException but threw " + exception.getClass().getName());
		}
	}
	
	public void testConstructor_ThrowsExceptionIfMissingColumns() {
		
		try {
			new Index(tableName, null);
			fail("Should have thrown a SchemaMigrationException");
		} catch (SchemaMigrationException expected) {
		} catch (Exception exception) {
			fail("Should have thrown a SchemaMigrationException but threw " + exception.getClass().getName());
		}
	}
	
	public void testConstructor_ThrowsExceptionIfMissingColumnHaveNoSize() {
		
		columnNames = new String[0];
		
		try {
			new Index(tableName, columnNames);
			fail("Should have thrown a SchemaMigrationException");
		} catch (SchemaMigrationException expected) {
		} catch (Exception exception) {
			fail("Should have thrown a SchemaMigrationException but threw " + exception.getClass().getName());
		}
	}
	
	public void testConstructor_ThrowsExceptionIfMissingColumnsFoundButNull() {
		
		columnNames = new String[1];
		
		try {
			new Index(tableName, columnNames);
			fail("Should have thrown a SchemaMigrationException");
		} catch (SchemaMigrationException expected) {
		} catch (Exception exception) {
			fail("Should have thrown a SchemaMigrationException but threw " + exception.getClass().getName());
		}
	}
	
	public void testConstructor_DefaultNameUsesEightCharactersFromTableAndColumn() {

		String[] columnNames = new String[1];
		columnNames[0] = "onecolumn";
		
		String expected = "idx_tablenam_onecolum";
		
		assertEquals(expected, new Index(tableName, columnNames).getName());
	}
	
	public void testConstructor_DefaultNameUsesFullTableNameIfItIsLessThanEightCharactersLong() {

		String[] columnNames = new String[1];
		columnNames[0] = "onecolumn";
		
		String expected = "idx_table_onecolum";
		
		assertEquals(expected, new Index("table", columnNames).getName());
	}
	
	public void testConstructor_DefaultNameUsesFirstAndLastColumns() {

		String[] columnNames = new String[5];
		columnNames[0] = "onecolumn";
		columnNames[1] = "twocolumn";
		columnNames[2] = "threecolumn";
		columnNames[3] = "fourcolumn";
		columnNames[4] = "fivecolumn";
		
		String expected = "idx_tablenam_onec_five";
		
		assertEquals(expected, new Index(tableName, columnNames).getName());
	}
	
	public void testConstructor_DefaultNameUsesFirstAndLastColumns_ShortNamesShouldWork() {

		String[] columnNames = new String[5];
		columnNames[0] = "one";
		columnNames[1] = "twocolumn";
		columnNames[2] = "threecolumn";
		columnNames[3] = "fourcolumn";
		columnNames[4] = "fivecolumn";
		
		String expected = "idx_tablenam_one_five";
		
		assertEquals(expected, new Index(tableName, columnNames).getName());
	}
	
	public void testConstructor_DefaultNameUsesFirstAndLastColumns_NullNamesShouldWork() {

		String[] columnNames = new String[5];
		columnNames[0] = null;
		columnNames[1] = "twocolumn";
		columnNames[2] = "threecolumn";
		columnNames[3] = "fourcolumn";
		columnNames[4] = null;
		
		String expected = "idx_tablenam_null_null";
		
		assertEquals(expected, new Index(tableName, columnNames).getName());
	}
	
	
}
