package br.com.ittree.migrate;

/**
 * Represents changes to a database schema.
 * Migrations are applied in the order they are added to the {@link Configuration} object.
 * Name migrations with something descriptive, with the database version in the name to ease
 * reading, like M05_CreatePeople.
 * 
 */
public interface Migration {

	/**
	 * Work to perform when upgrading the database
	 * schema
	 */
	public void up();

	/**
	 * Work to perform when reverting the database
	 * schema to a previous version
	 */
	public void down();

	/**
	 * Returns the database version of this migration.
	 * @return
	 */
	public int getVersion();

	/**
	 * Returns a unique identifier of this migration. Please, make sure this will never repeat in
	 * any of future migrations. You can use something like the current timestamp: YYYYMMDDHHMMSS.
	 * @return
	 */
	public String getMigration();
}
