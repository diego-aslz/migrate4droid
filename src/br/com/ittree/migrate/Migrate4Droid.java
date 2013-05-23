package br.com.ittree.migrate;

import android.database.sqlite.SQLiteDatabase;
import br.com.ittree.migrate.log.Logger;
import br.com.ittree.migrate.misc.M00_Setup;
import br.com.ittree.migrate.model.DbMigration;
import br.com.ittree.migrate.model.DbMigrationDao;

/**
 * Used to set up the database and run the migrations.
 * @author Diego Aguir Selzlein
 *
 */
public abstract class Migrate4Droid {
	public static final String VERSION = "0.0.1";
	private static Configuration configuration;
	private static DbMigrationDao migrationDao;

	/**
	 * Executes all pending migrations between <code>oldVersion</code> and <code>newVersion</code>,
	 * inclusive. A migration will get run only if it's added to the <code>configuration</code>, its
	 * version is in the range and its migration key (obtained by {@link Migration#getMigration()}
	 * is not in the migrations control table.
	 * 
	 * @param configuration
	 */
	public static void migrate(Configuration configuration, int oldVersion, int newVersion) {
		Migrate4Droid.configuration = configuration;
		ensureMigrationDao();
		for (Migration m : configuration.getMigrations()) {
			int v = m.getVersion();
			if (v > oldVersion && v <= newVersion &&
					migrationDao.find(m.getMigration()) == null) {
				runMigration(m);
			}
		}
	}

	private static void runMigration(Migration m) {
		SQLiteDatabase db = configuration.getDatabase();
		db.beginTransaction();
		try {
			m.up();
			DbMigration mig = new DbMigration();
			mig.setMigration(m.getMigration());
			mig.setVersion(m.getVersion());
			migrationDao.save(mig);
			db.setTransactionSuccessful();
		} catch(Exception e) {
			Logger.e("Exception when migrating " + m.getClass().getName(), e);
		} finally {
			db.endTransaction();
		}
	}

	private static void ensureMigrationDao() {
		if (migrationDao == null)
			migrationDao = new DbMigrationDao(configuration.getDatabase());
		else
			migrationDao.setDatabase(configuration.getDatabase());
	}

	/**
	 * This method needs to be called just once by database. Call it when creating your database
	 * in order to set up control tables needed to migration control.
	 * @param configuration
	 */
	public static void setup(Configuration configuration) {
		Migrate4Droid.configuration = configuration;
		ensureMigrationDao();
		runMigration(new M00_Setup());
		for (Migration m : configuration.getMigrations())
			runMigration(m);
	}

	public static Configuration getConfiguration() {
		return configuration;
	}
}
