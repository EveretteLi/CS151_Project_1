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
import java.util.stream.IntStream;


/**
 * This class modifies the functionality of main screen
 */
public class MainScreen {
    private Stage mainScreenStage;// main screen has it's own stage
    private BorderPane mainLayout;// main screen uses a border panel
    private HBox columnHolder;// all columns will be held by a HBox(just for now) at the center
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
        columnHolder = new HBox(12);
        tempStorePro = new ArrayList<>();
        //set the center of the main screen
        // put column holder into a scroll panel
        ScrollPane columnSpace = new ScrollPane(columnHolder);

        // main screen should should contains following buttons
        // New Project: create new project
        // Edit: edit current project(rename, add column)
        // Save: save current project
        // Load: load a existing project
        // Logout
        HBox top = new HBox(8);
        Button newProjectBtn = new Button("New Project");
        Button editBtn = new Button("Edit");
        Button saveBtn = new Button("Save");
        Button loadBtn = new Button("Load");
        Button logoutBtn = new Button("Logout");
        top.getChildren().addAll(
                newProjectBtn, editBtn, saveBtn, loadBtn, logoutBtn);
        mainLayout.setTop(top);
        mainLayout.setCenter(columnSpace);

        // new project
        newProjectBtn.setOnAction(e -> {
            createProject();
        });
        // edit
        editBtn.setOnAction(e -> {
            if(!tempStorePro.isEmpty())
                editProject();
        });
        // @TODO: implementing all buttons
        //set main scene
        stage.setScene(new Scene(mainLayout, Main.WINDOWWIDTH,Main.WINDOWHIGHT));
        mainScreenStage.show();
    }

    /**
     * create new project
     */
    public void createProject() {
        // pop-up window
        Stage createPop = new Stage();
        createPop.initModality(Modality.APPLICATION_MODAL);
        createPop.setTitle("New Project");
        // grid panel for the pop-up window
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        // text followed by textfield
        // put them in one HBox
        // ( name : [  ] )
        HBox namePane = new HBox(8);
        Text nameText = new Text("Project Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter project name");
        namePane.getChildren().addAll(nameText, nameField);
        layout.add(namePane, 0,0);
        // 2 buttons for adding and remove columns
        // put them in one HBox
        HBox addAndRemove = new HBox(8);
        Button addBtn = new Button("+");
        Button remBtn = new Button("-");
        addAndRemove.getChildren().addAll(addBtn, remBtn);
        layout.add(addAndRemove, 0, 1);
        // add columns
        // each slide is in a HBox
        // a VBox will be used to holds all HBoxes
        VBox addColumnPane = new VBox(8);
        ArrayList<TextField> columnsList = new ArrayList<>();
        layout.add(addColumnPane, 0, 2);
        // addBtn listener
        addBtn.setOnAction(e -> {
            HBox newColumn = new HBox(8);
            Text tempText = new Text("Column name:");
            TextField tempField = new TextField();
            columnsList.add(tempField);
            newColumn.getChildren().addAll(tempText, tempField);
            addColumnPane.getChildren().add(newColumn);
        });
        // fire the addBtn 3 time
        IntStream.range(0, 3).forEach(i -> addBtn.fire());
        // remBtn listener
        remBtn.setOnAction(e -> {
            if(addColumnPane.getChildren().size() > 1){
                addColumnPane.getChildren().remove(addColumnPane.getChildren().size()-1);
            }
        });
        // create and the cancel button
        HBox btnPane = new HBox(8);
        Button createBtn = new Button("Create");
        Button cancelBtn = new Button("Cancel");
        btnPane.getChildren().addAll(createBtn, cancelBtn);
        layout.add(btnPane, 0,3);
        //listeners
        createBtn.setOnAction(e -> {
            String pName = nameField.getText();// get name
            ArrayList<String> columnNameList = new ArrayList<>();
            // for each text field, get column name
            for(TextField each : columnsList) {
                columnNameList.add(each.getText());
            }
            // make new project
            tempStorePro.add(new Project(pName, columnNameList));
            // set current project
            currentProject = tempStorePro.get(tempStorePro.size()-1);
            showProject(currentProject);
            // reset stage title
            this.mainScreenStage.setTitle("Main Screen: " + currentProject.getName());
            createPop.close();
        });
        cancelBtn.setOnAction(e -> {
            createPop.close();
        });

        //set scene
        Scene cpScene = new Scene(layout, Main.POPUPWIDTH, Main.POPUPHIGHT);
        createPop.setScene(cpScene);
        createPop.show();
    }

    /**
     * start a pop up window
     * asking for edit information
     * almost the same as create project
     */
    public void editProject() {
        Stage createPop = new Stage();
        createPop.initModality(Modality.APPLICATION_MODAL);
        createPop.setTitle(currentProject.getName());

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //text followed by textfield
        //name: [  ]
        HBox namePane = new HBox(8);
        Text nameText = new Text("Project Name:");
        TextField nameField = new TextField(currentProject.getName());
        namePane.getChildren().addAll(nameText, nameField);
        layout.add(namePane, 0,0);

        HBox addAndRemove = new HBox(8);
        Button addBtn = new Button("+");
        Button remBtn = new Button("-");
        addAndRemove.getChildren().addAll(addBtn, remBtn);
        layout.add(addAndRemove, 0, 1);
        // add column:
        // all the columns
        VBox addColumnPane = new VBox(8);
        ArrayList<TextField> columnsList = new ArrayList<>();
        ArrayList<String> existColumn = currentProject.getColumns();
        for(String each : existColumn) {
            HBox oneColumn = new HBox(8);
            Text tempNameText = new Text("Column name:");
            TextField tempField = new TextField(each);
            columnsList.add(tempField);
            oneColumn.getChildren().addAll(tempNameText, tempField);
            addColumnPane.getChildren().add(oneColumn);
        }
        layout.add(addColumnPane, 0, 2);
        // addBtn listener
        addBtn.setOnAction(e -> {
            HBox newColumn = new HBox(8);
            Text tempText = new Text("Column name:");
            TextField tempField = new TextField();
            columnsList.add(tempField);
            newColumn.getChildren().addAll(tempText, tempField);
            addColumnPane.getChildren().add(newColumn);
        });
        // remBtn listener
        remBtn.setOnAction(e -> {
            if(addColumnPane.getChildren().size() > 1) {
                addColumnPane.getChildren().remove(addColumnPane.getChildren().size()-1);
                columnsList.remove(columnsList.size()-1);
            }
        });
        //buttons
        HBox btnPane = new HBox(8);
        Button doneBtn = new Button("Done");
        Button cancelBtn = new Button("Cancel");
        btnPane.getChildren().addAll(doneBtn, cancelBtn);
        layout.add(btnPane, 0,3);
        //listeners
        doneBtn.setOnAction(e -> {
            String pName = nameField.getText();
            currentProject.setName(pName);
            ArrayList<String> columnNameList = new ArrayList<>();
            for(TextField each : columnsList) {
                columnNameList.add(each.getText());
            }
            currentProject.setColumnsTo(columnNameList);
            showProject(currentProject);
            this.mainScreenStage.setTitle("Main Screen: " + currentProject.getName());
            createPop.close();
        });
        cancelBtn.setOnAction(e -> {
            createPop.close();
        });

        //set scene
        Scene cpScene = new Scene(layout, Main.POPUPWIDTH, Main.POPUPHIGHT);
        createPop.setScene(cpScene);
        createPop.show();


    }


    /**
     * show all columns contained by the project
     * @param project the project that we use to show
     */
    private void showProject(Project project) {
        this.columnHolder.getChildren().clear();
        ArrayList<String> columns = project.getColumns();
        for(String each : columns) {
            // each column is a VBox
            VBox column = new VBox(8);
            Text cName = new Text(each);
            column.getChildren().add(cName);
            columnHolder.getChildren().add(column);
        }
    }
}
