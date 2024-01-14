import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//import static sun.util.locale.LocaleUtils.isEmpty;

public class Monitor {
    private int MAX_BUFFER;
    private int max_elements;
    private int currentValue=0;

    private ReentrantLock producersLock=new ReentrantLock();
    private ReentrantLock consumersLock=new ReentrantLock();
    private ReentrantLock commonLock=new ReentrantLock();
    private Condition condition = commonLock.newCondition();

    public Monitor(int max_buffer, int max_elements){
        this.MAX_BUFFER=max_buffer;
        this.max_elements=max_elements;
    }

    public void produce(int id, int ile) throws InterruptedException {
        producersLock.lock();
        try{
            commonLock.lock();
            while (currentValue+ile>MAX_BUFFER) {
                condition.await();
            }
            currentValue+=ile;
            System.out.println("   Producent "+id+" wyprodukowal "+ile);
            condition.signal();
        } finally {
            commonLock.unlock();
            producersLock.unlock();
        }
    }

    public void consume(int id, int ile) throws InterruptedException {
        consumersLock.lock();
        try{
            commonLock.lock();
            while (currentValue-ile<0) {
                condition.await();
            }
            currentValue-=ile;
            System.out.println("Konsument "+id+" skonsumował "+ile);
            condition.signal();
        } finally {
            commonLock.unlock();
            consumersLock.unlock();
        }
    }

}

