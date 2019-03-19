import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

class Player {
	private String name;
	private String colour;
	private int turn;
	static Player playerRed;
	static Player playerBlue;
	HBox RedPlayer;
	HBox BluePlayer;
	private String p1Info;
	private String p2Info;
	private Effects effects;

	Player() {
		this.name = "";
		this.colour = "";
		this.turn = 0;
		effects = new Effects();
	}

	Player(String name, String colour, int t) {
		this.name = name;
		this.colour = colour;
		this.turn = t;
	}

	String getName() {
		return this.name;
	}

	String getColour() {
		return this.colour;
	}

	void getDetails(Stage primaryWindow) {
		effects = new Effects();

		Text label1 = new Text("Enter Player's Name:");
		label1.setEffect(effects.goldGlow);
		label1.setFill(Color.RED);
		label1.setUnderline(true);
		label1.setFont(Font.font(null, FontWeight.BOLD, 25));
		TextField textField1 = new TextField();
		textField1.setPromptText("Enter your name.");
		textField1.setOnAction(E -> {
			if ((textField1.getText() != null && !textField1.getText().isEmpty())) {
				playerRed = new Player(textField1.getText(), "Red", 0);
				p1Info = playerRed.getName() + " = " + playerRed.getColour() + "\n";
				RedPlayer.getChildren().remove(textField1);
				label1.setText(p1Info);
			} else { }
		});

		Text label2 = new Text("Enter Player's Name:");
		label2.setEffect(effects.goldGlow);
		label2.setFill(Color.BLUE);
		label2.setUnderline(true);
		label2.setFont(Font.font(null, FontWeight.BOLD, 25));
		TextField textField2 = new TextField();
		textField2.setPromptText("Enter your name.");
		textField2.setOnAction(E -> {
			if ((textField2.getText() != null && !textField2.getText().isEmpty())) {
				playerBlue = new Player(textField2.getText(), "Blue", 0);
				p2Info = playerBlue.getName() + " = " + playerBlue.getColour() + "\n";
				BluePlayer.getChildren().remove(textField2);
				label2.setText(p2Info);
				primaryWindow.setScene(BoardPanel.gameBoard);
			} else { }
		});

		RedPlayer = new HBox();
		RedPlayer.getChildren().setAll(label1, textField1);
		RedPlayer.setSpacing(5);

		BluePlayer = new HBox();
		BluePlayer.getChildren().setAll(label2, textField2);
		BluePlayer.setSpacing(5);
	}

	void setTurn(int t) {
		this.turn = t;
	}

	int getTurn() {
		return this.turn;
	}
}
