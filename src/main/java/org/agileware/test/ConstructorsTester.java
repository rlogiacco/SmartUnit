/**
 * 
 */
package org.agileware.test;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is a utility class to test exception constructors
 * 
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 * 
 */
public class ConstructorsTester extends AbstractTester {

	public void testAll(Class<? extends Exception> type) throws Exception {
		this.testAllConstructors(type, false);
	}
	public void testAllConstructors(Class<? extends Exception> type, boolean lenient) throws Exception {
		Constructor<?>[] constructors = type.getConstructors();
		for (Constructor<?> constructor : constructors) {
			List<Object> params = new ArrayList<Object>();
			for (Class<?> paramType : constructor.getParameterTypes()) {
				params.add(this.getInstance(paramType));
			}
			this.test(type, constructor.getParameterTypes(), params.toArray(), lenient);
		}
	}
	

	public void test(Class<?> type, Class<?>[] paramTypes, Object[] params) throws Exception {
		this.test(type, paramTypes, params, false);
	}

	public void test(Class<?> type, Class<?>[] paramTypes, Object[] params, boolean lenient) throws Exception {
		assertNotNull(paramTypes);
		assertNotNull(params);
		Constructor<?> constructor = type.getConstructor(paramTypes);
		Collection<Field> fields = listInherithed(type, type, new ArrayList<Field>());
		try {
			Object instance = constructor.newInstance(params);
			for (int i = 0; i < paramTypes.length; i++) {
				this.testParameter(instance, paramTypes[i], params[i], fields, lenient);
			}
		} catch (InstantiationException ie) {
			System.out.println("InstantiationException");
			throw new AssertionError("Instantiation failed: " + ie);
		} catch (InvocationTargetException ite) {
			System.out.println("InvocationTargetException");
			throw new AssertionError("An exception has been thrown during instantiation: " + ite.getCause());
		}
	}

	private void testParameter(Object instance, Class<?> type, Object value, Collection<Field> fields, boolean lenient)
			throws Exception {
		for (Field field : fields) {
			if (field.getType().equals(type)) {
				if (field.get(instance) == value) {
					return;
				}
			}
		}
		if (!lenient) {
			throw new AssertionError("Cannot verify constructor parameter");
		}
	}

	private Collection<Field> listInherithed(Class<?> orig, Class<?> type, Collection<Field> fields) {
		if (type.equals(Object.class)) {
			return fields;
		} else {
			for (Field field : type.getDeclaredFields()) {
				field.setAccessible(true);
				fields.add(field);
			}
			return listInherithed(orig, type.getSuperclass(), fields);
		}
	}
}
