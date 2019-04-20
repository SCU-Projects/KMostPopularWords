package implementation;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static implementation.Constants.*;

public class Solution {
    private static ConcurrentHashMap<String, Integer> wordCountMap = new ConcurrentHashMap<>();
    private static PriorityQueue<WordCount> minHeapPQ = new PriorityQueue<>(k, new WordCountComparator());
    static String[] words;

    public static List<String> getKMostFrequentWords(String filename){
        int k = 100;
        List<String> result = new ArrayList<>();
        //result = nioGetAllFrequentWords(k, filename);
        brGetFrequentWords(k, filename);
        //result = nioBufferedGetFrequentWords(k, filename);
        return getResult(k);
    }

    private static void multithreadedWorkers(){
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println(cores);
    }

    private static void brGetFrequentWords(int k, String fileName){
        List<String> result = new ArrayList<>();
        BufferedReader br = null;
        String line;
        try {

            br = new BufferedReader(new FileReader(fileName), 8000);
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static List<String> getResult(int k){
        return getKFrequentWords(k);
    }

    private static List<String> nioBufferedGetFrequentWords(int k, String fileName){
        //fixed size buffer 1024 -> 36 seconds, 2048 -> 39 seconds
        //i/o: 1024 -> 1037141,  2048 -> 518571, 20048 -> 52975 35s, 2000048 ->
        Integer count = 0;
        try
        {
            RandomAccessFile aFile = new RandomAccessFile(fileName,"r");
            FileChannel inChannel = aFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
            while(inChannel.read(buffer) > 0){
                System.out.println("S:"+LocalDateTime.now());
                buffer.flip();
                String content =  new String(buffer.array(),"UTF-8");
                processLine(content);
                buffer.clear(); // do something with the data and clear/compact it.
                count++;
                System.out.println("E:"+LocalDateTime.now());
            }
            inChannel.close();
            aFile.close();
        }
        catch (IOException exc)
        {
            System.out.println(exc);
            System.exit(1);
        }
        System.out.println("I/O:"+count);
        return getKFrequentWords(k);
    }

    private static List<String> nioMappedByteBufferGetFrequentWords(int k, String fileName){
        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile(fileName, "r");
            FileChannel inChannel = aFile.getChannel();
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            buffer.load();
            String content =  new String(buffer.array(),"UTF-8");
            processLine(content);
            buffer.clear(); // do something with the data and clear/compact it.
            inChannel.close();
            aFile.close();
        }
        catch (IOException e){
            System.out.println(e);
            System.exit(1);
        }
        return getKFrequentWords(k);
    }

    private static List<String> nioGetAllFrequentWords(int k, String fileName){

        File file = new File(fileName);
        byte [] fileBytes = new byte[0];
        try {
            fileBytes = Files.readAllBytes(file.toPath());
            String content =  new String(fileBytes,"UTF-8");
            processLine(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getKFrequentWords(k);
    }

    public static void produceLinesToQueue(){
        try
        {
            RandomAccessFile aFile = new RandomAccessFile(fileName,"r");
            FileChannel inChannel = aFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
            while(inChannel.read(buffer) > 0){
                buffer.flip();
                String content =  new String(buffer.array(),"UTF-8");
                //System.out.println(Thread.currentThread().getName()+" producing line: "+ LocalDateTime.now().toLocalTime());
                for(String line : content.split("\n|\r"))
                    if(line.length() > 0)
                        queue.put(line);
                buffer.clear(); // do something with the data and clear/compact it.
            }
            inChannel.close();
            aFile.close();
        }
        catch (IOException exc)
        {
            System.out.println(exc);
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        producerFinished = true;
    }

    private static void callWorkerThreadsToProcessLine(String line){
        words = line.split(" ");
        for(String word : words){
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            word = null;
        }
        line = null;
    }

    public static synchronized void processLine(String lineContent){
        callWorkerThreadsToProcessLine(lineContent);
        lineContent = null;
    }

    private static List<String> getKFrequentWords(int k){
        System.out.println("Starting the k:"+ LocalDateTime.now().toLocalTime());
        for(Map.Entry<String, Integer> keyValue : wordCountMap.entrySet()){
            if(minHeapPQ.size() >= k){
                if(minHeapPQ.peek().getCount() < keyValue.getValue()){
                    minHeapPQ.poll();
                    minHeapPQ.add(new WordCount(keyValue.getKey(), keyValue.getValue()));
                }
            }
            else
                minHeapPQ.add(new WordCount(keyValue.getKey(), keyValue.getValue()));
        }

        List<String> kFreqWords = new ArrayList<>();

        while(!minHeapPQ.isEmpty()){
            kFreqWords.add(minHeapPQ.poll().getWord());
        }

        return kFreqWords;
    }


    public static class WordCount{
       String word;
       Integer count;

       public WordCount(String word, Integer count){
           this.word  = word;
           this.count = count;
       }

       public String getWord(){
           return this.word;
       }

       public Integer getCount(){
           return this.count;
       }
    }
}
