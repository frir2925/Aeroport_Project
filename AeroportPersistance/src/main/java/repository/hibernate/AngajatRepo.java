package repository.hibernate;
import model.Angajat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import repository.JdbcUtils;
import repository.RepoAngajat;
import utils.HibernateSession;

import javax.persistence.Query;
import java.util.List;
import java.util.Properties;

public class AngajatRepo implements RepoAngajat {
    private JdbcUtils dbUtils;
    private SessionFactory sessionFactory;

    public AngajatRepo(Properties props, SessionFactory sessionFactory){
        dbUtils = new JdbcUtils(props);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Angajat findOneByUsername(String username){
       Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Angajat angajat = session.createQuery("from Angajat where username = :username", Angajat.class)
                    .setParameter("username", username)
                    .setMaxResults(1).uniqueResult();
            transaction.commit();
            return angajat;
        } catch (RuntimeException e){
            if (transaction != null)
                transaction.rollback();
            return null;
        }

    }

    @Override
    public Angajat findOne(Long id) {
        return null;
    }

    @Override
    public Iterable<Angajat> findAll() {
        return null;
    }

    @Override
    public void save(Angajat entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Long id, Angajat entity) {

    }


}
