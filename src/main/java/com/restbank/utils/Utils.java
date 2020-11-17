package com.restbank.utils;

import com.restbank.domain.User;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

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

    public static String[] getNullProps(User user) {
        // workaround solution
        // When i send a request body with no userRoles/accountList
        // They are initialized with empty list or hashset -
        // Then this function override non empty lists
        user.setAccountList(null);
        user.setUserRoles(null);

        final BeanWrapper wrappedSource = new BeanWrapperImpl(user);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}
