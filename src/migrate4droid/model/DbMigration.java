package migrate4droid.model;

public class DbMigration {
	public static final String TABLE = "db_migrations";
	public static final String COLUMN_MIGRATION = "migration";
	private String migration;

	public String getMigration() {
		return migration;
	}

	public void setMigration(String migration) {
		this.migration = migration;
	}
}
