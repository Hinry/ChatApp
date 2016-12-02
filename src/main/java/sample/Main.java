package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    private Stage primaryStage;

    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void start(final Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("ChatApp");
        this.primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene sce = new Scene(root, Color.TRANSPARENT);
        this.primaryStage.setScene(sce);

        this.primaryStage.show();


        sce.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        sce.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
