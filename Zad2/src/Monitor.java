import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


//rozwiązanie na 2 zmiennych typu boolean bo na lock.haswaiters może się zakleszczyć

public class Monitor {
    private int MAX_BUFFER;
    private int max_elements;
    private int currentValue=0;

    private ReentrantLock lock=new ReentrantLock();
    private Condition FirstProducer=lock.newCondition();
    private Condition Producers=lock.newCondition();
    private Condition FirstConsumer=lock.newCondition();
    private Condition Consumers=lock.newCondition();

    boolean firstProducerBool=false;
    boolean firstConsumerBool=false;

    public Monitor(int max_buffer, int max_elements){
        this.MAX_BUFFER=max_buffer;
        this.max_elements=max_elements;
    }

    public void produce(int id, int ile) throws InterruptedException {
        lock.lock();
        try{
            while(firstProducerBool){
                Producers.await();
            }
            while (currentValue+ile>MAX_BUFFER) {
                firstProducerBool=true;
                FirstProducer.await();
            }
            firstProducerBool=false;
            currentValue+=ile;
            System.out.println("   Producent "+id+" wyprodukowal "+ile);
            FirstConsumer.signal();
            Producers.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consume(int id, int ile) throws InterruptedException {
        lock.lock();
        try{
            while(firstConsumerBool){
                Consumers.await();
            }
            while (currentValue-ile<0) {
                firstConsumerBool=true;
                FirstConsumer.await();
            }
            firstConsumerBool=false;
            currentValue-=ile;
            System.out.println("Konsument "+id+" skonsumował "+ile);
            FirstProducer.signal();
            Consumers.signal();
        } finally {
            lock.unlock();
        }
    }

}

