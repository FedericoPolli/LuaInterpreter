import org.junit.jupiter.api.Test;

public class TestOutputRedirection {

    @Test
    public void outputRedirection(){
        ApplicationRunner application = new ApplicationRunner();
        application.runPrint();
        application.redirectedOutput();
    }
}
