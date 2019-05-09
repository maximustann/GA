public enum NumOfVmTypes {
    Five(5, "five"),
    Seven(7, "seven"),
    Ten(10, "ten");

    private int numOfVmTypes;
    private String name;
    NumOfVmTypes(int numOfVmTypes, String name){
        this.numOfVmTypes = numOfVmTypes;
        this.name = name;
    }

    public int getNumOfVmTypes(){
        return numOfVmTypes;
    }

    public String getName() {
        return name;
    }
}
