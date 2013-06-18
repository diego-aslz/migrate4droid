package migrate4droid.misc;

import migrate4droid.log.Logger;

public class Validator {

	public static void notNull(Object object, String message) {
		if (object == null) {
			throwException(message);
		}
	}

	public static void isTrue(boolean trueExpression, String message) {
		if (!trueExpression) {
			throwException(message);
		}
	}

	private static void throwException(String message) {
		SchemaMigrationException exception = new SchemaMigrationException(message);
		Logger.e("Required object was null", exception);
		throw exception;
	}
	
}
