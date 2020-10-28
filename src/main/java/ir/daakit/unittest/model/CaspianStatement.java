package ir.daakit.unittest.model;

import org.junit.runners.model.Statement;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */
public class CaspianStatement {
    private Statement statement;
    private Object test;

    public CaspianStatement(Statement statement) {
        this.statement = statement;
    }

    public CaspianStatement(Statement statement, Object test) {
        this.statement = statement;
        this.test = test;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Object getTest() {
        return test;
    }

    public void setTest(Object test) {
        this.test = test;
    }
}
