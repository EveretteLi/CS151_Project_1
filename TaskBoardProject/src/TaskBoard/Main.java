package TaskBoard;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

/**
 * This's the main class
 * The first class the application will visit
 */
public class Main extends Application {

    public static boolean DIRTY = false; // dirty flag

    private Stage window; // first stage
    // size of window
    public static final int WINDOWWIDTH = 1500;
    public static final int WINDOWHIGHT = 600;
    public static final int POPUPWIDTH = 1000;
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
    public static void toLogin() throws Exception { new Login(new Stage()); }

    /**
     * start a new main screen stage
     */
    public static void toMainScreen() throws Exception { new MainScreen(new Stage()); }

    /**
     * get primary stage that has been passed into start()
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return this.window;
    }

    public static Color RandomColorGenerator() {
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.LIGHTSEAGREEN);
        colors.add(Color.TEAL);
        colors.add(Color.PALEVIOLETRED);
        colors.add(Color.DARKSLATEBLUE);
        colors.add(Color.LIGHTSLATEGREY);

        Random picker = new Random();
        int next = picker.nextInt(colors.size());
        return colors.get(next);
    }

    // main
    public static void main(String[] args) {
        launch();
    }
}

