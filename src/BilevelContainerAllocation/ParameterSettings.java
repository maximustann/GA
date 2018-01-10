package BilevelContainerAllocation;
import ProblemDefine.CoGAProblemParameterSettings;
import algorithms.Distance;
import algorithms.CoEvaluate;
import algorithms.InitPop;
import algorithms.Mutation;

import java.util.Arrays;

public class ParameterSettings extends CoGAProblemParameterSettings {
    private int vmTypes;
    private int containerNum;
    private double pmCpu;
    private double pmMem;
    private double pmEnergy; // idle energy consumption
    private double[] vmCpu;
    private double[] vmMem;
    private double[] containerCpu;
    private double[] containerMem;
    private InitPop[] initMethods;
    private Mutation[] mutations;
    private ContainerConstraint[] constraints;

    public ParameterSettings(
                    CoEvaluate[] evaluates,
                    InitPop[] initMethods,
                    Mutation[] mutations,
                    ContainerConstraint[] constraints,
                    int vmTypes,
                    int containerNum,
                    double pmCpu,
                    double pmMem,
                    double pmEnergy,
                    double[] vmCpu,
                    double[] vmMem,
                    double[] containerCpu,
                    double[] containerMem
                    ){
        super(evaluates);
        this.vmTypes = vmTypes;
        this.containerNum = containerNum;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.pmEnergy = pmEnergy;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.containerCpu = containerCpu;
        this.containerMem = containerMem;
        this.initMethods = initMethods;
        this.mutations = mutations;
        this.constraints = constraints;
    }

    public ContainerConstraint[] getConstraints() {
        return constraints;
    }

    public Mutation[] getMutations() {
        return mutations;
    }

    public double getPmCpu() {
        return pmCpu;
    }

    public int getVmTypes() {
        return vmTypes;
    }

    public int getContainerNum() {
        return containerNum;
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

    public double[] getContainerCpu() {
        return containerCpu;
    }

    public double[] getContainerMem() {
        return containerMem;
    }

    public InitPop[] getInitMethods() {
        return initMethods;
    }
}
