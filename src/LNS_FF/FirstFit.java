package LNS_FF;

import cloudResourceUnit.PM;
import cloudResourceUnit.VM;

import java.util.ArrayList;

public class FirstFit {

    private double pmCpu;
    private double pmMem;
    private double pmMaxEnergy;
    private double k;
    private double crushPro;
    private ArrayList<PM> pmList;


    private FFCollector collector;
    private ReplicaAllocationDecisionPack decisionPack;


    protected ArrayList<Double> timeData;
    protected long start, end;


    public FirstFit(
            FFCollector collector,
            ReplicaAllocationDecisionPack decisionPack,
            DataPack dataPack
            ){
        this.collector = collector;
        this.decisionPack = decisionPack;
        this.pmCpu = dataPack.getPmCpu();
        this.pmMaxEnergy = dataPack.getPmEnergy();
        this.pmMem = dataPack.getPmMem();
        this.k = dataPack.getK();
        this.crushPro = dataPack.getCrushPro();
        pmList = new ArrayList<>();
    }

    public void run(){
        //start
        collector.collectTime(0);
        ArrayList<VM> vmList = decisionPack.getVmList();


        for(VM vm:vmList) {
            boolean allocated = false;
            for (PM pm : pmList) {
                if (pm.getCpuRemain() >= vm.getConfigureCpu()
                        && pm.getMemRemain() >= vm.getConfigureMem()) {
                    pm.allocate(vm);
                    allocated = true;
                    break;
                }
            }

            if (!allocated) {
                PM pm = new PM(pmCpu, pmMem, k, pmMaxEnergy, crushPro);
                pm.allocate(vm);
                pmList.add(pm);
            }
        }
        collector.collectTime(1);
        collector.collect(pmList);

    }


    public ArrayList<PM> getPmList() {
        return pmList;
    }
}
