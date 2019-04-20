package implementation;

import sun.rmi.server.LoaderHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static implementation.Constants.BUFFER_CAPACITY;
import static implementation.Constants.fileName;

public class Producer implements Runnable {

    private final BlockingQueue<String> queue;

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
//BUFFERED READER
//        List<String> result = new ArrayList<>();
//        BufferedReader br = null;
//        String line;
//        try {
//
//            br = new BufferedReader(new FileReader(fileName), BUFFER_CAPACITY);
//            while ((line = br.readLine()) != null) {
//                queue.put(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (br != null)
//                    br.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
// NIO
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
    }
}
