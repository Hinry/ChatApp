package hrininlab;

import hrininlab.DAO.UserDao;
import hrininlab.Entity.User;
import hrininlab.config.UsersOnlineListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.security.access.method.P;

import javax.jws.soap.SOAPBinding;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Класс сервера. Сидит тихо на порту, принимает сообщение, создает SocketProcessor на каждое сообщение
 */
public class ChatServer {
    private ServerSocket ss; // сам сервер-сокет
    private Thread serverThread; // главная нить обработки сервер-сокета
    private int port; // порт сервер сокета.
    //очередь, где храняться все SocketProcessorы для рассылки
    BlockingQueue<SocketProcessor> q = new LinkedBlockingQueue<SocketProcessor>();
    List<User> userList = new ArrayList<>();

    /**
     * Конструктор объекта сервера
     * @param port Порт, где будем слушать входящие сообщения.
     * @throws IOException Если не удасться создать сервер-сокет, вылетит по эксепшену, объект Сервера не будет создан
     */
    public ChatServer(int port) throws IOException {
        ss = new ServerSocket(port); // создаем сервер-сокет
        this.port = port; // сохраняем порт.
    }

    /**
     * главный цикл прослушивания/ожидания коннекта.
     */
    void run() {
        serverThread = Thread.currentThread(); // со старта сохраняем нить (чтобы можно ее было interrupt())
        while (true) { //бесконечный цикл, типа...
            Socket s = getNewConn(); // получить новое соединение или фейк-соедиение
            if (serverThread.isInterrupted()) { // если это фейк-соединение, то наша нить была interrupted(),
                // надо прерваться
                break;
            } else if (s != null){ // "только если коннект успешно создан"...
                try {
                    final SocketProcessor processor = new SocketProcessor(s); // создаем сокет-процессор
                    final Thread thread = new Thread(processor); // создаем отдельную асинхронную нить чтения из сокета
                    thread.setDaemon(true); //ставим ее в демона (чтобы не ожидать ее закрытия)
                    thread.start(); //запускаем
                    q.offer(processor); //добавляем в список активных сокет-процессоров

                } //тут прикол в замысле. Если попытка создать (new SocketProcessor()) безуспешна,
                // то остальные строки обойдем, нить запускать не будем, в список не сохраним
                catch (IOException ignored) {}  // само же исключение создания коннекта нам не интересно.
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
            shutdownServer(); // если ошибка в момент приема - "гасим" сервер
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

    public BlockingQueue<SocketProcessor> getQ() {
        return q;
    }

    public void setQ(BlockingQueue<SocketProcessor> q) {
        this.q = q;
    }

    /**
     * вложенный класс асинхронной обработки одного коннекта.
     */
    private class SocketProcessor implements Runnable{
        Socket s; // наш сокет
        ObjectInputStream br; // буферизировнный читатель сокета
        ObjectOutputStream bw; // буферизированный писатель в сокет




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

            while (!s.isClosed()) { // пока сокет не закрыт...
                Object message = null;

                try {
                    message = br.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if(message instanceof Message){
                    if(((Message) message).getMessage() != null){
                        for(SocketProcessor sp : q){
                            sp.send((Message) message);
                        }
                    }else{
                        for (SocketProcessor sp : q){
                            sp.send(new Message(new User("SERVER"),"УПС"));
                        }
                    }

                }else if(message instanceof SystemMessage){
                    if(((SystemMessage) message).getMessage().equals("<---подключен")){

                        userList.add(((SystemMessage) message).getUser());
                        ((SystemMessage) message).setUserList(userList);

                        for(SocketProcessor sp : q){
                            sp.sendSysMessage((SystemMessage) message);
                        }
                    }else if (((SystemMessage) message).getMessage().equals("<---отключился")){
                        userList.remove(((SystemMessage) message).getUser());
                        ((SystemMessage) message).setUserList(userList);
                        for (SocketProcessor sp : q){
                            sp.sendSysMessage((SystemMessage) message);
                        }
                    }
                }
            }
        }

        /**
         * Метод посылает в сокет полученную строку
         * @param line строка на отсылку
         */
        public synchronized void send(Message line) {
            try {
                bw.writeObject(line);
                // пишем строку
                bw.flush();// пишем перевод строки
                // отправляем
            } catch (IOException e) {
                close(); //если глюк в момент отправки - закрываем данный сокет.
            }
        }
        public synchronized void sendSysMessage(SystemMessage message) {
            try {
                bw.writeObject(message);
                // пишем строку
                bw.flush();// пишем перевод строки
                // отправляем
            } catch (IOException e) {
                close(); //если глюк в момент отправки - закрываем данный сокет.
            }
        }

        /**
         * метод аккуратно закрывает сокет и убирает его со списка активных сокетов
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
