package TaskBoard;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;

/**
 * This class modifies the functionality of main screen
 */
public class MainScreen implements ModelListener {
    private Stage mainScreenStage;// main screen has it's own stage
    private BorderPane boardLayout;// main screen uses a border panel
    private TaskBoardModel currentBoard;// the current project showing
    private ProjectView currentProjectView;
    private Button newProjectBtn, newTaskBtn, editBtn, saveBtn, loadBtn, logoutBtn;

    /**
     * Make the main screen scene
     * @param stage
     */
    public MainScreen(Stage stage) throws Exception {
        mainScreenStage = stage;
        boardLayout = new BorderPane();
        currentBoard = new TaskBoardModel();
        currentBoard.attach(this);

        setMainScene();

        stage.setScene(new Scene(boardLayout, Main.WINDOWWIDTH, Main.POPUPHIGHT));
        stage.show();
    }

    public void load(File file) throws IOException {
        XMLDecoder decoder = new XMLDecoder(new BufferedInputStream( new FileInputStream(file.getPath())));
        this.currentBoard = (TaskBoardModel) decoder.readObject();
        decoder.close();
        currentBoard.attach(this);
        currentProjectView = new ProjectView(this.mainScreenStage, this.currentBoard, this.currentBoard.getCurrentProjectModel());
        currentProjectView.update();
        update();

        editBtn.setDisable(false);
        saveBtn.setDisable(false);
        newTaskBtn.setDisable(false);

        this.mainScreenStage.show();
    }
    public void save(File file) throws IOException {
        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream( new FileOutputStream(file)));
        encoder.writeObject(currentBoard);
        encoder.close();
    }

    public void setMainScene(){
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
       this.newProjectBtn = new Button("New Project");
        this.newTaskBtn = new Button("New Task");
        newTaskBtn.setDisable(true);
        this.editBtn = new Button("Edit");
        editBtn.setDisable(true);
        this.saveBtn = new Button("Save");
        saveBtn.setDisable(true);
        this.loadBtn = new Button("Load");
        this.logoutBtn = new Button("Logout");
        top.getChildren().addAll(
                newProjectBtn, newTaskBtn, editBtn, saveBtn, loadBtn, logoutBtn);
        boardLayout.setTop(top);
        // =============End make screen=============

        // new project
        newProjectBtn.setOnAction(e -> {
            currentProjectView = new ProjectView(this.mainScreenStage, this.currentBoard, new ProjectModel());
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

        saveBtn.setOnAction(e -> {

            try{
                FileChooser chooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter =
                        new FileChooser.ExtensionFilter("XML file (*.xml)", "*.xml");
                chooser.getExtensionFilters().add(extensionFilter);

                File file = chooser.showSaveDialog(this.mainScreenStage);
                if(file != null)
                    save(file);
            }catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        loadBtn.setOnAction(e -> {
            try{
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Open TaskBoard");
                FileChooser.ExtensionFilter extensionFilter =
                        new FileChooser.ExtensionFilter("XML file (*.xml)", "*.xml");
                chooser.getExtensionFilters().add(extensionFilter);

                File file = chooser.showOpenDialog(this.mainScreenStage);
                if(file != null)
                    load(file);
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        logoutBtn.setOnAction(e -> {
            try{
                Main.toLogin();
                this.mainScreenStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });
    }

    public void update() {
        ScrollPane sp = new ScrollPane();
        boardLayout.setCenter(sp);
        sp.setContent(currentProjectView.getProjectView());
        sop("main got called");
    }

    public static void sop(Object x){ System.out.println(x);}
}
