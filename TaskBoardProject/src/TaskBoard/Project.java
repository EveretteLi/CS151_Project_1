package TaskBoard;

import java.util.*;

/**
 * project class
 * a project can contains multiple columns
 * and also stores all the Tasks that has been added to columns
 */
public class Project {

    private String name;
    private ArrayList<String> columns;

    public Project(String name, ArrayList<String> columns) {
        this.name = name;
        this.columns = columns;
    }

    public String getName() { return this.name; }
    public void setName(String name) {this.name = name; }
    public ArrayList<String> getColumns() { return columns; }

    public void addColumn(String name) {
        columns.add(name);
    }
    public void setColumnsTo(ArrayList<String> newColumns) { this.columns = newColumns; }
}
