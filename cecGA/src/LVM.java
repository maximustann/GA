public enum LVM {
    EQ(true, "LVMePM"),
    NEQ(false, "LVMnePM");

    private boolean equal;
    private String name;
    LVM(boolean equal, String name){
        this.equal = equal;
        this.name = name;
    }

    public boolean getEqual(){
        return equal;
    }

    public String getName() {
        return name;
    }
}
