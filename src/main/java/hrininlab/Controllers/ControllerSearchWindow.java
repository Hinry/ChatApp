package hrininlab.Controllers;

import hrininlab.Client.ChatClient;
import hrininlab.Entity.User;
import hrininlab.Interfaces.SearchListener;
import hrininlab.Messengers.SearchRequest;
import hrininlab.Messengers.SystemRequestMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by mrhri on 21.03.2017.
 */
public class ControllerSearchWindow implements SearchListener{



    private ObservableList<User> userObservableList = FXCollections.observableArrayList();

    private ChatClient client;
    private Stage dialogStage;
    private ControllerChat controllerChat;
    private User user;

    @FXML
    private TableView<User> tableSearch;

    @FXML
    private TableColumn<User, String> loginColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private TextField login;

    @FXML
    private TextField name;

    @FXML
    private TextField lastName;

    @FXML
    private Label result;


    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public ControllerChat getControllerChat() {
        return controllerChat;
    }

    public void setControllerChat(ControllerChat controllerChat) {
        this.controllerChat = controllerChat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void initialize() {
        // Инициализация таблиц и слушатели.

        tableSearch.setItems(userObservableList);
        loginColumn.setCellValueFactory(cellData -> cellData.getValue().getPropertyLogin());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getPropertyName());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().getPropertyLastName());

        tableSearch.getItems().addListener((ListChangeListener<User>)
                c -> loginColumn.setCellValueFactory(cellData -> cellData.getValue().getPropertyLogin()));

        tableSearch.getItems().addListener((ListChangeListener<User>)
                c -> nameColumn.setCellValueFactory(cellData -> cellData.getValue().getPropertyName()));

        tableSearch.getItems().addListener((ListChangeListener<User>)
                c -> lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().getPropertyLastName()));

        // Слушаем изменения выбора, и при изменении отображаем
        // дополнительную информацию о Юзере.


    }

    @FXML
    public void searchUsers() throws IOException {

        client = new ChatClient("localhost", 45000,this);
        SearchRequest request = new SearchRequest(login.getText(),name.getText(),lastName.getText());
        client.sendSearchRequest(request);
    }

    @FXML
    public void addUserToContact(){
        controllerChat.addUsersToContactFromSearch(user, tableSearch.getSelectionModel().selectedItemProperty().get());
        dialogStage.close();
    }
    @FXML
    public void close(){
        dialogStage.close();
    }

    @Override
    public void add(SearchRequest request) {
        Platform.runLater(()-> {
            if(request.getUserlist().size() == 0){
                userObservableList.clear();
                result.setText("не найдено");
            }else{
                result.setText(String.valueOf(request.getUserlist().size()));
                userObservableList.clear();
                userObservableList.addAll(request.getUserlist());
            }

        });
        client.close();
    }
}
