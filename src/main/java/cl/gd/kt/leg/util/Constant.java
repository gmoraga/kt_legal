package cl.gd.kt.leg.util;

public class Constant {

    public static final String DB_SCHEMA = SystemUtil.getEnvironmentStrValue(DataBaseEnum.DB_SCHEMA.name());

    public static final String YES = "Y";
    public static final String NO = "N";

    private Constant() {
        throw new IllegalAccessError(Constant.class.toString());
    }
}
