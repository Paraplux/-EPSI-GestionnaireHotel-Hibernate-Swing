package org.dme.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegistrationTest {

    private Client client;
    private Reservation reservation;
    private Registration registration;
    private Room room;

    @BeforeEach
    public void setUp() {
        room = new Room();
        room.setId(1);
        room.setNumber(1);
        room.setFinaleBasePrice(50);
        room.setOccupation(0);
        room.setActive(1);

        client = new Client();
        client.setId(1);
        client.setFirstname("Jojo");
        client.setLastname("Bizarre");
        client.setMail("adventure@test.com");
        client.setAnniversaire(LocalDate.of(1980, 01, 01));
        client.setActive(1);

        reservation = new Reservation();
        reservation.setNumber(01);
        reservation.setOccupation(3);
        reservation.setTotal_price(50);
        reservation.setClient(client);
        reservation.setDate(LocalDate.of(2021, 01, 01));
        reservation.setCb_number(123456);

        List<Reservation> clientRes = new ArrayList<>();
        clientRes.add(reservation);
        client.setReservations(clientRes);

        registration = new Registration();
        registration.setId(1);
        registration.setReservation(reservation);
        registration.setRoom(room);
    }

    @Test
    void getIdShouldReturnRegistrationId() {
        assertEquals(1, registration.getId());
    }

    @Test
    void setIdShouldSetNewIdToRegistration() {
        registration.setId(2);
        assertEquals(2, registration.getId());
    }

    @Test
    void getReservationShouldReturnRegistrationReservation() {
        assertEquals(reservation, registration.getReservation());
    }

    @Test
    void setReservationShouldSetNewReservationToRegistration() {
        Reservation newReservation = new Reservation();
        registration.setReservation(newReservation);
        assertEquals(newReservation, registration.getReservation());
    }

    @Test
    void getRoomShouldReturnRegistrationRoom() {
        assertEquals(room, registration.getRoom());
    }

    @Test
    void setRoomShouldSetNewRoomToRegistration() {
        Room newRoom = new Room();
        registration.setRoom(newRoom);
        assertEquals(newRoom, registration.getReservation());
    }
}