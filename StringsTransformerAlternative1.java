package taboola.israelrozen.solution_3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StringsTransformerAlternative1 {

    private static final int NUMBER_OF_THREADS = 10;

    private List<String> data = new ArrayList<String>();

    public StringsTransformerAlternative1(List<String> startingData) {
        this.data = startingData;
    }


    private void forEach(StringFunction function) {
        List<String> newData = new ArrayList<String>();
        for (String str : data) {
            newData.add(function.transform(str));
        }
        data = newData;
    }

    /**
     * The first and preferreble approach is to use the ThreadPool (fixed length that can be configured) 
     * and a Runnable task that will iterate and apply all transformations for each string 
     * this way we are controlling the transformations order on one hand  
     * and preventing context switches on the other hand as transformations are not being changed
     * Strings are the items that being changed and we are handeling them seperatly as we should. 
     **/

    public List<String> transform(final List<StringFunction> functions) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        for (String str : data) {
            final String textToTransform = str;
            executor.execute(new Runnable() {
                public void run() {
                    for (StringFunction stringFunction : functions) {
                        stringFunction.transform(textToTransform);
                    }
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        return data;
    }

    public static interface StringFunction {
        public String transform(String str);
    }
} 
