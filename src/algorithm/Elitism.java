package algorithm;

public interface Elitism {
	public void carryover(Chromosome[] popVar, Chromosome[] newPop);
	public int getSize();
}
