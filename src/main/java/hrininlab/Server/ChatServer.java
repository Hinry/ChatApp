package hrininlab.Server;


import hrininlab.DAO.UserDao;
import hrininlab.Entity.ContactList;
import hrininlab.Entity.User;
import hrininlab.Interfaces.Message;
import hrininlab.Interfaces.SystemMessage;
import hrininlab.Interfaces.SystemRequestMessage;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Класс сервера. Сидит порту, принимает сообщение, создает SocketProcessor на каждое сообщение
 */
public class ChatServer {
    private ServerSocket ss;
    private Thread serverThread;
    private int port;
    //очередь, где храняться все SocketProcessorы для рассылки
    BlockingQueue<SocketProcessor> q = new LinkedBlockingQueue<SocketProcessor>();
    private LinkedList<User> userList = new LinkedList<>();
    private UserDao dao = new UserDao();

    /**
     * Конструктор объекта сервера
     * @param port Порт, где будем слушать входящие сообщения.
     * @throws IOException Если не удасться создать сервер-сокет, вылетит по эксепшену, объект Сервера не будет создан
     */
    public ChatServer(int port) throws IOException {
        ss = new ServerSocket(port);
        this.port = port;
    }

    /**
     * главный цикл прослушивания/ожидания коннекта.
     */
    void run() {
        serverThread = Thread.currentThread();
        while (true) {
            Socket s = getNewConn();
            if (serverThread.isInterrupted()) {
                break;
            } else if (s != null){
                try {
                    final SocketProcessor processor = new SocketProcessor(s);
                    final Thread thread = new Thread(processor);
                    thread.setDaemon(true);
                    thread.start();

                }
                catch (IOException ignored) {}
            }
        }
    }

    /**
     * Ожидает новое подключение.
     * @return Сокет нового подключения
     */
    private Socket getNewConn() {
        Socket s = null;
        try {
            s = ss.accept();
        } catch (IOException e) {
            shutdownServer();
        }
        return s;
    }

    /**
     * метод "глушения" сервера
     */
    private synchronized void shutdownServer() {
        // обрабатываем список рабочих коннектов, закрываем каждый
        for (SocketProcessor s: q) {
            s.close();
        }
        if (!ss.isClosed()) {
            try {
                ss.close();
            } catch (IOException ignored) {}
        }
    }

    /**
     * входная точка программы
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new ChatServer(45000).run(); // если сервер не создался, программа
        // вылетит по эксепшену, и метод run() не запуститься

    }

    /**
     * вложенный класс асинхронной обработки одного коннекта.
     */
    private class SocketProcessor implements Runnable{
        Socket s; // наш сокет
        ObjectInputStream br; // буферизировнный читатель сокета
        ObjectOutputStream bw; // буферизированный писатель в сокет
        User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        /**
         * Сохраняем сокет, пробуем создать читателя и писателя. Если не получается - вылетаем без создания объекта
         * @param socketParam сокет
         * @throws IOException Если ошибка в создании br || bw
         */
        SocketProcessor(Socket socketParam) throws IOException {
            s = socketParam;
            br = new ObjectInputStream(s.getInputStream());
            bw = new ObjectOutputStream(s.getOutputStream());
        }


        /**
         * Главный цикл чтения сообщений/рассылки
         */
        public void run() {

            while (!s.isClosed()) {
                Object message = null;

                try {
                    message = br.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if(message instanceof Message){

                    if(((Message) message).getMessage() != null){
                        if(((Message) message).getUserprivate() != null){
                            for(SocketProcessor sp : q) {
                                if (sp.getUser().getLogin().equals(((Message) message).getUserprivate().getLogin()) || sp.getUser().getLogin().equals(((Message) message).getUser().getLogin())) {
                                    sp.sendMessage((Message) message);
                                }
                            }
                        }else {
                            for (SocketProcessor sp : q){
                                sp.sendMessage((Message) message);
                            }
                        }
                    }else{
                        for (SocketProcessor sp : q){
                            sp.sendMessage(new Message(new User("SERVER"),"УПС"));
                        }
                    }

                }
                if(message instanceof SystemMessage){
                    if(((SystemMessage) message).getMessage().equals("<---подключен")){

                        ((SystemMessage) message).getUser().setOnline(true);
                        dao.update_User(((SystemMessage) message).getUser());

                        userList.add(((SystemMessage) message).getUser());
                        ((SystemMessage) message).setUserList(userList);
                        this.user = ((SystemMessage) message).getUser();
                        q.add(this);
                        for(SocketProcessor sp : q){
                            sp.sendSysMessage((SystemMessage) message);
                            System.out.println(sp.getUser().getFirst_name());
                        }

                    }else if (((SystemMessage) message).getMessage().equals("<---отключился")){
                        ((SystemMessage) message).getUser().setOnline(false);
                        dao.update_User(((SystemMessage) message).getUser());
                        userList.remove(((SystemMessage) message).getUser());
                        ((SystemMessage) message).setUserList(userList);
                        this.close();
                        for (SocketProcessor sp : q){
                            sp.sendSysMessage((SystemMessage) message);
                        }
                    }
                }
                if(message instanceof SystemRequestMessage){
                    if(((SystemRequestMessage) message).getMessage().equals("добавить")){


                        if(dao.add_contact_to_User(((SystemRequestMessage) message).getUser(),((SystemRequestMessage) message).getUser2())){
                            ((SystemRequestMessage) message).setMessage("Добавлен");
                            List<User> list = new ArrayList<>();
                            User user = dao.get_user_by_login(((SystemRequestMessage) message).getUser().getLogin());
                            for(ContactList e : user.getContactList()){
                                list.add(e.getUser_id());
                            }
                            ((SystemRequestMessage) message).setUserlist(list);

                            for(SocketProcessor sp : q){
                                if(sp.getUser().getLogin().equals(((SystemRequestMessage) message).getUser().getLogin())){
                                    sp.sendSysReqMessage((SystemRequestMessage) message);
                                }
                            }
                        }else {
                            ((SystemRequestMessage) message).setMessage("Уже добавлен");
                            List<User> list = new ArrayList<>();

                            User user = dao.get_user_by_login(((SystemRequestMessage) message).getUser().getLogin());
                            for(ContactList e : user.getContactList()){
                                list.add(e.getUser_id());
                            }
                            ((SystemRequestMessage) message).setUserlist(list);
                            for(SocketProcessor sp : q){
                                if(sp.getUser().getLogin().equals(((SystemRequestMessage) message).getUser().getLogin())){
                                    sp.sendSysReqMessage((SystemRequestMessage) message);
                                }
                            }
                        }
                    }
                    if(((SystemRequestMessage) message).getMessage().equals("удалить")){

                        if(dao.deleteUserFromContactList(((SystemRequestMessage) message).getUser(),((SystemRequestMessage) message).getUser2())){
                            ((SystemRequestMessage) message).setMessage("Удалён");
                            User user = dao.get_user_by_login(((SystemRequestMessage) message).getUser().getLogin());
                            List<User> list = new ArrayList<>();
                            for(ContactList e : user.getContactList()){
                                list.add(e.getUser_id());
                            }
                            ((SystemRequestMessage) message).setUserlist(list);

                            for(SocketProcessor sp : q){
                                if(sp.getUser().getLogin().equals(((SystemRequestMessage) message).getUser().getLogin())){
                                    sp.sendSysReqMessage((SystemRequestMessage) message);
                                }
                            }
                        }else {
                            ((SystemRequestMessage) message).setMessage("Уже удалён");
                            List<User> list = new ArrayList<>();
                            User user = dao.get_user_by_login(((SystemRequestMessage) message).getUser().getLogin());
                            for(ContactList e : user.getContactList()){
                                list.add(e.getUser_id());
                            }
                            ((SystemRequestMessage) message).setUserlist(list);
                            for(SocketProcessor sp : q){
                                if(sp.getUser().getLogin().equals(((SystemRequestMessage) message).getUser().getLogin())){
                                    sp.sendSysReqMessage((SystemRequestMessage) message);
                                }
                            }
                        }


                    }
                    if (((SystemRequestMessage) message).getMessage().equals("start")){
                        List<User> list = new ArrayList<>();
                        for(ContactList e : ((SystemRequestMessage) message).getUser().getContactList()){
                            list.add(e.getUser_id());
                        }
                        ((SystemRequestMessage) message).setUserlist(list);
                        ((SystemRequestMessage) message).setMessage(" ");
                        for(SocketProcessor sp : q){
                            if(sp.getUser().getLogin().equals(((SystemRequestMessage) message).getUser().getLogin())){
                                sp.sendSysReqMessage((SystemRequestMessage) message);
                            }
                        }
                    }


                }
            }
        }


        public void send(Object object){
            try {
                bw.reset();
                bw.writeObject(object);
                // пишем строку
                bw.flush();// пишем перевод строки
                // отправляем
            } catch (IOException e) {
                close(); //если глюк в момент отправки - закрываем данный сокет.
            }
        }
        /**
         * Метод посылает в сокет полученную строку
         * @param line строка на отсылку
         */
        public synchronized void sendMessage(Message line) {
            send(line);

        }
        public synchronized void sendSysMessage(SystemMessage message) {
            send(message);
        }
        private synchronized void sendSysReqMessage(SystemRequestMessage message) {
            send(message);
        }

        /**
         * метод закрывает сокет и убирает его со списка активных сокетов
         */
        public synchronized void close() {
            q.remove(this); //убираем из списка
            if (!s.isClosed()) {
                try {
                    s.close(); // закрываем
                } catch (IOException ignored) {}
            }
        }

        /**
         * финализатор просто на всякий случай.
         * @throws Throwable
         */
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            close();
        }
    }
}
