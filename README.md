# Migrate4Droid

<strong>This API is under construction.</strong>

This API was built to be used in Android applications in order to manage
database migrations similarly to Rails.

It's based on the [migrate4j project](http://migrate4j.sourceforge.net/). By the
way, thank you so much, Migrate4J team. Your project is awesome.

## Installation

Add the migrate4droid.jar file to your lib directory.

## Configuring your database helper

You will need to configure your DB Helper to use the Migrate4Droid methods. To
do so, do this in your DBHelper:

```java
public class DBHelper extends SqliteOpenHelper {
  // ...

  /**
   * This is called when the database is first created. Usually you should call createTable statements here to create
   * the tables that will store your data.
   */
  @Override
  public void onCreate(SQLiteDatabase db) {
    Configuration config = new Configuration();
    config.setDatabase(db);
    // Add migrations to run when the database is created. These will run without
    // checking versions and migrations keys.
    // config.addMigration(new M00_MySetup());
    Migrate4Droid.setup(config);
  }


  /**
   * This is called when your application is upgraded and it has a higher version
   * number. This allows you to adjust the various data to match the new version number.
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Configuration config = new Configuration();
    config.setDatabase(db);
    config.addMigration(new M01_CreateSomeTable());
    config.addMigration(new M02_AddSomeColumnToSomeTable());
    Migrate4Droid.migrate(config, oldVersion, newVersion);
  }
```

<strong>IMPORTANT:</strong> The order you add your migrations is the order they
will get run if they fit the requisites of the method <code>Migrate4Droid.migrate</code>.

## Creating your migration classes

The migration classes work like they do in Rails. If you don't know how it works
there, please [read about it](http://guides.rubyonrails.org/migrations.html).

```java
import static br.com.ittree.migrate.Define.column;
import static br.com.ittree.migrate.Define.notnull;
import static br.com.ittree.migrate.Define.primarykey;
import static br.com.ittree.migrate.Define.table;
import static br.com.ittree.migrate.Define.DataTypes.INTEGER;
import static br.com.ittree.migrate.Define.DataTypes.VARCHAR;
import br.com.ittree.migrate.Execute;
import br.com.ittree.migrate.Migration;
import br.com.ittree.migrate.model.DbMigration;

public class M01_CreatePeople implements Migration {

  @Override
  public void up() {
    Execute.createTable(table("people",
        column("_id", INTEGER, notnull(), primarykey()),
        column("name", VARCHAR, notnull())
        ));
  }

  @Override
  public void down() {
    Execute.dropTable("people");
  }

  @Override
  public int getVersion() {
    return 1;
  }

  @Override
  public String getMigration() {
    return "201305222340"; // Sorry, you will have to generate this by hand.
  }
}
```

## Testing

There is a `test` folder with JUnit tests. Just import this app in your Eclipse
and run it as JUnit Test.

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
