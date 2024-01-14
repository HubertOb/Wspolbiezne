import org.jcsp.lang.*;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static int PRODUCERS=2;
    public static int CONSUMERS=2;
    public static int BUFFORS=4;
    public static int BUFFOR_SIZE=2;


    public static void main(String[] args){
        new Main();
    }

    public Main(){
        int j=0;
        CSProcess[] procList = new CSProcess[PRODUCERS + CONSUMERS + BUFFORS + 1];
        List<One2OneChannelInt> prod2managerChannels = new ArrayList<>();
        List<One2OneChannelInt> manager2prodChannels = new ArrayList<>();
        List<One2OneChannelInt> buforProdChannels = new ArrayList<>();
        List<One2OneChannelInt> cons2managerChannels = new ArrayList<>();
        List<One2OneChannelInt> manager2consChannels = new ArrayList<>();
        List<One2OneChannelInt> buforConsChannels = new ArrayList<>();

        for (int i = 0; i<PRODUCERS; i++){
            One2OneChannelInt prod2managerChannel= Channel.one2oneInt();
            One2OneChannelInt manager2prodChannel= Channel.one2oneInt();
            procList[j]=new Producer(i,prod2managerChannel,manager2prodChannel, buforProdChannels);
            j++;
            prod2managerChannels.add(prod2managerChannel);
            manager2prodChannels.add(manager2prodChannel);
        }

        for (int i = 0; i<CONSUMERS; i++){
            One2OneChannelInt cons2managerChan=Channel.one2oneInt();
            One2OneChannelInt manager2consChan=Channel.one2oneInt();
            procList[j]=new Consumer(i,cons2managerChan,manager2consChan, buforConsChannels);
            j++;
            cons2managerChannels.add(cons2managerChan);
            manager2consChannels.add(manager2consChan);
        }

        for(int i=0;i<BUFFORS;i++){
            One2OneChannelInt buffProdChan=Channel.one2oneInt();
            One2OneChannelInt buffConsChan=Channel.one2oneInt();
            procList[j]=new Bufor(i,BUFFOR_SIZE, buffProdChan,buffConsChan);
            j++;
            buforProdChannels.add(buffProdChan);
            buforConsChannels.add(buffConsChan);
        }

        procList[j]=new Manager(prod2managerChannels, manager2prodChannels, cons2managerChannels, manager2consChannels, buforProdChannels, buforConsChannels);
        j++;

        Parallel par=new Parallel(procList);
        par.run();
    }
}
