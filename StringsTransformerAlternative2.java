package taboola.israelrozen.solution_3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StringsTransformerAlternative2 {

    private static final int NUMBER_OF_THREADS = 10;

    private List<String> data = new ArrayList<String>();

    public StringsTransformerAlternative2(List<String> startingData) {
        this.data = startingData;
    }


    private void forEach(StringFunction function) {
        List<String> newData = new ArrayList<String>();
        for (String str : data) {
            newData.add(function.transform(str));
        }
        data = newData;
    }

    /*
     * The second approach here is to iterate the StringFunctions and for each one of them 
     * run the iterated transformation over all the strings
     * The disadvantage here is that latch.await will wait untill all tasks 
     * (that are running simutaniously under the thread pool) will end..
     */

    public List<String> transform(final List<StringFunction> functions) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        for (final StringFunction stringFunction : functions) {
            final CountDownLatch latch = new CountDownLatch(data.size());
            
            for (final String str : data) {
                executor.execute(new Runnable() {
                    public void run() {
                        stringFunction.transform(str);
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        return data;
    }

    public static interface StringFunction {
        public String transform(String str);
    }
} 