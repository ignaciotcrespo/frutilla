package org.frutilla;

import java.lang.reflect.Field;

/**
 * Utilities for java exceptions.
 */
class ExceptionUtils {

    /**
     * Addes a message at the beginning of the stacktrace.
     */
    public static void insertMessage(Throwable onObject, String msg) {
        try {
            Field field = Throwable.class.getDeclaredField("detailMessage"); //Method("initCause", new Class[]{Throwable.class});
            field.setAccessible(true);
            if (onObject.getMessage() != null) {
                field.set(onObject, "\n[\n" + msg + "\n]\n[\nMessage: " + onObject.getMessage() + "\n]");
            } else {
                field.set(onObject, "\n[\n" + msg + "]\n");
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
        }
    }

}
