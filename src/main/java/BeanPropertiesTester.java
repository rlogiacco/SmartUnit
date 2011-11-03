package net.sourceforge.smartunit;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

/**
 * This is a utility class to test bean properties accessor methods
 * 
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
public class BeanPropertiesTester {

	private List<String> excludesByName = new ArrayList<String>();
	
	private List<Class<?>> excludesByType = new ArrayList<Class<?>>();

	private Map<Class<?>, Object> mappings = new HashMap<Class<?>, Object>();

	public BeanPropertiesTester() {
		mappings.put(boolean.class, new Boolean(true));
		mappings.put(byte.class, new Byte(Byte.MAX_VALUE));
		mappings.put(char.class, new Character(Character.MAX_VALUE));
		mappings.put(short.class, new Short(Short.MAX_VALUE));
		mappings.put(int.class, new Integer(Integer.MAX_VALUE));
		mappings.put(long.class, new Long(Long.MAX_VALUE));
		mappings.put(float.class, new Float(Float.MAX_VALUE));
		mappings.put(double.class, new Double(Double.MAX_VALUE));
		mappings.put(Collection.class, new ArrayList<Object>());
		mappings.put(List.class, new ArrayList<Object>());
		mappings.put(Set.class, new HashSet<Object>());
		mappings.put(Map.class, new HashMap<Object, Object>());
	}

	/**
	 * Add a type mapping to instruct the tester how a certain type should be
	 * instantiated. If your bean uses interfaces to define properties than all
	 * interfaces should be added to the mappings before testing the property
	 * otherwise instantion exceptions will be thrown.
	 * 
	 * @param type
	 *            the type you want to map
	 * @param value
	 *            the value that will be used to valorize/check fields
	 */
	public void addMapping(Class<?> type, Object value) {
		mappings.put(type, value);
	}

	public void setNameExclusions(final String... names) {
		excludesByName = Arrays.asList(names);
	}
	
	public void setTypeExclusions(final Class<?>... types) {
		excludesByType = Arrays.asList(types);
	}

	public void testAllProperties(Class<?> type) throws Exception {
		Field[] fields = type.getDeclaredFields();
		Object instance = this.getInstance(type);
		for (Field field : fields) {
			this.testField(instance, field);
		}
	}

	public void testProperty(Class<?> type, String fieldName, Object value)
			throws Exception {
		Field field = type.getDeclaredField(fieldName);
		Object instance = this.getInstance(type);
		this.testField(instance, field);
	}

	private void testField(Object instance, Field field) throws Exception {
		Method setter = this.getSetter(field);
		Method getter = this.getGetter(field);
		if (setter != null && getter != null) {
			Object value = this.getInstance(field.getType());
			setter.invoke(instance, value);
			TestCase.assertEquals("Field test failed on `" + field.getName()
					+ "`", value, getter.invoke(instance));
		} else {
			if (setter != null) {
				Object value = this.getInstance(field.getType());
				setter.invoke(instance, value);
				field.setAccessible(true);
				TestCase.assertEquals(
						"Field test failed on `" + field.getName() + "`",
						value, field.get(instance));
			}
			if (getter != null) {
				Object value = this.getInstance(field.getType());
				field.setAccessible(true);
				field.set(instance, value);
				TestCase.assertEquals(
						"Field test failed on `" + field.getName() + "`",
						value, getter.invoke(instance));
			}
		}
	}

	private Method getSetter(final Field field) {
		if (excludesByName.contains(field.getName()) || excludesByType.contains(field.getType())) {
			return null;
		}
		try {
			return field.getDeclaringClass().getMethod(this.getMethodName("set", field), field.getType());
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	private Method getGetter(final Field field) {
		if (excludesByName.contains(field.getName()) || excludesByType.contains(field.getType())) {
			return null;
		}
		Method method = null;
		try {
			method = field.getDeclaringClass().getMethod(this.getMethodName("get", field));
		} catch (NoSuchMethodException nsme) {
			try {
				method = field.getDeclaringClass().getMethod(this.getMethodName("is", field));
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

	private Object getInstance(final Class<?> type)
			throws InstantiationException, IllegalAccessException {
		if (mappings.containsKey(type)) {
			return mappings.get(type);
		} else if (type.isEnum()) {
			return type.getEnumConstants()[0];
		} else {
			return type.newInstance();
		}
	}
}
