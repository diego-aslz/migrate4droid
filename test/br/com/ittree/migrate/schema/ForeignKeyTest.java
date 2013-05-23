package br.com.ittree.migrate.schema;

import junit.framework.TestCase;
import br.com.ittree.migrate.misc.SchemaMigrationException;

public class ForeignKeyTest extends TestCase {

	private String parentTable;
	private String[] parentColumns;
	private String childTable;
	private String[] childColumns;
	
	protected void setUp() throws Exception {
		parentTable = "parentTable";
		childTable = "childTable";
		parentColumns = new String[] { "pcolumn" };
		childColumns = new String[] { "ccolumn" };
		
		super.setUp();
	}
	
	public void testConstructor_ThrowsExceptionIfMissingParentTableName() {
		
		try {
			new ForeignKey("name", null, parentColumns, childTable, childColumns);
			fail("Should have thrown a SchemaMigrationException");
		} catch (SchemaMigrationException expected) {
		} catch (Exception exception) {
			fail("Should have thrown a SchemaMigrationException but threw " + exception.getClass().getName());
		}
	}
	
	public void testConstructor_ThrowsExceptionIfMissingParentColumns() {
				
		try {
			new ForeignKey("name", parentTable, null, childTable, childColumns);
			fail("Should have thrown a SchemaMigrationException");
		} catch (SchemaMigrationException expected) {
		} catch (Exception exception) {
			fail("Should have thrown a SchemaMigrationException but threw " + exception.getClass().getName());
		}
	}
	
	public void testConstructor_ThrowsExceptionIfMissingChildTableName() {
				
		try {
			new ForeignKey("name", parentTable, parentColumns, null, childColumns);
			fail("Should have thrown a SchemaMigrationException");
		} catch (SchemaMigrationException expected) {
		} catch (Exception exception) {
			fail("Should have thrown a SchemaMigrationException but threw " + exception.getClass().getName());
		}
	}
	
	public void testConstructor_ThrowsExceptionIfMissingChildColumns() {
				
		try {
			new ForeignKey("name", parentTable, parentColumns, childTable, null);
			fail("Should have thrown a SchemaMigrationException");
		} catch (SchemaMigrationException expected) {
		} catch (Exception exception) {
			fail("Should have thrown a SchemaMigrationException but threw " + exception.getClass().getName());
		}
	}
	
	public void testConstructor_ThrowsExceptionIfParentColumnHaveNoSize() {
		
		parentColumns = new String[0];
		
		try {
			new ForeignKey("name", parentTable, parentColumns, childTable, childColumns);
			fail("Should have thrown a SchemaMigrationException");
		} catch (SchemaMigrationException expected) {
		} catch (Exception exception) {
			fail("Should have thrown a SchemaMigrationException but threw " + exception.getClass().getName());
		}
	}
	
	public void testConstructor_ThrowsExceptionIfChildColumnHaveNoSize() {
		
		childColumns = new String[0];
		
		try {
			new ForeignKey("name", parentTable, parentColumns, childTable, childColumns);
			fail("Should have thrown a SchemaMigrationException");
		} catch (SchemaMigrationException expected) {
		} catch (Exception exception) {
			fail("Should have thrown a SchemaMigrationException but threw " + exception.getClass().getName());
		}
	}
	
	public void testConstructor_ThrowsExceptionIfParentColumnsFoundButNull() {
		
		parentColumns = new String[1];
		
		try {
			new ForeignKey("name", parentTable, parentColumns, childTable, childColumns);
			fail("Should have thrown a SchemaMigrationException");
		} catch (SchemaMigrationException expected) {
		} catch (Exception exception) {
			fail("Should have thrown a SchemaMigrationException but threw " + exception.getClass().getName());
		}
	}

	public void testConstructor_ThrowsExceptionIfChildColumnsFoundButNull() {
		
		childColumns = new String[1];
		
		try {
			new ForeignKey("name", parentTable, parentColumns, childTable, childColumns);
			fail("Should have thrown a SchemaMigrationException");
		} catch (SchemaMigrationException expected) {
		} catch (Exception exception) {
			fail("Should have thrown a SchemaMigrationException but threw " + exception.getClass().getName());
		}
	}
	
	public void testCreateName() {
		
		String name = ForeignKey.createName(parentTable, childTable, parentColumns);
		assertNotNull(name);
		assertEquals("fky_paren_pcolumn_child", name);
	}
}
