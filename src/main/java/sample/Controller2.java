package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Created by mrhri on 02.12.2016.
 */
public class Controller2 {
    @FXML
    private Label user;

    private Stage dialogStage;
    private String log;

    public Label getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user.setText(user);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
