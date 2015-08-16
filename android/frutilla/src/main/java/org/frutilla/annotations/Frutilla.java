package org.frutilla.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a test using sentences in plain text, similar to Cucumber. <br/>
 * This description is included in many places:
 * <ul>
 *     <li>the stacktrace of the failed tests</li>
 *     <li>the javadocs</li>
 *     <li>the logs in the console</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Frutilla {

    String Scenario() default "";

    /**
     * Describes the entry point of the use case.
     * @return a sentence or group of sentences
     */
    String[] Given();

    /**
     * Describes what the use case does, the main action of the use case.
     * @return a sentence or group of sentences
     */
    String[] When();

    /**
     * Describes what is the expected behavior after doing the main action.
     * @return a sentence or group of sentences
     */
    String[] Then();

}
