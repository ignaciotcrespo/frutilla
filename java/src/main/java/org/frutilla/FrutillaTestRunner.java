package org.frutilla;

import org.frutilla.annotations.Frutilla;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import static org.frutilla.FrutillaParser.given;
import static org.frutilla.FrutillaParser.scenario;

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
            String text = preEvaluate();
            try {
                mStatement.evaluate();
            } catch (Throwable exc) {
                if (!text.isEmpty()) {
                    ExceptionUtils.insertMessage(exc, text);
                }
                throw exc;
            }
        }

        private String preEvaluate() {
            StringBuffer sb = new StringBuffer();
            sb.append("" + "\n");
            sb.append("/------------------------------------------\\" + "\n");
            sb.append("Test: " + mMethod.getName() + "\n");
            sb.append("--------------------------------------------" + "\n");
            String text = "";
            try {
                text = scenario(mMethod.getAnnotation(Frutilla.class));
            } catch (Throwable exc) {
                // do nothing
            }
            if (!text.isEmpty()) {
                sb.append(text + "\n");
            }
            sb.append("\\------------------------------------------/" + "\n");
            sb.append("" + "\n");
            System.out.println(sb.toString());
            return text;
        }

    }


}
