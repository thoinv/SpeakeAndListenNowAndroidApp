package vn.eway.englishpronunciationpractice.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class LogUtils {

	public static void loge(Exception exception) {
			logErrorHandler(exception.getMessage());
			exception.printStackTrace();
	}

	public static void showToast(final Context context, final String message) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
				log(message);
			}
		});
	}

	public static void log(String message) {
		String fullClassName = Thread.currentThread().getStackTrace()[3]
				.getClassName();
		String className = fullClassName.substring(fullClassName
				.lastIndexOf(".") + 1);
		if (className.contains("$")) {
			className = className.substring(0, className.lastIndexOf("$"));
		}
		String methodName = Thread.currentThread().getStackTrace()[3]
				.getMethodName();
		int lineNumber = Thread.currentThread().getStackTrace()[3]
				.getLineNumber();

		Log.d("Speak Instance", "at (" + className + ".java:"
				+ lineNumber + ") " + "[" + methodName + "]" + message);
	}

	private static void logErrorHandler(String message) {
		String fullClassName = Thread.currentThread().getStackTrace()[3]
				.getClassName();
		String className = fullClassName.substring(fullClassName
				.lastIndexOf(".") + 1);
		if (className.contains("$")) {
			className = className.substring(0, className.lastIndexOf("$"));
		}
		String methodName = Thread.currentThread().getStackTrace()[3]
				.getMethodName();
		int lineNumber = Thread.currentThread().getStackTrace()[3]
				.getLineNumber();

		Log.e("Speak Instance", "at (" + className + ".java:" + lineNumber
				+ ") " + "[" + methodName + "]" + message);
	}
}
