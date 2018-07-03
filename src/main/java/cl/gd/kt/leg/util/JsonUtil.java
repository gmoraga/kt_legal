package cl.gd.kt.leg.util;

import io.vertx.core.json.JsonArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

@Slf4j
public class JsonUtil {


    private JsonUtil() {
        throw new IllegalAccessError(JsonUtil.class.toString());
    }


    public static void addParam(JsonArray params, String value) {
        if (StringUtils.isNotEmpty(value)) {
            params.add(value);
        } else {
            params.addNull();
        }

    }

    public static void addParam(JsonArray params, Instant value) {
        if (value != null) {
            params.add(value);
        } else {
            params.addNull();
        }

    }

    public static void addParam(JsonArray params, Double value) {
        if (value != null) {
            params.add(value);
        } else {
            params.addNull();
        }
    }

    public static void addParam(JsonArray params, Long value) {
        if (value != null) {
            params.add(value);
        } else {
            params.addNull();
        }
    }

    public static void addParam(JsonArray params, Integer value) {
        if (value != null) {
            params.add(value);
        } else {
            params.addNull();
        }
    }

}
