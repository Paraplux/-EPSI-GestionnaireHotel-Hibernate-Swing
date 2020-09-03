package org.dme.entities;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Where(clause = "active = 1")
public class Room {

    private int id;
    private int number;
    private int finaleBasePrice;
    private int occupation;
    private int active;
    private List<Registration> registrations;

    public Room() {
    }

    public Room(int number, int finaleBasePrice, int occupation) {
        this.number = number;
        this.finaleBasePrice = finaleBasePrice;
        this.occupation = occupation;
        this.active = 1;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(unique = true)
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    public int getFinaleBasePrice() {
        return finaleBasePrice;
    }

    public void setFinaleBasePrice(int finaleBasePrice) {
        this.finaleBasePrice = finaleBasePrice;
    }

    public int getOccupation() {
        return occupation;
    }

    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }


    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(mappedBy = "room")
    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }

    public boolean isAvailable(LocalDate date) {
        boolean available = true;
        for (Registration r : this.registrations)
            if (r.getReservation().getDate().isEqual(date)) {
                available = false;
            }
        return available;
    }
}
