package com.atqc.framework;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.*;

import static com.atqc.framework.Config.userEmail;

@Log4j2
public class Common {

    public static Map<String, String> realAddresses() {
        Map<String, String> addressesList = new HashMap<>();
        addressesList.put("22 West 32nd Street 1 New York NY 10001", "2124732233");
        addressesList.put("591 North Eastern Avenue 1 Las Vegas NV 89101", "7023808089");
        addressesList.put("3656 Wall Avenue 1 Ogden UT 84405", "8013173953");
        addressesList.put("700 West Albro Street 1 Claflin KS 67525", "6205873342");
        addressesList.put("805 South Kiwanis Avenue 1 Sioux Falls SD 57104", "6053677003");
        addressesList.put("301 West 29th Street 1 Baltimore MD 21211", "4433473570");
        return addressesList;
    }

    static Faker faker = new Faker();

    public static String getEmail() {
        String[] arrOfStr = userEmail.split("@", 2);
        return arrOfStr[0] + "+" + getRandomInt(99999) + "@" + arrOfStr[1];
    }

    public static String getRealSSN() {
        return "66600" + Common.fakeRandomIntFromTo(1111, 9999);
    }

    public static String getSuspendedSSN() {
        return "671" + Common.fakeRandomIntFromTo(100000, 999999);
    }


    public static String getRandomInt(int bound) {
        return String.valueOf(new Random().nextInt(bound));
    }

    public static String fakeFirstName() {
        return faker.name().firstName();
    }

    public static String fakeLastName() {
        return faker.name().lastName();
    }

    public static String fakeEmail() {
        return faker.internet().emailAddress();
    }

    public static <T> T getRandomElementFromList(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public static String getRandomKeyFromMap(Map<String, String> map ) {
        List<String> keysAsArray = new ArrayList<>(map.keySet());
        return getRandomElementFromList(keysAsArray);
    }

    public static String fakeFixedString(int lettersCount) {
        return faker.lorem().characters(lettersCount);
    }

    public static int fakeRandomIntFromTo(int from, int to) {
        return faker.number().numberBetween(from, to);
    }

    public static String changePhoneNumberToUIFormat(String phone) {
        return "(" + phone.substring(0, 3) + ") " + phone.substring(3, 6) + "-" + phone.substring(6);
    }
    @SneakyThrows
    public static ObjectMapper mapper() {
        return new ObjectMapper().configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
    }

    public static String replace(String data) {
        return data.replace("'", "\"");
    }

    public static String changeDOBFormat(String dateOfBirth) {
        String[] args = dateOfBirth.split(" ", 3);
        if (args[0].length() == 1) args[0] = "0" + args[0];
        if (args[1].length() == 1) args[1] = "0" + args[1];
        return args[2] + "-" + args[0] + "-" + args[1];
    }
}
