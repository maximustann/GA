package constants;

public enum Parts {
    FIRST(0),
    SECOND(1);
    private int num;
    Parts(int num){
        this.num = num;
    }
    public int getNum(){
        return num;
    }
}
