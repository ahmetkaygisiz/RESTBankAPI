package com.restbank.utils;

import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static String generateCvcNumber(){
        return String.valueOf((new Random().nextInt(900) + 100));
    }

    public static LocalDate generateExpirationDate(){
        LocalDate minDay = LocalDate.now().plusYears(3);
        LocalDate maxDay = minDay.plusYears(10);

        long randomDay = ThreadLocalRandom.current()
                .nextLong(minDay.toEpochDay(), maxDay.toEpochDay());

        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);

        return randomDate;
    }
}
