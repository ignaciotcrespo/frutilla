package org.frutilla.fresa;

import java.util.LinkedList;

/**
 * Created by crespo on 14/08/15.
 */
public class FresaParser {

    private static Given sGiven;
    private static PseudocodeListener sPseudocodeListener;

    public static Given given(String text) {
        reset();
        sGiven = new Given(text);
        return sGiven;
    }

    static boolean has(String sentence) {
        if (sGiven != null) {
            return sGiven.has(sentence);
        }
        return false;
    }

    public static void reset() {
        if (sGiven != null) {
            sGiven.reset();
            sGiven = null;
        }
    }

    public static boolean isEmpty() {
        final boolean empty = sGiven == null || sGiven.isEmpty();
        return empty;
    }

    public static String popSentence() {
        if (sGiven != null) {
            return sGiven.popSentence();
        }
        return "";
    }

    public static void setListener(PseudocodeListener testXTestRunner) {
        sPseudocodeListener = testXTestRunner;
    }

    public static boolean isEndPending() {
        return sGiven != null && sGiven.isEndPending();
    }

    //----------------------------------------------------------------------------------------------

    private static abstract class AbstractRules {

        private final LinkedList<String> mRules = new LinkedList();
        private final LinkedList<String> mRulesEnd = new LinkedList();
        private AbstractRules mChild;
        private AbstractRules mParent;

        private AbstractRules(String rule) {
            addRule(rule);
        }

        protected void addRule(String rule) {
            mRules.add(rule);
        }

        public boolean has(String sentence) {
            return mRulesEnd.contains(sentence) || (mChild != null && mChild.has(sentence));
        }

        public void reset() {
            mRules.clear();
            mRulesEnd.clear();
            if (mChild != null) {
                mChild.reset();
                mChild = null;
            }

        }

        public AbstractRules and(String rule) {
            addRule(rule);
            return this;
        }

        protected boolean isEmpty() {
            return mRulesEnd.isEmpty() && (mChild == null || mChild.isEmpty());
        }

        protected <T extends AbstractRules> T setChild(T child) {
            mChild = child;
            mChild.setParent(this);
            return child;
        }

        private void setParent(AbstractRules parent) {
            mParent = parent;
        }

        public String popSentence() {
            StringBuilder sentence = new StringBuilder()
                    .append(header())
                    .append(" ")
                    .append(sentences())
                    .append("\n") ;
            if (mChild != null) {
                sentence.append(mChild.popSentence());
            }
            reset();
            return sentence.toString();
        }

        private String sentences() {
            StringBuilder text = new StringBuilder();
            int i = 0;
            for (String sentence : mRulesEnd) {
                if (i > 0) {
                    text.append("\n AND ");
                }
                text.append(sentence);
                text.append(" ");
                i++;
            }
            mRules.clear();
            return text.toString();
        }

        protected abstract String header();

        public void end() {
            mRulesEnd.addAll(mRules);
            mRules.clear();
            if (mParent != null) {
                mParent.end();
            } else {
                if (sPseudocodeListener != null) {
                    sPseudocodeListener.sendStatusStartDelayed();
                }
            }
        }

        public boolean isEndPending() {
            boolean isPending = mRulesEnd.isEmpty() && !mRules.isEmpty();
            if (!isPending) {
                isPending = mChild != null && mChild.isEndPending();
            }
            return isPending;
        }
    }

    public interface PseudocodeListener {
        void sendStatusStartDelayed();
    }

    //----------------------------------------------------------------------------------------------

    public static class Given extends AbstractRules {

        private Given(String rule) {
            super(rule);
        }

        public When when(String text) {
            When when = new When(text);
            setChild(when);
            return when;
        }

        @Override
        public Given and(String rule) {
            return (Given) super.and(rule);
        }

        @Override
        protected String header() {
            return "GIVEN";
        }

    }

    //----------------------------------------------------------------------------------------------

    public static class When extends AbstractRules {

        private When(String rule) {
            super(rule);
        }

        public Then then(String s) {
            return setChild(new Then(s));
        }

        @Override
        public When and(String rule) {
            return (When) super.and(rule);
        }

        @Override
        protected String header() {
            return "WHEN";
        }

    }

    //----------------------------------------------------------------------------------------------

    public static class Then extends AbstractRules {

        private Then(String rule) {
            super(rule);
        }

        @Override
        public Then and(String rule) {
            return (Then) super.and(rule);
        }

        @Override
        protected String header() {
            return "THEN";
        }

    }

}
