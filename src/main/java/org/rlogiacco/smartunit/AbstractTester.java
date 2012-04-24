package org.rlogiacco.smartunit;

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

public class AbstractTester {

    protected Map<Class<?>, Object> mappings = new HashMap<Class<?>, Object>();
    
    public AbstractTester() {
        // primitives
        mappings.put(boolean.class, new Boolean(true));
        mappings.put(byte.class, new Byte(Byte.MAX_VALUE));
        mappings.put(char.class, new Character(Character.MAX_VALUE));
        mappings.put(short.class, new Short(Short.MAX_VALUE));
        mappings.put(int.class, new Integer(Integer.MAX_VALUE));
        mappings.put(long.class, new Long(Long.MAX_VALUE));
        mappings.put(float.class, new Float(Float.MAX_VALUE));
        mappings.put(double.class, new Double(Double.MAX_VALUE));
        // wrappers
        mappings.put(Boolean.class, new Boolean(true));
        mappings.put(Byte.class, new Byte(Byte.MAX_VALUE));
        mappings.put(Character.class, new Character(Character.MAX_VALUE));
        mappings.put(Short.class, new Short(Short.MAX_VALUE));
        mappings.put(Integer.class, new Integer(Integer.MAX_VALUE));
        mappings.put(Long.class, new Long(Long.MAX_VALUE));
        mappings.put(Float.class, new Float(Float.MAX_VALUE));
        mappings.put(Double.class, new Double(Double.MAX_VALUE));
        // collections
        mappings.put(Collection.class, new ArrayList<Object>());
        mappings.put(List.class, new ArrayList<Object>());
        mappings.put(Set.class, new HashSet<Object>());
        mappings.put(SortedSet.class, new TreeSet<Object>());
        mappings.put(Map.class, new HashMap<Object, Object>());
        mappings.put(SortedMap.class, new TreeMap<Object, Object>());
        // date & time
        mappings.put(Date.class, new Date(System.currentTimeMillis()));
        mappings.put(Time.class, new Time(System.currentTimeMillis()));
        mappings.put(Timestamp.class, new Timestamp(System.currentTimeMillis()));
        // others
        mappings.put(Runnable.class, new Thread());
        mappings.put(Thread.class, new Thread());
        mappings.put(Locale.class, Locale.getDefault());
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

    protected Object getInstance(final Class<?> type)
            throws InstantiationException, IllegalAccessException {
        if (mappings.containsKey(type)) {
            return mappings.get(type);
        } else if (type.isEnum()) {
            return type.getEnumConstants()[0];
        } else if (Modifier.isAbstract(type.getModifiers()) || Modifier.isInterface(type.getModifiers())) {
            return Mockito.mock(type);
        } else {
            return type.newInstance();
        }
    }
}
