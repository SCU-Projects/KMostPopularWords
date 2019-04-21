import implementation.Solution;
import implementation.Solution.WordCount;
import implementation.multithreading.Controller;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import static implementation.Constants.*;

public class Main {

    public static void main(String[] args) {

        FileOutputStream f = null;
        try {
            f = new FileOutputStream(logFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.setOut(new PrintStream(f));
        Controller.start(); //to enable multi-threading
//        List<WordCount> result  = Solution.getKMostFrequentWords(fileName);
//        
//        for(WordCount word : result)
//        	System.out.println(word.getWord() +"->" + word.getCount());
    }
}
