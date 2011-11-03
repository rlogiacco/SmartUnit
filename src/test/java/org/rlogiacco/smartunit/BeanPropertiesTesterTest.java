package org.rlogiacco.smartunit;
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

	@SuppressWarnings("unused")
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
		private Map<?,?> aMap;
		private Set<?> aSet;
		
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
		public Collection getACollection() {
			return aCollection;
		}
		public void setACollection(Collection aCollection) {
			this.aCollection = aCollection;
		}
		public List getAList() {
			return aList;
		}
		public void setAList(List aList) {
			this.aList = aList;
		}
		public Map getAMap() {
			return aMap;
		}
		public void setAMap(Map aMap) {
			this.aMap = aMap;
		}
		public Set getASet() {
			return aSet;
		}
		public void setASet(Set aSet) {
			this.aSet = aSet;
		}
	}
	
	public enum Enum {
		ONE, TWO, THREE;
	}
}
