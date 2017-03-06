package hrininlab;

import hrininlab.DAO.UserDao;
import hrininlab.Entity.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by mrhri on 02.12.2016.
 */
public class ControllerChat implements MessageListener{


    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    private UserDao dao = new UserDao();


    @FXML
    private TableView<User> table_users;
    @FXML
    private TableColumn<User, String> users_online;

    @FXML
    private Label countContacts;

    @FXML
    private Label user;
    @FXML
    private TextField input;

    private Stage dialogStage;
    private String log;
    @FXML
    private TextArea textArea;
    Start start;
    ChatClient chatClient;
    Message message;
    SystemMessage systemMessage;
    User polzovatel;
    ObservableList<User> usersOnlineList = FXCollections.observableArrayList();

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

    public Label getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user.setText(user);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(Start mainApp) throws IOException, SQLException {

        this.polzovatel = mainApp.getUser();
        start = mainApp;
        chatClient = new ChatClient("localhost", 45000, this, polzovatel);

        user.setText(polzovatel.getLogin());
        table_users.setItems(usersOnlineList);
        sendStartMessage();

    }
    public void sendOff(){

    }

    @FXML
    public void closeApp(){

        start.getUser().setOnline(false);
        dao.update_User(start.getUser());
        systemMessage = new SystemMessage(start.getUser(),"<---отключился");
        try {
            chatClient.sendSystemMessage(systemMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    @FXML
    private void initialize() {
        // Инициализация таблицы адресатов с двумя столбцами.

        users_online.setCellValueFactory(cellData -> cellData.getValue().getPropertyLogin());
        table_users.getItems().addListener((ListChangeListener<User>)
                c -> users_online.setCellValueFactory(cellData -> cellData.getValue().getPropertyLogin()));

        // Слушаем изменения выбора, и при изменении отображаем
        // дополнительную информацию об адресате.
        /*table_users.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));*/

    }

    public String getTextArea(){
        return textArea.getText();
    }

    @Override
    public void addMessage(Message str){
        Platform.runLater(()->textArea.appendText(str.getUser().getLogin()+": "+str.getMessage()+"\n"));

    }

    @Override
    public void addSystemMessage(SystemMessage message) {
        Platform.runLater(()->textArea.appendText(message.getUser().getLogin()+": "+message.getMessage()+"\n"));

        usersOnlineList.addAll(message.getUserList());
    }

}
