package org.rlogiacco.smartunit;

import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class BeanPropertiesTesterTest {

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
    public void testExcludeByName() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        tester.setNameExclusions("getter");
        Partial partial = mock(Partial.class);
        when(partial.getGetter()).thenCallRealMethod();
        tester.testProperty(Partial.class, "getter", new String());
        verify(partial, times(0)).getGetter();
    }
    
    @Test
    public void testExcludeByType() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        tester.setTypeExclusions(Object.class);
        Partial partial = mock(Partial.class);
        when(partial.getGetter()).thenCallRealMethod();
        tester.testProperty(Partial.class, "setter", new String());
        verify(partial, times(0)).getGetter();
    }
    
    @Test
    public void testDefined() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        Inherited inherited = new Inherited();
        inherited = spy(inherited);
        tester.addMapping(Inherited.class, inherited);
        tester.testAllDefinedProperties(Inherited.class);
        verify(inherited, times(0)).getInherited();
        verify(inherited, times(0)).setInherited(any(Object.class));
        verify(inherited, times(0)).getAString();
        verify(inherited, times(0)).setAString(any(String.class));
        verify(inherited).getAnotherString();
        verify(inherited).setAnotherString(any(String.class));
    }
    
    @Test
    public void testInheritedAndDefined() throws Exception {
        BeanPropertiesTester tester = new BeanPropertiesTester();
        Inherited inherited = new Inherited();
        inherited = spy(inherited);
        tester.addMapping(Inherited.class, inherited);
        tester.testAllProperties(Inherited.class);
        verify(inherited).getInherited();
        verify(inherited).setInherited(any(Object.class));
        verify(inherited).getAString();
        verify(inherited).setAString(any(String.class));
        verify(inherited).getAnotherString();
        verify(inherited).setAnotherString(any(String.class));
    }
    
    @Test
    public void testInheritedExcluded() throws Exception {
        
    }

    public static class Simple {
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
        
        protected Object inherited;

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
        
        public Object getInherited() {
            return inherited;
        }

        public void setInherited(Object inherited) {
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
        public Object getInherited() {
            return inherited;
        }

        @Override
        public void setInherited(Object inherited) {
            this.inherited = inherited;
        }

        @Override
        public String getAString() {
            return super.getAString();
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
