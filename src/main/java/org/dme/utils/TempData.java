package org.dme.utils;

import org.dme.entities.Reservation;
import org.dme.entities.Room;

import java.util.List;

public class TempData {

    private static List<String> reservationClientSelectionData;
    private static String lastPageData;
    private static Reservation tempsReservationObjecteData;
    private static Reservation tempsRegistrationReservation;
    private static Room tempsRegistrationRoom;

    public static List<String> getReservationClientSelectionData() {
        return reservationClientSelectionData;
    }

    public static void setReservationClientSelectionData(List<String> prevReservationClientSelectionData) {
        reservationClientSelectionData = prevReservationClientSelectionData;
    }

    public static String getLastPageData() {
        return lastPageData;
    }

    public static void setLastPageData(String lastPage) {
        TempData.lastPageData = lastPage;
    }

    public static Reservation getTempsReservationObjecteData() {
        return tempsReservationObjecteData;
    }

    public static void setTempsReservationObjecteData(Reservation tempsReservationObjecteData) {
        TempData.tempsReservationObjecteData = tempsReservationObjecteData;
    }

    public static Room getTempsRegistrationRoom() {
        return tempsRegistrationRoom;
    }

    public static void setTempsRegistrationRoom(Room r) {
        tempsRegistrationRoom = r;
    }

    public static Reservation getTempsRegistrationReservation() {
        return tempsRegistrationReservation;
    }

    public static void setTempsRegistrationReservation(Reservation res) {
        tempsRegistrationReservation = res;
    }


}
