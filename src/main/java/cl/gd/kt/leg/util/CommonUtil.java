package cl.gd.kt.leg.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {


    private CommonUtil () {
        throw new IllegalAccessError(CommonUtil.class.toString());
    }


    public static Boolean stringToBoolean (String value) {

        return Constant.YES.equalsIgnoreCase(value) ? Boolean.TRUE : Boolean.FALSE;
    }

    public static String booleanToString (Boolean value) {
        return (value != null && value) ? Constant.YES : Constant.NO;
    }
}
