package dev.jackass.kwikeproxy.util;

public class ExceptionUtil {

    public static String printException(Exception e) {
        StringBuilder builder = new StringBuilder(e.toString());

        for (StackTraceElement element : e.getStackTrace()) {
            builder.append("\n    at " + element.toString());
        }

        return builder.toString();
    }

}
