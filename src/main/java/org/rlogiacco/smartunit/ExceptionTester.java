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

    public void testConstructor(Class<?> type, Class<?>[] types, Object[] values) throws Exception {
        Constructor<?> constructor = type.getConstructor(types);
        try {
            constructor.newInstance(values);
        } catch (InstantiationException ie) {
            Assert.fail("Instantiation failed: " + ie);
        } catch (InvocationTargetException ite) {
            Assert.fail("An exception has been thrown during instantiation: " + ite.getCause());
        }
    }
}
