package hrininlab;

import hrininlab.DAO.UserDao;
import hrininlab.Entity.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

public class ControllerStartPage  {

    Start mainApp;

    @FXML
    private TextField login;
    @FXML
    private TextField password;

    @FXML
    public void validClient() throws SQLException, IOException {

        User user;
        String log = login.getText();
        String pass = password.getText();
        UserDao dao = new UserDao();

        try {
            user = dao.get_user_by_login(log);

            String password = user.getPassword();
            if (password.equals(pass)){
                user.setOnline(true);
                dao.update_User(user);
                mainApp.setUser(user);
                mainApp.showPersonOverview();

            }
            else{
                System.out.println(log);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    public void setMainApp(Start mainApp) {
        this.mainApp = mainApp;
    }
    public Start getStart(){
        return this.mainApp;
    }

    public static void main(String[] args){

    }

}
