package br.com.ittree.migrate.generators;

import java.sql.SQLException;

public class GeneratorFactory {
	public static Generator getGenerator() throws SQLException {
		return new GenericGenerator();
	}
}
