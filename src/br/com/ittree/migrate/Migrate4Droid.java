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
	 * Moves the database from the <code>oldVersion</code> to the <code>newVersion</code>.
	 * <br />
	 * <br />
	 * If the <code>oldVersion</code> is smaller than or equal to <code>newVersion</code>, all the
	 * migrations in <code>configuration</code> which version is greater than or equal
	 * to <code>oldVersion</code> and smaller than
	 * or equal to the <code>newVersion</code> and which migration String is not in migrations
	 * table will get upgraded. That is, their method {@link Migration#up()} will be called.
	 * <br />
	 * <br />
	 * If the <code>oldVersion</code> is greater than <code>newVersion</code>, all the
	 * migrations in <code>configuration</code> which version is greater than <code>newVersion</code>
	 * and which migration String is not in migrations table will get downgraded.
	 * That is, their method {@link Migration#down()} will be called.
	 * 
	 * @param configuration Configuration with the database and all the migrations that have
	 * to be considered during this migration.
	 * @param oldVersion Version the database is coming from.
	 * @param newVersion Version the database is going to.
	 */
	public static void migrate(Configuration configuration, int oldVersion, int newVersion) {
		Migrate4Droid.configuration = configuration;
		ensureMigrationDao();
		for (Migration m : configuration.getMigrations()) {
			int v = m.getVersion();
			if (v >= oldVersion && v <= newVersion &&
					migrationDao.find(m.getMigration()) == null) {
				runMigration(m);
			} else if (oldVersion > newVersion && v > newVersion &&
					migrationDao.find(m.getMigration()) != null)
				runMigration(m, false);
		}
	}

	/**
	 * Executes the migration in the database, upgrading it.The method {@link Migration#up()} of
	 * the {@link Migration} will get called
	 * @param m The migration to run.
	 */
	private static void runMigration(Migration m) {
		runMigration(m, true);
	}

	/**
	 * Executes the migration in the database. If <code>up</code> is set to <code>true</code>,
	 * then the method {@link Migration#up()} will be called. Otherwise, the method
	 * {@link Migration#down()} will get called.
	 * @param m The migration to run.
	 * @param up If <code>true</code>, it will upgrade. Otherwise, it will downgrade.
	 */
	private static void runMigration(Migration m, boolean up) {
		SQLiteDatabase db = configuration.getDatabase();
		db.beginTransaction();
		try {
			if (up) {
				m.up();
				DbMigration mig = new DbMigration();
				mig.setMigration(m.getMigration());
				mig.setVersion(m.getVersion());
				migrationDao.save(mig);
			} else {
				m.down();
				migrationDao.delete(m.getMigration());
			}
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
