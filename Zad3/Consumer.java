import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.List;

public class Consumer implements CSProcess {
    private final One2OneChannelInt cons2managerChannel;
    private final One2OneChannelInt manager2consChannel;
    private final List<One2OneChannelInt> buffChannels;

    private final int id;

    public Consumer(int id, One2OneChannelInt cons2managerChannel,One2OneChannelInt manager2consChannel, List<One2OneChannelInt> buffChannels){
        this.cons2managerChannel=cons2managerChannel;
        this.manager2consChannel=manager2consChannel;
        this.buffChannels=buffChannels;
        this.id=id;
    }

    @Override
    public void run() {
        int i=10;
        while (i>0){
            cons2managerChannel.out().write(2);
            int buffIndex=manager2consChannel.in().read();
            if(buffIndex!=-1){
                buffChannels.get(buffIndex).out().write(2);
                System.out.println("Konsument "+id+" pobiera "+"2 "+"z bufora "+buffIndex);
                i--;
            }
            else{
                System.out.println("Konsument "+id+" bufory zajete");
                i--;
            }
        }
        cons2managerChannel.out().write(Integer.MAX_VALUE);
        System.out.println("Konsument "+id+" kończy działanie");
    }
}
