package TaskBoard;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.IntStream;

/**
 * project class
 * a project can contains multiple columns
 * and also stores all the Tasks that has been added to columns
 */
public class Project {

    private Stage stage;
    private String name;
    private ArrayList<String> columns;
    private ArrayList<Task> taskList;
    private HBox columnHolder;// all columns will be held by a HBox(just for now) at the center
    private ArrayList<VBox> columnsList;



    public Project(Stage stage) {
        this.stage = stage;
        this.columnHolder = new HBox(12);
        this.taskList = new ArrayList<>();
        this.columnsList = new ArrayList<>();
        this.createProject();
    }

    public String getName() { return this.name; }
    public void setName(String name) {this.name = name; }
    public ArrayList<String> getColumns() { return columns; }

    public void addColumn(String name) {
        columns.add(name);
    }
    public void setColumnsTo(ArrayList<String> newColumns) { this.columns = newColumns; }

    public void addTask() {
        Task task = new Task(this);
        taskList.add(task);
    }

    public HBox getColumnHolder() {
        return columnHolder;
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
             this.name = nameField.getText();// get name
             this.columns = new ArrayList<>();
            // for each text field, get column name
            for(TextField each : columnsList) {
                this.columns.add(each.getText());
            }
            // reset stage title
            stage.setTitle("Main Screen: " + this.name);
            // load columns
            loadColumn();
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
        createPop.setTitle(name);

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //text followed by textfield
        //name: [  ]
        HBox namePane = new HBox(8);
        Text nameText = new Text("Project Name:");
        TextField nameField = new TextField(name);
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
        for(String each : columns) {
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
            this.name = nameField.getText();
            this.columns = new ArrayList<>();
            for(TextField each : columnsList) {
                columns.add(each.getText());
            }
            stage.setTitle("Main Screen: " + this.name);
            // load columns
            loadColumn();
            createPop.close();
        });
        cancelBtn.setOnAction(e -> {
            createPop.close();
        });

        // load column
        loadColumn();
        //set scene
        Scene cpScene = new Scene(layout, Main.POPUPWIDTH, Main.POPUPHIGHT);
        createPop.setScene(cpScene);
        createPop.show();
    }

    /**
     * show all columns contained by the project
     */
    public void loadColumn() {
        this.columnHolder.getChildren().clear();
        for(String each : this.columns) {
            // each column is a VBox
            VBox column = new VBox(8);
            column.setId(each);// each VBox has an ID same as column name
            Text cName = new Text(each);
            column.getChildren().add(cName);
            columnHolder.getChildren().add(column);
            columnsList.add(column);
        }
        putTask();
    }

    private void putTask() {
       for(VBox column : this.columnsList) {
           for(Task task : this.taskList) {
               if(column.getId().equals(task.getStatus())) {
                   column.getChildren().add(task.getTaskInGridPane());
               }
           }
       }
    }

    public static void sop(Object x){System.out.println(x);}
}
