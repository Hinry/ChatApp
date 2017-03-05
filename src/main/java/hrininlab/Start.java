package hrininlab;

import hrininlab.Entity.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class Start extends Application {
    private Stage primaryStage;
    transient private BorderPane rootLayout;
    
    private static double xOffset = 0;
    private static double yOffset = 0;

    private User user;



    @Override
    public void start(final Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("ChatApp");
        this.primaryStage.initStyle(StageStyle.UNDECORATED);
        initRootLayout();

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Инициализация главного окна.
     */
    public void initRootLayout() throws IOException {
        try {

            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Start.class
                    .getResource("/fxml/StartPage.fxml"));

            rootLayout = (BorderPane) loader.load();
            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Даём контроллеру доступ к главному приложению.
            ControllerStartPage controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();

            scene.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            scene.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Вывод контактов внутри главного окна.
     */
    public void showPersonOverview() throws SQLException {
        try {
            // Загружаем обзор контактов
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Start.class.getResource("/fxml/ChatApp.fxml"));
            StackPane personOverview = (StackPane) loader.load();

            rootLayout.setCenter(personOverview);

            // Даём контроллеру доступ к главному приложению.
            ControllerChat controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
