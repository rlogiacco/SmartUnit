package org.agileware.test;

import java.lang.reflect.InvocationTargetException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;

import org.junit.Test;

/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
public class AbstractTesterTest {

    private AbstractTester<Mock> tester = new Mock();

    private static class PojoPrivate {
        private PojoPrivate() {
        }
    }

    @Test
    public void testAddMapping() {
        assertFalse(tester.mappings.containsKey(Type.class));
        tester.addMapping(Type.class, new Type());
        assertTrue(tester.mappings.containsKey(Type.class));
        assertNotNull(tester.mappings.get(Type.class));
        assertTrue(tester.mappings.get(Type.class).build() instanceof Type);
    }

    @Test
    public void testGetInstance() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        assertTrue(tester.getInstance(boolean.class) instanceof Boolean); // Primitive
        assertTrue(tester.getInstance(Date.class) instanceof Date); // Class
        assertTrue(tester.getInstance(AbstractTester.class) instanceof AbstractTester); // Abstract
        assertTrue(tester.getInstance(Collection.class) instanceof Collection); // Interface
        assertTrue(tester.getInstance(EnumType.class) instanceof EnumType); // Enumeration
        assertTrue(tester.getInstance(Type.class) instanceof Type); // Unknown
        assertTrue(tester.getInstance(PojoPrivate.class) instanceof PojoPrivate); // Unknown

        tester.addMapping(Type.class, new Type());
        assertTrue(tester.getInstance(Type.class) instanceof Type); // Custom

    }

    public static class Mock extends AbstractTester<Mock> {
    }

    public static class Type {
    }

    public static enum EnumType {
        VALUE;
    }
}
