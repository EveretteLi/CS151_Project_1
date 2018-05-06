package TaskBoard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * This class modifies the functionality in login
 */
public class Login {

    private Stage loginStage;// login has it's own stage
    private Text errorMsg = new Text("");// empty
    private final String USERNAME = "admin";// username
    private final String PASSWORD  = "123";// password

    public Login(Stage stage) throws Exception {
        loginStage = stage;
        loginStage.setTitle("Login");

        GridPane layout = new GridPane();
        Color tempSceneColor = Main.RandomColorGenerator();

        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(10);
        shadow.setOffsetY(3);
        //text part
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(new BackgroundFill(tempSceneColor, CornerRadii.EMPTY, Insets.EMPTY)));
        VBox texts = new VBox(2);
        texts.setMinWidth(Main.POPUPWIDTH/2);
        Text task = new Text("WELLCOME\nTO\nTASK\nBOARD:");
        task.setFill(Color.WHITE);
        task.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 65));
        texts.getChildren().addAll(task);
        texts.setPadding(new Insets(12));
        texts.setAlignment(Pos.CENTER);

        // set login layout
        VBox wrapper = new VBox();
        wrapper.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        wrapper.setAlignment(Pos.CENTER_RIGHT);

        VBox loginLayout = new VBox(20);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(Main.POPUPWIDTH/16));

        Label loginText = new Label("LOGIN");
        loginText.setTextFill(tempSceneColor);
        loginText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR,20 ));

        // username
        VBox usernameBox = new VBox();
        usernameBox.setBackground
                (new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,Insets.EMPTY)));
        usernameBox.setPadding(new Insets(12));
        usernameBox.setAlignment(Pos.CENTER_LEFT);
        Label usernameText = new Label("Username:");
        usernameText.setTextFill(tempSceneColor);
        usernameText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 17));
        usernameText.setMaxWidth(Main.POPUPWIDTH/8);
        TextField username = new TextField();
        username.setPromptText("Enter your username");
        username.setMaxWidth(Main.POPUPWIDTH/4);
        usernameBox.getChildren().addAll(usernameText, username);
        // password
        VBox passwordBox = new VBox();
        passwordBox.setBackground
                (new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        passwordBox.setPadding(new Insets(12));
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        Label passwordText = new Label("Password:");
        passwordText.setTextFill(tempSceneColor);
        passwordText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 17));
        passwordText.setMaxWidth(Main.POPUPWIDTH/8);
        TextField password = new TextField();
        password.setPromptText("Enter your password");
        password.setMaxWidth(Main.POPUPWIDTH/4);
        passwordBox.getChildren().addAll(passwordText, password);


        Button loginBtn = new Button("Login");
        loginBtn.setMinWidth(Main.POPUPWIDTH/4);
        loginBtn.setEffect(shadow);
        loginBtn.setStyle("-fx-background-color: #FFFFFF");
        loginBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            loginBtn.setStyle("-fx-background-color: dodgerblue");
        });
        loginBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            loginBtn.setStyle("-fx-background-color: #FFFFFF");
        });
        loginLayout.getChildren().addAll(loginText, usernameBox, passwordBox, loginBtn, errorMsg);
        wrapper.getChildren().addAll(loginLayout);
        // loginBtn listener
        // once it got clicked, jump to main screen stage
        loginBtn.setOnAction(e -> {
            if(checkValidity(username.getText(), password.getText())) {
                try{
                    Main.toMainScreen();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                this.loginStage.close();
            }
        });
        layout.add(texts, 0,0);
        layout.add(wrapper, 1,0);
        //set login scene
        loginStage.setScene(new Scene(layout, Main.POPUPWIDTH, Main.POPUPHIGHT));
        loginStage.show();
    }

    /**
     * Support function for login functionality.
     * only if the username is USERNAME and the password is PASSWORD the operation is valid
     * @param name the name entered by user
     * @param pw the password entered by user
     * @return if the input is valid or not
     */
    private boolean checkValidity(String name, String pw) {
        boolean valid = false;
        //check input
        if(name.equals(USERNAME) && pw.equals(PASSWORD))
            valid = true;

        if(!valid) {
            //set error massage
            errorMsg.setText("Invalid password / username");
            errorMsg.setFill(Color.RED);
        } else {
            errorMsg.setText("");
        }
        return valid;
    }
}
