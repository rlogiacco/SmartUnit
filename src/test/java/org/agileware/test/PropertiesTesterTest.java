package org.agileware.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.AssertionFailedError;

import org.junit.Test;

public class PropertiesTesterTest {

    @Test(expected=InstantiationException.class)
    public void testNonInstantiable() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        tester.testAll(URL.class);
    }
    
    @Test
    public void testClass() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        tester.testAll(Simple.class);
    }

    @Test
    public void testEnum() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        tester.testAll(Enum.class);
    }

    @Test
    public void testAbstractClass() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        tester.testAll(Abstract.class);
    }

    @Test
    public void testInterface() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        tester.testAll(Interface.class);
    }

    @Test
    public void testPartial() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        Partial mock = spy(new Partial());
        tester.addMapping(Partial.class, mock);
        tester.testAll(Partial.class);
        verify(mock, times(2)).getGetter();
        verify(mock, times(2)).setSetter(any(Object.class));
        verify(mock, times(1)).getGetterInt();
        verify(mock, times(1)).setSetterInt(anyInt());
        verify(mock, times(0)).getNonMatching();
        verify(mock, times(0)).setNonMatchingInt(anyInt());
    }

    @Test
    public void testProperty() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        tester.test(Partial.class, "field", new String());
    }
    
    @Test
    public void testBooleanProperty() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        Simple simple = spy(new Simple());
        tester.addMapping(Simple.class, simple);
        tester.test(Simple.class, "aBoolean", new Boolean(true));
        verify(simple).isABoolean();
    }

    @Test
    public void testExcludeByName() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        tester.setNameExclusions("getter");
        Partial partial = spy(new Partial());
        tester.test(Partial.class, "getter", new String());
        verify(partial, times(0)).getGetter();
    }
    
    @Test
    public void testExcludeByType() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        tester.setTypeExclusions(Object.class);
        Partial partial = spy(new Partial());
        tester.test(Partial.class, "setter", new String());
        verify(partial, times(0)).getGetter();
    }

    @Test
    public void testDefined() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.testAll(Inherited.class);
        verify(inherited, times(0)).getInherited();
        verify(inherited, times(0)).setInherited(any(Boolean.class));
        verify(inherited, times(0)).getAString();
        verify(inherited, times(0)).setAString(any(String.class));
        verify(inherited, times(2)).getAnotherString();
        verify(inherited, times(2)).setAnotherString(any(String.class));
    }

    @Test
    public void testInheritedAndDefined() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.testAll(Inherited.class, false);
        verify(inherited, times(2)).getInherited();
        verify(inherited, times(2)).setInherited(any(Boolean.class));
        verify(inherited, times(2)).getAString();
        verify(inherited, times(2)).setAString(any(String.class));
        verify(inherited, times(2)).getAnotherString();
        verify(inherited, times(2)).setAnotherString(any(String.class));
    }

    @Test
    public void testInherithedWithNoSuperclass() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        tester.testAll(Simple.class, false);
    }
    
    @Test
    public void testInherithedInterface() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        tester.testAll(Interface.class, false);
    }

    @Test
    public void testPropertiesMappings() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.setNameMappings(
                new String[] { "inherited", "mappedProperty" }, // not mapped as it is missing the # in front of the field name
                new String[] { "anotherString", "mappedString" });
        tester.testAll(Inherited.class);
        verify(inherited, times(0)).isMappedProperty();
        verify(inherited, times(0)).setMappedProperty(any(Boolean.class));
        verify(inherited, times(2)).getMappedString();
        verify(inherited, times(2)).setMappedString(any(String.class));
        verify(inherited, times(0)).setMappedAgain(any(String.class));
    }

    @Test
    public void testNonExistantPropertiesMappings() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.setNameMappings(
                new String[] { "anotherString", "nonExistant" });
        tester.testAll(Inherited.class);
        verify(inherited, times(0)).isMappedProperty();
        verify(inherited, times(0)).setMappedProperty(any(Boolean.class));
        verify(inherited, times(0)).getMappedString();
        verify(inherited, times(0)).setMappedString(any(String.class));
        verify(inherited, times(0)).setMappedAgain(any(String.class));
    }
    
    @Test
    public void testInheritedPropertiesMappings() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.setNameMappings(
                new String[] { "#inherited", "mappedProperty" }, 
                new String[] { "anotherString", "mappedString" });
        tester.testAll(Inherited.class, false);
        verify(inherited, times(2)).isMappedProperty();
        verify(inherited, times(2)).setMappedProperty(any(Boolean.class));
        verify(inherited, times(2)).getMappedString();
        verify(inherited, times(2)).setMappedString(any(String.class));
        verify(inherited, times(0)).setMappedAgain(any(String.class));
    }
    
    @Test
    public void testPropertiesMappingsMultiple() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.setNameMappings(
                new String[] { "#inherited", "mappedProperty", "mappedPropertyAgain", "nonExisting"});
        tester.testAll(Inherited.class, false);
        verify(inherited, times(2)).isMappedProperty();
        verify(inherited, times(2)).setMappedProperty(any(Boolean.class));
        verify(inherited, times(2)).setMappedPropertyAgain(any(Boolean.class));
    }
    
    @Test
    public void testPropertiesMappingsConcatenated() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.setNameMappings(
                new String[] { "anotherString", "mappedString"},
                new String[] { "anotherString", "mappedAgain"});
        tester.testAll(Inherited.class);
        verify(inherited, times(0)).isMappedProperty();
        verify(inherited, times(0)).setMappedProperty(any(Boolean.class));
        verify(inherited, times(2)).getMappedString();
        verify(inherited, times(2)).setMappedString(any(String.class));
        verify(inherited, times(2)).setMappedAgain(any(String.class));
    }
    
    @Test(expected=AssertionFailedError.class)
    public void testPropertiesMappingsFailure() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        Inherited inherited = new Inherited();

        tester.addMapping(Inherited.class, inherited);
        tester.setNameMappings(
                new String[] { "anotherString"});
        tester.testAll(Inherited.class);
    }

    public static class Simple {

        private boolean aBoolean;
        private byte aByte;
        private char aChar;
        private short aShort;
        private int anInt;
        private long aLong;
        private float aFloat;
        private double aDouble;
        private String aString;
        private Object anObject;
        private Collection<Object> aCollection;
        private List<String> aList;
        private Map<String, Date> aMap;
        private Set<?> aSet;
        private Abstract abstractType;
        private Interface interfaceType;
        private Thread thread;
        private Runnable runnable;
        private Date date;
        private Time time;
        private Timestamp timestamp;
        private java.util.Date utilDate;
        private java.util.Calendar calendar;

        protected Boolean inherited;
        
		public boolean isABoolean() {
            return aBoolean;
        }

        public void setABoolean(boolean aBoolean) {
            this.aBoolean = aBoolean;
        }

        public byte getAByte() {
            return aByte;
        }

        public void setAByte(byte aByte) {
            this.aByte = aByte;
        }

        public char getAChar() {
            return aChar;
        }

        public void setAChar(char aChar) {
            this.aChar = aChar;
        }

        public short getAShort() {
            return aShort;
        }

        public void setAShort(short aShort) {
            this.aShort = aShort;
        }

        public int getAnInt() {
            return anInt;
        }

        public void setAnInt(int anInt) {
            this.anInt = anInt;
        }

        public long getALong() {
            return aLong;
        }

        public void setALong(long aLong) {
            this.aLong = aLong;
        }

        public float getAFloat() {
            return aFloat;
        }

        public void setAFloat(float aFloat) {
            this.aFloat = aFloat;
        }

        public double getADouble() {
            return aDouble;
        }

        public void setADouble(double aDouble) {
            this.aDouble = aDouble;
        }

        public String getAString() {
            return aString;
        }

        public void setAString(String aString) {
            this.aString = aString;
        }

        public Object getAnObject() {
            return anObject;
        }

        public void setAnObject(Object anObject) {
            this.anObject = anObject;
        }

        public Collection<Object> getACollection() {
            return aCollection;
        }

        public void setACollection(Collection<Object> aCollection) {
            this.aCollection = aCollection;
        }

        public List<String> getAList() {
            return aList;
        }

        public void setAList(List<String> aList) {
            this.aList = aList;
        }

        public Map<String, Date> getAMap() {
            return aMap;
        }

        public void setAMap(Map<String, Date> aMap) {
            this.aMap = aMap;
        }

        public Set<?> getASet() {
            return aSet;
        }

        public void setASet(Set<?> aSet) {
            this.aSet = aSet;
        }

        public Abstract getAbstractType() {
            return abstractType;
        }

        public void setAbstractType(Abstract abstractType) {
            this.abstractType = abstractType;
        }

        public Interface getInterfaceType() {
            return interfaceType;
        }

        public void setInterfaceType(Interface interfaceType) {
            this.interfaceType = interfaceType;
        }

        public Thread getThread() {
            return thread;
        }

        public void setThread(Thread thread) {
            this.thread = thread;
        }
        
        public Runnable getRunnable() {
			return runnable;
		}

		public void setRunnable(Runnable runnable) {
			this.runnable = runnable;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public Time getTime() {
			return time;
		}

		public void setTime(Time time) {
			this.time = time;
		}

		public Timestamp getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Timestamp timestamp) {
			this.timestamp = timestamp;
		}

		public java.util.Date getUtilDate() {
			return utilDate;
		}

		public void setUtilDate(java.util.Date utilDate) {
			this.utilDate = utilDate;
		}

		public java.util.Calendar getCalendar() {
			return calendar;
		}

		public void setCalendar(java.util.Calendar calendar) {
			this.calendar = calendar;
		}

        public Boolean getInherited() {
            return inherited;
        }

        public void setInherited(Boolean inherited) {
            this.inherited = inherited;
        }
    }

    public static class Inherited extends Simple {
        private String anotherString;

        public String getAnotherString() {
            return anotherString;
        }

        public void setAnotherString(String anotherString) {
            this.anotherString = anotherString;
        }

        @Override
        public Boolean getInherited() {
            return inherited;
        }

        @Override
        public void setInherited(Boolean inherited) {
            this.inherited = inherited;
        }

        @Override
        public String getAString() {
            return super.getAString();
        }
        
        @Override
        public void setThread(Thread inherited) {
            super.setThread(inherited);
        }

        public Boolean isMappedProperty() {
            return inherited;
        }

        public void setMappedProperty(Boolean inherited) {
            this.inherited = inherited;
        }

        public void setMappedPropertyAgain(Boolean inherited) {
            this.inherited = inherited;
        }

        public String getMappedString() {
            return anotherString;
        }

        public void setMappedString(String anotherString) {
            this.anotherString = anotherString;
        }

        public void setMappedAgain(String anotherString) {
            this.anotherString = anotherString;
        }
    }

    @SuppressWarnings("unused")
    public static class Partial {
        private Object getter;
        private int getterInt;
        private Object setter;
        private int setterInt;
        private Object field;
        private String nonMatching;
        private int nonMatchingInt;

        public Object getGetter() {
            return getter;
        }
        
        public int getGetterInt() {
            return getterInt;
        }

        public void setSetter(Object value) {
            setter = value;
        }
        
        public void setSetterInt(int value) {
            setterInt = value;
        }

        public Object getNonMatching() {
            return nonMatching;
        }
        
        public void setNonMatchingInt(float value) {
            nonMatchingInt = (int)value;
        }
    }

    public enum Enum {
        ONE, TWO, THREE;
    }

    public static abstract class Abstract {
    	private boolean property;

		public boolean isProperty() {
			return property;
		}

		public void setProperty(boolean property) {
			this.property = property;
		}

    }

    public static interface Interface {
		public int getProperty();

		public void setProperty(int property);
    }
}
