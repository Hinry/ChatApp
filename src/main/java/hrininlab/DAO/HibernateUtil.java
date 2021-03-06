package hrininlab.DAO;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Created by mrhri on 29.01.2017.
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            //  hibernate.cfg.xml
            return new AnnotationConfiguration()
                    .configure()
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("ОШИБКА" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
