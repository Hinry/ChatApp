package hrininlab;

import hrininlab.Entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * Класс-клиент чат-сервера. Работает в консоли. Командой с консоли shutdown посылаем сервер в оффлайн
 */
public class ChatClient implements Runnable {
    final Socket s;  // это будет сокет для сервера
    final ObjectInputStream socketReader; // буферизированный читатель с сервера
    final ObjectOutputStream socketWriter; // буферизированный писатель на сервер
    // буферизированный читатель пользовательского ввода с консоли
    MessageListener listener;
    User user;
    private List<User> usersOnline;

    /**
     * Конструктор объекта клиента
     * @param host - IP адрес или localhost или доменное имя
     * @param port - порт, на котором висит сервер
     * @throws java.io.IOException - если не смогли приконнектиться, кидается исключение, чтобы
     * предотвратить создание объекта
     */

    public ChatClient(String host, int port, MessageListener listener, User user) throws IOException {
        this.user = user;
        this.listener = listener;
        s = new Socket(host, port); // создаем сокет
        // создаем читателя и писателя в сокет
        socketWriter = new ObjectOutputStream(s.getOutputStream());
        socketReader = new ObjectInputStream((s.getInputStream()));
        // создаем читателя с консоли (от пользователя)
        new Thread(new Receiver()).start();// создаем и запускаем нить асинхронного чтения из сокета
    }

    public List<User> getUsersOnline() {
        return usersOnline;
    }

    public void setUsersOnline(List<User> usersOnline) {
        this.usersOnline = usersOnline;
    }

    /**
     * метод, где происходит главный цикл чтения сообщений с консоли и отправки на сервер
     */
    public void run() {
        /*System.out.println("Type phrase(s) (hit Enter to exit):");
        while (true) {
            String userString = null;
            try {
                userString = userInput.readLine(); // читаем строку от пользователя
            } catch (IOException ignored) {} // с консоли эксепшена не может быть в принципе, игнорируем
            //если что-то не так или пользователь просто нажал Enter...
            if (userString == null || userString.length() == 0 || s.isClosed()) {
                close(); // ...закрываем коннект.
                break; // до этого break мы не дойдем, но стоит он, чтобы компилятор не ругался
            } else { //...иначе...
                SendMessage(userString+"\r\n");
            }
        }*/
    }
    public void SendMessage(Message userString) throws IOException {
        try {
            socketWriter.writeObject(userString);
            //пишем строку пользователя
            //добавляем "новою строку", дабы readLine() сервера сработал
            socketWriter.flush();// отправляем

        } catch (IOException e) {
            e.printStackTrace();
            close(); // в любой ошибке - закрываем.
        }
    }
    public void sendSystemMessage(SystemMessage message) throws IOException {

        try {
            socketWriter.writeObject(message);
            socketWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * метод закрывает коннект и выходит из
     * программы (это единственный  выход прервать работу BufferedReader.readLine(), на ожидании пользователя)
     */
    public synchronized void close() {//метод синхронизирован, чтобы исключить двойное закрытие.
        if (!s.isClosed()) { // проверяем, что сокет не закрыт...
            try {
                s.close(); // закрываем...
                System.exit(0); // выходим!
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
    }

    public static void main(String[] args)  { // входная точка программы
        /*try {
            new ChatClient("localhost", 45000, ).run(); // Пробуем приконнетиться...
        } catch (IOException e) { // если объект не создан...
            System.out.println("Unable to connect. Server not running?"); // сообщаем...
        }*/
    }



    /**
     * Вложенный приватный класс асинхронного чтения
     */
    private class Receiver implements Runnable{


        /**
         * run() вызовется после запуска нити из конструктора клиента чата.
         */
        public void run() {
            while (!s.isClosed()) { //сходу проверяем коннект.
                Object message = null;

                try {
                    message = socketReader.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (message instanceof Message){
                    listener.addMessage((Message) message);
                }
                if(message instanceof SystemMessage){
                    listener.addSystemMessage((SystemMessage) message);
                }
            }
        }
    }
}
