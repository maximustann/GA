package cloudResourceUnit;

import java.util.ArrayList;
import java.util.HashSet;

public class MicroService {
    private int ID;
    private ArrayList<Container> containerList;

    public MicroService(int ID){
        this.ID = ID;
        this.containerList = new ArrayList<>();
    }

    public void addContainer(Container container){
        containerList.add(container);
    }

    public ArrayList<Container> getContainerList() {
        return containerList;
    }


    // If all the PM has the same crush probability
    public double probability(){
        HashSet<PM> tempPmSet = new HashSet<>();
        for(Container container:containerList){
            tempPmSet.add(container.getPm());
        }
        int num = tempPmSet.size();
        PM pm = containerList.get(0).getPm();
        double crushPro = pm.getCrushPro();

        return Math.pow(crushPro, num);
    }

    private Integer checkExist(ArrayList<int[]> tempPmReplicaList, int pmId){
        for(int i = 0; i < tempPmReplicaList.size(); i++){
            if(tempPmReplicaList.get(i)[0] == pmId) return i;
        }
        return null;
    }

    //
    public ArrayList<int[]> replicaNum(){
        ArrayList<int[]> pm = new ArrayList<>();

        // for each container, we count the replica number of PMs
        // If more than two replicas are allocated to the same PM, we add up all of these replicas
        for(Container container:containerList){
            // check we have add this pm into the temp PM list
            Integer index = checkExist(pm, container.getPm().getID());
            // If this pm exists, increment one for the replica
            if(index != null){
                pm.get(index)[1] += 1;
            // else, create a pm
            } else {
               int[] pmReplicas = new int[] {container.getPm().getID(), 1};
               pm.add(pmReplicas);
            }
        }

        // create a list just for the PMs which contain replicas
        ArrayList<int[]> result = new ArrayList<>();
        for(int[] pmReplica:pm){
            if(pmReplica[1] > 1) {
                result.add(pmReplica);
            }
        }
        return result;
    }

    public int getID() {
        return ID;
    }
}
