package org.agileware.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;

import org.agileware.test.AbstractTester;
import org.junit.Test;

public class AbstractTesterTest {

    private AbstractTester tester = new Mock();

    @Test
    public void testAddMapping() {
        assertFalse(tester.mappings.containsKey(Type.class));
        tester.addMapping(Type.class, new Type());
        assertTrue(tester.mappings.containsKey(Type.class));
        assertNotNull(tester.mappings.get(Type.class));
        assertTrue(tester.mappings.get(Type.class).build() instanceof Type);
    }

    @Test
    public void testGetInstance() throws InstantiationException, IllegalAccessException {
        assertTrue(tester.getInstance(boolean.class) instanceof Boolean); // Primitive
        assertTrue(tester.getInstance(Date.class) instanceof Date); // Class
        assertTrue(tester.getInstance(AbstractTester.class) instanceof AbstractTester); // Abstract
        assertTrue(tester.getInstance(Collection.class) instanceof Collection); // Interface
        assertTrue(tester.getInstance(EnumType.class) instanceof EnumType); // Enumeration
        assertTrue(tester.getInstance(Type.class) instanceof Type); // Unknown

        tester.addMapping(Type.class, new Type());
        assertTrue(tester.getInstance(Type.class) instanceof Type); // Custom

    }

    public static class Mock extends AbstractTester {
    }

    public static class Type {
    }

    public static enum EnumType {
        VALUE;
    }
}
