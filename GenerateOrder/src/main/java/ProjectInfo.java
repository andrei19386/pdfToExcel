public class ProjectInfo {
    private static String projectName;
    private static int numberOfMasks;

    private static int layerOut;

    private static String orderType;

    private static int lightFieldCD;

    private static int darkFieldCD;

    private static int lightFieldMLT;

    private static int darkFieldMLT;

    public static String getProjectName() {
        return projectName;
    }

    public static void setProjectName(String projectName) {
        ProjectInfo.projectName = projectName;
    }

    public static int getNumberOfMasks() {
        return numberOfMasks;
    }

    public static void setNumberOfMasks(int numberOfMasks) {
        ProjectInfo.numberOfMasks = numberOfMasks;
    }

    public static String getOrderType() {
        return orderType;
    }

    public static void setOrderType(String orderType) {
        ProjectInfo.orderType = orderType;
    }

    public static int getLayerOut() {
        return layerOut;
    }

    public static void setLayerOut(int layerOut) {
        ProjectInfo.layerOut = layerOut;
    }


    public static int getLightFieldCD() {
        return lightFieldCD;
    }

    public static void setLightFieldCD(int lightFieldCD) {
        ProjectInfo.lightFieldCD = lightFieldCD;
    }

    public static int getDarkFieldCD() {
        return darkFieldCD;
    }

    public static void setDarkFieldCD(int darkFieldCD) {
        ProjectInfo.darkFieldCD = darkFieldCD;
    }

    public static int getLightFieldMLT() {
        return lightFieldMLT;
    }

    public static void setLightFieldMLT(int lightFieldMLT) {
        ProjectInfo.lightFieldMLT = lightFieldMLT;
    }

    public static int getDarkFieldMLT() {
        return darkFieldMLT;
    }

    public static void setDarkFieldMLT(int darkFieldMLT) {
        ProjectInfo.darkFieldMLT = darkFieldMLT;
    }
}
