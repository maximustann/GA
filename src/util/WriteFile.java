package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
    // energy
    private double energy;
    private String energyPath;

    private double[][] allParetoFront;
    private String paretoFrontPath;

    // PM cpu and memory utilization
    private double[] cpuMemUtil;
    private String cpuMemUtilPath;


    // wasted resources
    private double wastedResource;
    private String wastedResourcePath;

    // number of used PMs
    private double numOfPm;
    private String numOfPmPath;

    // number of used VMs
    private double numOfVm;
    private String numOfVmPath;

    // convergence curve is the average fitness of each generation among all runs
    private double[][] convergenceCurve;
    private String convergenceCurvePath;

    // average and Sd of the execution time (each run)
    private double time;
    private String timePath;



    private WriteFile(Builder builder){
        energy = builder.energy;
        energyPath = builder.energyPath;

        allParetoFront = builder.allParetoFront;
        paretoFrontPath = builder.paretoFrontPath;

        cpuMemUtil = builder.cpuMemUtil;
        cpuMemUtilPath = builder.cpuMemUtilPath;

        wastedResource = builder.wastedResource;
        wastedResourcePath = builder.wastedResourcePath;

        numOfPm = builder.numOfPm;
        numOfPmPath = builder.numOfPmPath;

        numOfVm = builder.numOfVm;
        numOfVmPath = builder.numOfVmPath;

        convergenceCurve = builder.convergenceCurve;
        convergenceCurvePath = builder.convergenceCurvePath;

        time = builder.time;
        timePath = builder.timePath;
    }



    public WriteFile writeEnergy() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(energyPath));
        writer.write(String.valueOf(energy) + "\n");
        writer.close();
        return this;
    }

    public WriteFile writeParetoFront() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(paretoFrontPath));
        for(int i = 0; i < allParetoFront.length; i++){
                writer.write(String.valueOf(
                          allParetoFront[i][0] + ","
                        + allParetoFront[i][1] + ","
                        + allParetoFront[i][2] + "\n"));
        }
        writer.close();
        return this;
    }

    public WriteFile writeWastedResource() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(wastedResourcePath));
        writer.write(String.valueOf(wastedResource) + "\n");
        writer.close();
        return this;
    }

    public WriteFile writeCpuMemUtil() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(cpuMemUtilPath));
        writer.write(String.valueOf(cpuMemUtil[0]) + "," + String.valueOf(cpuMemUtil[1]) + "\n");
        writer.close();
        return this;
    }

    public WriteFile writeNumOfPm() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(numOfPmPath));
        writer.write(String.valueOf(numOfPm) + "\n");
        writer.close();
        return this;
    }

    public WriteFile writeNumOfVm() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(numOfVmPath));
        writer.write(String.valueOf(numOfVm) + "\n");
        writer.close();
        return this;
    }

    public WriteFile writeConvergenceCurve() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(convergenceCurvePath));
        writer.write(String.valueOf(convergenceCurve[0][0]) + "," + convergenceCurve[0][1] +"\n");
        for(int i = 1; i < convergenceCurve.length; i++){
            writer.append(String.valueOf(convergenceCurve[i][0] + "," + convergenceCurve[i][1] + "\n"));
        }
        writer.close();
        return this;
    }

    public WriteFile writeTime() throws  IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(timePath));
        writer.write(String.valueOf(time) + "\n");
        writer.close();
        return this;
    }


    public static class Builder {
        private double energy;
        private String energyPath;

        // required
        private double[][] allParetoFront;
        private String paretoFrontPath;

        // PM cpu and memory utilization
        private double[] cpuMemUtil;
        private String cpuMemUtilPath;

        // PM wasted resource;
        private double wastedResource;
        private String wastedResourcePath;

        // number of used PMs
        private double numOfPm;
        private String numOfPmPath;

        // number of used VMs
        private double numOfVm;
        private String numOfVmPath;

        // convergence curve is the average fitness of each generation among all runs
        private double[][] convergenceCurve;
        private String convergenceCurvePath;

        // time (each run)
        private double time;
        private String timePath;


        public Builder setEnergy(double energy, String energyPath){
            this.energy = energy;
            this.energyPath = energyPath;
            return this;
        }

        public Builder setParetoFront(double[][] allParetoFront, String paretoFrontPath){
            this.allParetoFront = allParetoFront;
            this.paretoFrontPath = paretoFrontPath;
            return this;
        }

        public Builder setCpuMemUtil(double[] cpuMemUtil,
                                        String cpuMemUtilPath){
            this.cpuMemUtil = cpuMemUtil;
            this.cpuMemUtilPath = cpuMemUtilPath;
            return this;
        }

        public Builder setNumOfPm(double numOfPm,
                                     String numOfPmPath){
            this.numOfPm = numOfPm;
            this.numOfPmPath = numOfPmPath;
            return this;
        }

        public Builder setNumOfVm(double numOfVm,
                                     String numOfVmPath){
            this.numOfVm = numOfVm;
            this.numOfVmPath = numOfVmPath;
            return this;
        }

        public Builder setWastedResource(double wastedResource,
                                         String wastedResourcePath){
            this.wastedResource = wastedResource;
            this.wastedResourcePath = wastedResourcePath;
            return this;
        }

        public Builder setConvergenceCurve(double[][] convergenceCurve,
                                           String convergenceCurvePath){
            this.convergenceCurve = convergenceCurve;
            this.convergenceCurvePath = convergenceCurvePath;
            return this;
        }

        public Builder setTime(double time,
                               String timePath){
            this.time = time;
            this.timePath = timePath;
            return this;
        }

        //  build method
        public WriteFile build(){
            return new WriteFile(this);
        }

    }
}
