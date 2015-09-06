package org.frutilla;

import org.frutilla.annotations.Frutilla;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Parses the Frutilla JUnit sentences.
 */
class FrutillaParser {

    private static final String AND = " AND";
    private static final String BUT = " BUT";
    private static AbstractSentence sRoot;

    /**
     * Describes the scenario of the use case.
     *
     * @param text the sentence describing the scenario
     * @return a Scenario object to conitnue adding sentences
     */
    static Scenario scenario(String text) {
        reset();
        final Scenario scenario = new Scenario(text);
        sRoot = scenario;
        return scenario;
    }

    /**
     * Describes the entry point of the use case.
     *
     * @param text the sentence describing the entry point
     * @return a Given object to continue adding sentences
     */
    static Given given(String text) {
        reset();
        final Given given = new Given(text);
        sRoot = given;
        return given;
    }

    static boolean has(String sentence) {
        return sRoot != null && sRoot.has(sentence);
    }

    static void reset() {
        if (sRoot != null) {
            sRoot.reset();
            sRoot = null;
        }
    }

    static boolean isEmpty() {
        return sRoot == null || sRoot.isEmpty();
    }

    static String popSentence() {
        if (sRoot != null) {
            return sRoot.popSentence();
        }
        return "";
    }

    static String scenario(Frutilla annotation) {
        String value = "";
        if (annotation != null) {
            final Scenario scenario = new Scenario(annotation.Scenario());
            scenario.given(annotation.Given())
                    .when(annotation.When())
                    .then(annotation.Then());
            value = scenario.popSentence();
        }
        return value;
    }

    //----------------------------------------------------------------------------------------------

    static abstract class AbstractSentence {

        private String mSentence;
        private final List<AbstractSentence> mChildren = new LinkedList<>();
        private final String mHeader;

        private AbstractSentence(String sentence, String header) {
            if (sentence != null && sentence.trim().toLowerCase(Locale.ENGLISH).startsWith(header.trim().toLowerCase(Locale.ENGLISH) + " ")) {
                mSentence = sentence.trim().substring((header.trim().toLowerCase(Locale.ENGLISH) + " ").length());
            } else {
                mSentence = sentence;
            }
            mHeader = header;
        }

        AbstractSentence(String[] sentences, String header) {
            this(sentences == null || sentences.length == 0 ? "" : sentences[0], header);
            if (sentences != null) {
                AbstractSentence child = this;
                for (int i = 1; i < sentences.length; i++) {
                    final String sentence = sentences[i];
                    if (sentence != null && sentence.trim().toLowerCase(Locale.ENGLISH).startsWith("and ")) {
                        child = child.and(sentence);
                    } else if (sentence != null && sentence.trim().toLowerCase(Locale.ENGLISH).startsWith("but ")) {
                        child = child.but(sentence);
                    } else {
                        child = child.and(sentence);
                    }
                }
            }
        }

        boolean has(String sentence) {
            boolean has = mSentence.equals(sentence);
            if (!has) {
                for (AbstractSentence child : mChildren) {
                    has = child.has(sentence);
                    if (has) {
                        break;
                    }
                }
            }
            return has;
        }

        void reset() {
            mSentence = null;
            for (AbstractSentence child : mChildren) {
                child.reset();
            }
            mChildren.clear();
        }

        /**
         * Adds another sentence to the current group, starting with AND.
         *
         * @param sentence the sentence in plain text
         * @return the current group of sentences
         */
        public abstract AbstractSentence and(String sentence);

        boolean isEmpty() {
            return mSentence == null && mChildren.isEmpty();
        }

        AbstractSentence addChild(AbstractSentence child) {
            mChildren.add(child);
            return child;
        }

        String popSentence() {
            StringBuilder sentence = new StringBuilder();
            if (mSentence != null && mSentence.trim().length() > 0) {
                sentence.append(header());
                sentence.append(" ");
                sentence.append(mSentence);
            }
            for (AbstractSentence child : mChildren) {
                final String text = child.popSentence();
                if (!text.trim().isEmpty()) {
                    if (sentence.length() > 0) {
                        sentence.append("\n");
                    }
                    sentence.append(text);
                }
            }
            reset();
            return sentence.toString();
        }

        String header() {
            return mHeader;
        }

        /**
         * Adds another sentence to the current group, starting with BUT.
         *
         * @param sentence the sentence in plain text
         * @return the current group of sentences
         */
        public abstract AbstractSentence but(String sentence);

    }

    //----------------------------------------------------------------------------------------------

    /**
     * Group of sentences describing the scenario of the use case, using plain text.
     */
    public static class Scenario extends AbstractSentence {

        private Scenario(String sentence) {
            super(sentence, "SCENARIO");
        }

        private Scenario(String[] sentences) {
            super(sentences, "SCENARIO");
        }

        private Scenario(String sentence, String header) {
            super(sentence, header);
        }

        /**
         * Starts describing the action executed in the use case.
         *
         * @param sentence the sentence in plain text
         * @return the current group of sentences
         */
        public Given given(String sentence) {
            Given given = new Given(sentence);
            addChild(given);
            return given;
        }

        Given given(String[] sentences) {
            Given given = new Given(sentences);
            addChild(given);
            return given;
        }


        @Override
        public Scenario and(String sentence) {
            return (Scenario) addChild(new Scenario(sentence, AND));
        }

        @Override
        public Scenario but(String sentence) {
            return (Scenario) addChild(new Scenario(sentence, BUT));
        }

    }

    /**
     * Group of sentences describing the entry point of the use case, using plain text.
     */
    public static class Given extends AbstractSentence {

        private Given(String sentence) {
            super(sentence, "GIVEN");
        }

        private Given(String[] sentences) {
            super(sentences, "GIVEN");
        }

        private Given(String sentence, String header) {
            super(sentence, header);
        }

        /**
         * Starts describing the action executed in the use case.
         *
         * @param sentence the sentence in plain text
         * @return the current group of sentences
         */
        public When when(String sentence) {
            When when = new When(sentence);
            addChild(when);
            return when;
        }

        When when(String[] sentences) {
            When when = new When(sentences);
            addChild(when);
            return when;
        }


        @Override
        public Given and(String sentence) {
            return (Given) addChild(new Given(sentence, AND));
        }

        @Override
        public Given but(String sentence) {
            return (Given) addChild(new Given(sentence, BUT));
        }

    }

    //----------------------------------------------------------------------------------------------

    /**
     * A group of sentences describing the action to execute in the use case, using plain text.
     */
    public static class When extends AbstractSentence {

        private When(String sentence) {
            super(sentence, "WHEN");
        }

        private When(String[] sentences) {
            super(sentences, "WHEN");
        }

        private When(String sentence, String header) {
            super(sentence, header);
        }

        /**
         * Starts describing in plain text the expected behavior after executing the use case.
         *
         * @param sentence the sentence in plain text
         * @return the current group of sentences
         */
        public Then then(String sentence) {
            return (Then) addChild(new Then(sentence));
        }

        Then then(String[] sentences) {
            return (Then) addChild(new Then(sentences));
        }

        @Override
        public When and(String sentence) {
            return (When) addChild(new When(sentence, AND));
        }

        @Override
        public When but(String sentence) {
            return (When) addChild(new When(sentence, BUT));
        }


    }

    //----------------------------------------------------------------------------------------------

    /**
     * Describes in plain text the expected behavior after executing the use case.
     */
    public static class Then extends AbstractSentence {

        private Then(String sentence) {
            super(sentence, "THEN");
        }

        private Then(String[] sentences) {
            super(sentences, "THEN");
        }

        private Then(String sentence, String header) {
            super(sentence, header);
        }

        @Override
        public Then and(String sentence) {
            return (Then) addChild(new Then(sentence, AND));
        }

        @Override
        public Then but(String sentence) {
            return (Then) addChild(new Then(sentence, BUT));
        }

    }

}
