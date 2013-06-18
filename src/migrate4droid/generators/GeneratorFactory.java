package migrate4droid.generators;

import java.sql.SQLException;

public class GeneratorFactory {
	public static Generator getGenerator() throws SQLException {
		return new GenericGenerator();
	}
}
