SmartUnit is intended to be a place where unit test utility and helper classes can be collected to be shared.

SmartUnit artifacts, including sources and javadocs, are available on the [central Maven repository](http://search.maven.org/#search%7Cga%7C1%7Csmartunit).

Have a look at [our wiki](https://github.com/rlogiacco/SmartUnit/wiki) for more information.

### Release

Before initiating a release it's strongly adviced to execute the full integration test suite which is normally not executed during the common *install* phase.
During the *release* phase the additional `ie`, `chrome` and `firefox` Maven profiles are enabled which execute browser specific tests: this means the releaser need to have a Windows box (due to Internet Explorer dependency) with the Selenium IE and Chrome drivers installed (Firefox does not need a driver).
The easiest way to provide the additional drivers path is to use system properties:
```
mvn integration-test -Pfirefox,chrome,ie -Dwebdriver.ie.driver=<path>\IEDriverServer.exe -Dwebdriver.chrome.driver=<path>\chromedriver.exe
```

**Do not proceed to the release process unless the above command executes without errors**
