package implementation;

import java.util.concurrent.BlockingQueue;

public class Constants {
    //Constants file to store the runtime configuration of the program
	public static Integer BUFFER_CAPACITY = 100000;
	public static Integer k = 100;
	public static Integer fileSize = 1;
	public static String basePath = "/Users/reshmasubramaniam/Downloads/DataSet";
	public static String inputFile = "/data_"+ fileSize +"GB.txt";
	public static String fileName = basePath + inputFile;
	public static String logFileName = basePath + "/log_" + String.valueOf(BUFFER_CAPACITY) + "_" + inputFile.substring(1);
	public static final int NUMBER_OF_PRODUCERS = 1;
	public static final int NUMBER_OF_CONSUMERS = Runtime.getRuntime().availableProcessors() - NUMBER_OF_PRODUCERS;
	public static BlockingQueue<String> queue;
}
