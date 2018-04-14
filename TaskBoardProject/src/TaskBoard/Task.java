package TaskBoard;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Task class
 */
public class Task {
    private String name, description, status, dueDate;
    private GridPane taskForm;
    private Project onProject;// the project this task'll be in

    public Task(Project project) {
        this.onProject = project;
        createTask();
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    /**
     * create pop-up to make new task
     */
    public void createTask() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Task");
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
        status.getItems().addAll(onProject.getColumns());
        status.setValue(status.getItems().get(0));
        layout.add(status, 0, 2);
        // due date
        HBox dueDate = new HBox(8);
        Text ddText  = new Text("Due Date");
        TextField ddField = new TextField();
        dueDate.getChildren().addAll(ddText, ddField);
        layout.add(dueDate, 0, 3);

        // create and Cancel button
        HBox createAndCancel = new HBox(8);
        Button createBtn = new Button("Create");
        Button cancelBtn = new Button("Cancel");
        createAndCancel.getChildren().addAll(createBtn, cancelBtn);
        layout.add(createAndCancel, 0,4);
        createBtn.setOnAction(e -> {
            this.name = nameField.getText();
            this.description = desField.getText();
            this.status = status.getValue();
            this.dueDate = ddField.getText();
            this.taskForm = layout;
            onProject.loadColumn();
            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            System.out.println("Cancel");
            stage.close();
        });

        stage.setScene(new Scene(layout, Main.POPUPWIDTH, Main.POPUPHIGHT));
        stage.show();
    }

    public GridPane getTaskInGridPane() {
        GridPane taskReport  = new GridPane();
        Text name = new Text("Task Name: " + this.name);
        Text description = new Text("Description: " + this.description);
        Text dueDate = new Text("Due Date: " + this.dueDate);
        taskReport.add(name, 0,0);
        taskReport.add(description, 0, 1);
        taskReport.add(dueDate, 0, 2);
        return taskReport;
    }

    public static void sop(Object x){System.out.println(x);}

}
