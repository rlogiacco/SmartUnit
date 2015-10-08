package org.agileware.test;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;

/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
public class ConstructorsTesterTest {

    @Test
    public void testTestAllConstructors() throws Exception {
        ConstructorsTester tester = new ConstructorsTester();
        tester.testAll(TestException.class);
    }

    @Test
    public void testTestConstructor() throws Exception {
        ConstructorsTester tester = new ConstructorsTester();
        tester.test(TestException.class, new Class<?>[0], new Object[0]);
    }

    @Test(expected=AssertionError.class)
    public void testInvocationTargetException() throws Exception {
        ConstructorsTester tester = new ConstructorsTester();
        tester.test(InvocationTargetExceptionTest.class, new Class<?>[0], new Object[0]);
    }

    @Test(expected=AssertionError.class)
    public void testInstantiationException() throws Exception {
        ConstructorsTester tester = new ConstructorsTester();
        tester.test(InstantiationExceptionTest.class, new Class<?>[0], new Object[0]);
    }
    
    @Test
    public void testAllLenient() throws Exception {
        ConstructorsTester tester = new ConstructorsTester();
        tester.testAllConstructors(LenientExceptionTest.class, true);
    }
    
    @Test(expected=AssertionError.class)
    public void testAllLenientException() throws Exception {
        ConstructorsTester tester = new ConstructorsTester();
        tester.testAll(LenientExceptionTest.class);
    }
    
    @Test(expected=AssertionError.class)
    public void testConstructorNotPrivateShouldFail() throws Exception {
        ConstructorsTester tester = new ConstructorsTester();
        tester.testConstructorIsPrivate(InvocationTargetExceptionTest.class);
    }
    
    @Test
    public void testConstructorPrivateShouldPass() throws Exception {
        ConstructorsTester tester = new ConstructorsTester();
        tester.testConstructorIsPrivate(PriveConstructorClass.class);
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
    
    @SuppressWarnings("all")
    public static class PriveConstructorClass extends Exception {
        private PriveConstructorClass() {
        }
    }
}
