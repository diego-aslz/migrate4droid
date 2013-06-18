package migrate4droid.log;

import android.util.Log;

public abstract class Logger {
	public static void e(String message) {
		e(message, null);
	}

	public static void e(String message, Exception exception) {
		try {
			Log.e("Schema Migration", message, exception);
		} catch(Exception e) {
		}
	}

	public static void w(String message) {
		w(message, null);
	}

	public static void w(String message, Exception exception) {
		try {
			Log.w("Schema Migration", message, exception);
		} catch(Exception e) {
		}
	}

	public static void i(String message) {
		i(message, null);
	}

	public static void i(String message, Exception exception) {
		try {
			Log.i("Schema Migration", message, exception);
		} catch(Exception e) {
		}
	}
}
