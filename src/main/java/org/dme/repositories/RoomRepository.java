package org.dme.repositories;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dme.entities.Reservation;
import org.dme.entities.Room;
import org.dme.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class RoomRepository {

    static final Logger repoLogger = LogManager.getLogger("RepositoryLog");
    private static final Session session;

    static {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public static void createRoom(Room r) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            r.setActive(1);
            r.setOccupation(0);
            r.setFinaleBasePrice(50);
            r.setRegistrations(new ArrayList<>());
            session.save(r);
            tx.commit();
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            tx.rollback();
        }
    }


    public static void updateRoom(Room r) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(r);
            tx.commit();
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            tx.rollback();
        }
    }

    public static Room getRoomByNumber(int number) {
        Query<Room> query = session.createSQLQuery("select *  from room where number = :number").addEntity("room", Room.class);
        query.setParameter("number", number);
        Room room = query.getSingleResult();
        return room;
    }

    public static List<Room> readRoom() {
        try {
            Query<Room> query = session.createQuery("from Room order by number asc");
            List<Room> rooms = query.list();
            return rooms;
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }

    public static List<Room> readRoomunchived() {
        try {
            Query<Room> query = session.createQuery("from Room  where active=1 ");
            List<Room> roomsunrchived = query.list();
            return roomsunrchived;
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }

    public static void deleteRoomIfNotUsed(int number) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<Room> query = session.createQuery("from Room where number =:number");
            query.setParameter("number", number);
            Room room = query.getSingleResult();
            if (room.getOccupation() == 0) {
                System.out.println("occupation = 0 pre-delete");
                session.delete(room);
                tx.commit();
            } else {
                repoLogger.error("####  - On ne peut supprimer une chambre utilisée");
            }
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            tx.rollback();
        }
    }

    public static List<Room> getAllRoomsAndIgnoreWhere() {
        try {
            Query<Room> query = session.createSQLQuery("select *  from room").addEntity("room", Room.class);
            List<Room> rooms = query.list();
            return rooms;
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }

    public static List<Room> getAllAvailableRoomByDate(Reservation res) {
        try {
            Query<Room> query = session.createQuery("from Room order by number asc");
            List<Room> roomsAvalaibles = query.list();
            List<Room> roomsAvailableByDate = new ArrayList<>();
            for (Room r : roomsAvalaibles) {
                if (r.isAvailable(res.getDate())) {
                    roomsAvailableByDate.add(r);
                }
            }
            return roomsAvailableByDate;
        } catch (Exception e) {
            repoLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }
}
