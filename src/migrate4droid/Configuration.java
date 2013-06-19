package migrate4droid;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

/**
 * Configuration with database connection and all the available migrations.
 * @author nerde
 *
 */
public class Configuration {
	private SQLiteDatabase database;
	protected int initialVersion = 0;
	private List<Migration> migrations = new ArrayList<Migration>();

	public Configuration() {
		super();
	}

	public Configuration(SQLiteDatabase database) {
		this();
		this.database = database;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	public List<Migration> getMigrations() {
		return migrations;
	}

	/**
	 * Adds a migration to be used when migrating with this configuration. The order you add
	 * migrations here will be the order they will get run when upgrading and the inverse
	 * order they will be run when downgrading.
	 * @param m
	 * @return
	 */
	public Configuration addMigration(Migration m) {
		migrations.add(m);
		return this;
	}

	/**
	 * Calculates the database version based on the {@link Configuration#initialVersion}
	 * field and the size of {@link Configuration#migrations} added.
	 * @return
	 */
	public int getVersion() {
		return initialVersion + migrations.size();
	}
}
