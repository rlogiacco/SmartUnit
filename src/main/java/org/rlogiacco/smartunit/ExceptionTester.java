/**
 * 
 */
package org.rlogiacco.smartunit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

/**
 * This is a utility class to test exception constructors
 * 
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
public class ExceptionTester extends AbstractTester {
        
    public void testAllConstructors(Class<? extends Exception> type) throws Exception {
        Constructor<?>[] constructors = type.getConstructors();
        for (Constructor<?> constructor : constructors) {
            List<Object> values = new ArrayList<Object>();
            for (Class<?> param : constructor.getParameterTypes()) {
                values.add(this.getInstance(param));
            }
            this.testConstructor(type, constructor.getParameterTypes(), values.toArray());
        }
    }

    public void testConstructor(Class<?> type, Class<?>[] types, Object[] values) throws SecurityException, NoSuchMethodException {
        Constructor<?> constructor = type.getConstructor(types);
        try {
            constructor.newInstance(values);
        } catch (IllegalArgumentException iae) {
            Assert.fail("An illegal argument has been provided: " + iae);
        } catch (InstantiationException ie) {
            Assert.fail("Instantion failed: " + ie);
        } catch (IllegalAccessException iae) {
            Assert.fail("Constructor access denied: " + iae);
        } catch (InvocationTargetException ite) {
            Assert.fail("An exception has been thrown during instantiation: " + ite.getCause());
        }
    }
}
