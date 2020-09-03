package org.dme.entities;


import org.dme.repositories.ReservationRepository;
import org.dme.repositories.RoomRepository;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "reservations")
public class Reservation {

    private int number;
    private int occupation;
    private LocalDate date;
    private double total_price;
    private int cb_number;

    private Client client;
    private Registration registration;

    private Date updated;

    //Constructeur
    public Reservation() {
    }

    public Reservation(Client client, int occupation, LocalDate date, int cb) {
        this.client = client;
        this.occupation = occupation;
        this.date = date;
        this.total_price = this.calculPrice(date);
        this.cb_number = cb;
    }

    public double calculPrice(LocalDate date) {
        double defaultPrice = 50;
        double hotelCapacity = RoomRepository.readRoomunchived().size(); //MaxRoom, 10 by Default
        double nbReservation = ReservationRepository.getReservationByDate(date).size();
        double occupationRate = 1 + (nbReservation / hotelCapacity);

        if (occupationRate > 2.1) {
            occupationRate = 2.1;
        }

        double price = defaultPrice * occupationRate;
        return price;
    }

    //Getter & Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Access(AccessType.PROPERTY)
    @ManyToOne
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getOccupation() {
        return occupation;
    }

    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public int getCb_number() {
        return cb_number;
    }

    public void setCb_number(int cb_number) {
        this.cb_number = cb_number;
    }

    @Access(AccessType.PROPERTY)
    @OneToOne
    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    @Version
    @Type(type = "dbtimestamp")
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
