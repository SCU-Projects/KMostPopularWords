import implementation.Controller;
import implementation.Solution;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import static implementation.Constants.logFileName;

public class Main {

    public static void main(String[] args) {

        FileOutputStream f = null;
        try {
            f = new FileOutputStream(logFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.setOut(new PrintStream(f));
        Controller.start();
    }
}
