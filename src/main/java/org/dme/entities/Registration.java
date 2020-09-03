package org.dme.entities;

import javax.persistence.*;

@Entity
public class Registration {
    private int id;
    private Reservation reservation;
    private Room room;

    public Registration() {
    }

    public Registration(Reservation reservation, Room room) {
        this.reservation = reservation;
        this.room = room;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToOne
    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @Access(AccessType.PROPERTY)
    @ManyToOne
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
