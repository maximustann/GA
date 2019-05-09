import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import com.sun.org.apache.xml.internal.security.Init;

public class DualPermutationParameterSettings extends ProblemParameterSettings {
    private int vmTypes;
    private int numOfContainers;
    private int numOfVms;
    private double pmCpu;
    private double pmMem;
    private double pmEnergy;
    private double[] vmCpu;
    private double[] vmMem;
    private double[] taskCpu;
    private double[] taskMem;
    private InitPop initMethod;
    private Mutation mutation;
    private Crossover crossover;

    public int getVmTypes() {
        return vmTypes;
    }

    public int getNumOfContainers() {
        return numOfContainers;
    }

    public int getNumOfVms() {
        return numOfVms;
    }

    public double getPmCpu() {
        return pmCpu;
    }

    public double getPmMem() {
        return pmMem;
    }

    public double getPmEnergy() {
        return pmEnergy;
    }

    public double[] getVmCpu() {
        return vmCpu;
    }

    public double[] getVmMem() {
        return vmMem;
    }

    public double[] getTaskCpu() {
        return taskCpu;
    }

    public double[] getTaskMem() {
        return taskMem;
    }

    public InitPop getInitMethod() {
        return initMethod;
    }

    public Mutation getMutation() {
        return mutation;
    }

    public Crossover getCrossover() {
        return crossover;
    }

    public DualPermutationParameterSettings(
                                        Evaluate evaluate,
                                        InitPop initMethod,
                                        Mutation mutation,
                                        Crossover crossover,
                                        int vmTypes,
                                        int numOfContainers,
                                        int numOfVms,
                                        double pmCpu,
                                        double pmMem,
                                        double pmEnergy,
                                        double[] vmCpu,
                                        double[] vmMem,
                                        double[] taskCpu,
                                        double[] taskMem
                                        ){
        super(evaluate, null);
        this.vmTypes = vmTypes;
        this.numOfContainers = numOfContainers;
        this.numOfVms = numOfVms;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.pmEnergy = pmEnergy;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.initMethod = initMethod;
        this.mutation = mutation;
        this.crossover = crossover;
    }
}
