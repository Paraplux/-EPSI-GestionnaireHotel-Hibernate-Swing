package org.dme.entities;

import org.dme.repositories.ReservationRepository;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Where(clause = "active = 1")
public class Client {
    private int id;
    private String lastname;
    private String firstname;
    private LocalDate anniversaire;
    private String mail;
    private int active = 1;
    private List<Reservation> reservations;

    //Constructeur
    public Client() {
    }

    public Client(String lastname, String firstname, LocalDate anniversaire, String mail) {
        super();
        this.lastname = lastname;
        this.firstname = firstname;
        this.anniversaire = anniversaire;
        this.mail = mail;
    }

    //Methodes
    @Transient
    public int turnover() {
        int turnover = 0;

        List<Reservation> pastReservations = ReservationRepository.getCompletedReservationsByClient(this);
        for (Reservation r : pastReservations) {
            turnover += r.getTotal_price();
        }
        return turnover;
    }

    @Transient
    public int anticipatedIncome() {
        int turnover = 0;

        List<Reservation> furturReservation = ReservationRepository.getOngoingReservationsByClient(this);
        for (Reservation r : furturReservation) {
            turnover += r.getTotal_price();
        }
        return turnover;
    }

    @Transient
    public int projectedTurnover() {
        int turnover = 0;
        turnover = this.turnover() + this.anticipatedIncome();

        return turnover;
    }

    @Transient
    public int completedReservationNumber() {
        int reservation = 0;
        List<Reservation> completedReservation = ReservationRepository.getCompletedReservationsByClient(this);
        reservation += completedReservation.size();
        return reservation;
    }

    @Transient
    public int ongoingReservationNumber() {
        int reservation = 0;
        List<Reservation> ongoingReservation = ReservationRepository.getOngoingReservationsByClient(this);
        reservation += ongoingReservation.size();
        return reservation;
    }

    //Getter & Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


    public LocalDate getAnniversaire() {
        return anniversaire;
    }

    public void setAnniversaire(LocalDate anniversaire) {
        this.anniversaire = anniversaire;
    }


    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }


    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public void desactive() {
        this.active = 0;
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "client")
    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
