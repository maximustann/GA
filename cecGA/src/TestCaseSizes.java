public enum TestCaseSizes {
        One(100, "Container100"),
        Two(200, "Container200"),
        Three(500, "Container500"),
        Four(1000, "Container1000"),
        Five(10000, "Container10000");


        private int testSize;
        private String name;
        TestCaseSizes(int testSize, String name){
                this.testSize = testSize;
                this.name = name;
        }
        public int getTestSize(){
                return testSize;
        }
        public String getName(){
                return name;
        }
}
