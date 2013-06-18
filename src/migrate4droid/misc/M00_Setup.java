package migrate4droid.misc;

import static migrate4droid.Define.column;
import static migrate4droid.Define.notnull;
import static migrate4droid.Define.primarykey;
import static migrate4droid.Define.table;
import static migrate4droid.Define.DataTypes.VARCHAR;
import migrate4droid.Execute;
import migrate4droid.Migration;
import migrate4droid.model.DbMigration;

public class M00_Setup implements Migration {

	@Override
	public void up() {
		Execute.createTable(table(DbMigration.TABLE,
				column(DbMigration.COLUMN_MIGRATION, VARCHAR, notnull(), primarykey())
				));
	}

	@Override
	public void down() {
		Execute.dropTable(DbMigration.TABLE);
	}

	@Override
	public String getMigration() {
		return "201305222340";
	}
}
