package br.com.ittree.migrate;

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
	private List<Migration> migrations = new ArrayList<Migration>();

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	public List<Migration> getMigrations() {
		return migrations;
	}

	public void addMigration(Migration m) {
		migrations.add(m);
	}
}
