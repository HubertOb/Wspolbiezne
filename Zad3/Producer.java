import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.List;

public class Producer implements CSProcess {
    private final One2OneChannelInt prod2managerChannel;
    private final One2OneChannelInt manager2prodChannel;
    private final List<One2OneChannelInt> buffChannels;
    private final int id;

    public Producer(int id, One2OneChannelInt prod2managerChannel, One2OneChannelInt manager2prodChannel, List<One2OneChannelInt> buffChannels){
        this.prod2managerChannel=prod2managerChannel;
        this.manager2prodChannel=manager2prodChannel;
        this.buffChannels=buffChannels;
        this.id=id;
    }

    @Override
    public void run() {
        int i=10;
        while(i>0){
            prod2managerChannel.out().write(2);
            int buffIndex=manager2prodChannel.in().read();
            if(buffIndex!=-1){
                buffChannels.get(buffIndex).out().write(2);
                System.out.println("Producent "+id+" wstawia "+"2 "+"do bufora "+buffIndex);
                i--;
            }
            else{
                System.out.println("Producent "+id+" bufory zajete");
                i--;
            }
        }
        prod2managerChannel.out().write(Integer.MAX_VALUE);
        System.out.println("Producent "+id+" kończy działanie");
    }
}
