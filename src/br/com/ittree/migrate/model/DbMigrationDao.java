package br.com.ittree.migrate.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbMigrationDao {
	private SQLiteDatabase db;

	public DbMigrationDao(SQLiteDatabase db) {
		this.db = db;
	}

	public DbMigration find(String migration) {
		Cursor c = db.query(DbMigration.TABLE, new String[] { DbMigration.COLUMN_MIGRATION },
				DbMigration.COLUMN_MIGRATION + " = ?", new String[] {
				migration }, null, null, null);
		if (c.moveToFirst()) {
			DbMigration mig = new DbMigration();
			mig.setMigration(c.getString(0));
			return mig;
		}
		return null;
	}

	public boolean delete(String migration) {
		return db.delete(DbMigration.TABLE, DbMigration.COLUMN_MIGRATION + " = ?", new String[] {
				migration }) > 0;
	}

	public void save(DbMigration migration) {
		ContentValues cv = new ContentValues();
		cv.put(DbMigration.COLUMN_MIGRATION, migration.getMigration());
		db.insert(DbMigration.TABLE, null, cv);
	}

	public SQLiteDatabase getDatabase() {
		return db;
	}

	public void setDatabase(SQLiteDatabase db) {
		this.db = db;
	}
}
