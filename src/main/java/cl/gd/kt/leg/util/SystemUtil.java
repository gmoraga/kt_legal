package cl.gd.kt.leg.util;

public class SystemUtil {
    private SystemUtil() {
        throw new IllegalAccessError(SystemUtil.class.toString());
    }

    public static String getEnvironmentStrValue(final String variableName) {
        return System.getenv(variableName);
    }

    public static Integer getEnvironmentIntValue(final String variableName) {
        return Integer.valueOf(System.getenv(variableName));
    }
}

