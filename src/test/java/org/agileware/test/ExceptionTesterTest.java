package org.agileware.test;

import java.util.Locale;

import org.agileware.test.ExceptionTester;
import org.junit.Test;

public class ExceptionTesterTest {

    @Test
    public void testTestAllConstructors() throws Exception {
        ExceptionTester tester = new ExceptionTester();
        tester.testAllConstructors(TestException.class);
    }

    @Test
    public void testTestConstructor() throws Exception {
        ExceptionTester tester = new ExceptionTester();
        tester.testConstructor(TestException.class, null, null);
    }

    @Test(expected=AssertionError.class)
    public void testInvocationTargetException() throws Exception {
        ExceptionTester tester = new ExceptionTester();
        tester.testConstructor(InvocationTargetExceptionTest.class, null, null);
    }

    @Test(expected=AssertionError.class)
    public void testInstantiationException() throws Exception {
        ExceptionTester tester = new ExceptionTester();
        tester.testConstructor(InstantiationExceptionTest.class, null, null);
    }

    @SuppressWarnings("all")
    public static class TestException extends Exception {
        public TestException() {
        }

        public TestException(String message) {
            super(message);
        }

        public TestException(Throwable cause) {
            super(cause);
        }

        public TestException(String message, Throwable cause) {
            super(message, cause);
        }

        public TestException(String message, Locale locale) {
            // Do something with that locale
            super(message);
        }

        public TestException(String message, Locale locale, Throwable cause) {
            // Do something with that locale
            super(message, cause);
        }
    }

    @SuppressWarnings("all")
    public abstract static class InstantiationExceptionTest extends Exception {
        public InstantiationExceptionTest() {
        }
    }

    @SuppressWarnings("all")
    public static class InvocationTargetExceptionTest extends Exception {
        public InvocationTargetExceptionTest() {
            throw new RuntimeException();
        }
    }
}
