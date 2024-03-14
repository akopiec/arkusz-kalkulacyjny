import javax.swing.*;
import java.awt.*;

public class ArkuszKalkulacyjnyShow {

    public static void main(String[] args) {

        EventQueue.invokeLater(()->{

            ArkuszKalkulacyjnyFrame arkuszKalkulacyjnyFrame=new ArkuszKalkulacyjnyFrame();

            arkuszKalkulacyjnyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            arkuszKalkulacyjnyFrame.setVisible(true);
        });
    }
}
