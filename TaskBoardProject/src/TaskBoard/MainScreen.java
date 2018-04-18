package TaskBoard;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * This class modifies the functionality of main screen
 */
public class MainScreen implements ModelListener {
    private Stage mainScreenStage;// main screen has it's own stage
    private BorderPane boardLayout;// main screen uses a border panel
    private TaskBoardModel currentBoard;// the current project showing
    private ProjectView currentProjectView;

    /**
     * Make the main screen scene
     * @param stage
     */
    public MainScreen(Stage stage) throws Exception {
        mainScreenStage = stage;
        boardLayout = new BorderPane();
        currentBoard = new TaskBoardModel();
        currentBoard.attach(this);

        // =============Start make screen=============

        // main screen should should contains following buttons
        // New Project: create new project
        // New Task: to create a task in the running project
        // Edit Project: edit current project(rename, add column)
        // Edit Task: edit task by clicking it
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
        boardLayout.setTop(top);

        // =============End make screen=============

        // new project
        newProjectBtn.setOnAction(e -> {
            currentProjectView = new ProjectView(this.mainScreenStage, this.currentBoard);
            currentProjectView.createProject();
            editBtn.setDisable(false);
            saveBtn.setDisable(false);
            newTaskBtn.setDisable(false);
        });
        // new task
        newTaskBtn.setOnAction(e -> {
            TaskModel taskModel = new TaskModel();
            TaskView taskView = new TaskView(this.mainScreenStage, currentBoard.getCurrentProjectModel(), taskModel);
            taskView.createTask();
        });
        // edit
        editBtn.setOnAction(e -> {
            currentProjectView.editProject();
        });
        // @TODO: implementing all buttons

        stage.setScene(new Scene(boardLayout, Main.WINDOWWIDTH, Main.POPUPHIGHT));
        stage.show();
    }

    public void update() {
        ScrollPane sp = new ScrollPane();
        boardLayout.setCenter(sp);
        sp.setContent(currentProjectView.getProjectView());
    }


}
