import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class AnnounceGame {

    Scene welcomeScene;
    private Effects effects;
    private Group welcomeGroup;
    private VBox playerDetails;
    private Player player;

    AnnounceGame(Stage primaryWindow) {
        welcomeGroup = new Group();
        effects = new Effects();
        new BoardPanel(primaryWindow);
        player = new Player();
        player.getDetails(primaryWindow);
        setWelcomeText();
        setButtons();
        setPlayerDetails();
        setWelcomeScene();
    }

    private void setWelcomeText() {
        Text welcome = new Text("BACKGAMMON");
        welcome.setX(10.0f);
        welcome.setY(50.0f);
        welcome.setCache(true);
        welcome.setFill(effects.redToBlue);
        welcome.setUnderline(true);
        welcome.setFont(Font.font(null, FontWeight.BOLD, 100));
        welcome.setTranslateY(100);
        welcome.setTranslateX(157.5);
        welcome.setEffect(effects.goldGlow);
        welcomeGroup.getChildren().add(welcome);
    }

    private void setButtons() {
        Button startButton = new Button("START");
        startButton.setLayoutX(475);
        startButton.setLayoutY(250);
        startButton.setTextFill(Color.BLACK);
        startButton.setMinSize(100, 40);

        Button quitButton = new Button("QUIT");
        quitButton.setLayoutX(475);
        quitButton.setLayoutY(300);
        quitButton.setTextFill(Color.BLACK);
        quitButton.setMinSize(100, 40);

        startButton.setOnMouseEntered(E -> startButton.setEffect(effects.goldGlow));
        startButton.setOnMouseExited(E -> startButton.setEffect(effects.noGlow));
        startButton.setOnAction(E -> {
            welcomeGroup.getChildren().addAll(playerDetails);
            welcomeGroup.getChildren().removeAll(startButton,quitButton);
        });

        quitButton.setOnMouseEntered(E -> quitButton.setEffect(effects.goldGlow));
        quitButton.setOnMouseExited(E -> quitButton.setEffect(effects.noGlow));
        quitButton.setOnAction(E -> System.exit(0));
        welcomeGroup.getChildren().addAll(startButton, quitButton);
    }

    private void setPlayerDetails() {
        playerDetails = new VBox(player.RedPlayer, player.BluePlayer);
        playerDetails.setSpacing(5);
        playerDetails.setLayoutX(200);
        playerDetails.setLayoutY(300);
    }

    private void setWelcomeScene() {
        welcomeScene = new Scene(welcomeGroup, 1060, 600);
        welcomeScene.setFill(effects.blueToRed); // Set color
    }
}
