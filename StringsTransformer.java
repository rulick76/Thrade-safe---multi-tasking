package taboola.israelrozen.solution_3;

import java.util.*;

public class StringsTransformer {
    private List<String> data = new ArrayList<String>();

    public StringsTransformer(List<String> startingData) {
        this.data = startingData;
    }

    private void forEach(StringFunction function) {
        List<String> newData = new ArrayList<String>();
        for (String str : data) {
            newData.add(function.transform(str));
        }
        data = newData;
    }
    
    // The issue here is that we don't have control on the transformations order
    // The thread context is the transformation f()
    // On each thread we are iterating the strings and apply a transformation f()
    // All threads fired simutaniously 
    // morethanthis, this code is not thread safe and a context switch can occur here
    
    public List<String> transform(List<StringFunction> functions) throws InterruptedException {
        List<Thread> threads = new ArrayList<Thread>();
        for (final StringFunction f : functions) {
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    forEach(f);
                }
            }));
        }
        for (Thread t : threads) {
            t.join();
        }
        return data;
    }

    public static interface StringFunction {
        public String transform(String str);
    }
}
