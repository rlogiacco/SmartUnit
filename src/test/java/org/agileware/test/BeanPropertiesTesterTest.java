package org.agileware.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.AssertionFailedError;

import org.agileware.test.BeanPropertiesTester;
import org.junit.Test;

public class BeanPropertiesTesterTest {

    @Test(expected=InstantiationException.class)
    public void testNonInstantiable() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        tester.testAllProperties(URL.class);
    }
    
    @Test
    public void testClass() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        tester.testAllProperties(Simple.class);
    }

    @Test
    public void testEnum() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        tester.testAllProperties(Enum.class);
    }

    @Test
    public void testAbstractClass() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        tester.testAllProperties(Abstract.class);
    }

    @Test
    public void testInterface() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        tester.testAllProperties(Interface.class);
    }

    @Test
    public void testPartial() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        tester.testAllProperties(Partial.class);
    }

    @Test
    public void testProperty() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        tester.testProperty(Partial.class, "field", new String());
    }
    
    @Test
    public void testBooleanProperty() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        Simple simple = spy(new Simple());
        tester.addMapping(Simple.class, simple);
        tester.testProperty(Simple.class, "aBoolean", new Boolean(true));
        verify(simple).isABoolean();
    }

    @Test
    public void testExcludeByName() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        tester.setNameExclusions("getter");
        Partial partial = spy(new Partial());
        tester.testProperty(Partial.class, "getter", new String());
        verify(partial, times(0)).getGetter();
    }

    @Test
    public void testExcludeByType() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        tester.setTypeExclusions(Object.class);
        Partial partial = spy(new Partial());
        tester.testProperty(Partial.class, "setter", new String());
        verify(partial, times(0)).getGetter();
    }

    @Test
    public void testDefined() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.testAllProperties(Inherited.class);
        verify(inherited, times(0)).getInherited();
        verify(inherited, times(0)).setInherited(any(Boolean.class));
        verify(inherited, times(0)).getAString();
        verify(inherited, times(0)).setAString(any(String.class));
        verify(inherited).getAnotherString();
        verify(inherited).setAnotherString(any(String.class));
    }

    @Test
    public void testInheritedAndDefined() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.testAllProperties(Inherited.class, false);
        verify(inherited).getInherited();
        verify(inherited).setInherited(any(Boolean.class));
        verify(inherited).getAString();
        verify(inherited).setAString(any(String.class));
        verify(inherited).getAnotherString();
        verify(inherited).setAnotherString(any(String.class));
    }

    @Test
    public void testInherithedWithNoSuperclass() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        tester.testAllProperties(Simple.class, false);
    }
    
    @Test
    public void testInherithedInterface() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        tester.testAllProperties(Interface.class, false);
    }

    @Test
    public void testPropertiesMappings() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.setNameMappings(
                new String[] { "inherited", "mappedProperty" }, // not mapped as it is missing the # in front of the field name
                new String[] { "anotherString", "mappedString" });
        tester.testAllProperties(Inherited.class);
        verify(inherited, times(0)).isMappedProperty();
        verify(inherited, times(0)).setMappedProperty(any(Boolean.class));
        verify(inherited).getMappedString();
        verify(inherited).setMappedString(any(String.class));
        verify(inherited, times(0)).setMappedAgain(any(String.class));
    }

    @Test
    public void testNonExistantPropertiesMappings() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.setNameMappings(
                new String[] { "anotherString", "nonExistant" });
        tester.testAllProperties(Inherited.class);
        verify(inherited, times(0)).isMappedProperty();
        verify(inherited, times(0)).setMappedProperty(any(Boolean.class));
        verify(inherited, times(0)).getMappedString();
        verify(inherited, times(0)).setMappedString(any(String.class));
        verify(inherited, times(0)).setMappedAgain(any(String.class));
    }
    
    @Test
    public void testInheritedPropertiesMappings() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.setNameMappings(
                new String[] { "#inherited", "mappedProperty" }, 
                new String[] { "anotherString", "mappedString" });
        tester.testAllProperties(Inherited.class, false);
        verify(inherited).isMappedProperty();
        verify(inherited).setMappedProperty(any(Boolean.class));
        verify(inherited).getMappedString();
        verify(inherited).setMappedString(any(String.class));
        verify(inherited, times(0)).setMappedAgain(any(String.class));
    }
    
    @Test
    public void testPropertiesMappingsMultiple() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.setNameMappings(
                new String[] { "#inherited", "mappedProperty", "mappedPropertyAgain", "nonExisting"});
        tester.testAllProperties(Inherited.class, false);
        verify(inherited).isMappedProperty();
        verify(inherited).setMappedProperty(any(Boolean.class));
        verify(inherited).setMappedPropertyAgain(any(Boolean.class));
    }
    
    @Test
    public void testPropertiesMappingsConcatenated() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        Inherited inherited = spy(new Inherited());
        tester.addMapping(Inherited.class, inherited);
        tester.setNameMappings(
                new String[] { "anotherString", "mappedString"},
                new String[] { "anotherString", "mappedAgain"});
        tester.testAllProperties(Inherited.class);
        verify(inherited, times(0)).isMappedProperty();
        verify(inherited, times(0)).setMappedProperty(any(Boolean.class));
        verify(inherited).getMappedString();
        verify(inherited).setMappedString(any(String.class));
        verify(inherited).setMappedAgain(any(String.class));
    }
    
    @Test(expected=AssertionFailedError.class)
    public void testPropertiesMappingsFailure() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        Inherited inherited = new Inherited();

        tester.addMapping(Inherited.class, inherited);
        tester.setNameMappings(
                new String[] { "anotherString"});
        tester.testAllProperties(Inherited.class);
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
        private Collection<?> aCollection;
        private List<?> aList;
        private Map<?, ?> aMap;
        private Set<?> aSet;
        private Abstract abstractType;
        private Interface interfaceType;
        private Thread thread;

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

        public Collection<?> getACollection() {
            return aCollection;
        }

        public void setACollection(Collection<?> aCollection) {
            this.aCollection = aCollection;
        }

        public List<?> getAList() {
            return aList;
        }

        public void setAList(List<?> aList) {
            this.aList = aList;
        }

        public Map<?, ?> getAMap() {
            return aMap;
        }

        public void setAMap(Map<?, ?> aMap) {
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
        private Object setter;
        private Object field;
        private String nonMatching;

        public Object getGetter() {
            return getter;
        }

        public void setSetter(Object value) {
            setter = value;
        }

        public Object getNonMatching() {
            return nonMatching;
        }
    }

    public enum Enum {
        ONE, TWO, THREE;
    }

    public static abstract class Abstract {

    }

    public static interface Interface {

    }
}
