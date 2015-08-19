package org.frutilla;

import java.util.LinkedList;

/**
 * Parses the Frutilla JUnit rules.
 */
class FrutillaParser {

    private static Given sGiven;

    static Given given(String text) {
        reset();
        sGiven = new Given(text);
        return sGiven;
    }

    static boolean has(String sentence) {
        return sGiven != null && sGiven.has(sentence);
    }

    static void reset() {
        if (sGiven != null) {
            sGiven.reset();
            sGiven = null;
        }
    }

    static boolean isEmpty() {
        return sGiven == null || sGiven.isEmpty();
    }

    static String popSentence() {
        if (sGiven != null) {
            return sGiven.popSentence();
        }
        return "";
    }

    //----------------------------------------------------------------------------------------------

    private static abstract class AbstractRules {

        private final LinkedList<String> mRules = new LinkedList<>();
        private AbstractRules mChild;
        private AbstractRules mParent;

        private AbstractRules(String rule) {
            addRule(rule);
        }

        void addRule(String rule) {
            mRules.add(rule);
        }

        boolean has(String sentence) {
            return mRules.contains(sentence) || (mChild != null && mChild.has(sentence));
        }

        void reset() {
            mRules.clear();
            if (mChild != null) {
                mChild.reset();
                mChild = null;
            }

        }

        /**
         * Adds another sentence to the current group, starting with AND.
         *
         * @param sentence the sentence in plain text
         * @return the current group of sentences
         */
        public And and(String sentence) {
            final And and = new And(sentence);
            setChild(and);
            return and;
        }

        boolean isEmpty() {
            return mRules.isEmpty() && (mChild == null || mChild.isEmpty());
        }

        <T extends AbstractRules> T setChild(T child) {
            mChild = child;
            mChild.setParent(this);
            return child;
        }

        private void setParent(AbstractRules parent) {
            mParent = parent;
        }

        String popSentence() {
            StringBuilder sentence = new StringBuilder()
                    .append(header())
                    .append(" ")
                    .append(sentences());
            if (mChild != null) {
                sentence.append(mChild.popSentence());
            }
            reset();
            return sentence.toString();
        }

        private String sentences() {
            StringBuilder text = new StringBuilder();
            int i = 0;
            for (String sentence : mRules) {
                if (i > 0) {
                    text.append(" ");
                }
                text.append(sentence);
                text.append("\n");
                i++;
            }
            mRules.clear();
            return text.toString();
        }

        protected abstract String header();

        /**
         * Adds another sentence to the current group, starting with BUT.
         *
         * @param sentence the sentence in plain text
         * @return the current group of sentences
         */
        public But but(String sentence) {
            final But but = new But(sentence);
            setChild(but);
            return but;
        }
    }

    //----------------------------------------------------------------------------------------------

    public static class But extends And {

        private But(String rule) {
            super(rule);
        }

        @Override
        protected String header() {
            return " BUT";
        }
    }

    public static class And extends AbstractRules {

        private And(String rule) {
            super(rule);
        }

        /**
         * Starts describing the action executed in the use case.
         *
         * @param sentence the sentence in plain text
         * @return the current group of sentences
         */
        public When when(String sentence) {
            When when = new When(sentence);
            setChild(when);
            return when;
        }

        /**
         * Starts describing in plain text the expected behavior after executing the use case.
         *
         * @param sentence the sentence in plain text
         * @return the current group of sentences
         */
        public Then then(String sentence) {
            Then then = new Then(sentence);
            setChild(then);
            return then;
        }

        @Override
        protected String header() {
            return " AND";
        }

    }


    /**
     * Group of sentences describing the entry point of the use case, using plain text.
     */
    public static class Given extends AbstractRules {

        private Given(String rule) {
            super(rule);
        }

        /**
         * Starts describing the action executed in the use case.
         *
         * @param sentence the sentence in plain text
         * @return the current group of sentences
         */
        public When when(String sentence) {
            When when = new When(sentence);
            setChild(when);
            return when;
        }

        @Override
        protected String header() {
            return "GIVEN";
        }

    }

    //----------------------------------------------------------------------------------------------

    /**
     * A group of sentences describing the action to execute in the use case, using plain text.
     */
    public static class When extends AbstractRules {

        private When(String rule) {
            super(rule);
        }

        /**
         * Starts describing in plain text the expected behavior after executing the use case.
         *
         * @param sentence the sentence in plain text
         * @return the current group of sentences
         */
        public Then then(String sentence) {
            return setChild(new Then(sentence));
        }

        @Override
        protected String header() {
            return "WHEN";
        }

    }

    //----------------------------------------------------------------------------------------------

    /**
     * Describes in plain text the expected behavior after executing the use case.
     */
    public static class Then extends AbstractRules {

        private Then(String rule) {
            super(rule);
        }

        @Override
        protected String header() {
            return "THEN";
        }

    }

}
