import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryWindow) {
        AnnounceGame announceGame = new AnnounceGame(primaryWindow);
        primaryWindow.setResizable(false);
        primaryWindow.setScene(announceGame.welcomeScene);
        primaryWindow.setTitle("Main");
        primaryWindow.show();
    }
}

