import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class MatchFinish {

    Scene finishScene;
    private Effects effects;
    private Group finishGroup;

    MatchFinish(Stage primaryWindow, int winner) {
        finishGroup = new Group();
        effects = new Effects();
        setFinishText(winner);
        setButtons(primaryWindow);
        setFinishScene();
    }

    private void setFinishText(int winner) {
        Text finish = new Text();
        if(winner == Player.playerRed.getTurn()) {
            finish.setText(Player.playerRed.getName() + " is the winner!");
        }

        if(winner == Player.playerBlue.getTurn()) {
            finish.setText(Player.playerBlue.getName() + " is the winner!");
        }

        finish.setX(10.0f);
        finish.setY(50.0f);
        finish.setCache(true);
        finish.setFill(effects.redToBlue);
        finish.setUnderline(true);
        finish.setFont(Font.font(null, FontWeight.BOLD, 80));
        finish.setTranslateY(100);
        finish.setTranslateX(150);
        finish.setEffect(effects.goldGlow);

        Text promptPlayAgain = new Text("Would you like to play again?");
        promptPlayAgain.setX(10.0f);
        promptPlayAgain.setY(50.0f);
        promptPlayAgain.setCache(true);
        promptPlayAgain.setFill(effects.redToBlue);
        promptPlayAgain.setUnderline(true);
        promptPlayAgain.setFont(Font.font(null, FontWeight.BOLD, 50));
        promptPlayAgain.setTranslateY(200);
        promptPlayAgain.setTranslateX(190);
        promptPlayAgain.setEffect(effects.goldGlow);

        finishGroup.getChildren().addAll(finish, promptPlayAgain);
    }

    public void setButtons(Stage primaryWindow) {
        Button startButton = new Button("YES");
        startButton.setLayoutX(375);
        startButton.setLayoutY(320);
        startButton.setTextFill(Color.BLACK);
        startButton.setMinSize(100, 40);

        Button quitButton = new Button("NO");
        quitButton.setLayoutX(575);
        quitButton.setLayoutY(320);
        quitButton.setTextFill(Color.BLACK);
        quitButton.setMinSize(100, 40);

        startButton.setOnMouseEntered(E -> startButton.setEffect(effects.goldGlow));
        startButton.setOnMouseExited(E -> startButton.setEffect(effects.noGlow));
        startButton.setOnAction(E -> {
            AnnounceGame announceGame = new AnnounceGame(primaryWindow);
            primaryWindow.setScene(announceGame.welcomeScene);
            announceGame.welcomeGroup.getChildren().removeAll(announceGame.startButton, announceGame.quitButton);
            announceGame.welcomeGroup.getChildren().add(announceGame.playerDetails);
        });

        quitButton.setOnMouseEntered(E -> quitButton.setEffect(effects.goldGlow));
        quitButton.setOnMouseExited(E -> quitButton.setEffect(effects.noGlow));
        quitButton.setOnAction(E -> System.exit(0));
        finishGroup.getChildren().addAll(startButton, quitButton);
    }


    private void setFinishScene() {
        finishScene = new Scene(finishGroup, 1100, 600);
        finishScene.setFill(effects.blueToRed); // Set color
    }
}

