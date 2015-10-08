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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import lombok.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This is a utility class to mainly test exception constructors.
 */
public class ConstructorsTester extends AbstractTester<ConstructorsTester> {

	public ConstructorsTester testAll(@NonNull Class<? extends Exception> type) throws Exception {
		this.testAllConstructors(type, false);
		return this;
	}

	public ConstructorsTester testAllConstructors(@NonNull Class<? extends Exception> type, boolean lenient) throws Exception {
		Constructor<?>[] constructors = type.getConstructors();
		for (Constructor<?> constructor : constructors) {
			List<Object> params = new ArrayList<Object>();
			for (Class<?> paramType : constructor.getParameterTypes()) {
				params.add(this.getInstance(paramType));
			}
			this.test(type, constructor.getParameterTypes(), params.toArray(), lenient);
		}
		return this;
	}
	
	/**
	 * Check that only constructor is provided, and that it is private.  This
	 * is particularly useful to ensure classes that only provide static 
	 * methods cannot be instantiated.
	 * 
	 * @param type - Class under test.
	 * @exception Exception - If anything goes wrong.
     * @return the <code>ConstructorTester</code> instance
	 */
	public ConstructorsTester testConstructorIsPrivate(@NonNull Class<?> type) throws Exception {
		Constructor<?>[] constructors = type.getDeclaredConstructors();
		assertNotNull(constructors);
		assertEquals(1, constructors.length);

		Constructor<?> theConstructor = constructors[0];
		if ((theConstructor.getModifiers() & Modifier.PRIVATE) != Modifier.PRIVATE) {
			throw new AssertionError(String.format("Expected %s ctor to be private.", type.getName()));
		}
		return this;
	}
	

	public ConstructorsTester test(@NonNull Class<?> type, @NonNull Class<?>[] paramTypes, @NonNull Object[] params) throws Exception {
		this.test(type, paramTypes, params, false);
		return this;
	}

	public ConstructorsTester test(@NonNull Class<?> type, @NonNull Class<?>[] paramTypes, @NonNull Object[] params, boolean lenient) throws Exception {
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
			throw new AssertionError("Instantiation failed: " + ie);
		} catch (InvocationTargetException ite) {
			throw new AssertionError("An exception has been thrown during instantiation: " + ite.getCause());
		}
		return this;
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
