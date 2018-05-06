package TaskBoard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;


/**
 * Task view class
 */
public class TaskView implements ModelListener {
    private TaskModel task;// the task model this view class rely on
    private GridPane taskForm;// the layout where the task got displayed
    private ProjectModel project;// the project where it has been aggregated
    private Stage stage;// the stage it's on

    public TaskView(Stage stage, ProjectModel project, TaskModel task) {
        this.task = task;
        this.project = project;
        taskForm = showTask(new GridPane(), task);
        this.stage = stage;
        task.attach(this);
    }

    public TaskModel getTask() {
        return task;
    }
    public GridPane getTaskForm() { return taskForm; }
    public Stage getStage() { return stage; }
    public ProjectModel getProject() { return project; }
    public void setProject(ProjectModel project) { this.project = project; }
    public void setStage(Stage stage) { this.stage = stage; }
    public void setTaskForm(GridPane taskForm) { this.taskForm = taskForm; }
    public void setTask(TaskModel task) {
        this.task = task;
    }

    /**
     * How task view would update
     */
    public void update() {
        this.taskForm = showTask(taskForm, task);
        project.updateAll();
    }
    /**
     * create pop-up to get task information from users
     */
    public void createTask() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Task");
        // =============Start make screen=============
        Color color = Main.RandomColorGenerator();

        GridPane createLayout = new GridPane();
        createLayout.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));



        VBox textBox = new VBox();
        textBox.setMinSize(Main.POPUPWIDTH/2, Main.POPUPHIGHT);
        Text newTaskText = new Text("New\nTask:");
        newTaskText.setFill(Color.WHITE);
        newTaskText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 100));
        textBox.getChildren().addAll(newTaskText);
        textBox.setAlignment(Pos.CENTER);
        textBox.setPadding(new Insets(12));

        // create grid panel that holds everything
        VBox layout = new VBox(30);
        layout.setPadding(new Insets(12));
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setPrefSize(Main.POPUPWIDTH/2, Main.POPUPHIGHT);
        layout.setAlignment(Pos.CENTER);

        // each line will be in a HBox
        // name
        HBox name = new HBox(8);
        Text nameText = new Text("Task name:");
        nameText.setFill(color);
        nameText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 20));
        TextField nameField = new TextField();
        nameField.setPromptText("Enter task name");
        name.getChildren().addAll(nameText, nameField);
        // status
        HBox statusBox = new HBox(8);
        Text statusText = new Text("Pick Status:");
        statusText.setFill(color);
        statusText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 20));
        ChoiceBox<String> status = new ChoiceBox<>();
        status.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        status.getItems().addAll(project.getColumns());
        status.setValue(task.getStatus());
        statusBox.getChildren().addAll(statusText, status);

        // due date
        HBox dueDate = new HBox(8);
        Text ddText  = new Text("Due Date");
        ddText.setFill(color);
        ddText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 20));
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        dueDate.getChildren().addAll(ddText, datePicker);


        // description
        VBox description = new VBox(8);
        Text desText = new Text("Task Description:");
        desText.setFill(color);
        desText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 20));
        TextArea desArea = new TextArea();
        desArea.setPrefSize(100, 150);
        desArea.setPromptText("Enter task description");
        description.getChildren().addAll(desText, desArea);

        // create and Cancel button
        HBox createAndCancel = new HBox(8);
        Button createBtn = new Button("Create");
        Button cancelBtn = new Button("Cancel");
        createBtn.setMinWidth(Main.POPUPWIDTH/4-30);
        cancelBtn.setMinWidth(Main.POPUPWIDTH/4-30);
        createAndCancel.getChildren().addAll(createBtn, cancelBtn);

        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(10);
        shadow.setOffsetY(3);

        for(Node each : createAndCancel.getChildren()) {
            each.setStyle("-fx-background-color: #FFFFFF");
            each.setEffect(shadow);
        }

        Text errorText = new Text("");
        errorText.setFill(Color.RED);

        layout.getChildren().addAll(name, statusBox, dueDate, description, createAndCancel, errorText);
        createLayout.add(textBox, 0,0);
        createLayout.add(layout, 1,0);
        // =============End make screen=============

        createBtn.setOnAction(e -> {
            if(nameField.getText().equals("")) {
                errorText.setText("Invalid input: task name cannot be empty.");
                return;
            }
            task.setName(nameField.getText());
            task.setDescription(desArea.getText());
            task.setDueDate(datePicker.getValue().toString());
            task.setStatus(status.getValue());
            // add the changed task back in
            project.addTask(task);
            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });
        stage.setScene(new Scene(createLayout, Main.POPUPWIDTH, Main.POPUPHIGHT));
        stage.show();
    }

    /**
     * return a solution show to show task
     * @param taskReport the layout that being modified
     * @return a solution of showing it
     */
    public GridPane showTask(GridPane taskReport, TaskModel task) {
        taskReport.setId(task.getName()); // set task name as id of taskReport, used in drag and drop later

        taskReport.setStyle("-fx-background-color: #FFFFFF");

        VBox reportBox = new VBox(8);
        reportBox.setPadding(new Insets(12));
        Text name = new Text("Task Name: " + task.getName());
        name.setFont(Font.font("verdana", FontWeight.BOLD, 15));
        name.setFill(Color.BLACK);
        Text description = new Text("Description: " + task.getDescription());
        description.setFont(Font.font("verdana", 15));
        description.setFill(Color.DARKGRAY);
        Text addDate = new Text("Add Date: " + task.getAddDate());
        addDate.setFont(Font.font("verdana", FontWeight.BOLD, 8));
        addDate.setFill(Color.BLACK);
        Text dueDate = new Text("Due Date: " + task.getDueDate());
        dueDate.setFont(Font.font("verdana", FontWeight.BOLD, 8));
        dueDate.setFill(Color.RED);
        reportBox.getChildren().addAll(name, description, addDate, dueDate);
        taskReport.add(reportBox, 0,0);
        taskReport.setOnMouseClicked((e -> editTask()));
        // UI part
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(10);
        shadow.setOffsetY(3);
        taskReport.setEffect(shadow);
        taskReport.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            taskReport.setStyle("-fx-background-color: #97ffbe");
        });
        taskReport.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            taskReport.setStyle("-fx-background-color: #FFFFFF");
        });

        // when drag on task report
        taskReport.setOnDragDetected(e -> {
            Dragboard db = taskReport.startDragAndDrop(TransferMode.MOVE);

            ClipboardContent cb = new ClipboardContent();
            cb.putString(taskReport.getId());

            db.setContent(cb);
            e.consume();
        });
        return taskReport;
    }

    public void editTask() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(task.getName());

        // =============Start make screen=============
        Color color = Main.RandomColorGenerator();

        GridPane editLayout = new GridPane();
        editLayout.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));



        VBox textBox = new VBox();
        textBox.setMinSize(Main.POPUPWIDTH/2, Main.POPUPHIGHT);
        Text newTaskText = new Text("New\nTask:");
        newTaskText.setFill(Color.WHITE);
        newTaskText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 100));
        textBox.getChildren().addAll(newTaskText);
        textBox.setAlignment(Pos.CENTER);
        textBox.setPadding(new Insets(12));

        // create grid panel that holds everything
        VBox layout = new VBox(30);
        layout.setPadding(new Insets(12));
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setPrefSize(Main.POPUPWIDTH/2, Main.POPUPHIGHT);
        layout.setAlignment(Pos.CENTER);

        // each line will be in a HBox
        // name
        HBox name = new HBox(8);
        Text nameText = new Text("Task name:");
        nameText.setFill(color);
        nameText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 20));
        TextField nameField = new TextField(task.getName());
        name.getChildren().addAll(nameText, nameField);
        // status
        HBox statusBox = new HBox(8);
        Text statusText = new Text("Pick Status:");
        statusText.setFill(color);
        statusText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 20));
        ChoiceBox<String> status = new ChoiceBox<>();
        status.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        status.getItems().addAll(project.getColumns());
        status.setValue(task.getStatus());
        statusBox.getChildren().addAll(statusText, status);

        // due date
        HBox dueDate = new HBox(8);
        Text ddText  = new Text("Due Date");
        ddText.setFill(color);
        ddText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 20));
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(task.getAddDate());
        datePicker.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        dueDate.getChildren().addAll(ddText, datePicker);


        // description
        VBox description = new VBox(8);
        Text desText = new Text("Task Description:");
        desText.setFill(color);
        desText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 20));
        TextArea desArea = new TextArea(task.getDescription());
        desArea.setPrefSize(100, 150);
        description.getChildren().addAll(desText, desArea);

        // create and Cancel button
        HBox doneAndCancel = new HBox(8);
        Button doneBtn = new Button("Done");
        Button cancelBtn = new Button("Cancel");
        doneBtn.setMinWidth(Main.POPUPWIDTH/4-30);
        cancelBtn.setMinWidth(Main.POPUPWIDTH/4-30);
        doneAndCancel.getChildren().addAll(doneBtn, cancelBtn);

        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(10);
        shadow.setOffsetY(3);

        for(Node each : doneAndCancel.getChildren()) {
            each.setStyle("-fx-background-color: #FFFFFF");
            each.setEffect(shadow);
        }

        Text errorText = new Text("");
        errorText.setFill(Color.RED);

        layout.getChildren().addAll(name, statusBox, dueDate, description, doneAndCancel, errorText);
        editLayout.add(textBox, 0,0);
        editLayout.add(layout, 1,0);
        // =============End make screen=============

        doneBtn.setOnAction(e -> {
            project.removeTask(task);
            task.setName(nameField.getText());
            task.setDescription(desArea.getText());
            task.setStatus(status.getValue());
            task.setDueDate(datePicker.getValue().toString());
            project.addTask(task);
            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });
        stage.setScene(new Scene(editLayout, Main.POPUPWIDTH, Main.POPUPHIGHT));
        stage.show();
    }

    public static void sop(Object x){ System.out.println(x);}

    public static void main(String[] args){
    }

}
