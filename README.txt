This library is licensed under LGPL v3.0 and MPL v2.0 licenses and as such is freely usable in commercial products.

There are two instantiable classes in the library:

- BeanPropertiesTester which provides test support for bean properties, the most common usage is

BeanPropertiesTester tester = new BeanPropertiesTester();
tester.setTypeExclusions(DoNotTest.class, DoNotTest2.class); // exclude properties of those two types from testing
tester.setNameExclusions("prop1","prop2"); // exclude properties named prop1 and prop2 from tests
tester.addMapping(MYCustomType.class, new MYCustomType()); // adds a mapping for a custom type allowing for automatic tests to be run on it
tester.testAllProperties(BeanClass.class); // tests all bean properties

- ExceptionTester which provides test support for exceptions, mainly aimed at testing exception constructors, the most common usage is

ExceptionTester tester = new ExceptionTester();
tester.addMapping(MYCustomType.class, new MYCustomType()); // adds a mapping for a custom type allowing for automatic tests to be run on it
tester.testAllConstructors(ExceptionClass.class);
