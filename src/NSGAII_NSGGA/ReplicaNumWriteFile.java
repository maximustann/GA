package NSGAII_NSGGA;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ReplicaNumWriteFile {

    private String replicaNumPath;
    public ReplicaNumWriteFile(String replicaNumPath){
        this.replicaNumPath = replicaNumPath;
    }

    public void writeReplicaNum(
            double[] containerCpu,
            double[] containerMem,
            double[] containerAppId,
            double[] containerMicroServiceId,
            double[] containerReplicaId) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(replicaNumPath));
        for(int i = 0; i < containerCpu.length; i++){
            writer.write(String.valueOf(containerCpu[i]) + "," +
                            String.valueOf(containerMem[i]) + "," +
                            String.valueOf(containerAppId[i]) + "," +
                            String.valueOf(containerMicroServiceId[i]) + "," +
                            String.valueOf(containerReplicaId[i]) + "\n");
        }
        writer.close();
    }

}
