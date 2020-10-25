package com.caspco.unittest.runner;

import com.caspco.unittest.interfaces.CaspianTest;
import com.caspco.unittest.model.CaspianStatement;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public final class CaspianRunner extends BlockJUnit4ClassRunner implements CaspianTest {
    public CaspianRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected Object createTest() throws Exception {
        return setRequirements(super.createTest());
    }

    @Override
    public CaspianStatement caspianMethodBlock(FrameworkMethod frameworkMethod) {
        Object test;
        try {
            test = (new ReflectiveCallable() {
                protected Object runReflectiveCall() throws Throwable {
                    return CaspianRunner.this.createTest();
                }
            }).run();
        } catch (Throwable e) {
            return new CaspianStatement(new Fail(e));
        }

        Statement statement = this.methodInvoker(frameworkMethod, test);
        statement = this.possiblyExpectingExceptions(frameworkMethod, test, statement);

        //TODO from davood akbari: handle - withPotentialTimeout
        statement = this.withPotentialTimeout(frameworkMethod, test, statement);

        statement = this.withBefores(frameworkMethod, test, statement);
        statement = this.withAfters(frameworkMethod, test, statement);

        //TODO from davood akbari: Do not forget! - withTestRules
//        statement = this.withRules(method, test, statement);

        return new CaspianStatement(statement, test);
    }

    @Override
    protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) {
        Description description = this.describeChild(frameworkMethod);
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);

        if (this.isIgnored(frameworkMethod)) {
            notifier.fireTestIgnored(description);
        } else {
            CaspianStatement caspianStatement = this.caspianMethodBlock(frameworkMethod);

            Object test = caspianStatement.getTest();
            if (test != null) {
                mockStatic(frameworkMethod, test, eachNotifier);
            }

            this.runLeaf(caspianStatement.getStatement(), description, notifier);

            takeRequirements(frameworkMethod, caspianStatement.getTest(), eachNotifier);
        }
    }
}
