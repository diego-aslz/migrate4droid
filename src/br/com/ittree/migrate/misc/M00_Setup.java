package br.com.ittree.migrate.misc;

import static br.com.ittree.migrate.Define.column;
import static br.com.ittree.migrate.Define.notnull;
import static br.com.ittree.migrate.Define.primarykey;
import static br.com.ittree.migrate.Define.table;
import static br.com.ittree.migrate.Define.DataTypes.VARCHAR;
import br.com.ittree.migrate.Execute;
import br.com.ittree.migrate.Migration;
import br.com.ittree.migrate.model.DbMigration;

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
