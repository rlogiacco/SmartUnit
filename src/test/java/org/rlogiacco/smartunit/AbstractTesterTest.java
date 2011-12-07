package org.rlogiacco.smartunit;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AbstractTesterTest {

    private AbstractTester tester = new Mock();

    @Test
    public void testAddMapping() {
        assertFalse(tester.mappings.containsKey(Type.class));
        tester.addMapping(Type.class, new Type());
        assertTrue(tester.mappings.containsKey(Type.class));
        assertNotNull(tester.mappings.get(Type.class));
        assertTrue(tester.mappings.get(Type.class) instanceof Type);
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
