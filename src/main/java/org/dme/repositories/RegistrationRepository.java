package org.dme.repositories;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dme.entities.Registration;
import org.dme.entities.Reservation;
import org.dme.entities.Room;
import org.dme.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class RegistrationRepository {

    static final Logger repoLogger = LogManager.getLogger("RepositoryLog");
    private static final Session session;

    static {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public static void createRegistration(Reservation res, Room r) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Registration reg = new Registration();
            reg.setRoom(r);
            reg.setReservation(res);


            //Gestion de la chambre
            Room room = session.load(Room.class, r.getId());
            room.getRegistrations().add(reg);

            room.setOccupation(res.getOccupation());

            session.update(room);

            session.save(reg);
            tx.commit();
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            tx.rollback();
        }
    }


    public static void updateRegistrationById(Registration reg) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(reg);
            tx.commit();
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            tx.rollback();
        }
    }

    public static int checkIfRoomIsOccupiedByNow(Room r) {
        try {
            List<Registration> registrations = readRegistration();

            if (registrations != null) {
                for (Registration reg : r.getRegistrations()) {
                    if (reg.getReservation().getDate().equals(LocalDate.now())) {
                        return reg.getReservation().getOccupation();
                    }
                }
            }
            return 0;
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            return -1;
        }
    }

    public static List<Registration> getRegistrationsByDate(LocalDate date) {
        try {
            List<Registration> registrations = new ArrayList<>();
            List<Registration> registrationsByDate = new ArrayList<>();

            if (readRegistration() != null) {
                registrations.addAll(readRegistration());
            }

            for (Registration reg : registrations) {
                if (reg.getReservation().getDate().equals(date)) {
                    registrationsByDate.add(reg);
                }
            }
            return registrationsByDate;
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }

    public static List<Registration> readRegistration() {
        try {
            Query<Registration> query = session.createQuery("from Registration ", Registration.class);
            List<Registration> registrations = query.list();
            return registrations;
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }

    public static Registration readRegistrationById(int id) {
        try {
            return session.load(Registration.class, id);
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }

    public static void deleteRegistretion(int idRegistration) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Registration registration = session.load(Registration.class, idRegistration);
            session.delete(registration);
            tx.commit();
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            tx.rollback();
        }
    }

    public static int getRegistrationCountByMonth(int m) {
        try {
            String month = "0" + m;
            int currentYear = LocalDate.now().getYear();
            Query<Registration> query = session.createQuery("from Registration where reservation.date LIKE '" + currentYear + "-" + month + "-%' ", Registration.class);
            List<Registration> registrations = query.list();
            System.out.println(month + " => " + registrations.size());
            return registrations.size();
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            return -1;
        }
    }
}

