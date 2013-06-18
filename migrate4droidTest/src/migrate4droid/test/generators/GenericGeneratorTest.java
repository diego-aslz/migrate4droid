package migrate4droid.test.generators;

import java.sql.Types;

import migrate4droid.Define;
import migrate4droid.generators.GenericGenerator;
import migrate4droid.schema.Column;
import migrate4droid.schema.Table;
import android.test.AndroidTestCase;

public class GenericGeneratorTest extends AndroidTestCase {

	private GenericGenerator generator;

	protected void setUp() throws Exception {
		super.setUp();

		generator = new GenericGenerator();
	}

	public void testMakeColumnString_SimpleColumn() {
		Column column = new Column("basic", Types.INTEGER);

		String columnString = generator.makeColumnString(column);

		assertEquals("\"basic\" INT NULL", columnString);
	}

	public void testMakeColumnString_PrimaryKeyNonIncrementing() {
		Column column = new Column("basic", Types.INTEGER, -1, true, false, null, false);

		String columnString = generator.makeColumnString(column);

		assertEquals("\"basic\" INT NOT NULL PRIMARY KEY", columnString);
	}

	public void testMakeColumnString_PrimaryKeyIncrementing() {
		Column column = new Column("basic", Types.INTEGER, -1, true, false, null, true);

		String columnString = generator.makeColumnString(column);

		assertEquals("\"basic\" INT NOT NULL AUTO_INCREMENT PRIMARY KEY", columnString);
	}

	public void testMakeColumnString_VarcharWithDefault() {
		Column column = new Column("basic", Types.VARCHAR, 50, false, false, "NA", false);

		String columnString = generator.makeColumnString(column);

		assertEquals("\"basic\" VARCHAR(50) NOT NULL DEFAULT 'NA'", columnString);
	}

	public void testCreateTableStatement() {
		String expected = "CREATE TABLE \"sample\" (\"id\" INT NOT NULL PRIMARY KEY, \"desc\" VARCHAR(50) NOT NULL);";

		Column[] columns = new Column[2];
		columns[0] = new Column("id", Types.INTEGER, -1, true, false, null, false);
		columns[1] = new Column("desc", Types.VARCHAR, 50, false, false, null, false);

		Table table = Define.table("sample", columns);

		String tableString = generator.createTableStatement(table);

		assertEquals(expected, tableString);
	}
}
