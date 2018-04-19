package org.agileware.test;

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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.junit.Assert;

import lombok.NonNull;

/**
 * This is a utility class to test bean properties' accessor methods.
 * 
 */
public class PropertiesTester extends AbstractTester<PropertiesTester> {

    /**
     * Property names exclusion list.
     */
    protected List<String> excludesByName = new ArrayList<String>();

    /**
     * Property types exclusion list.
     */
    protected List<Class<?>> excludesByType = new ArrayList<Class<?>>();

    /**
     * Property names mappings.
     */
    protected Map<String, Collection<String>> nameMappings = new HashMap<String, Collection<String>>();

    /**
     * Sets a list of properties you want to exclude by name. Use this feature whenever you want to exclude some
     * specific property from the testing. If you need to exclude multiple properties because because you can't provide
     * an instance for the property type consider using <code>setTypeExclusions</code> instead.
     * 
     * @param names
     *            the list of property names you want to exclude.
     * @return the <code>PropertiesTester</code> instance.
     */
    public PropertiesTester setNameExclusions(final @NonNull String... names) {
        excludesByName = Arrays.asList(names);
        return this;
    }

    /**
     * Sets the list of property types that are not going to be tested. Use this feature whenever your class uses some
     * type you can't provide an instance for.
     * 
     * @param types
     *            the list of types you want to be excluded from properties testing.
     * @return the <code>PropertiesTester</code> instance.
     */
    public PropertiesTester setTypeExclusions(final @NonNull Class<?>... types) {
        excludesByType = Arrays.asList(types);
        return this;
    }

    /**
     * Sets the properties mapping by specifying field to property associations allowing to test properties which uses a
     * field with a different name.
     * 
     * Use this feature specifying a mapping of <code>{"name", "authority"}</code> if you have something like:
     * 
     * <code>
     *   private String name;
     *   
     *   public void setAuthority(String name) {
     *       this.name = name;
     *   }
     * </code>
     * 
     *  
     * If the mapping uses a field defined in a superclass then you should prefix the field with the hash (#) symbol,
     * like <code>{"#inheritedFieldName", "propertyName"}</code>, if you have something like:
     * 
     * <code>
     *   public void setPropertyName(String name) {
     *       super.inherithedFieldName = name;
     *   }
     * </code>
     * 
     * @param mappings
     *            a list of string arrays where each array defines in it's first position the field name and in all
     *            subsequent positions the properties using that field. Multiple properties can be specified for each
     *            mapping and multiple mappings can be set.
     * @return the <code>PropertiesTester</code> instance.
     */
    public PropertiesTester setNameMappings(final @NonNull String[]... mappings) {
        for (String[] mapping : mappings) {
            Assert.assertTrue("A mapping is defined by an array of 2 strings minimum", mapping.length > 1);
            Collection<String> properties = nameMappings.get(mapping[0]);
            if (properties == null) {
                properties = new HashSet<String>();
                this.nameMappings.put(mapping[0], properties);
            }
            for (int i = 1; i < mapping.length; i++) {
                properties.add(mapping[i]);
            }
        }
        return this;
    }

    /**
     * Tests all the properties defined in this class.
     * 
     * A property is considered to have been defined locally if the class defines a field member of the same type used
     * in the accessor methods. Accessor methods overriding superclass accessor methods or accessor methods for
     * properties without an associated member field are not considered `defined properties`.
     * 
     * @param type
     *            the class the test is going to be run on.
     * @throws Exception
     *             thrown if any of the tests fail.
     * @return the <code>PropertiesTester</code> instance.
     */
    public PropertiesTester testAll(@NonNull Class<?> type) throws Exception {
        this.testAll(type, true);
        return this;
    }

    /**
     * Test all the properties in this class using the <code>definedOnly</code> parameter to restrict the set to locally
     * defined properties or not. A property is considered to have been defined locally if the class defines a field
     * member of the same type used in the accessor methods.
     * 
     * @param type
     *            the class the test is going to be run on.
     * @param definedOnly
     *            determines the set of properties to be tested: if you want to test accessor methods overriding
     *            superclass accessor methods or accessor methods using inherited fields you need to set this value to
     *            <code>false</code>.
     * @throws Exception
     *             thrown if any of the tests fail.
     * @return the <code>PropertiesTester</code> instance.
     */
    public PropertiesTester testAll(@NonNull Class<?> type, boolean definedOnly) throws Exception {
        Object instance = this.getInstance(type);
        for (Field field : type.getDeclaredFields()) {
            this.testField(instance, type, field, null);
        }
        if (!definedOnly) {
            for (Field field : this.listInherithed(type, type.getSuperclass(), new HashSet<Field>())) {
                this.testField(instance, type, field, null);
            }
        }
        return this;
    }

    /**
     * Tests a property by name checking the accessor methods work as expected.
     * 
     * @param type
     *            the class on which the test is going to be run.
     * @param fieldName
     *            the name of the property to test.
     * @param value
     *            the value used to check the accessor methods.
     * @throws Exception
     *             thrown if any of the tests fail.
     * @return the <code>PropertiesTester</code> instance.
     */
    public PropertiesTester test(@NonNull Class<?> type, @NonNull String fieldName, @NonNull Object value) throws Exception {
        Field field = type.getDeclaredField(fieldName);
        Object instance = this.getInstance(type);
        this.testField(instance, type, field, value);
        return this;
    }
    
    /**
     * Performs a field check including field name mappings.
     * 
     * @param instance
     *            the instance on which the properties will be accessed.
     * @param type
     *            the class under test.
     * @param field
     *            the field to test.
     * @param value 
     *            the value used to test the field.
     * @throws Exception
     *             thrown if any of the tests fail.
     */
    private void testField(Object instance, Class<?> type, Field field, Object value) throws Exception {
        this.testField(instance, type, field, field.getName(), value);
        if (nameMappings.containsKey(field.getName())) {
            for (String property : nameMappings.get(field.getName())) {
                this.testField(instance, type, field, property, value);
            }
        }
        if (nameMappings.containsKey("#" + field.getName())) {
            for (String property : nameMappings.get("#" + field.getName())) {
                this.testField(instance, type, field, property, value);
            }
        }
    }

    /**
     * Performs a property check.
     * 
     * @param instance
     *            the instance on which the properties will be accessed.
     * @param type
     *            the class under test.
     * @param field
     *            the field to test.
     * @param property
     *            the property to test.
     * @param value 
     *            the value used to instantiate the property.
     * @throws Exception
     *             thrown if any of the tests fail.
     */
    private void testField(Object instance, Class<?> type, Field field, final String property, Object value) throws Exception {
        Method setter = this.getSetter(type, property, field.getType(), false);
        Method getter = this.getGetter(type, property, field.getType(), false);
        if (setter != null && getter != null) {
            testSetterGetterInteraction(instance, field, setter, getter, value);
        } else {
            if (setter != null) {
                testSetter(instance, field, setter, value);
            }
            if (getter != null) {
                testGetter(instance, field, getter, value);
            }
        }
    }

    /**
     * This method tests the interaction between a fields setter and getter methods
     * @param instance the instance on which to invoke calls
     * @param field the field under test
     * @param setter the setter method to invoke
     * @param getter the getter method to invoke
     * @param value the value passed to setter method, and expected to return from getter method invocation
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException 
     */
    private void testSetterGetterInteraction(Object instance, Field field, Method setter, Method getter, Object value) throws InstantiationException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
        Object getterInvocationResult;
        if (value == null){
            value = this.getInstance(field.getType());
        }
        setter.invoke(instance, value);
        getterInvocationResult = getter.invoke(instance);
        if(Optional.class.isInstance(getterInvocationResult) && !Optional.class.isInstance(value)){
            Assert.assertTrue("Setter-Getter interaction for field test failed on `" + field.getName() + "`, expected none empty Optional", Optional.class.cast(getterInvocationResult).isPresent());
            Assert.assertEquals("Setter-Getter interaction for field test failed on `" + field.getName() + "`, expected Optional.get() to return: " + value, value, Optional.class.cast(getterInvocationResult).orElse(null));
        }else{
            Assert.assertEquals("Setter-Getter interaction for field test failed on `" + field.getName() + "`", value, getterInvocationResult);
        }
        if (!field.getType().isPrimitive()) {
            setter.invoke(instance, field.getType().cast(null));
            getterInvocationResult = getter.invoke(instance);
            if (Optional.class.isInstance(getterInvocationResult)) {
                Assert.assertEquals("Setter-Getter interaction for null field test failed on `" + field.getName() + "`, expected empty Optional", Optional.empty(), getterInvocationResult);
            }else{
                Assert.assertEquals("Setter-Getter interaction for null field test failed on `" + field.getName() + "`", null, getterInvocationResult);
            }
        }
    }

    /**
     * This method tests a field getter method
     * @param instance the instance on which to invoke calls 
     * @param field the field under test
     * @param getter the getter method to invoke
     * @param value the expected value to return from getter invocation
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws InstantiationException
     * @throws SecurityException
     * @throws IllegalAccessException 
     */
    private void testGetter(Object instance, Field field, Method getter, Object value) throws InvocationTargetException, NoSuchMethodException, IllegalArgumentException, InstantiationException, SecurityException, IllegalAccessException {
        Object getterInvocationResult;
        if (value == null){
            value = this.getInstance(field.getType());
        }
        field.setAccessible(true);
        field.set(instance, value);
        getterInvocationResult = getter.invoke(instance);
        if (Optional.class.isInstance(getterInvocationResult) && !Optional.class.isInstance(value)) {
            Assert.assertTrue("Getter for field test failed on `" + field.getName() + "`, expected none empty Optional", Optional.class.cast(getterInvocationResult).isPresent());
            Assert.assertEquals("Getter for field test failed on `" + field.getName() + "`, expected Optional.get() to return: " + value, value, Optional.class.cast(getterInvocationResult).orElse(null));
        }else{
            Assert.assertEquals("Getter for field test failed on `" + field.getName() + "`", value, getterInvocationResult);
        }
        if (!field.getType().isPrimitive()) {
            field.set(instance, null);
            getterInvocationResult = getter.invoke(instance);
            if(Optional.class.isInstance(getterInvocationResult)){
                Assert.assertEquals("Getter for null field test failed on `" + field.getName() + "`, expected empty Optional", Optional.empty(), getterInvocationResult);
            }else{
                Assert.assertEquals("Getter for null field test failed on `" + field.getName() + "`", null, getterInvocationResult);
            }
        }
    }

    /**
     * This method test a field setter method
     * @param instance the instance on which to invoke calls
     * @param field the field under test
     * @param setter the setter method to invoke
     * @param value the value passed to setter method invocation
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IllegalArgumentException
     * @throws SecurityException 
     */
    private void testSetter(Object instance, Field field, Method setter, Object value) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException, IllegalArgumentException, SecurityException {
        if (value == null){
            value = this.getInstance(field.getType());
        }
        field.setAccessible(true);
        setter.invoke(instance, value);
        Assert.assertEquals("Setter for field test failed on `" + field.getName() + "`", value, field.get(instance));
        if (!field.getType().isPrimitive()) {
            setter.invoke(instance, field.getType().cast(null));
            Assert.assertEquals("Setter for null field test failed on `" + field.getName() + "`", null, field.get(instance));
        }
    }

    /**
     * Recursively traverse the class hierarchy to find fields used by access methods defined on the class under test.
     * 
     * @param orig
     *            the class under test.
     * @param type
     *            the class in the hierarchy currently under field investigation.
     * @param fields
     *            the collection of fields found.
     * @return a collection of fields found in the hierarchy mapping to accessor methods locally defined.
     */
    private Collection<Field> listInherithed(Class<?> orig, Class<?> type, Collection<Field> fields) {
        if (type == null || type.equals(Object.class)) {
            return fields;
        } else {
            for (Field field : type.getDeclaredFields()) {
                if (this.getGetter(orig, field.getName(), field.getType(), true) != null
                        || this.getSetter(orig, field.getName(), field.getType(), true) != null) {
                    field.setAccessible(true);
                    fields.add(field);
                }
                if (nameMappings.containsKey("#" + field.getName())) {
                    for (String property : nameMappings.get("#" + field.getName())) {
                        if (this.getGetter(orig, property, field.getType(), true) != null
                                || this.getSetter(orig, property, field.getType(), true) != null) {
                            field.setAccessible(true);
                            fields.add(field);
                        }
                    }
                }
            }
            return listInherithed(orig, type.getSuperclass(), fields);
        }
    }

    /**
     * Returns the setter method for a property.
     * 
     * @param type
     *            the class under test.
     * @param propertyName
     *            the property name.
     * @param propertyType
     *            the property type.
     * @return the setter method.
     */
    private Method getSetter(final Class<?> type, final String propertyName, final Class<?> propertyType,
            boolean declared) {
        if (excludesByName.contains(propertyName) || excludesByType.contains(propertyType)) {
            return null;
        }
        try {

            if (declared) {
                return type.getDeclaredMethod(this.getMethodName("set", propertyName), propertyType);
            } else {
                return type.getMethod(this.getMethodName("set", propertyName), propertyType);
            }
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Returns the getter method for a property.
     * 
     * @param type
     *            the class under test.
     * @param propertyName
     *            the property name.
     * @param propertyType
     *            the property type.
     * @return the getter method.
     */
    private Method getGetter(final Class<?> type, final String propertyName, final Class<?> propertyType,
            boolean declared) {
        if (excludesByName.contains(propertyName) || excludesByType.contains(propertyType)) {
            return null;
        }
        Method method = null;
        try {
            if (declared) {
                method = type.getDeclaredMethod(this.getMethodName("get", propertyName));
            } else {
                method = type.getMethod(this.getMethodName("get", propertyName));
            }
        } catch (NoSuchMethodException nsme) {
            if (propertyType.equals(Boolean.class) || propertyType.equals(boolean.class)) {
                try {
                    if (declared) {
                        method = type.getDeclaredMethod(this.getMethodName("is", propertyName));
                    } else {
                        method = type.getMethod(this.getMethodName("is", propertyName));
                    }
                } catch (NoSuchMethodException nsmeAgain) {
                    return null;
                }
            }
        }
        if (method != null && !(method.getReturnType().equals(propertyType) || method.getReturnType().equals(Optional.class))) {
            method = null;
        }
        return method;
    }

    /**
     * Returns the proper string concatenation for an accessor method.
     * 
     * @param prefix
     *            the method prefix.
     * @param property
     *            the property name.
     * @return the camel case concatenation of prefix and property.
     */
    private String getMethodName(final String prefix, final String property) {
        return prefix + Character.toUpperCase(property.charAt(0)) + property.substring(1);
    }
}
