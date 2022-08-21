### Project libraries:

#### Frontend: testng, selenium-java, webdrivermanager

#### Backend: rest-assured, lombok, jackson-databind, json-simple

#### Additional: javafaker, log4j-core, log4j-api, slf4j-simple

-----------------------------------------------------------------
### Before testing MAKE SURE that you have already set:
####  - JAVA_HOME, M2_HOME environment variables.
####  - configure Myconfig.properties properties in src/main/resources/... file as in the example (config.properties)
####  - !!! Do not commit Myconfig.properties. File should be saved locally.

-----------------------------------------------------------------
### Web testing in Chrome browser

To run test in terminal:
1. Download the project
2. Place file "MYconfig.properties" to src/main/resources/ with properties relatively environment  
3. Open terminal and go to the project root directory
4. Run command

Run functional suite:

`mvn clean -Dsuite=functional.xml test`

Run full suite:

`mvn clean -Dsuite=fullSuite.xml test`

Run class with tests

`mvn test -Dtest="OnboardingEndToEnd"`

Run one test:

"TEST_CLASS_NAME # TEST_NAME"

`mvn test -Dtest="TEST_CLASS_NAME#TEST_NAME"`

Run one test userCanFinishRegistration which is located in ...src/test/java/com.atqc/frontend/OnboardingEndToEnd.class 

`mvn test -Dtest="OnboardingEndToEnd#userCanFinishRegistration"`

-----------------------------------------------------------------
### RestApi testing

To run test in terminal:
1. Download the project
2. Place file "MYconfig.properties" to src/main/resources/ with properties relatively environment
3. Open terminal and go to the project directory
4. Run command

Run restApi (back-end) suite:

`mvn clean -Dsuite=restApi.xml test`

Run full suite:

`mvn clean -Dsuite=fullSuite.xml test`

Run class with tests

`mvn test -Dtest="LogoEndpointTests"`

Run one test emailAlreadyExists which is located in ...src/test/java/com.atqc/backend/RegistrationsEndpointTests.class

`mvn test -Dtest="RegistrationsEndpointTests#emailAlreadyExists"`

Check equality between OAI and IEX endpoints:

`mvn test -Dtest="CompaniesEndpointTests#checkResponseContainsListOfCompanies"`

`mvn test -Dtest="CompaniesEndpointTests#checkStatsAreEqualed"`

`mvn test -Dtest="DividendsEndpointTests#dividendsReturnsWith1YearRange"`

`mvn test -Dtest="SplitsEndpointTests#splitsReturnsWith1YearRangeAndEqualsToIEX"`

-----------------------------------------------------------------

### Adding new test classes to suite

1. Relatively of test level - for backend or frontend create class inside /src/test/java/com.atqc package
2. New class should be inherited from BaseTest.class
3. After tests are developed, open file ...src/testngXml/... relatively of test level (restAPI for BE, functional for FE)
4. Add new line with class name. It should be looks like:

&lt;class name="com.atqc.frontend.NewClass1"&gt;&lt;/class&gt;

&lt;class name="com.atqc.frontend.NewClass2"&gt;&lt;/class&gt;

-----------------------------------------------------------------

### Reports
To generate Allure Reports run command:

`allure serve target/allure-results` or

`allure generate target/allure-results`

`allure open`

To open report run command:

`mvn allure:serve`
