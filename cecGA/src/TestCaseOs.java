public enum TestCaseOs {
    One(1, "OS1"),
    Two(2, "OS2"),
    Three(3, "OS3");

    private int numOfOs;
    private String name;
    TestCaseOs(int numOfOs, String name){
        this.numOfOs = numOfOs;
        this.name = name;
    }

    public int getNumOfOs(){
        return numOfOs;
    }

    public String getName() {
        return name;
    }
}
