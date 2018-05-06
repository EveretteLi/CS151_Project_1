package TaskBoard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
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


    public ProjectView(Stage stage, TaskBoardModel taskBoard, ProjectModel project) {
        this.stage = stage;
        this.project = project;
        this.taskBoard = taskBoard;
        projectView = new HBox(12);
        this.stage = stage;
        project.attach(this);
    }

    @Override
    public void update() {
        //sop("Project update got called");
        this.projectView = showProject();
        taskBoard.updateAll();
    }

    public void dragAndDrop() {
        for(Node each : projectView.getChildren()) {
            each.setOnDragOver(e -> {
                if (e.getDragboard().hasString()) {
                    e.acceptTransferModes(TransferMode.MOVE);
                }
            });
            if(each.getId().equals("delete")) {
                each.setOnDragEntered(e ->  each.setStyle("-fx-background-color: dimgray"));
                each.setOnDragExited(e -> each.setStyle("-fx-background-color: white"));
            }
            else {
                each.setOnDragEntered(e ->  each.setStyle("-fx-background-color: gray"));
                each.setOnDragExited(e -> each.setStyle("-fx-background-color: white"));
            }
            if(each.getId().equals("delete")){
                each.setOnDragDropped(e -> {
                    if(deleteAlert()) {
                        String taskName = e.getDragboard().getString();
                        TaskModel target = new TaskModel();
                        for (TaskModel task : project.getTaskSet()) {
                            if (task.getName().equals(taskName)) {
                                target = task;
                                taskName = each.getId();
                                break;
                            }
                        }
                        project.removeTask(target);
                    }
                });
            }
            else {
                each.setOnDragDropped(e -> {
                    String taskName = e.getDragboard().getString();
                    TaskModel target = new TaskModel();
                    for (TaskModel task : project.getTaskSet()) {
                        if (task.getName().equals(taskName)) {
                            target = task;
                            taskName = each.getId();
                            break;
                        }
                    }
                    target.setStatus(taskName);
                });
            }
        }
    }

    private boolean deleteAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Task Delete Warning");
        alert.setHeaderText(null);
        alert.setContentText("Delete this Task?");
        ButtonType deleteTp = new ButtonType("DELETE");
        ButtonType cancelTp = new ButtonType("CANCEL");
        alert.getButtonTypes().setAll(deleteTp, cancelTp);
        Optional<ButtonType> op = alert.showAndWait();
        if(op.get() == deleteTp) {
            return true;
        }
        return false;
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

        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(10);
        shadow.setOffsetY(3);

        // =============Start make screen=============
        Color tempSceneColor = Main.RandomColorGenerator();

        // grid panel for the pop-up window
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(new BackgroundFill(tempSceneColor, CornerRadii.EMPTY, Insets.EMPTY)));

        //text part
        VBox texts = new VBox(2);
        texts.setMinWidth(Main.POPUPWIDTH/2);
        Text task = new Text("New\nProject:");
        task.setFill(Color.WHITE);
        task.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 70));
        texts.getChildren().addAll(task);
        texts.setAlignment(Pos.CENTER);

        VBox wrapper = new VBox();
        wrapper.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(20));
        wrapper.setMinWidth(400);

        VBox projectLayout = new VBox(8);
        projectLayout.setAlignment(Pos.CENTER);

        // text followed by textfield
        // put them in one HBox
        // ( name : [  ] )
        VBox namePane = new VBox(8);
        Label nameText = new Label("Project Name:");
        nameText.setTextFill(tempSceneColor);
        nameText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 17));
        TextField nameField = new TextField();
        nameField.setPromptText("Enter project name");
        namePane.getChildren().addAll(nameText, nameField);
        namePane.setPadding(new Insets(8));
        namePane.setMaxWidth(Main.POPUPWIDTH/4);

        // 2 buttons for adding and remove columns
        // put them in one HBox
        HBox addAndRemove = new HBox(100);
        addAndRemove.setAlignment(Pos.CENTER);
        Button addBtn = new Button("+");
        Button remBtn = new Button("-");
        addBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        remBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        addBtn.setMinSize(50, 50);
        remBtn.setMinSize(50, 50);
        addBtn.setStyle("-fx-background-color: white");
        remBtn.setStyle("-fx-background-color: white");
        addBtn.setEffect(shadow);
        remBtn.setEffect(shadow);
        addBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> addBtn.setStyle("-fx-background-color: dodgerblue"));
        addBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> addBtn.setStyle("-fx-background-color: white"));
        remBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> remBtn.setStyle("-fx-background-color: dodgerblue"));
        remBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> remBtn.setStyle("-fx-background-color: white"));
        addAndRemove.getChildren().addAll(addBtn, remBtn);
        addAndRemove.setMaxWidth(Main.POPUPWIDTH/4);
        // add columns
        // each slide is in a HBox
        // a VBox will be used to holds all HBoxes
        VBox addColumnPane = new VBox(8);
        ArrayList<TextField> columnsList = new ArrayList<>();
        addColumnPane.setPadding(new Insets(8));
        addColumnPane.setMaxWidth(Main.POPUPWIDTH/4);


        // create and the cancel button
        HBox btnPane = new HBox(100);
        btnPane.setAlignment(Pos.CENTER);
        Button createBtn = new Button("Create");
        Button cancelBtn = new Button("Cancel");
        createBtn.setStyle("-fx-background-color: white");
        cancelBtn.setStyle("-fx-background-color: white");
        createBtn.setEffect(shadow);
        cancelBtn.setEffect(shadow);
        createBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> createBtn.setStyle("-fx-background-color: dodgerblue"));
        createBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> createBtn.setStyle("-fx-background-color: white"));
        cancelBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> cancelBtn.setStyle("-fx-background-color: dodgerblue"));
        cancelBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> cancelBtn.setStyle("-fx-background-color: white"));
        btnPane.getChildren().addAll(createBtn, cancelBtn);
        btnPane.setMaxWidth(Main.POPUPWIDTH/4);

        Text errorText = new Text("");
        errorText.setFill(Color.RED);

        projectLayout.getChildren().addAll(namePane, addAndRemove, addColumnPane, btnPane, errorText);
        wrapper.getChildren().addAll(projectLayout);

        // =============End make screen=============

        //listeners
        // addBtn listener
        addBtn.setOnAction(e -> {
            if(addColumnPane.getChildren().size() < 5) {
                remBtn.setDisable(false);
                VBox newColumn = new VBox(8);
                Label tempText = new Label("Column name:");
                tempText.setTextFill(tempSceneColor);
                tempText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 17));
                TextField tempField = new TextField();
                columnsList.add(tempField);
                newColumn.getChildren().addAll(tempText, tempField);
                addColumnPane.getChildren().add(newColumn);
                if(addColumnPane.getChildren().size() == 5) addBtn.setDisable(true);
            }
        });// fire the addBtn 3 time
        IntStream.range(0, 3).forEach(i -> addBtn.fire());
        // remBtn listener
        remBtn.setOnAction(e -> {
            if(addColumnPane.getChildren().size() > 1) {
                addBtn.setDisable(false);
                columnsList.remove(columnsList.size()-1);
                addColumnPane.getChildren().remove(addColumnPane.getChildren().size()-1);
                if(addColumnPane.getChildren().size() == 1) remBtn.setDisable(true);
            }
        });
        createBtn.setOnAction(e -> {
            ArrayList<String> seen = new ArrayList<>();
            for(TextField each : columnsList) {
                if(each.getText().equals("")) {
                    errorText.setText("Input invalid: column name cannot be empty.");
                    return;
                }
                else if(seen.contains(each.getText())) {
                    errorText.setText("Input invalid: column name cannot be duplicated.");
                    return;
                }
                seen.add(each.getText());
            }
            if(nameField.getText().equals("")) {errorText.setText("Input invalid: project name cannot be empty. "); return; }
            project.setName(nameField.getText());// set name
            project.setColumns(new ArrayList<String>());
            for(TextField each : columnsList) {
                project.addColumn(each.getText());
            }

            taskBoard.addProject(project);// start add it in
            taskBoard.setCurrentProjectModel(project);// set it to current running project

            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });

        layout.add(texts, 0,0);
        layout.add(wrapper, 1, 0);

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

        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(10);
        shadow.setOffsetY(3);

        // =============Start make screen=============

        Color tempSceneColor = Main.RandomColorGenerator();

        // grid panel for the pop-up window
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(new BackgroundFill(tempSceneColor, CornerRadii.EMPTY, Insets.EMPTY)));

        //text part
        VBox texts = new VBox(2);
        texts.setMinWidth(Main.POPUPWIDTH/2);
        Text task = new Text("New\nProject:");
        task.setFill(Color.WHITE);
        task.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 70));
        texts.getChildren().addAll(task);
        texts.setAlignment(Pos.CENTER);

        VBox wrapper = new VBox();
        wrapper.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(20));
        wrapper.setMinWidth(400);

        VBox projectLayout = new VBox(8);
        projectLayout.setAlignment(Pos.CENTER);

        // text followed by textfield
        // edit
        VBox namePane = new VBox(8);
        Label nameText = new Label("Project Name:");
        nameText.setTextFill(tempSceneColor);
        nameText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 17));
        TextField nameField = new TextField();
        nameField.setText(project.getName());
        namePane.getChildren().addAll(nameText, nameField);
        namePane.setPadding(new Insets(8));
        namePane.setMaxWidth(Main.POPUPWIDTH/4);

        // 2 buttons for adding and remove columns
        HBox addAndRemove = new HBox(100);
        addAndRemove.setAlignment(Pos.CENTER);
        Button addBtn = new Button("+");
        Button remBtn = new Button("-");
        addBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        remBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        addBtn.setMinSize(50, 50);
        remBtn.setMinSize(50, 50);
        addBtn.setStyle("-fx-background-color: white");
        remBtn.setStyle("-fx-background-color: white");
        addBtn.setEffect(shadow);
        remBtn.setEffect(shadow);
        addBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> addBtn.setStyle("-fx-background-color: dodgerblue"));
        addBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> addBtn.setStyle("-fx-background-color: white"));
        remBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> remBtn.setStyle("-fx-background-color: dodgerblue"));
        remBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> remBtn.setStyle("-fx-background-color: white"));
        addAndRemove.getChildren().addAll(addBtn, remBtn);
        addAndRemove.setMaxWidth(Main.POPUPWIDTH/4);

        // add columns
        // each slide is in a HBox
        // a VBox will be used to holds all HBoxes
        VBox addColumnPane = new VBox(8);
        ArrayList<TextField> columnsList = new ArrayList<>();
        for(String each : project.getColumns()) {
            VBox newColumn = new VBox(8);
            Label tempText = new Label("Column name:");
            tempText.setTextFill(tempSceneColor);
            tempText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 17));
            TextField tempField = new TextField(each);
            columnsList.add(tempField);
            newColumn.getChildren().addAll(tempText, tempField);
            addColumnPane.getChildren().add(newColumn);
        }
        addColumnPane.setPadding(new Insets(8));
        addColumnPane.setMaxWidth(Main.POPUPWIDTH/4);

        // create and the cancel button
        HBox btnPane = new HBox(100);
        btnPane.setAlignment(Pos.CENTER);
        Button doneBtn = new Button("Done");
        Button cancelBtn = new Button("Cancel");
        doneBtn.setStyle("-fx-background-color: white");
        cancelBtn.setStyle("-fx-background-color: white");
        doneBtn.setEffect(shadow);
        cancelBtn.setEffect(shadow);
        doneBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> doneBtn.setStyle("-fx-background-color: dodgerblue"));
        doneBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> doneBtn.setStyle("-fx-background-color: white"));
        cancelBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> cancelBtn.setStyle("-fx-background-color: dodgerblue"));
        cancelBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> cancelBtn.setStyle("-fx-background-color: white"));
        btnPane.getChildren().addAll(doneBtn, cancelBtn);
        btnPane.setMaxWidth(Main.POPUPWIDTH/4);

        Text errorText = new Text("");
        errorText.setFill(Color.RED);

        projectLayout.getChildren().addAll(namePane, addAndRemove, addColumnPane, btnPane, errorText);
        wrapper.getChildren().addAll(projectLayout);



        // =============End make screen=============

        //listeners
        // addBtn listener
        addBtn.setOnAction(e -> {
            if(addColumnPane.getChildren().size() < 5) {
                remBtn.setDisable(false);
                VBox newColumn = new VBox(8);
                Label tempText = new Label("Column name:");
                tempText.setTextFill(tempSceneColor);
                tempText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 17));
                TextField tempField = new TextField();
                columnsList.add(tempField);
                newColumn.getChildren().addAll(tempText, tempField);
                addColumnPane.getChildren().add(newColumn);
                if(addColumnPane.getChildren().size() == 5) addBtn.setDisable(true);
            }
        });// fire the addBtn 3 time
        // remBtn listener
        remBtn.setOnAction(e -> {
            if(addColumnPane.getChildren().size() > 1) {
                addBtn.setDisable(false);
                columnsList.remove(columnsList.size()-1);
                addColumnPane.getChildren().remove(addColumnPane.getChildren().size()-1);
                if(addColumnPane.getChildren().size() == 1) remBtn.setDisable(true);
            }
        });
        // need to repair the task into new column
        doneBtn.setOnAction(e -> {

            ArrayList<String> seen = new ArrayList<>();
            for(TextField each : columnsList) {
                if(each.getText().equals("")) {
                    errorText.setText("Input invalid: column name cannot be empty.");
                    return;
                }
                else if(seen.contains(each.getText())) {
                    errorText.setText("Input invalid: column name cannot be duplicated.");
                    return;
                }
                seen.add(each.getText());
            }

            if(nameField.getText().equals("")) {errorText.setText("Input invalid: project name cannot be empty. "); return; }
            project.setName(nameField.getText());// set name
            // get old and new columns
            ArrayList<String> preColumnList = project.getColumns();
            ArrayList<String> newColumnList = new ArrayList<>();
            for(TextField each : columnsList) {
                newColumnList.add(each.getText());
            }
            // map old column to new column
            // if new list is shorter, pair with null
            // if new list is longer, ignore
            Map<String, String> oldToNew = new HashMap<>();
            for(int i = 0; i < preColumnList.size(); i++) {
                if(i < newColumnList.size())
                    oldToNew.put(preColumnList.get(i), newColumnList.get(i));
                else oldToNew.put(preColumnList.get(i), null);
            }
            // adjust status of task
            // temp will holds task for status changing
            TreeSet<TaskModel> temp = new TreeSet<>();
            for(TaskModel each : project.getTaskSet()) {
                temp.add(new TaskModel(each));
            }
            for(TaskModel each : temp) {
                // set the new status
                for(String oldColumnName : oldToNew.keySet()) {
                    sop("Name: " + each.getName() + " : " + each.getStatus());
                    if(each.getStatus().equals(oldColumnName)) {
                        each.setStatus(oldToNew.get(oldColumnName));
                    }
                }
            }

            TreeSet<TaskModel> temp1 = new TreeSet<>();
            for(TaskModel each : temp) {
                temp1.add(new TaskModel(each));
            }
            project.setTaskSet(temp);
            // if task status is null remove it from task set
            for(TaskModel each : temp1) {
                if(each.getStatus() == null)
                    project.removeTask(each);
            }
            project.setColumns(newColumnList);
            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });

        layout.add(texts, 0,0);
        layout.add(wrapper, 1, 0);

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
        columnHolder.setMinSize(stage.getWidth(), stage.getHeight());
        columnHolder.setStyle("-fx-background-color: #FFFFFF");

        ArrayList<VBox> columnsList = new ArrayList<>();
        ArrayList<TaskView> taskViews = new ArrayList<>();

        for(TaskModel each : project.getTaskSet()) {
            taskViews.add(new TaskView(this.stage, project, each));
        }

        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(20);
        shadow.setOffsetY(10);

        for (String each : project.getColumns()) {
            // each column is a VBox
            VBox column = new VBox(12);
            column.setId(each); // id of column is column name, used for drag and drop
            Button addTaskBtn = new Button("+");
            addTaskBtn.setId(each);
            addTaskBtn.setStyle("-fx-background-color: #FFFFFF");
            addTaskBtn.setFont(Font.font("verdana", FontWeight.BLACK, 30));
            addTaskBtn.setEffect(shadow);
            addTaskBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                addTaskBtn.setStyle("-fx-background-color: dodgerblue");
                addTaskBtn.setTextFill(Main.RandomColorGenerator());
            });
            addTaskBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                addTaskBtn.setStyle("-fx-background-color: #FFFFFF");
                addTaskBtn.setTextFill(Color.BLACK);
            });
            addTaskBtn.setOnAction(e -> {
                TaskModel taskModel = new TaskModel();
                // create a task under the add Btn of that column
                taskModel.setStatus(addTaskBtn.getId());
                TaskView taskView = new TaskView(this.stage, taskBoard.getCurrentProjectModel(), taskModel);
                taskView.createTask();
            });
            column.setPadding(new Insets(12));
            column.setId(each);// each VBox has an ID same as column name
            Text cName = new Text(each);
            cName.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 30));
            cName.setFill(Main.RandomColorGenerator());
            column.getChildren().addAll(cName, addTaskBtn);
            columnsList.add(column);
        }
//        sop("pj TS size;: "+taskSet.size());
        for (TaskView each : taskViews) {
            for (VBox column : columnsList) {
                if (column.getId().equals(each.getTask().getStatus())) {
                    column.getChildren().add(each.getTaskForm());
                }
            }
        }
        for(VBox each : columnsList) {
            columnHolder.getChildren().addAll(each);
        }

        //UI
        for(VBox each : columnsList) {
            //UI part
            each.setPrefSize(180, Main.WINDOWHIGHT);

        }
        // this delete area will be used in drag and drop function
        VBox deleteArea = new VBox(20);
        deleteArea.setId("delete");
        deleteArea.setMinSize(150, Main.WINDOWHIGHT);
        Label deleteLb = new Label("X");
        deleteLb.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 300));
        deleteLb.setTextFill(Color.WHITE);
        deleteArea.getChildren().add(deleteLb);
        deleteArea.setAlignment(Pos.CENTER);
        columnHolder.getChildren().add(deleteArea);

        return columnHolder;
    }
    public static void sop(Object x){System.out.println(x);}
}
