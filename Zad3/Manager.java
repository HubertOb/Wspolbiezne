import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

import java.util.Arrays;
import java.util.List;

public class Manager implements CSProcess {

    private int runningProducersAndConsumers=Main.PRODUCERS+Main.CONSUMERS;

    private final List<One2OneChannelInt> prod2managerChannels;
    private final List<One2OneChannelInt> manager2prodChannels;
    private final List<One2OneChannelInt> cons2managerChannels;
    private final List<One2OneChannelInt> manager2consChannels;
    private final List<One2OneChannelInt> buforProdChannels;

    private final int[] bufforsLoad=new int[Main.BUFFORS];

    private final Guard[] guards=new Guard[Main.CONSUMERS+Main.PRODUCERS];

    public Manager(List<One2OneChannelInt> prod2managerChannels,List<One2OneChannelInt> manager2prodChannels, List<One2OneChannelInt> cons2managerChannels, List<One2OneChannelInt> manager2consChannels, List<One2OneChannelInt> buforProdChannels,List<One2OneChannelInt> buforConsChannels){
        this.prod2managerChannels=prod2managerChannels;
        this.manager2prodChannels=manager2prodChannels;
        this.cons2managerChannels=cons2managerChannels;
        this.manager2consChannels=manager2consChannels;
        this.buforProdChannels=buforProdChannels;
        Arrays.fill(bufforsLoad, 0);
        addProcessesToGuardsList();
    }

    private void addProcessesToGuardsList(){
        for (int i=0;i<Main.PRODUCERS;i++){
            guards[i]=prod2managerChannels.get(i).in();
        }

        for(int i=Main.PRODUCERS;i<Main.PRODUCERS+Main.CONSUMERS;i++){
            guards[i]=cons2managerChannels.get(i-Main.PRODUCERS).in();
        }
    }

    @Override
    public void run() {
        Alternative alt=new Alternative(guards);
        while (runningProducersAndConsumers>0){
            int index=alt.select();
            if(index>=Main.PRODUCERS){
                int k=cons2managerChannels.get(index-Main.PRODUCERS).in().read();
                System.out.println("Manager odebrał od kons");
                if(k==Integer.MAX_VALUE){
                    System.out.println("Manager kons konczy");
                    runningProducersAndConsumers--;
                    continue;
                }
                boolean flag=false;
                for(int j=0;j<Main.BUFFORS;j++){
                    if(bufforsLoad[j]>=k){
                        manager2consChannels.get(index-Main.PRODUCERS).out().write(j);
                        bufforsLoad[j]-=k;
                        System.out.println("Manager pozwala konsumować");
                        flag=true;
                        break;
                    }
                }
                if (!flag){
                    manager2consChannels.get(index-Main.PRODUCERS).out().write(-1);
                }
            }
            else{
                int k=prod2managerChannels.get(index).in().read();
                System.out.println("Manager odebrał od prod");
                boolean flag=false;
                if(k==Integer.MAX_VALUE){
                    System.out.println("Manager prod konczy");
                    runningProducersAndConsumers--;
                    continue;
                }
                for(int j=0;j<Main.BUFFORS;j++){
                    if(bufforsLoad[j]+k<=Main.BUFFOR_SIZE){
                        manager2prodChannels.get(index).out().write(j);
                        bufforsLoad[j]+=k;
                        System.out.println("Manager pozwala produkować");
                        flag=true;
                        break;
                    }
                }
                if (!flag){
                    manager2prodChannels.get(index).out().write(-1);
                }
            }
        }

        for (One2OneChannelInt buffor:buforProdChannels){
            System.out.println("Manager konczy bufory");
            buffor.out().write(Integer.MAX_VALUE);
        }
        System.out.println("Manager kończy działanie");
    }
}
