import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

class GameFinish {

    Scene finishScene;
    private Effects effects;
    private Group finishGroup;

    GameFinish() {
        finishGroup = new Group();
        effects = new Effects();
        setFinishText();
//        setButtons();
        setFinishScene();
    }

    private void setFinishText() {
        Text finish = new Text();
        if(BoardPanel.getWinner() == Player.playerRed.getTurn()) {
            finish.setText(Player.playerRed.getName() + " is the winner!");
        }

        if(BoardPanel.getWinner() == Player.playerBlue.getTurn()) {
            finish.setText(Player.playerBlue.getName() + " is the winner!");
        }

        finish.setX(10.0f);
        finish.setY(50.0f);
        finish.setCache(true);
        finish.setFill(effects.redToBlue);
        finish.setUnderline(true);
        finish.setFont(Font.font(null, FontWeight.BOLD, 80));
        finish.setTranslateY(100);
        finish.setTranslateX(100);
        finish.setEffect(effects.goldGlow);
        finishGroup.getChildren().add(finish);
    }

    private void setFinishScene() {
        finishScene = new Scene(finishGroup, 1100, 600);
        finishScene.setFill(effects.blueToRed); // Set color
    }
}

