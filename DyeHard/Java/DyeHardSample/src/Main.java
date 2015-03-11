import Engine.GameWindow;

public class Main extends GameWindow {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws Exception {
        (new Main()).startProgram();
    }

    public Main() {
        setRunner(new UserCode());
    }
}