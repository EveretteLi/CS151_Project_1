package TaskBoard;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

        // set login layout
        VBox loginLayout = new VBox(20);
        loginLayout.setAlignment(Pos.CENTER);
        TextField username = new TextField();
        username.setPromptText("Enter your username");
        TextField password = new TextField("");
        password.setPromptText("Enter your password");
        Button loginBtn = new Button("Login");
        loginLayout.getChildren().addAll(username, password, loginBtn, errorMsg);
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
        //set login scene
        loginStage.setScene(new Scene(loginLayout, 300, 600));
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
