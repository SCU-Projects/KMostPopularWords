package implementation;

import java.util.concurrent.BlockingQueue;
import static implementation.Constants.producerFinished;

public class Consumer implements Runnable {

    private final BlockingQueue<String> queue;

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true){
            String line = queue.poll();

            if(line == null && !Controller.isProducerAlive())
                return;

            //if(line == null && producerFinished)
              //  return;

            if(line != null){
                Solution.processLine(line);
                //System.out.println(Thread.currentThread().getName()+" processing line: "+line);
            }

        }
    }
}
