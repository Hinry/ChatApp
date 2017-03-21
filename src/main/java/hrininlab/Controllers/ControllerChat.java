package hrininlab.Controllers;

import hrininlab.*;
import hrininlab.Client.ChatClient;
import hrininlab.DAO.UserDao;
import hrininlab.Entity.User;
import hrininlab.Interfaces.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


/**
 * Created by mrhri on 02.12.2016.
 */
public class ControllerChat implements MessageListener {


    @FXML
    private TableView<User> table_users;
    @FXML
    private TableView<User> tableContacts;

    @FXML
    private TableColumn<User, String> users_online;
    @FXML
    private TableColumn<User, String> users_contacts;

    @FXML
    private Button btnInNetwork;
    @FXML
    private Button btnShowContacts;

    @FXML
    private Label userLabel;

    @FXML
    public TextField input;

    @FXML
    private Label labelFIO;

    @FXML
    Label error;

    @FXML
    private TextArea textArea;

    private Start start;
    private ChatClient chatClient;
    private Message message;
    private SystemMessage systemMessage;
    private SystemRequestMessage requestMessage;
    private User user;
    private ObservableList<User> usersOnlineList = FXCollections.observableArrayList();
    private ObservableList<User> usersContactList = FXCollections.observableArrayList();

    /**
     *
     * @param mainApp
     * @throws IOException
     * @throws SQLException
     */
    public void setMainApp(Start mainApp) throws IOException, SQLException {

        this.user = mainApp.getUser();
        start = mainApp;
        chatClient = new ChatClient("localhost", 45000, this);

        userLabel.setText(user.getLogin());
        table_users.setItems(usersOnlineList);
        tableContacts.setItems(usersContactList);
        sendStartMessage();
        sendSysRequestMessageStart();

    }


    /**
     * отправить простое сообщение
     */
    @FXML
    public void sendMessage(){
        message = new Message(start.getUser(),input.getText());

        Platform.runLater(() -> {
            try {
                chatClient.SendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * отправить приватное сообщение
     */
    @FXML
    public void sendPrivateMessage(){
        message = new Message(start.getUser(),input.getText());
        if (table_users.getSelectionModel().selectedItemProperty().get() != null) {
            message.setUserprivate(table_users.getSelectionModel().selectedItemProperty().get());
            Platform.runLater(() -> {
                try {
                    chatClient.SendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }else {
            error.setText("Выбери получателя");
        }

    }

    /**
     * запрос на добавление пользователя в список контактов
     */
    @FXML
    public void addUserToContactList(){
        requestMessage = new SystemRequestMessage();
        requestMessage.setUser(user);
        requestMessage.setUser2(table_users.getSelectionModel().selectedItemProperty().get());
        requestMessage.setMessage("добавить");
        Platform.runLater(() -> {
            try {
                chatClient.sendSystemRequestMessage(requestMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * запрос на удаление пользователя из списка контактов
     */
    @FXML
    public void deleteUserFromContactList(){
        requestMessage = new SystemRequestMessage();
        requestMessage.setUser(user);
        requestMessage.setUser2(tableContacts.getSelectionModel().selectedItemProperty().get());
        requestMessage.setMessage("удалить");
        Platform.runLater(() -> {
            try {
                chatClient.sendSystemRequestMessage(requestMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * при старте отправить запрос на сервер что бы получить список контактов
     */
    public void sendSysRequestMessageStart(){
        requestMessage = new SystemRequestMessage();
        requestMessage.setUser(user);
        requestMessage.setMessage("start");
        Platform.runLater(() -> {
            try {
                chatClient.sendSystemRequestMessage(requestMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * показать таблицу с пользователями в сети
     */
    @FXML
    public void showInNetwork(){
        btnInNetwork.setStyle("-fx-background-color: #3d3d3d; -fx-background-radius: 0px");
        btnShowContacts.setStyle("-fx-background-color: #1d1d1d; -fx-background-radius: 0px");
        table_users.toFront();
    }

    /**
     * показать таблицу с контактами
     */
    @FXML
    void showContacts(){
        tableContacts.refresh();
        btnInNetwork.setStyle("-fx-background-color: #1d1d1d; -fx-background-radius: 0px");
        btnShowContacts.setStyle("-fx-background-color: #3d3d3d; -fx-background-radius: 0px");
        tableContacts.toFront();
    }


    /**
     * при старте отправить всем сообщение что пользователь подключен к чату
     */
    public void sendStartMessage(){
        systemMessage = new SystemMessage(start.getUser(),"<---подключен");

        Platform.runLater(() -> {
            try {
                chatClient.sendSystemMessage(systemMessage);
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    /**
     * закрыть приложение и всех уведомить о выходе клиента с чата
     */
    @FXML
    public void closeApp(){

        systemMessage = new SystemMessage(start.getUser(),"<---отключился");
        try {
            chatClient.sendSystemMessage(systemMessage);
            chatClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    /**
     * выход из профиля (возврат в меню с формой входа)
     */
    @FXML
    public void exitProfile(){

        start.getUser().setOnline(false);
        try {
            systemMessage = new SystemMessage(start.getUser(),"<---отключился");
            try {
                chatClient.sendSystemMessage(systemMessage);
                chatClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            start.initRootLayout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * метод для слушателя с таблицы для указания Имени и Фамили пользователя
     * @param user
     */
    public void showPersonDetails(User user){
        if(user != null){
            labelFIO.setText(user.getFirst_name()+" "+user.getLast_name());
            error.setText(" ");
        }
    }

    @FXML
    private void initialize() {
        // Инициализация таблиц и слушатели.

        users_online.setCellValueFactory(cellData -> cellData.getValue().getPropertyLogin());
        users_contacts.setCellValueFactory(cellData -> cellData.getValue().getPropertyLogin());

        table_users.getItems().addListener((ListChangeListener<User>)
                c -> users_online.setCellValueFactory(cellData -> cellData.getValue().getPropertyLogin()));

        tableContacts.getItems().addListener((ListChangeListener<User>)
                c -> users_contacts.setCellValueFactory(cellData -> cellData.getValue().getPropertyLogin()));

        // Слушаем изменения выбора, и при изменении отображаем
        // дополнительную информацию о Юзере.
        table_users.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue)
        );
        tableContacts.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue)
        );

    }

    @Override
    public void addMessage(Message str){

        Platform.runLater(()->textArea.appendText(str.getUser().getLogin()+": "+str.getMessage()+"\n"));

    }

    @Override
    public void addSystemMessage(SystemMessage message) {

        Platform.runLater(()->textArea.appendText(message.getUser().getLogin()+": "+message.getMessage()+"\n"));
        usersOnlineList.clear();
        usersOnlineList.addAll(message.getUserList());

    }

    @Override
    public void addSystemRequestMessage(SystemRequestMessage requestMessage) {
        Platform.runLater(()->error.setText(requestMessage.getMessage()));
        usersContactList.clear();
        usersContactList.addAll(requestMessage.getUserlist());

    }


    public User getPolzovatel() {
        return user;
    }

    public Label getUser() {
        return userLabel;
    }

    public void setUser(String user) {
        this.userLabel.setText(user);
    }

}
