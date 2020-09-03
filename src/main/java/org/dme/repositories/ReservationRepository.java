package org.dme.repositories;

import org.dme.entities.Client;
import org.dme.entities.Reservation;
import org.dme.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class ReservationRepository {
    private static final Session session;

    static {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public static List<Reservation> getAllReservation() {
        Query<Reservation> query = session.createQuery("from Reservation", Reservation.class);
        List<Reservation> reservations = query.list();
        return reservations;
    }

    public static Reservation getReservationById(int id) {
        return session.load(Reservation.class, id);
    }

    public static List<Reservation> getReservationByDate(LocalDate date) {
        Query<Reservation> query = session.createQuery("from Reservation where date ='" + date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth() + "' ", Reservation.class);
        List<Reservation> reservations = query.list();
        return reservations;
    }

    public static List<Reservation> getReservationByClient(Client c) {
        Query<Reservation> query = session.createQuery("from Reservation where client_id ='" + c.getId() + "' ", Reservation.class);
        List<Reservation> reservations = query.list();
        return reservations;
    }

    public static List<Reservation> getOngoingReservationsByClient(Client c) {
        LocalDate date = LocalDate.now();
        Query<Reservation> query = session.createQuery("from Reservation where client_id='" + c.getId() + "' AND date >='" + date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth() + "' ", Reservation.class);
        List<Reservation> reservations = query.list();
        return reservations;
    }

    public static List<Reservation> getCompletedReservationsByClient(Client c) {
        LocalDate date = LocalDate.now();
        Query<Reservation> query = session.createQuery("from Reservation where client_id='" + c.getId() + "' AND date <'" + date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth() + "' ", Reservation.class);
        List<Reservation> reservations = query.list();
        return reservations;
    }


    public static void addReservation(Reservation r) {
        Transaction tx = session.beginTransaction();
        session.save(r);
        tx.commit();
    }

    public static void updateReservation(int id, Reservation nr) {
        Transaction tx = session.beginTransaction();
        Reservation r = session.load(Reservation.class, id);
        r.setClient(nr.getClient());
        r.setCb_number(nr.getCb_number());
        r.setOccupation(nr.getOccupation());
        r.setTotal_price(nr.getTotal_price());
        session.update(r);
        tx.commit();
    }

    public static void removeReservation(int id) {
        Reservation r = session.load(Reservation.class, id);
        Transaction tx = session.beginTransaction();
        session.remove(r);
        tx.commit();
    }


}
