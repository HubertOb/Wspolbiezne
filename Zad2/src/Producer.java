import java.util.Random;

class Producer implements Runnable {
    private Monitor monitor;
    private int id;
    private int max_elem;

    private Random random=new Random();

    public Producer(Monitor monitor, int id, int max_elem) {
        this.id=id;
        this.monitor = monitor;
        this.max_elem=max_elem;
    }

    @Override
    public void run() {
        while (true) {
            try {
//                int random_num=random.nextInt(max_elem-1)+1;
//                monitor.produce(id, random_num);
                if(id==123465){
                    monitor.produce(id,max_elem);
                }
                else{
                    monitor.produce(id,1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}