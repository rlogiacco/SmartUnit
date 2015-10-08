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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.mockito.Mockito;

import lombok.NonNull;

public abstract class AbstractTester<T extends AbstractTester<?>> {

	protected Map<Class<?>, ValueBuilder<?>> mappings = new HashMap<Class<?>, ValueBuilder<?>>();

	public AbstractTester() {
		// primitives
		mappings.put(boolean.class, new ValueBuilder<Boolean>() {

			public Boolean build() {
				return new Boolean((Math.random() * 100 % 2) == 0);
			}

		});
		mappings.put(Boolean.class, new ValueBuilder<Boolean>() {

			public Boolean build() {
				return new Boolean((Math.random() * 100 % 2) == 0);
			}

		});
		mappings.put(byte.class, new ValueBuilder<Byte>() {

			public Byte build() {
				return new Byte((byte) (Math.random() * Byte.MAX_VALUE));
			}

		});
		mappings.put(Byte.class, new ValueBuilder<Byte>() {

			public Byte build() {
				return new Byte((byte) (Math.random() * Byte.MAX_VALUE));
			}

		});
		mappings.put(char.class, new ValueBuilder<Character>() {

			public Character build() {
				return new Character((char) (Math.random() * Character.MAX_VALUE));
			}

		});
		mappings.put(Character.class, new ValueBuilder<Character>() {

			public Character build() {
				return new Character((char) (Math.random() * Character.MAX_VALUE));
			}

		});
		mappings.put(short.class, new ValueBuilder<Short>() {

			public Short build() {
				return new Short((short) (Math.random() * Short.MAX_VALUE));
			}

		});
		mappings.put(Short.class, new ValueBuilder<Short>() {

			public Short build() {
				return new Short((short) (Math.random() * Short.MAX_VALUE));
			}

		});
		mappings.put(int.class, new ValueBuilder<Integer>() {

			public Integer build() {
				return new Integer((int) (Math.random() * Integer.MAX_VALUE));
			}

		});
		mappings.put(Integer.class, new ValueBuilder<Integer>() {

			public Integer build() {
				return new Integer((int) (Math.random() * Integer.MAX_VALUE));
			}

		});
		mappings.put(long.class, new ValueBuilder<Long>() {

			public Long build() {
				return new Long((long) (Math.random() * Long.MAX_VALUE));
			}

		});
		mappings.put(Long.class, new ValueBuilder<Long>() {

			public Long build() {
				return new Long((long) (Math.random() * Long.MAX_VALUE));
			}

		});
		mappings.put(float.class, new ValueBuilder<Float>() {

			public Float build() {
				return new Float((float) (Math.random() * Float.MAX_VALUE));
			}

		});
		mappings.put(Float.class, new ValueBuilder<Float>() {

			public Float build() {
				return new Float((float) (Math.random() * Float.MAX_VALUE));
			}

		});
		mappings.put(double.class, new ValueBuilder<Double>() {

			public Double build() {
				return new Double((double) (Math.random() * Double.MAX_VALUE));
			}

		});
		mappings.put(Double.class, new ValueBuilder<Double>() {

			public Double build() {
				return new Double((double) (Math.random() * Double.MAX_VALUE));
			}

		});
		// collections
		mappings.put(Collection.class, new ValueBuilder<Collection<?>>() {

			public Collection<?> build() {
				return new ArrayList<Object>();
			}

		});
		mappings.put(List.class, new ValueBuilder<List<?>>() {

			public List<?> build() {
				return new ArrayList<Object>();
			}

		});
		mappings.put(Set.class, new ValueBuilder<Set<?>>() {

			public Set<?> build() {
				return new HashSet<Object>();
			}

		});
		mappings.put(SortedSet.class, new ValueBuilder<SortedSet<?>>() {

			public SortedSet<?> build() {
				return new TreeSet<Object>();
			}

		});
		mappings.put(Map.class, new ValueBuilder<Map<?, ?>>() {

			public Map<?, ?> build() {
				return new HashMap<Object, Object>();
			}

		});
		mappings.put(SortedMap.class, new ValueBuilder<SortedMap<?, ?>>() {

			public SortedMap<?, ?> build() {
				return new TreeMap<Object, Object>();
			}

		});
		// date & time
		mappings.put(Date.class, new ValueBuilder<Date>() {

			public Date build() {
				return new Date(System.currentTimeMillis());
			}

		});
		mappings.put(Time.class, new ValueBuilder<Time>() {

			public Time build() {
				return new Time(System.currentTimeMillis());
			}

		});
		mappings.put(Timestamp.class, new ValueBuilder<Timestamp>() {

			public Timestamp build() {
				return new Timestamp(System.currentTimeMillis());
			}

		});
		// others
		mappings.put(Runnable.class, new ValueBuilder<Runnable>() {

			public Runnable build() {
				return new Thread();
			}

		});
		mappings.put(Thread.class, new ValueBuilder<Thread>() {

			public Thread build() {
				return new Thread();
			}

		});
		mappings.put(Locale.class, new ValueBuilder<Locale>() {

			public Locale build() {

				return Locale.getAvailableLocales()[(int) (Math.random() * Locale.getAvailableLocales().length)];
			}

		});
	}

	/**
	 * Adds a type mapping to instruct the tester how a certain type should be
	 * mapped. If your bean uses interfaces to define properties than all
	 * interfaces should be added to the mappings before testing the property
	 * otherwise instantiation exceptions will be thrown.
	 * 
	 * @param type
	 *            the type you want to map
	 * @param value
	 *            the value that will be used to valorize/check fields
	 * @param <V>
	 *            the type of the mapping
     * @param <W>
     *            the value to be used when testing
     * @return the <code>AbstractTester</code> instance
	 */
	public <V, W extends V> T addMapping(@NonNull Class<V> type, final W value) {
		mappings.put(type, new ValueBuilder<Object>() {

			public Object build() {
				return value;
			}

		});
		return (T) this;
	}

	/**
	 * Adds a type mapping to instruct the tester how a certain type should be
	 * instantiated. This is the preferred method for type mapping because
	 * ensures maximum test stability. If your bean uses interfaces to define
	 * properties than all interfaces should be added to the mappings before
	 * testing the property otherwise instantiation exceptions will be thrown.
	 * 
	 * @param type
	 *            the type you want to map
	 * @param builder
	 *            a <code>ValueBuilder</code> implementation capable of
	 *            generating pseudo-random instances of the given type.
	 * @param <V>
	 *            the type of the mapping
     * @param <W>
     *            the value to be used when testing
     * @return the <code>AbstractTester</code> instance
	 */
	public <V, W extends V> T addMapping(@NonNull Class<V> type, ValueBuilder<W> builder) {
		mappings.put(type, builder);
		return (T)this;
	}

	protected Object getInstance(final Class<?> type) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		if (mappings.containsKey(type)) {
			return mappings.get(type).build();
		} else if (type.isEnum()) {
			return type.getEnumConstants()[(int) (Math.random() * type.getEnumConstants().length)];
		} else if (Modifier.isAbstract(type.getModifiers())) {
			return Mockito.mock(type, Mockito.CALLS_REAL_METHODS);
		} else {
			final Constructor<?> defaultConstructor = type.getDeclaredConstructor();
			defaultConstructor.setAccessible(true);
			return defaultConstructor.newInstance();
		}
	}
}
