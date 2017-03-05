package hrininlab.DAO;

import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Component;
import hrininlab.Entity.ContactList;
import hrininlab.Entity.User;
import hrininlab.Entity.UserRole;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrhri on 29.01.2017.
 */
@Component
public class UserDao {

    public UserDao() {
    }

    public void add_User(User user){

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<UserRole> findAll(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        Criteria crit = session.createCriteria(UserRole.class);
        crit.addOrder(Order.asc("type"));
        return (List<UserRole>)crit.list();
    }

    @SuppressWarnings("unchecked")
    public UserRole findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        UserRole role =  (UserRole) session.load(UserRole.class, id);

        return role;
    }

    public void delete_User(int user_id) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            User user = (User) session.load(User.class, user_id);
            session.delete(user);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
    }
    public void update_User(User user){

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        }catch (RuntimeException ex){
            if(transaction != null){
                transaction.rollback();
            }
            ex.printStackTrace();
        }finally {
            session.flush();
            session.close();
        }
    }

    public void add_contact_to_User(int user_id, int contact_id){

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            //ВЫТЯГИВАЕМ ЮЗЕРА
            User user = (User) session.load(User.class, user_id);
            User contact = (User) session.load(User.class, contact_id);
            ContactList list = new ContactList();
            list.setUser_id(contact);

            user.getContactList().add(list);
            session.update(user);
            session.beginTransaction().commit();
        }catch (RuntimeException ex){
            if (transaction != null){
                transaction.rollback();
            }
            ex.printStackTrace();
        }finally {
            session.flush();
            session.close();
        }
    }

    public User get_user_by_id(int userid) {

        User user = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            String queryString = "from User where id = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", userid);
            user = (User) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return user;
    }

    public User get_user_by_login(String login) {

        User user = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            String queryString = "from User where login = :login";
            Query query = session.createQuery(queryString);
            query.setString("login", login);
            user = (User) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return user;
    }

    public ObservableList<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            users = session.createQuery("from User where online=true").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        ObservableList<User> list = FXCollections.observableArrayList();
        list.addAll(users);
        return list;
    }

    public boolean isUserUnique(String login) {
        User user = get_user_by_login(login);
        return (user == null);
    }

    public static void main(String[] args){

        UserDao dao = new UserDao();
        User user = dao.get_user_by_login("admin");

        user.setOnline(true);

        dao.update_User(user);
    }
}
