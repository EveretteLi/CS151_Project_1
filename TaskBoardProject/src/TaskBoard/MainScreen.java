package TaskBoard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;


/**
 * This class modifies the functionality of main screen
 */
public class MainScreen {
    private Stage mainScreenStage;// main screen has it's own stage
    private BorderPane mainLayout;// main screen uses a border panel
    private ScrollPane columnSpace;// to put columns
    private ArrayList<Project> tempStorePro;// in the end, this array list should holds only the name of project data file, not actual projects

    private Project currentProject;// the current project showing

    /**
     * Make the main screen scene
     * @param stage
     */
    public MainScreen(Stage stage) {
        mainScreenStage = stage;
        mainScreenStage.setTitle("Task Board Main");

        //set layout for main
        mainLayout = new BorderPane();
        tempStorePro = new ArrayList<>();
        //set the center of the main screen
        // put column holder into a scroll panel
        columnSpace = new ScrollPane();

        // main screen should should contains following buttons
        // New Project: create new project
        // New Task: to create a task in the running project
        // Edit: edit current project(rename, add column)
        // Save: save current project
        // Load: load a existing project
        // Logout
        HBox top = new HBox(8);
        Button newProjectBtn = new Button("New Project");
        Button newTaskBtn = new Button("New Task");
        newTaskBtn.setDisable(true);
        Button editBtn = new Button("Edit");
        editBtn.setDisable(true);
        Button saveBtn = new Button("Save");
        saveBtn.setDisable(true);
        Button loadBtn = new Button("Load");
        Button logoutBtn = new Button("Logout");
        top.getChildren().addAll(
                newProjectBtn, newTaskBtn, editBtn, saveBtn, loadBtn, logoutBtn);
        mainLayout.setTop(top);
        mainLayout.setCenter(columnSpace);

        // new project
        newProjectBtn.setOnAction(e -> {
            Project newProject = new Project(stage);
            currentProject = newProject;
            tempStorePro.add(newProject);
            columnSpace.setContent(currentProject.getColumnHolder());
            editBtn.setDisable(false);
            saveBtn.setDisable(false);
            newTaskBtn.setDisable(false);
        });
        // new task
        newTaskBtn.setOnAction(e -> {
            currentProject.addTask();
        });
        // edit
        editBtn.setOnAction(e -> {
            if(!tempStorePro.isEmpty())
                currentProject.editProject();
            columnSpace.setContent(currentProject.getColumnHolder());
        });
        // @TODO: implementing all buttons
        //set main scene
        stage.setScene(new Scene(mainLayout, Main.WINDOWWIDTH,Main.WINDOWHIGHT));
        mainScreenStage.show();
    }
}
