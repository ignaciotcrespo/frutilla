package org.frutilla.utils;

import java.lang.reflect.Field;

/**
 * Created by crespo on 15/08/15.
 */
public class ExceptionUtils {

    public static void insertMessage(Throwable onObject, String msg) {
        try {
            Field field = Throwable.class.getDeclaredField("detailMessage"); //Method("initCause", new Class[]{Throwable.class});
            field.setAccessible(true);
            if (onObject.getMessage() != null) {
                field.set(onObject, "\n[\n" + msg + "\n]\n[\nMessage: " + onObject.getMessage() + "\n]");
            } else {
                field.set(onObject, "\n[\n" + msg + "\n]\n");
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
        }
    }

}
