package TaskBoard;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Set;


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

        // create grid panel that holds everything
        GridPane layout = new GridPane();
        // each line will be in a HBox
        // name
        HBox name = new HBox(8);
        Text nameText = new Text("Task name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter task name");
        name.getChildren().addAll(nameText, nameField);
        layout.add(name, 0,0);
        // description
        HBox description = new HBox(8);
        Text desText = new Text("Task Description:");
        TextField desField = new TextField();
        //description.setMinSize(100, 200);
        desField.setPromptText("Enter task description");
        description.getChildren().addAll(desText, desField);
        layout.add(description, 0,1);
        // status
        ChoiceBox<String> status = new ChoiceBox<>();
        status.getItems().addAll(project.getColumns());
        status.setValue(status.getItems().get(0));
        layout.add(status, 0, 2);
        // due date
        HBox dueDate = new HBox(8);
        Text ddText  = new Text("Due Date");
        TextField ddField = new TextField();
        ddField.setPromptText("MM/DD/YY");
        dueDate.getChildren().addAll(ddText, ddField);
        layout.add(dueDate, 0, 3);

        // create and Cancel button
        HBox createAndCancel = new HBox(8);
        Button createBtn = new Button("Create");
        Button cancelBtn = new Button("Cancel");
        createAndCancel.getChildren().addAll(createBtn, cancelBtn);
        layout.add(createAndCancel, 0,4);

        // =============End make screen=============

        createBtn.setOnAction(e -> {
            task.setName(nameField.getText());
            task.setDescription(desField.getText());
            task.setDueDate(ddField.getText());
            task.setStatus(status.getValue());
            // add the changed task back in
            project.addTask(task);
            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });
        stage.setScene(new Scene(layout, Main.POPUPWIDTH, Main.POPUPHIGHT));
        stage.show();
    }

    /**
     * return a solution show to show task
     * @param taskReport the layout that being modified
     * @return a solution of showing it
     */
    public GridPane showTask(GridPane taskReport, TaskModel task) {
        taskReport.setBackground(
                new Background(new BackgroundFill(Color.AQUA, CornerRadii.EMPTY, Insets.EMPTY)));
        Text name = new Text("Task Name: " + task.getName());
        Text description = new Text("Description: " + task.getDescription());
        Text dueDate = new Text("Due Date: " + task.getDueDate());
        taskReport.add(name, 0,0);
        taskReport.add(description, 0, 1);
        taskReport.add(dueDate, 0, 2);
        taskReport.setOnMouseClicked((e -> editTask()));
        return taskReport;
    }

    public void editTask() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(task.getName());

        // =============Start make screen=============

        // create grid panel that holds everything
        GridPane layout = new GridPane();
        // each line will be in a HBox
        // name
        HBox name = new HBox(8);
        Text nameText = new Text("Task name:");
        TextField nameField = new TextField(task.getName());
        name.getChildren().addAll(nameText, nameField);
        layout.add(name, 0,0);
        // description
        HBox description = new HBox(8);
        Text desText = new Text("Task Description:");
        TextField desField = new TextField(task.getDescription());
        description.getChildren().addAll(desText, desField);
        layout.add(description, 0,1);
        // status
        ChoiceBox<String> status = new ChoiceBox<>();
        status.getItems().addAll(project.getColumns());
        status.setValue(task.getStatus());
        layout.add(status, 0, 2);
        // due date
        HBox dueDate = new HBox(8);
        Text ddText  = new Text("Due Date");
        TextField ddField = new TextField(task.getDueDate());
        dueDate.getChildren().addAll(ddText, ddField);
        layout.add(dueDate, 0, 3);

        // create and Cancel button
        HBox createAndCancel = new HBox(8);
        Button doneBtn = new Button("Done");
        Button cancelBtn = new Button("Cancel");
        createAndCancel.getChildren().addAll(doneBtn, cancelBtn);
        layout.add(createAndCancel, 0,4);

        // =============End make screen=============

        doneBtn.setOnAction(e -> {
            project.removeTask(task);
            task.setName(nameField.getText());
            task.setDescription(desField.getText());
            task.setStatus(status.getValue());
            task.setDueDate(ddField.getText());
            project.addTask(task);
            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });
        stage.setScene(new Scene(layout, Main.POPUPWIDTH, Main.POPUPHIGHT));
        stage.show();
    }

    public static void sop(Object x){ System.out.println(x);}

    public static void main(String[] args){
    }

}
