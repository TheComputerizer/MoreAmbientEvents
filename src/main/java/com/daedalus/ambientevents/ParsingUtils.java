package com.daedalus.ambientevents;

import com.google.gson.*;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"DataFlowIssue", "unused"})
public class ParsingUtils {

    public static void tryCloseable(Closeable closeable, Consumer<Closeable> tryThis,
                                    @Nullable Consumer<Exception> onCatch) {
        try {
            tryThis.accept(closeable);
        } catch(Exception ex) {
            if(Objects.nonNull(onCatch)) onCatch.accept(ex);
        } finally {
            try {
                closeable.close();
            } catch(IOException ex) {
                AmbientEventsRef.LOGGER.error("Failed to close resource or stream! Things may leak!",ex);
            }
        }
    }

    public static <E> @Nullable E returnCloseable(Closeable closeable, Function<Closeable,E> returnThis,
                                                  @Nullable Consumer<Exception> onCatch) {
        try {
            return returnThis.apply(closeable);
        } catch(Exception ex) {
            if(Objects.nonNull(onCatch)) onCatch.accept(ex);
        } finally {
            try {
                closeable.close();
            } catch(IOException ex) {
                AmbientEventsRef.LOGGER.error("Failed to close resource or stream! Things may leak!",ex);
            }
        }
        return null;
    }

    public static <J> @Nullable J parseElement(JsonElement json, Function<JsonElement,J> parser) {
        try {
            return parser.apply(json);
        } catch(JsonIOException ex) {
            AmbientEventsRef.LOGGER.error("Failed to parse json element! {}",json,ex);
            return null;
        }
    }

    private static boolean validateKey(@Nullable JsonObject json, String key, boolean shouldThrow) {
        if(Objects.isNull(json) || !json.has(key)) {
            String msg = "Unable to find key `"+key+"` in json object "+json;
            if(shouldThrow) throw new JsonIOException(msg);
            AmbientEventsRef.LOGGER.error(msg);
            return false;
        }
        return true;
    }

    public static @Nullable JsonElement getNextElement(JsonElement json, String key) {
        return getNextElement(json,key,false);
    }

    public static @Nullable JsonElement getNextElement(JsonElement json, String key, boolean shouldThrow) {
        JsonObject asObj = getAsObject(json,key,shouldThrow);
        return validateKey(asObj,key,shouldThrow) ? asObj.get(key) : null;
    }

    public static @Nullable JsonObject getAsObject(JsonElement json) throws JsonIOException {
        return getAsObject(json,false);
    }

    public static @Nullable JsonObject getAsObject(JsonElement json, boolean shouldThrow) throws JsonIOException {
        try {
            return json.getAsJsonObject();
        } catch(IllegalStateException ex) {
            String msg = "Failed to get json element as object! "+json;
            if(shouldThrow) throw new JsonIOException(msg,ex);
            AmbientEventsRef.LOGGER.error(msg,ex);
            return null;
        }
    }

    public static @Nullable JsonObject getAsObject(JsonElement json, String key) throws JsonIOException {
        return getAsObject(json,key,false);
    }

    public static @Nullable JsonObject getAsObject(JsonElement json, String key, boolean shouldThrow) throws JsonIOException {
        JsonObject asObj = getAsObject(json,shouldThrow);
        return validateKey(asObj,key,shouldThrow) ? getAsObject(asObj.get(key),shouldThrow) : null;
    }

    public static @Nullable JsonArray getAsArray(JsonElement json) throws JsonIOException {
        return getAsArray(json,false);
    }

    public static @Nullable JsonArray getAsArray(JsonElement json, boolean shouldThrow) throws JsonIOException {
        try {
            return json.getAsJsonArray();
        } catch(IllegalStateException ex) {
            String msg = "Failed to get json element as array! "+json;
            if(shouldThrow) throw new JsonIOException(msg,ex);
            AmbientEventsRef.LOGGER.error(msg,ex);
            return null;
        }
    }

    public static @Nullable JsonArray getAsArray(JsonElement json, String key) throws JsonIOException {
        return getAsArray(json,key,false);
    }

    public static @Nullable JsonArray getAsArray(JsonElement json, String key, boolean shouldThrow) throws JsonIOException {
        JsonObject asObj = getAsObject(json,shouldThrow);
        return validateKey(asObj,key,shouldThrow) ? getAsArray(asObj.get(key),shouldThrow) : null;
    }

    public static @Nullable JsonPrimitive getAsPrimitive(JsonElement json) throws JsonIOException {
        return getAsPrimitive(json,false);
    }

    public static @Nullable JsonPrimitive getAsPrimitive(JsonElement json, boolean shouldThrow) throws JsonIOException {
        try {
            return json.getAsJsonPrimitive();
        } catch(IllegalStateException ex) {
            String msg = "Failed to get json element as primitive! "+json;
            if(shouldThrow) throw new JsonIOException(msg,ex);
            AmbientEventsRef.LOGGER.error(msg,ex);
            return null;
        }
    }

    public static @Nullable JsonPrimitive getAsPrimitive(JsonElement json, String key) throws JsonIOException {
        return getAsPrimitive(json,key,false);
    }

    public static @Nullable JsonPrimitive getAsPrimitive(JsonElement json, String key, boolean shouldThrow) throws JsonIOException {
        JsonObject asObj = getAsObject(json,shouldThrow);
        return validateKey(asObj,key,shouldThrow) ? getAsPrimitive(asObj.get(key),shouldThrow) : null;
    }

    public static @Nullable String getAsString(JsonElement json) throws JsonIOException {
        return getAsString(json,false);
    }

    public static @Nullable String getAsString(JsonElement json, boolean shouldThrow) throws JsonIOException {
        String msg = "Failed to get json element as string! "+json;
        String value = null;
        try {
            JsonPrimitive primitive = getAsPrimitive(json);
            if(Objects.isNull(primitive)) {
                if(shouldThrow) throw new JsonIOException(msg);
                AmbientEventsRef.LOGGER.error(msg);
            } else value = primitive.getAsString();
        } catch(IllegalStateException ex) {
            if(shouldThrow) throw new JsonIOException(msg,ex);
            AmbientEventsRef.LOGGER.error(msg,ex);
        }
        return validateString(value) ? value : null;
    }

    public static @Nullable String getAsString(JsonElement json, String key) throws JsonIOException {
        return getAsString(json,key,false);
    }

    public static @Nullable String getAsString(JsonElement json, String key, boolean shouldThrow) throws JsonIOException {
        JsonObject asObj = getAsObject(json,shouldThrow);
        return validateKey(asObj,key,shouldThrow) ? getAsString(asObj.get(key),shouldThrow) : null;
    }

    public static @Nullable Number getAsNumber(JsonElement json) throws JsonIOException {
        return getAsNumber(json,false);
    }

    public static @Nullable Number getAsNumber(JsonElement json, boolean shouldThrow) throws JsonIOException {
        String msg = "Failed to get json element as number! "+json;
        Number value = null;
        try {
            JsonPrimitive primitive = getAsPrimitive(json);
            if(Objects.isNull(primitive)) {
                if(shouldThrow) throw new JsonIOException(msg);
                AmbientEventsRef.LOGGER.error(msg);
            } else value = primitive.getAsNumber();
        } catch(IllegalStateException ex) {
            if(shouldThrow) throw new JsonIOException(msg,ex);
            AmbientEventsRef.LOGGER.error(msg,ex);
        }
        return value;
    }

    public static @Nullable Number getAsNumber(JsonElement json, String key) throws JsonIOException {
        return getAsNumber(json,key,false);
    }

    public static @Nullable Number getAsNumber(JsonElement json, String key, boolean shouldThrow) throws JsonIOException {
        JsonObject asObj = getAsObject(json,shouldThrow);
        return validateKey(asObj,key,shouldThrow) ? getAsNumber(asObj.get(key),shouldThrow) : null;
    }

    public static boolean validateString(@Nullable String string) {
        if(Objects.isNull(string) || string.isEmpty()) {
            AmbientEventsRef.LOGGER.error("Null or empty strings are not supported!");
            return false;
        }
        return true;
    }

    public static boolean validateNumber(Number value, Number min, Number max) {
        return validateNumber(value,min,max,true,true);
    }

    public static boolean validateNumber(Number value, Number min, Number max, boolean equalMin, boolean equalMax) {
        String bounds = (equalMin ? "[" : "(")+min+","+max+(equalMax ? "]" : ")");
        try {
            if(!validateMin(compareGeneric(value,min),equalMin) || !validateMax(compareGeneric(value,max),equalMax)) {
                AmbientEventsRef.LOGGER.error("Number `{}` was not in its bounds of {}!",value,bounds);
                return false;
            }
        } catch (NumberFormatException ex) {
            AmbientEventsRef.LOGGER.error("Unable to validate number `{}` with bounds {}!",value,bounds,ex);
            return false;
        }
        return true;
    }

    public static int compareGeneric(Number value, Number other) {
        if(isSpecial(value) || isSpecial(other)) return Double.compare(value.doubleValue(),other.doubleValue());
        else return toBigDecimal(value).compareTo(toBigDecimal(other));
    }

    private static boolean validateMin(int comparison, boolean equalMin) {
        return equalMin ? comparison>=0 : comparison>0;
    }

    private static boolean validateMax(int comparison, boolean equalMax) {
        return equalMax ? comparison<=0 : comparison<0;
    }

    private static boolean isSpecial(Number num) {
        if(num instanceof Double) {
            double d = num.doubleValue();
            return Double.isNaN(d) || Double.isInfinite(d);
        }
        if(num instanceof Float) {
            float f = num.floatValue();
            return Float.isNaN(f) || Float.isInfinite(f);
        }
        return false;
    }

    private static BigDecimal toBigDecimal(Number num) throws NumberFormatException {
        if(num instanceof BigDecimal || num instanceof BigInteger) return (BigDecimal)num;
        if(num instanceof Byte || num instanceof Short || num instanceof Integer || num instanceof Long)
            return new BigDecimal(num.longValue());
        if(num instanceof Float || num instanceof Double) return BigDecimal.valueOf(num.doubleValue());
        return new BigDecimal(num.toString());
    }
}
