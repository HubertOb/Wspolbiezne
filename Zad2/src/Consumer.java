import java.util.Random;

class Consumer implements Runnable {
    private Monitor monitor;
    private int id;
    private int max_elem;
    private Random random=new Random();

    public Consumer(Monitor monitor,int id, int max_elem) {
        this.id=id;
        this.monitor = monitor;
        this.max_elem=max_elem;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int random_num=random.nextInt(max_elem-1)+1;
                monitor.consume(id, random_num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}