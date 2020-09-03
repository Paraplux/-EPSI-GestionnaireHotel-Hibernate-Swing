package org.dme.utils;

public class IntValidator {

    public static boolean isThisIntValid(String s) {

        try {

            //if not valid, it will throw ParseException
            Integer.parseInt(s);


        } catch (NumberFormatException e) {

            return false;
        }

        return true;
    }

    public static boolean isDouble(String s) {

        try {

            //if not valid, it will throw ParseException
            Double.parseDouble(s);


        } catch (NumberFormatException e) {

            return false;
        }

        return true;
    }


}
