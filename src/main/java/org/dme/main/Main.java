package org.dme.main;

import org.dme.utils.HibernateUtil;
import org.dme.windows.LoginGUI;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {

        Session loading = HibernateUtil.getSessionFactory().openSession();
        loading.close();

        LoginGUI.load();

        //Date d'aujourd'hui
        LocalDate now = LocalDate.now();

        //L'heure délai (18h) par rapport à la date d'aujourd'hui
        LocalDateTime automatiqueRegistrationDelay = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 18, 00);

        //Crée un timer
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //Je teste si l'heure dépasse le délai
                if (automatiqueRegistrationDelay.isBefore(LocalDateTime.now())) {
                    //Après 18h
                    //Code here
                }
            }
        }, 0, 60000);
        // 60 000 => 10 minutes pour pas avoir un timer qui tourner trop rapidement

        HibernateUtil.getSessionFactory().getCurrentSession().close();

    }
}
