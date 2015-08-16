package org.frutilla;

import org.frutilla.annotations.Frutilla;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * Process Frutilla annotations in top of tests.
 */
public class FrutillaTestRunner extends BlockJUnit4ClassRunner {

    /**
     * Constructor inherited from {@link BlockJUnit4ClassRunner}
     */
    public FrutillaTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        return new FrutillaStatement(super.methodBlock(method), method);
    }

    class FrutillaStatement extends Statement {

        private final Statement mStatement;
        private final FrameworkMethod mMethod;

        public FrutillaStatement(Statement statement, FrameworkMethod method) {
            mStatement = statement;
            mMethod = method;
        }

        @Override
        public void evaluate() throws Throwable {
            String text = "";
            try {
                text = FrutillaAnnotationHelper.getText(mMethod.getAnnotation(Frutilla.class));
            } catch (Throwable exc) {
                // do nothing
            }
            try {
                mStatement.evaluate();
            } catch (Throwable exc) {
                if (!text.isEmpty()) {
                    ExceptionUtils.insertMessage(exc, text);
                }
                throw exc;
            } finally {
                if (!text.isEmpty()) {
                    System.out.println(text);
                }
            }
        }
    }
}
