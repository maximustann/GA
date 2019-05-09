import algorithms.Chromosome;
import algorithms.Mutation;
import algorithms.StdRandom;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class DoubleIntMutation implements Mutation{

    @Override
    public void update(Chromosome individual, double mutationRate){
        update((DoubleIntChromosome) individual, mutationRate);
    }

    /**
     * The mutation includes three types of mutations.
     * After the mutation of vmAllocation chromosome, we need to adjust the PM index.
     *
     * @param individual
     * @param mutationRate
     */
    private void update(DoubleIntChromosome individual, double mutationRate){

        // the theoretical maximum number of VMs
        int numOfVm = individual.getNumOfVms();
        // the number of active PMs
        int numOfPm = individual.getNumOfActivePM();

        // We implement reverseSequenceMutation and randomly choose mutation for
        // both container allocation and vm allocation
        if(StdRandom.uniform() <= mutationRate){
            reverseSequence(individual.containerAllocation);
        }
        if(StdRandom.uniform() <= mutationRate){
            reverseSequence(individual.vmAllocation);
        }

        changeVm(individual.containerAllocation, numOfVm, mutationRate);
        changePm(individual.vmAllocation, numOfVm, mutationRate);
        adjustment(individual.vmAllocation);
    }

    // Reverse a range of chromosome
    private void reverseSequence(int[] chromosome){
        int startPoint, endPoint;
        int chromoSize = chromosome.length;
        endPoint = StdRandom.uniform(0, chromoSize);
        startPoint = StdRandom.uniform(0, endPoint);

        int[] temp = new int[endPoint - startPoint];

        // copy the original part to temp chromosome
        for(int i = startPoint, j = 0; i < endPoint; i++, j++)
            temp[j] = chromosome[i];

        // copy the reversed temp back to the original
        for(int i = startPoint, j = endPoint - startPoint - 1; i < endPoint; i++, j--){
            chromosome[i] = temp[j];
        }
    }

    /**
     * @param containerAllocation
     * @param numOfVm The theoretical maximum number of VMs
     * @param mutationRate
     */
    private void changeVm(int[] containerAllocation, int numOfVm, double mutationRate){
        int length = containerAllocation.length;
        for(int i = 0; i < length; i++){
            // If mutation happens, change to a random number with the range of [1, numOfVm]
            if(StdRandom.uniform() <= mutationRate){
                containerAllocation[i] = StdRandom.uniform(1, numOfVm);
            }
        }
    }

    private void changePm(int[] vmAllocation, int numOfPm, double mutationRate){
        int length = vmAllocation.length;
        for(int i = 0; i < length; i++){
            // If the VM does not exist, then, we skip it.
            if(vmAllocation[i] == 0) continue;
            if(StdRandom.uniform() <= mutationRate){
                // We allow the VM to be allocated to a new PM
                vmAllocation[i] = StdRandom.uniform(1, numOfPm + 1);
            }
        }
    }

    // Here, after the mutation of vmAllocation chromosome, we need to keep the PM index in a sequence.
    private void adjustment(int[] vmAllocation){
        // Here we use treemap because it automatically sort by keys
        TreeMap<Integer, ArrayList<Integer>> pms = new TreeMap<>();

        // Initializing the hashmap for pms.
        for(int i = 0; i < vmAllocation.length; i++){
            // skip the non-exist VM
            if(vmAllocation[i] == 0) continue;
            // If the key exists, we update the index of the VM
            if(pms.containsKey(vmAllocation[i])){
                ArrayList<Integer> value = pms.get(vmAllocation[i]);
                value.add(i);
            // or the key does not exists, we need to create a new arrayList for the VM indexes
            } else {
                ArrayList<Integer> value = new ArrayList<>();
                value.add(i);
                pms.put(vmAllocation[i], value);
            }
        } // finish initializing


        int currentPmIndex = 1;
        // fix the index of PMs
        for(Map.Entry entry : pms.entrySet()){
            // make sure the first index of PM starts from 1
            if((Integer) entry.getKey() == 1){
                ArrayList<Integer> values = (ArrayList<Integer>) pms.firstEntry();
                for(int i = 0; i < values.size(); ++i){
                    vmAllocation[values.get(i)] = currentPmIndex;
                }
                currentPmIndex++;
                continue;
            }

            ArrayList<Integer> values = (ArrayList<Integer>) entry.getValue();
            for(int i = 0; i < values.size(); ++i){
                vmAllocation[values.get(i)] = currentPmIndex;
            }
            currentPmIndex++;
        } // finishing fixing


    } // finish repairing



}
