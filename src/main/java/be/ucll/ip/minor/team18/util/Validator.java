package be.ucll.ip.minor.team18.util;

import java.util.ArrayList;
import java.util.List;

public class Validator {

    public static int SEATSLOWERLIMIT = 1;
    public static int SEATSUPPERLIMIT = 100;
    static List<String> errorList;


    public static List<String> validateBusSeatsInput(String minSeats, String maxSeats){
        errorList = new ArrayList<>();

        if(isBlank(minSeats)){
            errorList.add("busesWithSeatsBetween.minSeats.missing");
        } else if(!checkLowerLimit(minSeats)){
            errorList.add("busesWithSeatsBetween.minSeats.min.invalid");
        } else if(!checkUpperLimit(minSeats)){
            errorList.add("busesWithSeatsBetween.minSeats.max.invalid");
        }

        if(isBlank(maxSeats)){
            errorList.add("busesWithSeatsBetween.maxSeats.missing");
        } else if(!checkLowerLimit(maxSeats)){
            errorList.add("busesWithSeatsBetween.maxSeats.min.invalid");
        } else if(!checkUpperLimit(maxSeats)){
            errorList.add("busesWithSeatsBetween.maxSeats.max.invalid");
        }

        if(!isBlank(minSeats) && !isBlank(maxSeats) && Integer.parseInt(minSeats) > Integer.parseInt(maxSeats)){
            errorList.add("busesWithSeatsBetween.boundary.invalid");
        }

        return errorList;
    }

    public static List<String> validateMinMaxAgeInput(int minAge, int maxAge){
        errorList = new ArrayList<>();

        if(minAge > maxAge) {
            errorList.add("teams.minmaxAge");
        }
        return errorList;
    }

    public static String validateInAgeGroup(int minAge, int maxAge, int playerAge) {
        if(minAge > playerAge || maxAge < playerAge) return "teams.addPlayer.age.error";
        else return null;
    }

    public static boolean isBlank(String data){
        return data == null || data.isEmpty();
    }
    public static boolean checkLowerLimit(String data){
        return Integer.parseInt(data) >= SEATSLOWERLIMIT;
    }
    public static boolean checkUpperLimit(String data){
        return Integer.parseInt(data) <= SEATSUPPERLIMIT;
    }

}
