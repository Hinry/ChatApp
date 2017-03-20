package hrininlab;

import hrininlab.Controllers.ControllerChat;
import hrininlab.Controllers.ControllerStartPage;
import hrininlab.Entity.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Start extends Application {
    private Stage primaryStage;
    transient private BorderPane rootLayout;
    
    private static double xOffset = 0;
    private static double yOffset = 0;

    private User user;
    private ControllerChat controller;



    @Override
    public void start(final Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;
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
            primaryStage.setResizable(false);
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
            loader.setLocation(Start.class.getResource("/fxml/mainApp.fxml"));
            BorderPane personOverview = (BorderPane) loader.load();

            Scene scene = new Scene(personOverview);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Даём контроллеру доступ к главному приложению.
            controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.setTitle(controller.getPolzovatel().getLogin()+" ["
                    +controller.getPolzovatel().getFirst_name()+" "+controller.getPolzovatel().getLast_name()
            +"]");

            //слушатель для отправки сообщения при нажатии клавиши "Enter"
            controller.input.setOnKeyPressed(event -> {
                if(event.getCode().equals(KeyCode.ENTER)){
                    controller.sendMessage();
                    controller.input.clear();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //метод при закрытии окна клиента
    @Override
    public void stop(){
        controller.closeApp();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
