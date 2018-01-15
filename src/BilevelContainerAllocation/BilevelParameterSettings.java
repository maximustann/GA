package BilevelContainerAllocation;

import ProblemDefine.CoGAProblemParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import wsrp.WSRP_Constraint;

public class BilevelParameterSettings extends CoGAProblemParameterSettings{
    private int vmTypes;
    private int taskNum;
    private double pmCpu;
    private double pmMem;
    private double pmEnergy;
    private double[] vmCpu;
    private double[] vmMem;
    private double[] taskCpu;
    private double[] taskMem;
    private double[] taskOS;

    private CoEvaluate evaluate;
    private InitPop[] initMethods;
    private Mutation[] mutations;
    private Crossover[] crossovers;
    private Selection[] selections;
    private Elitism[] elitisms;
    private Constraint[] constraints;

    public BilevelParameterSettings(
            CoEvaluate evaluate,
            InitPop[] initMethods,
            Mutation[] mutations,
            Crossover[] crossovers,
            Selection[] selections,
            Elitism[] elitisms,
            Constraint[] constraints,
            int vmTypes,
            int taskNum,
            double pmCpu,
            double pmMem,
            double pmEnergy,
            double[] vmCpu,
            double[] vmMem,
            double[] taskCpu,
            double[] taskMem,
            double[] taskOS
    ) {
        super(evaluate);
        this.vmTypes = vmTypes;
        this.taskNum = taskNum;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.pmEnergy = pmEnergy;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.taskOS = taskOS;
        this.evaluate = evaluate;
        this.initMethods = initMethods;
        this.mutations = mutations;
        this.crossovers = crossovers;
        this.selections = selections;
        this.elitisms = elitisms;
        this.constraints = constraints;
    }

    public Constraint[] getConstraints() {
        return constraints;
    }

    public Mutation[] getMutatioin(){
        return mutations;
    }

    public InitPop[] getInitPop(){
        return initMethods;
    }
    public int getVmTypes() {
        return vmTypes;
    }
    public int getTaskNum() {
        return taskNum;
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

    public double[] getTaskOS() {
        return taskOS;
    }

    @Override
    public CoEvaluate getEvaluate() {
        return evaluate;
    }

    public InitPop[] getInitMethods() {
        return initMethods;
    }

    public Mutation[] getMutations() {
        return mutations;
    }

    public Crossover[] getCrossovers() {
        return crossovers;
    }

    public Selection[] getSelections() {
        return selections;
    }

    public Elitism[] getElitisms() {
        return elitisms;
    }

    public double[] getTaskMem() {
        return taskMem;
    }
}
