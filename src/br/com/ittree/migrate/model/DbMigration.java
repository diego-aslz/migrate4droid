package br.com.ittree.migrate.model;

public class DbMigration {
	public static final String TABLE = "db_migrations";
	public static final String COLUMN_VERSION = "version";
	public static final String COLUMN_MIGRATION = "migration";
	private int version;
	private String migration;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getMigration() {
		return migration;
	}

	public void setMigration(String migration) {
		this.migration = migration;
	}
}
