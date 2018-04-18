package TaskBoard;

import java.io.*;

public class SaveLoad implements Serializable {
    private String test;
    public SaveLoad() {
        test = "this is a string";
    }

    public void save() throws IOException {
        sop("Creating output file...");
        FileOutputStream fileOut = new FileOutputStream("saveTest.ser");
        sop("Done");
        sop("Creating Object file...");
        ObjectOutputStream oos = new ObjectOutputStream(fileOut);
        sop("Done");
        sop("Writing...");
        oos.writeObject(test);
        sop("Done");
        sop("Closing...");
        oos.close();
        fileOut.close();
        sop("Done");
    }

    public void load() throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream("saveTest.ser");
        ObjectInputStream ois = new ObjectInputStream(fileIn);
        this.test = (String)ois.readObject();
    }

    @Override
    public String toString() {
        return this.test;
    }

    public static void sop(Object x) {System.out.println(x);}

    public static void main(String[] args) {
        SaveLoad sl = new SaveLoad();
        sop("Ori: "+sl.test);
        try{
            sl.save();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
        sop("================");
        sl.test = "change has been made here";
        sop("Change: "+sl.test);
        try{
            sl.load();
        } catch(IOException e){
            System.out.println(e.getMessage());
        } catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        sop("=======DONE=======");
        sop("load: "+sl.test);
    }

}
