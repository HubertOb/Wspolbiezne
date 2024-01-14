//rozwiązanie na 3 lockach

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final int MAX_BUFFER=10;
    private static final int MAX_ELEM=5;
    private static final int CONSUMERS =5;
    private static final int PRODUCERS =5;

    private static List<Thread> producerList=new ArrayList<>();

    private static List<Thread> consumerList=new ArrayList<>();

    public static void main(String[] args) {
        Monitor monitor = new Monitor(MAX_BUFFER,MAX_ELEM);

        for (int i = 0; i < PRODUCERS; i++) {
            producerList.add(new Thread(new Producer(monitor,i, MAX_ELEM)));
        }
        producerList.add(new Thread((new Producer(monitor, 123465,MAX_ELEM))));

        for (int i = 0; i < CONSUMERS; i++) {
            consumerList.add(new Thread(new Consumer(monitor,i, MAX_ELEM)));
        }

        for (Thread thread:producerList){
            thread.start();
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }

        for (Thread thread: consumerList){
            thread.start();
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }
    }
}
