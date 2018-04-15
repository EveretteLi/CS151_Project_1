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
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

/**
 * Task class
 */
public class Task implements Comparable<Task> {
    public String name, description, status, dueDate;
    private GridPane taskForm;
    private Project onProject;// the project this task'll be in

    public Task(Project project) {
        this.onProject = project;
        this.dueDate = "0/0/0";
        createTask();
    }
    public Task(Project project, String name, String description, String status, String dueDate) {
        this.onProject = project;
        this.name = name;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }
    public Task(){}

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getDueDate() {
        return dueDate;
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
        ddField.setPromptText("MM/DD/YY");
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
            onProject.getTaskSet().add(this);
            onProject.loadColumn();
            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });

        stage.setScene(new Scene(layout, Main.POPUPWIDTH, Main.POPUPHIGHT));
        stage.show();
    }

    public GridPane getTaskInGridPane() {
        GridPane taskReport  = new GridPane();
        taskReport.setBackground(
                new Background(new BackgroundFill(Color.AQUA, CornerRadii.EMPTY, Insets.EMPTY)));
        Text name = new Text("Task Name: " + this.name);
        Text description = new Text("Description: " + this.description);
        Text dueDate = new Text("Due Date: " + this.dueDate);
        taskReport.add(name, 0,0);
        taskReport.add(description, 0, 1);
        taskReport.add(dueDate, 0, 2);
        taskReport.setOnMouseClicked(e -> {
            editTask();
        });
        return taskReport;
    }

    public void editTask() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(this.name);
        // create grid panel that holds everything
        GridPane layout = new GridPane();
        // each line will be in a HBox
        // name
        HBox name = new HBox(8);
        Text nameText = new Text("Task name:");
        TextField nameField = new TextField(this.name);
        name.getChildren().addAll(nameText, nameField);
        layout.add(name, 0,0);
        // description
        HBox description = new HBox(8);
        Text desText = new Text("Task Description:");
        TextField desField = new TextField(this.description);
        description.getChildren().addAll(desText, desField);
        layout.add(description, 0,1);
        // status
        ChoiceBox<String> status = new ChoiceBox<>();
        status.getItems().addAll(onProject.getColumns());
        status.setValue(this.status);
        layout.add(status, 0, 2);
        // due date
        HBox dueDate = new HBox(8);
        Text ddText  = new Text("Due Date");
        TextField ddField = new TextField(this.dueDate);
        dueDate.getChildren().addAll(ddText, ddField);
        layout.add(dueDate, 0, 3);

        // create and Cancel button
        HBox createAndCancel = new HBox(8);
        Button doneBtn = new Button("Done");
        Button cancelBtn = new Button("Cancel");
        createAndCancel.getChildren().addAll(doneBtn, cancelBtn);
        layout.add(createAndCancel, 0,4);
        doneBtn.setOnAction(e -> {
            this.name = nameField.getText();
            this.description = desField.getText();
            this.status = status.getValue();
            this.dueDate = ddField.getText();
            this.taskForm = layout;
            Set<Task> temp = onProject.getTaskSet();
            TreeSet<Task> newTaskSet = new TreeSet<>();
            for(Task each : temp) {
                newTaskSet.add(each);
            }
            onProject.setTaskSet(newTaskSet);
            onProject.loadColumn();
            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });

        stage.setScene(new Scene(layout, Main.POPUPWIDTH, Main.POPUPHIGHT));
        stage.show();
    }


    public int compareTo(Task that) {
        // get 2 string arrays
        String[] thatIntegerString = that.getDueDate().split("/");
        String[] thisIntegerString = this.getDueDate().split("/");
        sop("that: "+Arrays.toString(thatIntegerString));
        sop("this: "+Arrays.toString(thisIntegerString));
        // convert them to int[]
        int[] thatIntArray = new int[thatIntegerString.length];
        int[] thisIntArray = new int[thisIntegerString.length];
        for(int i = 0; i < thatIntArray.length; i++)
            thatIntArray[i] = Integer.parseInt(thatIntegerString[i]);
        for(int i = 0; i < thisIntArray.length; i++)
            thisIntArray[i] = Integer.parseInt(thisIntegerString[i]);
        sop("int: "+Arrays.toString(thatIntArray));
        sop("int: "+Arrays.toString(thisIntArray));

        //year
        if(thisIntArray[2] == thatIntArray[2]) {
            // month
            if(thisIntArray[0] == thatIntArray[0]) {
                //day
                return thisIntArray[1] - thatIntArray[1];
            }
            else{
                return thisIntArray[0] - thatIntArray[0];
            }
        }
        else{
            return thisIntArray[2] - thatIntArray[2];
        }
    }
    @Override
    public boolean equals(Object ob) {
        if(!(ob instanceof Task)) return false;
        Task that = (Task) ob;
        // same name, status, due date
        if(name.equals(that.name) && status.equals(that.status) && (this.compareTo(that) == 0))
            return true;
        return false;
    }
    @Override
    public int hashCode(){
        Object[] getHash = new Object[]{name, description, status, dueDate};
        return getHash.hashCode();
    }
    @Override
    public String toString() {
        StringBuffer sf = new StringBuffer();
        sf.append(this.getName() +" : " + this.status + " : " + this.dueDate);
        return sf.toString();
    }

    public static void sop(Object x){ System.out.println(x);}

    public static void main(String[] args){
        TreeSet<Integer> i = new TreeSet<>();
        i.add(1);
        i.add(2);
        i.add(3);
        sop(i);
        i.remove(2);
        i.add(0);
        sop(i);

    }

}
