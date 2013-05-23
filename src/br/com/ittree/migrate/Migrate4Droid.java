package br.com.ittree.migrate;

import br.com.ittree.migrate.misc.M00_Setup;

public abstract class Migrate4Droid {
	private static Configuration configuration;

	public static void migrate(Configuration configuration) {
		Migrate4Droid.configuration = configuration;
		
	}

	public static void setup(Configuration configuration) {
		Migrate4Droid.configuration = configuration;
		new M00_Setup().up();
	}

	public static Configuration getConfiguration() {
		return configuration;
	}
}
