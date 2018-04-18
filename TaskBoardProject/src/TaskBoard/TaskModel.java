package TaskBoard;

import java.util.ArrayList;

/**
 * task model
 */
public class TaskModel implements Comparable<TaskModel> {
    private String name, description, dueDate, status;
    ArrayList<ModelListener> listeners = new ArrayList<>();

    public TaskModel(){
        this.name = "Task 1";
        this.description = "";
        this.dueDate = "0/0/0";
        this.status = "";
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getDueDate() {
        return dueDate;
    }
    public String getStatus() {
        return status;
    }
    public void setName(String name) {
        this.name = name;
        updateAll();
    }
    public void setDescription(String description) {
        this.description = description;
        updateAll();
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
        updateAll();
    }
    public void setStatus(String status) {
        this.status = status;
        updateAll();
    }

    /**
     * Attach the UI class into the listeners
     * so that if task changed, updateAll will update task in all it's UI class
     * @param listener the UI class that passed in
     */
    public void attach(ModelListener listener) {
        listeners.add(listener);
    }

    /**
     * update all UI
     */
    public void updateAll() {
        for(ModelListener each : listeners) {
            each.update();
        }
    }

    public int compareTo(TaskModel that) {
        // get 2 string arrays
        String[] thatIntegerString = that.getDueDate().split("/");
        String[] thisIntegerString = this.getDueDate().split("/");
//        sop("that: "+Arrays.toString(thatIntegerString));
//        sop("this: "+Arrays.toString(thisIntegerString));
        // convert them to int[]
        int[] thatIntArray = new int[thatIntegerString.length];
        int[] thisIntArray = new int[thisIntegerString.length];
        for(int i = 0; i < thatIntArray.length; i++)
            thatIntArray[i] = Integer.parseInt(thatIntegerString[i]);
        for(int i = 0; i < thisIntArray.length; i++)
            thisIntArray[i] = Integer.parseInt(thisIntegerString[i]);
//        sop("int: "+Arrays.toString(thatIntArray));
//        sop("int: "+Arrays.toString(thisIntArray));

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
        if(!(ob instanceof TaskModel)) return false;
        TaskModel that = (TaskModel) ob;
        // same name, status, due date
        if(name.equals(that.name) && status.equals(that.status) && (this.compareTo(that) == 0))
            return true;
        return false;
    }
    @Override
    public int hashCode(){
        int result = 17;
        result += 31 * this.name.hashCode();
        result += 31 * this.description.hashCode();
        result += 31 * this.status.hashCode();
        result += 31 * this.dueDate.hashCode();
        return result;
    }

    // debug use
    @Override
    public String toString() {
        StringBuffer sf = new StringBuffer();
        sf.append(this.name +" : " + this.status + " : " + this.dueDate);
        return sf.toString();
    }
}
