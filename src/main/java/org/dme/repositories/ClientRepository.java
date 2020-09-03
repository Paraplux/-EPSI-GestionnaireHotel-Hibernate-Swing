package org.dme.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dme.entities.Client;
import org.dme.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class ClientRepository {

    static final Logger repoLogger = LogManager.getLogger("RepositoryLog");
    static final Logger fileLogger = LogManager.getLogger("FileOutputLog");
    private static final Session session;

    static {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public static List<Client> getAllClient() {
        try {
            Query<Client> query = session.createQuery("from Client", Client.class);
            List<Client> clients = query.list();
            return clients;
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }

    public static Client getClientById(int id) {
        try {
            return session.load(Client.class, id);
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }

    public static void addClient(Client c) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(c);
            tx.commit();
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            tx.rollback();
        }
    }

    public static void updateClient(int id, Client nc) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Client c = session.load(Client.class, id);
            c.setAnniversaire(nc.getAnniversaire());
            c.setFirstname(nc.getFirstname());
            c.setLastname(nc.getLastname());
            c.setMail(nc.getMail());
            session.update(c);
            tx.commit();
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            tx.rollback();
        }
    }

    public static void removeClient(int id) {
        Transaction tx = null;
        Client c = session.load(Client.class, id);
        try {
            tx = session.beginTransaction();
            if (c.completedReservationNumber() > 0) {
                c.desactive();
                session.update(c);

            } else {
                session.remove(c);
            }
            tx.commit();
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            tx.rollback();
        }
    }

    public static void printClients() {
        LocalDate date = LocalDate.now();

        try {
            PrintWriter out;
            out = new PrintWriter("Client le " + date + ".csv");
            out.write("Fiche client enregistrée le" + date + "\n");
            out.write("Prenom \t nom \t mail \t anniversaire \t Reservation Complétée  \t Reservation en cours \t CA \t Rentrée d'argent prévu \t CA prévisionnelle  \n");
            for (Client c : ClientRepository.getAllClient()) {
                out.write(c.getFirstname() + "\t" + c.getLastname() + "\t" + c.getMail() + "\t" + c.getAnniversaire() + "\t" + c.completedReservationNumber() + "\t" + c.ongoingReservationNumber() + "\t" + c.turnover() + " \t" + c.anticipatedIncome() + " \t" + c.projectedTurnover() + " \n");
            }
            out.close();
        } catch (Exception e) {
            fileLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
        }
    }

    public static List<Client> searchClient(String prenom, String nom, String mail) {
        try {
            Query<Client> query = session.createQuery("from Client where firstname like :prenom "
                    + "                                    AND lastname like :nom "
                    + "                                    AND mail like :mail", Client.class);
            query.setParameter("prenom", prenom + "%");
            query.setParameter("nom", nom + "%");
            query.setParameter("mail", mail + "%");

            List<Client> clients = query.list();
            return clients;
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }
}
