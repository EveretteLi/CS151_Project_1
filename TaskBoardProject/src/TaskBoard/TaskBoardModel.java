package TaskBoard;

import java.util.ArrayList;

public class TaskBoardModel {
    private String name;// the name of the task board
    private ArrayList<ProjectModel> projectList;// list of project it holds
    private String filename;// file name
    private ProjectModel currentProjectModel;// the current showing project
    ArrayList<ModelListener> listeners = new ArrayList<>();

    public void attach(ModelListener listener) {
        listeners.add(listener);
    }
    public void updateAll() {
        Main.DIRTY = true;

        if (!listeners.isEmpty())
            listeners.get(0).update();
    }

    public TaskBoardModel() {
        this.name = "Task Board 1";
        this.projectList = new ArrayList<>();
        this.filename = "";
    }

    public void setCurrentProjectModel(ProjectModel currentProjectModel) {
        this.currentProjectModel = currentProjectModel;
        updateAll();
    }
    public void setName(String name) {
        this.name = name;
        updateAll();
    }
    public void setFilename(String filename) {
        this.filename = filename;
        updateAll();
    }
    public void setProjectList(ArrayList<ProjectModel> projectList) {
        this.projectList = projectList;
        updateAll();
    }
    public ProjectModel getCurrentProjectModel() {
        return currentProjectModel;
    }
    public String getName() {
        return name;
    }
    public ArrayList<ProjectModel> getProjectList() {
        return projectList;
    }
    public String getFilename() {
        return filename;
    }
    public void addProject(ProjectModel project) {
        projectList.add(project);
        updateAll();
    }
    public void removeProject(ProjectModel project) {
        projectList.remove(project);
        updateAll();
    }


}
