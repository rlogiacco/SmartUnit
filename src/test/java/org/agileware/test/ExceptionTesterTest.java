package org.agileware.test;

import java.util.Date;
import java.util.Locale;

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
        tester.testConstructor(TestException.class, new Class<?>[0], new Object[0]);
    }

    @Test(expected=AssertionError.class)
    public void testInvocationTargetException() throws Exception {
        ExceptionTester tester = new ExceptionTester();
        tester.testConstructor(InvocationTargetExceptionTest.class, new Class<?>[0], new Object[0]);
    }

    @Test(expected=AssertionError.class)
    public void testInstantiationException() throws Exception {
        ExceptionTester tester = new ExceptionTester();
        tester.testConstructor(InstantiationExceptionTest.class, new Class<?>[0], new Object[0]);
    }
    
    @Test
    public void testAllLenient() throws Exception {
        ExceptionTester tester = new ExceptionTester();
        tester.testAllConstructors(LenientExceptionTest.class, true);
    }
    
    @Test(expected=AssertionError.class)
    public void testAllLenientException() throws Exception {
        ExceptionTester tester = new ExceptionTester();
        tester.testAllConstructors(LenientExceptionTest.class);
    }

    @SuppressWarnings("all")
    public static class TestException extends Exception {
    	
    	private Locale locale;
    	
    	private String cause;
    	
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
            super(message);
            this.locale = locale;
        }

        public TestException(String message, Locale locale, Throwable cause) {
            super(message, cause);
            this.locale = locale;
        }
        
        public TestException(String message, String cause) {
            super(message);
            this.cause = cause;
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
    
    @SuppressWarnings("all")
    public static class LenientExceptionTest extends Exception {
        public LenientExceptionTest(Date date) {
            // the date is not stored
        }
    }
}
