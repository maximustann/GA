package multiobjectiveMicroService;

public class Test {
    public static void main(String[] args){
        int microServiceSize = 150;
        int maximumServiceNum = 5;
        String base = "/home/tanboxi/workspace/BilevelData/micro-service";
        String testCasePath = base + "/testCase" + microServiceSize + ".csv";
        String microServicePath = base + "/application" + microServiceSize + ".csv";
        ReadMicroServices readMicroServices = new ReadMicroServices(
                                                            microServiceSize,
                                                            maximumServiceNum,
                                                            microServicePath);

    }
}
