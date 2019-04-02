import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class AnnounceGame {

    Scene welcomeScene;
    private Effects effects;
    Group welcomeGroup;
    VBox playerDetails;
    private Player player;
    private HBox scoreBox;
    static int matchScore;
    private String matchInfo;
    Button startButton;
    Button quitButton;

    AnnounceGame(Stage primaryWindow) {
        welcomeGroup = new Group();
        effects = new Effects();
        new BoardPanel(primaryWindow);
        player = new Player();
        player.getDetails();
        setWelcomeText();
        setButtons();
        setMatchScore(primaryWindow);
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
        startButton = new Button("START");
        startButton.setLayoutX(475);
        startButton.setLayoutY(250);
        startButton.setTextFill(Color.BLACK);
        startButton.setMinSize(100, 40);

        quitButton = new Button("QUIT");
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
        playerDetails = new VBox(player.RedPlayer, player.BluePlayer,scoreBox);
        playerDetails.setSpacing(10);
        playerDetails.setLayoutX(200);
        playerDetails.setLayoutY(300);
    }

    private void setMatchScore(Stage primaryWindow){
        Text label = new Text("How many points you are playing to:");
        label.setEffect(effects.goldGlow);
        label.setFill(Color.BLACK);
        label.setFont(Font.font(null, FontWeight.BOLD, 16));

        TextField textField = new TextField();
        textField.setPromptText("Enter number of points.");
        textField.setOnAction(E -> {
            if ((textField.getText() != null && !textField.getText().isEmpty())) {
                matchScore = Integer.parseInt(textField.getText());
                System.out.println(matchScore);
                matchInfo = "To win you must get: " + matchScore + " points.\n";
                scoreBox.getChildren().remove(textField);
                label.setText(matchInfo);
                primaryWindow.setScene(BoardPanel.gameBoard);
            } else {
                getAlert();
            }
        });

        scoreBox = new HBox();
        scoreBox.getChildren().setAll(label, textField);
        scoreBox.setSpacing(5);
    }

    private void setWelcomeScene() {
        welcomeScene = new Scene(welcomeGroup, 1060, 600);
        welcomeScene.setFill(effects.blueToRed); // Set color
    }

    private void getAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No score entered");
        alert.setHeaderText("NO PLAYING SCORE ENTERED");
        alert.setContentText("Please enter a match score before continuing");
        alert.show();

        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        alert.close();
                    }
                });
            }
        });
        newThread.start();
    }

}