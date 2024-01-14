import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

public class Bufor implements CSProcess {
    private final One2OneChannelInt prodChannel;
    private final One2OneChannelInt consChannel;
    private int currentValue=0;
    private final int id;
    private final boolean endFlag=false;

    public Bufor(int id, int buffSize, One2OneChannelInt prodChannel, One2OneChannelInt consChannel){
        this.id=id;
        this.prodChannel =prodChannel;
        this.consChannel =consChannel;
    }
    @Override
    public void run() {
        Guard[] guards ={prodChannel.in(), consChannel.in()};
        Alternative alt=new Alternative(guards);
        while (true){
            int index=alt.select();
            if(index==0){
                int a= prodChannel.in().read();
                if(a==Integer.MAX_VALUE){
                    break;
                }
                currentValue+=a;
            }
            else if(index==1){
                int a=consChannel.in().read();
                currentValue-=a;
            }


            System.out.println("Zajętość bufora "+id+": "+currentValue);
        }
        System.out.println("Bufor kończy działanie");
    }
}
