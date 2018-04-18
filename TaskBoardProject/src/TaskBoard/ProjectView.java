package TaskBoard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.IntStream;

/**
 * project view class
 * a project can contains multiple columns
 * and also stores all the Tasks that has been added to columns
 */
public class ProjectView implements ModelListener {
    private ProjectModel project;// the project it shows
    private TaskBoardModel taskBoard;// the task board where it aggregates
    private HBox projectView;// how project should be viewed
    private Stage stage;


    public ProjectView(Stage stage, TaskBoardModel taskBoard) {
        this.project = new ProjectModel();
        this.taskBoard = taskBoard;
        projectView = new HBox(12);
        this.stage = stage;
        project.attach(this);
    }

    @Override
    public void update() {
        this.projectView = showProject();
        taskBoard.updateAll();
    }

    public HBox getProjectView() {
        return projectView;
    }

    /**
     * create new project
     */
    public void createProject() {
        // pop-up window
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Project");

        taskBoard.addProject(project);// start add it in
        taskBoard.setCurrentProjectModel(project);// set it to current running project

        // =============Start make screen=============

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
        // create and the cancel button
        HBox btnPane = new HBox(8);
        Button createBtn = new Button("Create");
        Button cancelBtn = new Button("Cancel");
        btnPane.getChildren().addAll(createBtn, cancelBtn);
        layout.add(btnPane, 0,3);

        // =============End make screen=============

        //listeners
        // addBtn listener
        addBtn.setOnAction(e -> {
            HBox newColumn = new HBox(8);
            Text tempText = new Text("Column name:");
            TextField tempField = new TextField();
            columnsList.add(tempField);
            newColumn.getChildren().addAll(tempText, tempField);
            addColumnPane.getChildren().add(newColumn);
        });// fire the addBtn 3 time
        IntStream.range(0, 3).forEach(i -> addBtn.fire());
        // remBtn listener
        remBtn.setOnAction(e -> {
            if(addColumnPane.getChildren().size() > 1){
                columnsList.remove(columnsList.size()-1);
                addColumnPane.getChildren().remove(addColumnPane.getChildren().size()-1);
            }
        });
        createBtn.setOnAction(e -> {
             project.setName(nameField.getText());// set name
            project.setColumns(new ArrayList<String>());
            for(TextField each : columnsList) {
                project.addColumn(each.getText());
            }
            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });

        //set scene
        Scene cpScene = new Scene(layout, Main.POPUPWIDTH, Main.POPUPHIGHT);
        stage.setScene(cpScene);
        stage.show();
    }

    /**
     * start a pop up window
     * asking for edit information
     * almost the same as create project
     */
    public void editProject() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(project.getName());

        // =============Start make screen=============

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //text followed by textfield
        //name: [  ]
        HBox namePane = new HBox(8);
        Text nameText = new Text("Project Name:");
        TextField nameField = new TextField(project.getName());
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
        ArrayList<TextField> columnsList = new ArrayList<>();;
        for(String each : project.getColumns()) {
            HBox oneColumn = new HBox(8);
            Text tempNameText = new Text("Column name:");
            TextField tempField = new TextField(each);
            columnsList.add(tempField);
            oneColumn.getChildren().addAll(tempNameText, tempField);
            addColumnPane.getChildren().add(oneColumn);
        }
        layout.add(addColumnPane, 0, 2);

        //buttons
        HBox btnPane = new HBox(8);
        Button doneBtn = new Button("Done");
        Button cancelBtn = new Button("Cancel");
        btnPane.getChildren().addAll(doneBtn, cancelBtn);
        layout.add(btnPane, 0,3);

        // =============End make screen=============

        //listeners
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
        doneBtn.setOnAction(e -> {
            project.setName(nameField.getText());
            project.setColumns(new ArrayList<String>());
            for(TextField each : columnsList) {
                project.addColumn(each.getText());
            }
            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });

        //set scene
        Scene cpScene = new Scene(layout, Main.POPUPWIDTH, Main.POPUPHIGHT);
        stage.setScene(cpScene);
        stage.show();
    }

    /**
     * return the project in a HBox
     */
    public HBox showProject() {
        HBox columnHolder = new HBox(12);
        ArrayList<VBox> columnsList = new ArrayList<>();
        ArrayList<TaskView> taskViews = new ArrayList<>();

        for(TaskModel each : project.getTaskSet()) {
            taskViews.add(new TaskView(new Stage(), project, each));
        }

        for (String each : project.getColumns()) {
            // each column is a VBox
            VBox column = new VBox(8);
            column.setPadding(new Insets(12));
            column.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
            column.setId(each);// each VBox has an ID same as column name
            Text cName = new Text(each);
            column.getChildren().add(cName);
            columnsList.add(column);
        }
//        sop("pj TS size;: "+taskSet.size());
        for (TaskView each : taskViews) {
            for (VBox column : columnsList) {
                column.setPrefSize(100, Main.POPUPHIGHT);
                if (column.getId().equals(each.getTask().getStatus())) {
                    column.getChildren().add(each.getTaskForm());
                }
            }
        }
        for(VBox each : columnsList) {
            columnHolder.getChildren().add(each);
        }
        return columnHolder;
    }
    public static void sop(Object x){System.out.println(x);}
}
