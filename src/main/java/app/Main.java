package app;


import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.utils.Populator;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        new Populator().createEntities(emf);
        ApplicationConfig.startServer(7070);

    }
}