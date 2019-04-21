package implementation.multithreading;

import implementation.Solution;
import implementation.Solution.WordCount;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import static implementation.Constants.*;


public class Controller {
    //private static BlockingQueue<String> queue;
    private static Collection<Thread> producerThreadCollection, allThreadCollection;

    //public static void main(String[] args) {
    public static void start() {
        LocalDateTime start = LocalDateTime.now();
        System.out.println("Starting:"+ start.toLocalTime());
        //String fileName = "F:\\BigData\\data_1GB.txt";
        startWorking();
//        List<String> kMostFrequentWords = Solution.getKMostFrequentWords(fileName);
//        for(String word : kMostFrequentWords){
//            System.out.println(word);
//        }
        LocalDateTime end = LocalDateTime.now();
        System.out.println("Ending:"+ end.toLocalTime());
        System.out.println("Took:"+ ChronoUnit.SECONDS.between(start, end));
    }

    private static void startWorking() {
        producerThreadCollection = new ArrayList<Thread>();
        allThreadCollection = new ArrayList<Thread>();
        queue = new LinkedBlockingDeque<String>();
        System.out.println("Starting P & C:"+LocalDateTime.now().toLocalTime());
        createAndStartProducers();
        createAndStartConsumers();
        System.out.println("Ending P & C:"+LocalDateTime.now().toLocalTime());
        for(Thread t: allThreadCollection){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Controller finished");
        List<WordCount> result = Solution.getResult(k);
        for(WordCount word : result)
            System.out.println(word.getWord() + "->" + word.getCount());
        System.out.println("Task finished at"+LocalDateTime.now());

    }

    private static void createAndStartProducers(){
        //Solution.produceLinesToQueue();
        for(int i = 1; i <= NUMBER_OF_PRODUCERS; i++){
            Producer producer = new Producer(queue);
            Thread producerThread = new Thread(producer,"producer-"+i);
            producerThreadCollection.add(producerThread);
            producerThread.start();
        }
        allThreadCollection.addAll(producerThreadCollection);
    }

    private static void createAndStartConsumers(){
        for(int i = 0; i < NUMBER_OF_CONSUMERS; i++){
            Thread consumerThread = new Thread(new Consumer(queue), "consumer-"+i);
            allThreadCollection.add(consumerThread);
            consumerThread.start();
        }
    }

    public static boolean isProducerAlive(){
        for(Thread t: producerThreadCollection){
            if(t.isAlive())
                return true;
        }
        return false;
    }
}
