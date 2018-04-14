package TaskBoard;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This's the main class
 * The first class the application will visit
 */
public class Main extends Application {

    private Stage window; // first stage
    // size of window
    public static final int WINDOWWIDTH = 1500;
    public static final int WINDOWHIGHT = 600;
    public static final int POPUPWIDTH = 300;
    public static final int POPUPHIGHT = 600;


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Task Board");
        // go directly to login
        new Login(window);

    }

    /**
     * start a new login stage
     */
    public static void toLogin() { new Login(new Stage()); }

    /**
     * start a new main screen stage
     */
    public static void toMainScreen() { new MainScreen(new Stage()); }

    /**
     * get primary stage that has been passed into start()
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return this.window;
    }

    // main
    public static void main(String[] args) {
        launch();
    }
}
