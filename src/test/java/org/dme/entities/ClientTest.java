package org.dme.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class ClientTest {

    private Client client;
    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        client = new Client();
        client.setId(1);
        client.setFirstname("Jojo");
        client.setLastname("Bizarre");
        client.setMail("adventure@test.com");
        client.setAnniversaire(LocalDate.of(1980, 01, 01));
        client.setActive(1);

        reservation = new Reservation();
        reservation.setNumber(01);
        reservation.setTotal_price(50);
        reservation.setClient(client);
        reservation.setDate(LocalDate.of(2021, 01, 01));
        reservation.setCb_number(123456);

        List<Reservation> clientRes = new ArrayList<>();
        clientRes.add(reservation);
        client.setReservations(clientRes);
    }


    @Test
    void getIdShoudReturnClientId() {
        assertEquals(1, client.getId());
    }

    @Test
    void setIdShouldSetNewIdToClient() {
        client.setId(2);
        assertEquals(2, client.getId());
    }

    @Test
    void getLastnameShouldReturnClientLastname() {
        assertEquals("Bizarre", client.getLastname());
    }

    @Test
    void setLastnameShouldSetNewLastnameToClient() {
        client.setLastname("Dupont");
        assertEquals("Dupont", client.getLastname());
    }

    @Test
    void getFirstnameShouldReturnClientFirstname() {
        assertEquals("Jojo", client.getFirstname());
    }

    @Test
    void setFirstnameShouldSetNewFirstnameToClient() {
        client.setFirstname("Jean");
        assertEquals("Jean", client.getFirstname());
    }

    @Test
    void getAnniversaireShoudReturnClientAnniversaire() {
        LocalDate date = LocalDate.of(1980, 01, 01);
        assertEquals(date, client.getAnniversaire());
    }

    @Test
    void setAnniversaireShouldSetNewAnniversaireToClient() {
        LocalDate date = LocalDate.of(1990, 01, 01);
        client.setAnniversaire(date);
        assertEquals(date, client.getAnniversaire());
    }

    @Test
    void getMailShouldReturnClientMail() {
        assertEquals("adventure@test.com", client.getMail());
    }

    @Test
    void setMailShouldSetNewMailToClient() {
        client.setMail("jeandupont@test.com");
        assertEquals("jeandupont@test.com", client.getMail());
    }

    @Test
    void getActiveShouldReturnIfAClientIsActive() {
        assertEquals(1, client.getActive());
    }

    @Test
    void setActiveShouldChangeIfAClientIsActive() {
        client.setActive(0);
        assertEquals(0, client.getActive());
    }

    @Test
    void desactiveShouldChangeActiveTo0() {
        client.desactive();
        assertEquals(0, client.getActive());
    }

    @Test
    void getReservationsShouldReturnListOfClientReservations() {
        List reservations = new ArrayList();
        reservations.add(reservation);
        assertIterableEquals(reservations, client.getReservations());
    }

    @Test
    void setReservations() {
        List reservations = new ArrayList();
        reservations.add(reservation);
        assertIterableEquals(reservations, client.getReservations());
    }

}