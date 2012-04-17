package org.rlogiacco.smartunit;

/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

/**
 * This is a utility class to test bean properties accessor methods
 * 
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 * 
 */
public class BeanPropertiesTester extends AbstractTester {

    protected List<String> excludesByName = new ArrayList<String>();

    protected List<Class<?>> excludesByType = new ArrayList<Class<?>>();

    public void setNameExclusions(final String... names) {
        excludesByName = Arrays.asList(names);
    }

    public void setTypeExclusions(final Class<?>... types) {
        excludesByType = Arrays.asList(types);
    }
    
    public void testAllDefinedProperties(Class<?> type) throws Exception {
        Field[] fields = type.getDeclaredFields();
        Object instance = this.getInstance(type);
        for (Field field : fields) {
            this.testField(instance, type, field);
        }
    }

    public void testAllProperties(Class<?> type) throws Exception {
        this.testAllDefinedProperties(type);
        
        Collection<Field> fields = this.listOverriden(type, type.getSuperclass(), new ArrayList<Field>());
        Object instance = this.getInstance(type);
        for (Field field : fields) {
            this.testField(instance, type, field);
        }
    }
    
    private Collection<Field> listOverriden(Class<?> orig, Class<?> type, Collection<Field> fields) {
        if (type == null || type.equals(Object.class)) {
            return fields;
        } else {
            for (Field field : type.getDeclaredFields()) {
                if (this.getGetter(orig, field) != null || this.getSetter(orig, field) != null) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
            return listOverriden(orig, type.getSuperclass(), fields);
        }
    }

    public void testProperty(Class<?> type, String fieldName, Object value) throws Exception {
        Field field = type.getDeclaredField(fieldName);
        Object instance = this.getInstance(type);
        this.testField(instance, type, field);
    }

    private void testField(Object instance, Class<?> type, Field field) throws Exception {
        Method setter = this.getSetter(type, field);
        Method getter = this.getGetter(type, field);
        if (setter != null && getter != null) {
            Object value = this.getInstance(field.getType());
            setter.invoke(instance, value);
            TestCase.assertEquals("Field test failed on `" + field.getName() + "`", value, getter.invoke(instance));
        } else {
            if (setter != null) {
                Object value = this.getInstance(field.getType());
                setter.invoke(instance, value);
                field.setAccessible(true);
                TestCase.assertEquals("Field test failed on `" + field.getName() + "`", value, field.get(instance));
            }
            if (getter != null) {
                Object value = this.getInstance(field.getType());
                field.setAccessible(true);
                field.set(instance, value);
                TestCase.assertEquals("Field test failed on `" + field.getName() + "`", value, getter.invoke(instance));
            }
        }
    }

    private Method getSetter(final Class<?> type, final Field field) {
        if (excludesByName.contains(field.getName()) || excludesByType.contains(field.getType())) {
            return null;
        }
        try {
            return type.getMethod(this.getMethodName("set", field), field.getType());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private Method getGetter(final Class<?> type, final Field field) {
        if (excludesByName.contains(field.getName()) || excludesByType.contains(field.getType())) {
            return null;
        }
        Method method = null;
        try {
            method = type.getMethod(this.getMethodName("get", field));
        } catch (NoSuchMethodException nsme) {
            try {
                method = type.getMethod(this.getMethodName("is", field));
            } catch (NoSuchMethodException nsmeAgain) {
                return null;
            }
        }
        if (method != null && !method.getReturnType().equals(field.getType())) {
            method = null;
        }
        return method;
    }

    private final String getMethodName(final String prefix, final Field field) {
        return prefix + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
    }
}
