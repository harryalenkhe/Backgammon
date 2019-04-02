import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
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
        TextField playAgain = new TextField();
        playAgain.setOnAction(e -> {
            if(playAgain.getText().equalsIgnoreCase("yes")) {
                AnnounceGame announceGame = new AnnounceGame(primaryWindow);
                primaryWindow.setScene(announceGame.welcomeScene);
                announceGame.welcomeGroup.getChildren().removeAll(announceGame.startButton, announceGame.quitButton);
                announceGame.welcomeGroup.getChildren().add(announceGame.playerDetails);
            } else if(playAgain.getText().equalsIgnoreCase("no")) {
                System.exit(0);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Not a valid option");
                alert.setHeaderText("Please enter yes or no");
                alert.setContentText("Enter yes or no");
                alert.show();

                Thread newThread = new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    Platform.runLater(alert::close);
                });
                newThread.start();
            }
        });
        playAgain.setLayoutX(425);
        playAgain.setLayoutY(320);
        finishGroup.getChildren().addAll(playAgain);
    }


    private void setFinishScene() {
        finishScene = new Scene(finishGroup, 1100, 600);
        finishScene.setFill(effects.blueToRed); // Set color
    }
}

