package hrininlab.Client;

import hrininlab.Entity.User;
import hrininlab.Interfaces.*;

import java.net.Socket;
import java.io.*;
import java.sql.SQLException;

/**
 * Класс-клиент чат-сервера. Работает в консоли. Командой с консоли shutdown посылаем сервер в оффлайн
 */
public class ChatClient  {
    private final Socket socket;  // это будет сокет для сервера
    final ObjectInputStream socketReader; // буферизированный читатель с сервера
    final ObjectOutputStream socketWriter; // буферизированный писатель на сервер

    MessageListener listener;
    LoginRequestListener loginRequestListener;

    /**
     * Конструктор объекта клиента
     * @param host - IP адрес или localhost
     * @param port - порт, на котором висит сервер
     * @param listener - слушатель для получения сообщений
     * @throws java.io.IOException - если не смогли приконнектиться, кидается исключение, чтобы
     * предотвратить создание объекта
     */

    public ChatClient(String host, int port, MessageListener listener) throws IOException {

        this.listener = listener;
        socket = new Socket(host, port);

        socketWriter = new ObjectOutputStream(socket.getOutputStream());
        socketReader = new ObjectInputStream((socket.getInputStream()));

        // создаем читателя с клиента приложения
        new Thread(new Receiver()).start(); // создаем и запускаем нить асинхронного чтения из сокета
    }
    public ChatClient(String host, int port, LoginRequestListener listener) throws IOException {

        this.loginRequestListener = listener;
        socket = new Socket(host, port);

        socketWriter = new ObjectOutputStream(socket.getOutputStream());
        socketReader = new ObjectInputStream((socket.getInputStream()));

        // создаем читателя с клиента приложения
        new Thread(new Receiver()).start(); // создаем и запускаем нить асинхронного чтения из сокета
    }


    /**
     * метод для отправки сообщения или запросов в буфер что бы не дублировать код
     * @param obj
     */
    public void send(Object obj){
        try {
            socketWriter.writeObject(obj);
            socketWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    /**
     * отправить запрос на логирование
     * @param request
     */
    public void sendLoginRequest(LoginRequest request){
        send(request);
    }

    /**
     * метод отправки простого сообщения или приватного
     * @param message
     * @throws IOException
     */
    public void SendMessage(Message message) throws IOException {
        send(message);
    }

    /**
     * метод отправки системного сообщения ( например подключился пользователь или отключился )
     * @param message
     * @throws IOException
     */
    public void sendSystemMessage(SystemMessage message) throws IOException {
        send(message);
    }

    /**
     * метод отправки сообщения с запросом (Добавить или удалить пользователя из списка контактов,
     * или при старте получить свой список контактов)
     * @param message
     * @throws IOException
     */
    public void sendSystemRequestMessage(SystemRequestMessage message) throws IOException {
        send(message);
    }

    /**
     * метод закрывающий соединение сокета
     */
    public synchronized void close() {  //метод синхронизирован, чтобы исключить двойное закрытие.
        if (!socket.isClosed()) { // проверяем, что сокет не закрыт
            try {
                socket.close(); // закрываем
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
    }

    public static void main(String[] args)  {
        /*try {
            new ChatClient("localhost", 45000, ).run(); // Пробуем приконнетиться
        } catch (IOException e) { // если объект не создан
            System.out.println("Ошибка!"); // сообщаем
        }*/
    }


    /**
     * вложеный приватный класс асинхронного чтения
     */
    private class Receiver implements Runnable{

        /**
         * run() вызовется после создания экземпляра класа ChatClient из его конструктора.
         */
        public void run() {
            while (!socket.isClosed()) { // проверяем коннект.
                Object message = null;  //создаем пустой обьект

                try {
                    message = socketReader.readObject();         //пробуем прочесть
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                //проверяем какому классу принадлежит прочитаный обьект и отправляем
                if (message instanceof Message){
                    listener.addMessage((Message) message);
                }
                if(message instanceof SystemMessage){
                    listener.addSystemMessage((SystemMessage) message);
                }
                if(message instanceof  SystemRequestMessage){
                    listener.addSystemRequestMessage((SystemRequestMessage) message);
                }
                if (message instanceof LoginRequest){
                    try {
                        loginRequestListener.addLoginRequest((LoginRequest) message);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
