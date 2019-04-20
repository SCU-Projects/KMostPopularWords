package implementation;

import java.util.concurrent.BlockingQueue;

public class Constants {
    //8-0s
    public static Integer BUFFER_CAPACITY = 100000;
    public static Integer k = 100;
    public static String fileName = "F:\\BigData\\data_8GB.txt";
    public static String logFileName = "F:\\BigData\\log.txt";
    public static final int NUMBER_OF_PRODUCERS = 1;
    public static final int NUMBER_OF_CONSUMERS = Runtime.getRuntime().availableProcessors() - NUMBER_OF_PRODUCERS;
    public static BlockingQueue<String> queue;
    public static boolean producerFinished = false;
    //Runtime.getRuntime().availableProcessors()
    //String fileName = "F:\\BigData\\data_1GB.txt";

}
