import Engine.GameWindow;

public class Main extends GameWindow {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            String javaHome = System.getProperty("java.home");
            if (javaHome.contains("eclipse")) {
                Process ps = Runtime.getRuntime().exec(
                        "java -jar -Xms1024m -Xmx1150m DyeHard.jar command");
                ps.waitFor();
            } else if (javaHome.contains("java.exe")) {
                Process ps = Runtime
                        .getRuntime()
                        .exec(javaHome
                                + " -jar -Xms1024m -Xmx1150m DyeHard.jar command");
                ps.waitFor();
            } else {
                Process ps = Runtime
                        .getRuntime()
                        .exec(javaHome
                                + "\\bin\\java.exe -jar -Xms1024m -Xmx1150m DyeHard.jar command");
                ps.waitFor();
            }
        } else {
            (new Main()).startProgram();
        }
        // System.out.println(heapMaxSize + "  " + heapFreeSize);
        // (new Main()).startProgram();
        //
        // heapMaxSize = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        // heapFreeSize = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        //
        // System.out.println(heapMaxSize + "  " + heapFreeSize);
        // }
    }

    public Main() {
        setRunner(new UserCode());
    }
}