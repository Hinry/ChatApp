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
import java.util.Iterator;
import java.util.List;

/**
 * Created by mrhri on 29.01.2017.
 */
@Component
public class UserDao {

    public UserDao() {
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

    public boolean add_contact_to_User(User user1, User contact){

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        ContactList contactList = null;
        User user = null;
        try {

            transaction = session.beginTransaction();
            //ВЫТЯГИВАЕМ ЮЗЕРА

            user = (User) session.load(User.class, user1.getId());


            String queryString2 = "from ContactList where owner_id = :id1 and user_id = :id2";
            Query query2 = session.createQuery(queryString2);
            query2.setInteger("id1", user1.getId());
            query2.setInteger("id2", contact.getId());
            contactList = (ContactList) query2.uniqueResult();
            if(contactList!=null){
                return false;
            }else {
                ContactList list = new ContactList();
                list.setUser_id(contact);
                user.getContactList().add(list);
                session.beginTransaction().commit();
                session.clear();
                return true;
            }


        }catch (RuntimeException ex){
            if (transaction != null){
                transaction.rollback();
            }
            ex.printStackTrace();
            return false;
        }finally {
            session.flush();
            session.close();
        }

    }

    public boolean deleteUserFromContactList(User user1, User user2){
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        ContactList contactList = null;
        User user = null;
        try{
            transaction = session.beginTransaction();
            String queryString = "from User where id = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", user1.getId());
            user = (User) query.uniqueResult();

            String queryString2 = "from ContactList where owner_id = :id1 and user_id = :id2";
            Query query2 = session.createQuery(queryString2);
            query2.setInteger("id1", user1.getId());
            query2.setInteger("id2", user2.getId());
            contactList = (ContactList) query2.uniqueResult();

            if(contactList!=null){
                user.getContactList().remove(contactList);
                session.delete(contactList);
                session.getTransaction().commit();
                session.clear();
                return true;
            }else {
                return false;
            }

        }catch (RuntimeException ex){
            if (transaction != null){
                transaction.rollback();
            }
            ex.printStackTrace();
            return false;
        }finally {
            session.flush();
            session.clear();
            session.close();
        }
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

    public List<User> getUsersOnline() {
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

        return users;
    }


    public static void main(String[] args){

    }
}
