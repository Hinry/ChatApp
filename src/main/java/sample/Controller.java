package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Controller {

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    private Main main;
    @FXML
    private Label user1;
    @FXML
    private Button submit;
    @FXML
    private TextField login;
    @FXML
    private TextField password;

    @FXML
    public void validClient() throws SQLException, IOException {

        String log = login.getText();
        String pass = password.getText();
        String query = "select * from chat_users WHERE login='"+log+"'";
        con = Connector.getDBConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery(query);

        while (rs.next()) {
            String password = rs.getString("password");

            if (password.equals(pass)){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/fxml/sample2.fxml"));
                StackPane page = (StackPane) loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("ChatApp : "+ log);
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);
                Controller2 controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setUser(log);

                dialogStage.show();
            }
            else{
                System.out.println(log);

            }
        }

    }

}
