package hrininlab.Controllers;

import hrininlab.Client.ChatClient;
import hrininlab.Interfaces.*;
import hrininlab.Messengers.LoginRequest;
import hrininlab.Start;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

public class ControllerStartPage  implements LoginRequestListener{

    Start mainApp;
    private ChatClient client;


    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField login;
    @FXML
    private TextField password;

    @FXML
    private Label error;

    @FXML
    public void validClient() throws SQLException, IOException, InterruptedException {

        LoginRequest request = new LoginRequest(login.getText(),password.getText());
        client.sendLoginRequest(request);
    }

    public void setMainApp(Start mainApp) throws IOException {
        this.mainApp = mainApp;
        client = new ChatClient("localhost", 45000, this);
    }


    public static void main(String[] args){

    }

    @Override
    public void addLoginRequest(LoginRequest request) throws SQLException {
        if(request.getUser()!= null){
            mainApp.setUser(request.getUser());
            Platform.runLater(() -> {
                try {
                    client.close();
                    mainApp.showPersonOverview();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } else {
            Platform.runLater(()->error.setText(request.getMessage()));
        }
    }
}
