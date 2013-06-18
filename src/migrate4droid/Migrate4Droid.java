package migrate4droid;

import java.util.ArrayList;
import java.util.List;

import migrate4droid.log.Logger;
import migrate4droid.misc.M00_Setup;
import migrate4droid.model.DbMigration;
import migrate4droid.model.DbMigrationDao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Used to set up the database and run the migrations.
 * @author Diego Aguir Selzlein
 *
 */
public abstract class Migrate4Droid {
	public static final String VERSION = "0.1.0";
	private static Configuration configuration;
	private static DbMigrationDao migrationDao;

	/**
	 * Moves the database to the <code>migration</code> specified, using the migrations in the
	 * <code>configuration</code>, iterating them and checking:
	 * <ul>
	 * <li>If the migration's key is smaller than or equal to the desired <code>migration</code>'s
	 * key parameterized and it's not present in the database, the migration will get upgraded.
	 * That is, its method {@link Migration#up()} will be called. The order will be the same as
	 * the migrations are in the <code>configuration</code>.</li>
	 * 
	 * <li>If the migration's key is greater than the desired <code>migration</code>'s
	 * key parameterized and it IS present in the database, the migration will get downgraded.
	 * That is, its method {@link Migration#down()} will be called. The order will be the opposite
	 * the migrations are in the <code>configuration</code>.</li>
	 * </ul>
	 * 
	 * @param configuration Configuration with the database and all the migrations that have
	 * to be considered during the migration.
	 * @param migration String key of the migration the database is desired to be.
	 */
	public static void migrate(Configuration configuration, String migration) {
		Migrate4Droid.configuration = configuration;
		ensureMigrationDao();
		List<Migration> migrations = configuration.getMigrations();
		List<Migration> downs = new ArrayList<Migration>();
		List<String> actual = migrationDao.getAllMigrations();
		for (Migration m : migrations) {
			String mig = m.getMigration();
			if (mig.compareTo(migration) <= 0 && !actual.contains(mig))
				runMigration(m);
			else if (mig.compareTo(migration) > 0 && actual.contains(mig))
				downs.add(0, m);
		}
		for (Migration m : downs)
			runMigration(m, false);
	}

	/**
	 * Runs all the migrations in the configuration which version key is not already
	 * present in the database. The migration will get run in the same order they are in
	 * the list.
	 * @param configuration Configuration with the database and all the migrations.
	 */
	public static void migrate(Configuration configuration) {
		Migrate4Droid.configuration = configuration;
		ensureMigrationDao();
		List<Migration> migrations = configuration.getMigrations();
		List<String> actual = migrationDao.getAllMigrations();
		for (Migration m : migrations)
			if (!actual.contains(m.getMigration()))
				runMigration(m);
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
