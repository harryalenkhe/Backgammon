import javafx.scene.effect.DropShadow;
import javafx.scene.paint.*;

class Effects {

    LinearGradient blueToRed;
    LinearGradient redToBlue;
    LinearGradient checkerBlue;
    LinearGradient checkerRed;
    DropShadow noGlow;
    DropShadow goldGlow;

    Effects() {
        setCustomColours();
        setGlows();
    }

    private void setCustomColours() {
        Stop[] blueRed = new Stop[]{ new Stop(0, Color.DARKBLUE), new Stop(1, Color.DARKRED) };
        blueToRed = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, blueRed);

        Stop[] redBlue = new Stop[] { new Stop(0, Color.DARKRED), new Stop(1, Color.DARKBLUE) };
        redToBlue = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, redBlue);

        Stop[] redBlend = new Stop[] { new Stop(0, Color.DARKRED), new Stop(1, Color.RED) };
        checkerRed = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, redBlend);

        Stop[] blueBlend = new Stop[] { new Stop(0, Color.GRAY),  new Stop(1, Color.BLUE) };
        checkerBlue = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, blueBlend);
    }

    private void setGlows() {
        noGlow = new DropShadow();
        noGlow.setColor(Color.DARKRED);
        noGlow.setHeight(30);

        goldGlow = new DropShadow();
        goldGlow.setColor(Color.GOLD);
        goldGlow.setHeight(30);
    }
}
