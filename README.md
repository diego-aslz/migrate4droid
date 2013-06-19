# Migrate4Droid

<strong>This API is under construction.</strong>

This API was built to be used in Android applications in order to manage
database migrations similarly to Rails.

It's based on the [migrate4j project](http://migrate4j.sourceforge.net/). By the
way, thank you so much, Migrate4J team. Your project is awesome.

## Installation

Add the migrate4droid.jar file to your <code>libs</code> directory.

## Creating your migration classes

The migration classes work like they do in Rails. If you don't know how it works
there, please [read about it](http://guides.rubyonrails.org/migrations.html).

```java
import static migrate4droid.Define.column;
import static migrate4droid.Define.notnull;
import static migrate4droid.Define.primarykey;
import static migrate4droid.Define.table;
import static migrate4droid.Define.DataTypes.INTEGER;
import static migrate4droid.Define.DataTypes.VARCHAR;
import migrate4droid.Execute;
import migrate4droid.Migration;
import migrate4droid.model.DbMigration;

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
  public String getMigration() {
    return "201305222340"; // Sorry, you will have to generate this by hand.
  }
}
```

Create these migrations in a specific folder. For any change in your database,
create a migration for it.

## Managing migrations

After creating your migrations, it's a good idea to manage them in one specific
place, like a single class. Since you will have instantiate a
<code>Configuration</code> object to add your migrations, it's a good
approach to create your own configuration class that extends from it.

```java
import migrate4droid.Configuration;
import migrate4droid.migrations.M08_RememberMyPasswordSeller;
import myapp.migrations.M01_CreatePeople;

public class MyConfiguration extends Configuration {
  private static MyConfiguration instance;

  {
    // initialVersion = 0; // the current version of your database.
    addMigration(new M01_CreatePeople());
    // Every time you have a new migration, add it here.
  }

  static {
    instance = new MyConfiguration();
  }

  private MyConfiguration() { }

  public static MyConfiguration getInstance() {
    return instance;
  }
}
```

<strong>IMPORTANT:</strong> The order you add your migrations is the order they
will get run.

## Configuring your database helper

You will need to configure your DB Helper to use the Migrate4Droid methods. To
do so, do this in your DBHelper:

```java
public class DBHelper extends SqliteOpenHelper {
  // ...

  /**
   * This is called when the database is first created. Usually you should call createTable
   * statements here to create the tables that will store your data.
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
    Configuration configuration = MyConfiguration.getInstance();
    configuration.setDatabase(db);
    Migrate4Droid.migrate(configuration);
  }
```

## Testing

There is an Android Test Project under the `test` folder.
Just import this project in your Eclipse and run it as Android JUnit Test.
