package wsrp;

import java.util.ArrayList;

public class ResourceHelper {
	private int taskNum;
	private int vmTypes;
	private double pmMem;
	private double pmCpu;
	private double[] vmMem;
	private double[] vmCpu;
	private double[] taskCpu;
	private double[] taskMem;
	private double[] taskFreq;

	private ArrayList<Integer> bound = new ArrayList<Integer>();
	private ArrayList<double[]> leftResource = new ArrayList<double[]>();

	public ResourceHelper(
			int taskNum,
			int vmTypes,
			double pmMem,
			double pmCpu,
			double[] vmMem,
			double[] vmCpu,
			double[] taskCpu,
			double[] taskMem,
			double[] taskFreq
			){
		this.taskNum = taskNum;
		this.vmTypes = vmTypes;
		this.pmMem = pmMem;
		this.pmCpu = pmCpu;
		this.vmMem = vmMem;
		this.vmCpu = vmCpu;
		this.taskCpu = taskCpu;
		this.taskMem = taskMem;
		this.taskFreq = taskFreq;
	}

	public void evaluate(WSRP_IntChromosome chromo){
		pmBound(chromo);
	}

	public void pmBound(WSRP_IntChromosome chromo){
		double cpuResource = 0;
		double memResource = 0;
		ArrayList[] indexCount = new ArrayList[4];
		for(int i = 0; i < 4; i++) indexCount[i] = new ArrayList<Integer>();

		for(int i = 0; i < taskNum; i++){
			int vmType = chromo.individual[i * 2];
			int vmIndex = chromo.individual[i * 2 + 1];
			if(pmCpu - cpuResource - vmCpu[vmType] <= 0 || pmMem - memResource - vmMem[vmType] <= 0){
				double cpu = pmCpu - cpuResource - vmCpu[vmType];
				double mem = pmMem - memResource - vmMem[vmType];
				System.out.println("taskNum = " + i + ", pmCpu = " + pmCpu + " vmCpu[" + vmType + "] = " +
									vmCpu[vmType] + ", cpuResource = " + cpuResource);
				System.out.println("taskNum = " + i + ", pmMem = " + pmMem + " vmMem[" + vmType + "] = " +
						vmMem[vmType] + ", memResource = " + memResource);
				System.out.println();
				bound.add(i);
				leftResource.add(new double[]{pmCpu - cpuResource, pmMem - memResource});
				cpuResource = 0;
				memResource = 0;
				if(indexCount[vmType].isEmpty() || !indexCount[vmType].contains(vmIndex)) {
					indexCount[vmType].add(vmIndex);
				}
				continue;
			}
			// If there is this type of vm, or, there is no such vmIndex
			if(indexCount[vmType].isEmpty() || !indexCount[vmType].contains(vmIndex)) {
				indexCount[vmType].add(vmIndex);
				cpuResource += vmCpu[vmType];
				memResource += vmMem[vmType];
			}
			if(i == taskNum - 1){
				bound.add(i);
				leftResource.add(new double[]{pmCpu - cpuResource, pmMem - memResource});
			}
		}
	}

	public ArrayList<double[]> getLeftResource(){
		return leftResource;
	}
	public ArrayList<Integer> getBound(){
		return bound;
	}
}
