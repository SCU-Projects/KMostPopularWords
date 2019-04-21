import implementation.Solution;
import implementation.Solution.WordCount;
import implementation.multithreading.Controller;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

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

        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter the file size to process:");
        fileSize = myObj.nextInt();
        System.out.println("Starting multi-threader buffered-reader for processing "+ fileSize + " GB file");
        Controller.start();                         //to run in a multi threaded consumer mode
        //startSingleThreadedBufferedReader();      //to run in a single threaded mode
    }

    static void startSingleThreadedBufferedReader(){
        List<WordCount> result  = Solution.getKMostFrequentWords(fileName);
        for(WordCount word : result)
        	System.out.println(word.getWord() +"->" + word.getCount());
    }
}
