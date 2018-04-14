package TaskBoard;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class logicController implements Initializable {
    @FXML private Button loginBtn;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private Text errorMsg;
    private String usernameStr;
    private String passwordStr;


    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               usernameStr = username.getText();
               passwordStr = password.getText();
               //checkValidity(usernameStr, passwordStr);
           }
       });
    }

//    private void checkValidity(String name, String pw) {
//        Map<String, String> userToPassword = new HashMap<>();
//        userToPassword.put("Admin", "12345");
//        userToPassword.put("Bdmin", "54321");
//        userToPassword.put("Cdmin", "13245");
//
//        Set<String> names = userToPassword.keySet();
//        boolean eq = false;
//        for(String each : names) {
//            if(!each.equals(name)) continue;
//            else eq = userToPassword.get(each).equals(pw);
//        }
//
//        if(!eq) {
//            errorMsg.setText("Invalid password / username");
//            errorMsg.setFill(Color.RED);
//        } else {
//            errorMsg.setFill(Color.BLACK);
//            errorMsg.setText("Please Login");
//        }
//    }
}
