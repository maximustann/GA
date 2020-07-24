//package twoStepALNS_NSGGA;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//
//public class WriteFile {
//    // average total energy among all runs
//    private double[] energy;
//    private double aveEnergy;
//    private double sdEnergy;
//    private String aveEnergyPath;
//    private String energyPath;
//
//    private double[][] allParetoFront;
//    private String paretoFrontPath;
//
//    // average PM cpu and memory utilization
//    private double[] aveCpuMemUtil;
//    private double[] sdCpuMemUtil;
//    private String aveCpuMemUtilPath;
//
//
//    // wasted resources
//    private double wastedResource;
//    private String wastedResourcePath;
//
//    // average number of used PMs
//    private double aveNumOfPm;
//    private double sdNumOfPm;
//    private String aveNumOfPmPath;
//
//    // average number of used VMs
//    private double aveNumOfVm;
//    private double sdNumOfVm;
//    private String aveNumOfVmPath;
//
//    // convergence curve is the average fitness of each generation among all runs
//    private double[][] convergenceCurve;
//    private String convergenceCurvePath;
//
//    // average and Sd of the execution time (each run)
//    private double aveTime;
//    private double sdTime;
//    private String aveTimePath;
//
//
//
//    private WriteFile(Builder builder){
////        energy = builder.energy;
////        aveEnergy = builder.aveEnergy;
////        sdEnergy = builder.sdEnergy;
////        aveEnergyPath = builder.aveEnergyPath;
////        energyPath = builder.energyPath;
//        allParetoFront = builder.allParetoFront;
//        paretoFrontPath = builder.paretoFrontPath;
//
//        aveCpuMemUtil = builder.aveCpuMemUtil;
//        sdCpuMemUtil = builder.sdCpuMemUtil;
//        aveCpuMemUtilPath = builder.aveCpuMemUtilPath;
//
//        wastedResource = builder.wastedResource;
//        wastedResourcePath = builder.wastedResourcePath;
//
//        aveNumOfPm = builder.aveNumOfPm;
//        sdNumOfPm = builder.sdNumOfPm;
//        aveNumOfPmPath = builder.aveNumOfPmPath;
//
//        aveNumOfVm = builder.aveNumOfVm;
//        sdNumOfVm = builder.sdNumOfVm;
//        aveNumOfVmPath = builder.aveNumOfVmPath;
//
//        convergenceCurve = builder.convergenceCurve;
//        convergenceCurvePath = builder.convergenceCurvePath;
//
//        aveTime = builder.aveTime;
//        sdTime = builder.sdTime;
//        aveTimePath = builder.aveTimePath;
//    }
//
//
//    // I'd like to give a try.
//    // By returning the object itself, my client code can actually support chain rule
//    public twoStepGA.WriteFile writeAveSdEnergy() throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(aveEnergyPath));
//        writer.write(String.valueOf(aveEnergy) + "," + String.valueOf(sdEnergy) + "\n");
//        writer.close();
//        return this;
//    }
//
//    public twoStepGA.WriteFile writeEnergy() throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(energyPath));
//        for(int i = 0; i < energy.length; i++){
//            writer.write(String.valueOf(energy[i]) + "\n");
//        }
//        writer.close();
//        return this;
//    }
//
//    public twoStepGA.WriteFile writeParetoFront() throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(paretoFrontPath));
//        for(int i = 0; i < allParetoFront.length; i++){
//                writer.write(String.valueOf(
//                          allParetoFront[i][0] + ","
//                        + allParetoFront[i][1] + ","
//                        + allParetoFront[i][2] + "\n"));
//        }
//        writer.close();
//        return this;
//    }
//
//    public twoStepGA.WriteFile writeWastedResource() throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(wastedResourcePath));
//        writer.write(String.valueOf(wastedResource) + "\n");
//        writer.close();
//        return this;
//    }
//
//    public twoStepGA.WriteFile writeAveSdCpuMemUtil() throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(aveCpuMemUtilPath));
//        writer.write(String.valueOf(aveCpuMemUtil[0]) + "," + String.valueOf(sdCpuMemUtil[0]) + "\n");
//        writer.write(String.valueOf(aveCpuMemUtil[1]) + "," + String.valueOf(sdCpuMemUtil[1]) + "\n");
//        writer.close();
//        return this;
//    }
//
//    public twoStepGA.WriteFile writeAveSdNumOfPm() throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(aveNumOfPmPath));
//        writer.write(String.valueOf(aveNumOfPm) + "," + String.valueOf(sdNumOfPm) + "\n");
//        writer.close();
//        return this;
//    }
//
//    public twoStepGA.WriteFile writeAveSdNumOfVm() throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(aveNumOfVmPath));
//        writer.write(String.valueOf(aveNumOfVm) + "," + String.valueOf(sdNumOfVm) + "\n");
//        writer.close();
//        return this;
//    }
//
//    public twoStepGA.WriteFile writeConvergenceCurve() throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(convergenceCurvePath));
//        writer.write(String.valueOf(convergenceCurve[0][0]) + "," + convergenceCurve[0][1] +"\n");
//        for(int i = 1; i < convergenceCurve.length; i++){
//            writer.append(String.valueOf(convergenceCurve[i][0] + "," + convergenceCurve[i][1] + "\n"));
//        }
//        writer.close();
//        return this;
//    }
//
//    public twoStepGA.WriteFile writeAveSdTime() throws  IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(aveTimePath));
//        writer.write(String.valueOf(aveTime) + "," + String.valueOf(sdTime) + "\n");
//        writer.close();
//        return this;
//    }
//
//
//    public static class Builder {
//        private double[] energy;
//        private double aveEnergy;
//        private double sdEnergy;
//        private String aveEnergyPath;
//        private String energyPath;
//
//        // required
//        private double[][] allParetoFront;
//        private String paretoFrontPath;
//
//        // average PM cpu and memory utilization
//        private double[] aveCpuMemUtil;
//        private double[] sdCpuMemUtil;
//        private String aveCpuMemUtilPath;
//
//        // average PM wasted resource;
//        private double wastedResource;
//        private String wastedResourcePath;
//
//        // average number of used PMs
//        private double aveNumOfPm;
//        private double sdNumOfPm;
//        private String aveNumOfPmPath;
//
//        // average number of used VMs
//        private double aveNumOfVm;
//        private double sdNumOfVm;
//        private String aveNumOfVmPath;
//
//        // convergence curve is the average fitness of each generation among all runs
//        private double[][] convergenceCurve;
//        private String convergenceCurvePath;
//
//        // average and sd of time (each run)
//        private double aveTime;
//        private double sdTime;
//        private String aveTimePath;
//
//
//        public Builder(
//                        double[][] allParetoFront,
//                        String paretoFrontPath
//                        ){
//            this.allParetoFront = allParetoFront;
//            this.paretoFrontPath = paretoFrontPath;
//        }
//
//        public Builder setAveCpuMemUtil(double[] aveCpuMemUtil,
//                                        double[] sdCpuMemUtil,
//                                        String aveCpuMemUtilPath){
//            this.aveCpuMemUtil = aveCpuMemUtil;
//            this.sdCpuMemUtil = sdCpuMemUtil;
//            this.aveCpuMemUtilPath = aveCpuMemUtilPath;
//            return this;
//        }
//
//        public Builder setAveNumOfPm(double aveNumOfPm,
//                                     double sdNumOfPm,
//                                     String aveNumOfPmPath){
//            this.aveNumOfPm = aveNumOfPm;
//            this.sdNumOfPm = sdNumOfPm;
//            this.aveNumOfPmPath = aveNumOfPmPath;
//            return this;
//        }
//
//        public Builder setAveNumOfVm(double aveNumOfVm,
//                                     double sdNumOfVm,
//                                     String aveNumOfVmPath){
//            this.aveNumOfVm = aveNumOfVm;
//            this.sdNumOfVm = sdNumOfVm;
//            this.aveNumOfVmPath = aveNumOfVmPath;
//            return this;
//        }
//
//        public Builder setWastedResource(double wastedResource,
//                                         String wastedResourcePath){
//            this.wastedResource = wastedResource;
//            this.wastedResourcePath = wastedResourcePath;
//            return this;
//        }
//
//        public Builder setConvergenceCurve(double[][] convergenceCurve,
//                                           String convergenceCurvePath){
//            this.convergenceCurve = convergenceCurve;
//            this.convergenceCurvePath = convergenceCurvePath;
//            return this;
//        }
//
//        public Builder setAveTime(double aveTime,
//                                  double sdTime,
//                                  String aveTimePath){
//            this.aveTime = aveTime;
//            this.sdTime = sdTime;
//            this.aveTimePath = aveTimePath;
//            return this;
//        }
//
//        //  build method
//        public twoStepGA.WriteFile build(){
//            return new twoStepGA.WriteFile(this);
//        }
//
//    }
//}
