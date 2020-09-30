package com.caspco.unittest.runner;

import com.caspco.unittest.interfaces.CaspianTest;
import com.caspco.unittest.model.CaspianStatement;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public final class CaspianSpringRunner extends SpringJUnit4ClassRunner implements CaspianTest {

    public CaspianSpringRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected Object createTest() throws Exception {
        return setRequirements(super.createTest());
    }

    @Override
    public CaspianStatement caspianMethodBlock(FrameworkMethod frameworkMethod) {
        Object testInstance;
        try {
            testInstance = (new ReflectiveCallable() {
                protected Object runReflectiveCall() throws Throwable {
                    return CaspianSpringRunner.this.createTest();
                }
            }).run();
        } catch (Throwable e) {
            return new CaspianStatement(new Fail(e));
        }

        Statement statement = this.methodInvoker(frameworkMethod, testInstance);
        statement = this.possiblyExpectingExceptions(frameworkMethod, testInstance, statement);
        statement = this.withBefores(frameworkMethod, testInstance, statement);
        statement = this.withAfters(frameworkMethod, testInstance, statement);

        //TODO from davood akbari: Do not forget! - withRulesReflectively
        //statement = this.withRulesReflectively(frameworkMethod, testInstance, statement);

        statement = this.withPotentialRepeat(frameworkMethod, testInstance, statement);
        statement = this.withPotentialTimeout(frameworkMethod, testInstance, statement);


        return new CaspianStatement(statement, testInstance);
    }

    private EachTestNotifier caspianMakeNotifier(FrameworkMethod method, RunNotifier notifier) {
        Description description = this.describeChild(method);
        return new EachTestNotifier(notifier, description);
    }

    @Override
    protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) {
        EachTestNotifier eachNotifier = this.caspianMakeNotifier(frameworkMethod, notifier);
        if (this.isTestMethodIgnored(frameworkMethod)) {
            eachNotifier.fireTestIgnored();
        } else {
            eachNotifier.fireTestStarted();

            try {
                CaspianStatement caspianStatement = this.caspianMethodBlock(frameworkMethod);

                mockStatic(frameworkMethod, caspianStatement.getTest(), eachNotifier);

                caspianStatement.getStatement().evaluate();

                takeRequirements(frameworkMethod, caspianStatement.getTest(), eachNotifier);

            } catch (AssumptionViolatedException var9) {
                eachNotifier.addFailedAssumption(var9);
            } catch (Throwable var10) {
                eachNotifier.addFailure(var10);
            } finally {
                eachNotifier.fireTestFinished();
            }
        }
    }
}
