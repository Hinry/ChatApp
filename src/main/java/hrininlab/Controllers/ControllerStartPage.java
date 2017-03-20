package hrininlab.Controllers;

import hrininlab.DAO.UserDao;
import hrininlab.Entity.User;
import hrininlab.Start;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

public class ControllerStartPage  {

    Start mainApp;


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

        //КОД ТРЕБУЕТ ИЗМЕНЕНИЙ (ПЕРЕНАПРАВИТЬ ЗАПРОСЫ НА СЕРВЕР!!)
        User user;

        String log = login.getText();
        String pass = password.getText();

        UserDao dao = new UserDao();
        user = dao.get_user_by_login(log);

        if(user!=null){
            String password = user.getPassword();
            if (password.equals(pass)){


                user.setOnline(true);
                dao.update_User(user);
                mainApp.setUser(user);
                mainApp.showPersonOverview();

            }
            else {
                error.setText("Не верный логин или пароль");
                progressBar.setProgress(0);
            }
        }
        else{
            error.setText("такого пользователя не существует");
            progressBar.setProgress(0);
        }
    }

    public void setMainApp(Start mainApp) {
        this.mainApp = mainApp;
    }


    public static void main(String[] args){

    }

}
