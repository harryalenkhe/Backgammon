import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

class UserInterface extends VBox {
    private static TextField textField;
    static TextArea textArea;
    private BufferedImage emptyBoardImageFlipped;
    private static Image boardImageFlipped;
    private static ArrayList<Integer> ownerCheckers;
    static ArrayList<Integer> MOVE_COUNT; // Has value of moves ( if a double move or single move )
    static ArrayList<String> LEGAL_MOVES;
    static ArrayList<Integer> FROM_PIPS;
    static ArrayList<Integer> TO_PIPS;
    private int moveCountTotal = 0;
    private int currentPlayer;
    static int currentHolder = 0;
    static boolean doubleRolled = false;

    UserInterface(Stage primaryWindow) {
        setBoardImageFlipped();
        setTextArea();
        setTextField();
        gamePlay(primaryWindow);
        getChildren().addAll(textField, textArea);
    }

    private void makeMove(int fromPip, int toPip, int currentPlayer) {
        textField.setText("");

        int fromColumn = GameLogic.convertPipToColumn(fromPip);
        int toColumn = GameLogic.convertPipToColumn(toPip);
        int fromRow = GameLogic.nextRow(fromColumn, fromPip, currentPlayer);
        if(fromRow < 15) {
            fromRow -= 1;
        } else {
            fromRow += 1;
        }
        int toRow = GameLogic.nextRow(toColumn, toPip, currentPlayer);
        char currentPlayerColour; // Each position has a one of 3 colours RED, BLUE OR EMPTY ( R, B, E )

        Circle toBeMoved = BoardPanel.checkerAtStartingPip(fromColumn,fromRow,currentPlayer,fromPip);

        // 3 types of moves can be made
        if (fromPip == 25) { // Moving from bar to board
            currentPlayerColour = BoardPanel.BAR[fromColumn][fromRow].getPlayerColour();
            BoardPanel.BAR[fromColumn][fromRow].releaseCoordinate();
            Checkers.moveCircle(toBeMoved, toColumn, toRow, toPip);
            BoardPanel.BOARD[toColumn][toRow].occupyCoordinate(currentPlayerColour);
        }

        if (toPip == 0) { // Moving from board to bear
            currentPlayerColour = BoardPanel.BOARD[fromColumn][fromRow].getPlayerColour();
            BoardPanel.BOARD[fromColumn][fromRow].releaseCoordinate();
            Checkers.moveCircle(toBeMoved, toColumn, toRow, toPip);
            BoardPanel.BEAR[toColumn][toRow].occupyCoordinate(currentPlayerColour);
        } else { // Moving from board to board
            currentPlayerColour = BoardPanel.BOARD[fromColumn][fromRow].getPlayerColour();
            BoardPanel.BOARD[fromColumn][fromRow].releaseCoordinate(); // Set position to no colour
            Checkers.moveCircle(toBeMoved, toColumn, toRow, toPip);
            BoardPanel.BOARD[toColumn][toRow].occupyCoordinate(currentPlayerColour); // Occupy position with colour of current player
        }
    }

    private void setBoardImageFlipped() {
        try {
            emptyBoardImageFlipped = ImageIO.read(this.getClass().getResource("/images/Pip_change.jpg"));
        } catch (IOException ex) {
            System.out.println("Image not showing / not found " + ex.toString());
        }
        boardImageFlipped = SwingFXUtils.toFXImage(emptyBoardImageFlipped, null); // For pip change
    }

    private void setTextField() {
        textField = new TextField();
        textField.setMinWidth(400);
        setSpacing(10);
        setLayoutY(60);
        setLayoutX(630);
    }

    private void setTextArea() {
        textArea = new TextArea("Please type start, to start the game:\n");
        textArea.setPrefHeight(420);
        textArea.setMaxWidth(400);
        textArea.setEditable(false);
        setSpacing(10);
        setLayoutY(95);
        setLayoutX(630);
    }

    private void gamePlay(Stage primaryWindow) {
        textField.setOnAction(E -> {
            // Takes input of two numbers and splits into two indexes of array
            String moveOption = textField.getText().toUpperCase().trim();

            if (textField.getText().equalsIgnoreCase("start")) {
                startCommand();
                BoardPanel.displayMatchScore();
                BoardPanel.setScoreBoard();
                if(LEGAL_MOVES.size() == 1) {
                    getForcedPlayAlert();
                }

                if(LEGAL_MOVES.isEmpty()) {
                    getNoMovesAlert();
                    nextCommand();
                }
            }

            if (textField.getText().equalsIgnoreCase("quit")) {
                System.exit(0);
            }

            if (moveOption.length() == 1 && moveOption.matches("\\w")) {
                char option = moveOption.charAt(0);

                int diceUsed = moveCommand(option);
                if(doubleRolled) {
                    if (moveCountTotal == 4) { // two moves have been made
                        moveCountTotal = 0;
                    } else {
                        ownerCheckers = GameLogic.findOwnCheckers(currentPlayer);
                        GameLogic.findMoveTo(ownerCheckers, currentPlayer, moveCountTotal, diceUsed); // Display moves for next dice roll;
                    }
                } else {
                    if (moveCountTotal == 2) { // two moves have been made
                        moveCountTotal = 0;
                    } else {
                        ownerCheckers = GameLogic.findOwnCheckers(currentPlayer);
                        GameLogic.findMoveTo(ownerCheckers, currentPlayer, moveCountTotal, diceUsed); // Display moves for next dice roll;
                    }
                }

                if(LEGAL_MOVES.size() == 1) {
                    getForcedPlayAlert();
                }

                if(LEGAL_MOVES.isEmpty()) {
                    getNoMovesAlert();
                    nextCommand();
                }

                announceGameWinner(primaryWindow, "Yes");
            }

            if (textField.getText().equalsIgnoreCase("cheat")) {
                textField.setText(""); textArea.setText("");
                cheatCommand();
                ownerCheckers = GameLogic.findOwnCheckers(currentPlayer);
                GameLogic.findMoveTo(ownerCheckers, currentPlayer, moveCountTotal, 0); // Display moves for next dice roll;

                if(LEGAL_MOVES.size() == 1) {
                    getForcedPlayAlert();
                }

                if(LEGAL_MOVES.isEmpty()) {
                    getNoMovesAlert();
                    nextCommand();
                }
            }

            if (textField.getText().equalsIgnoreCase("double")) {
                textField.setText("");
                doubleCommand(currentPlayer);
            }

            if (textField.getText().equalsIgnoreCase("yes")) {
                Dice.doubleOdds(currentPlayer);
                textField.setText("");
            }

            if (textField.getText().equalsIgnoreCase("no")) {
                announceGameWinner(primaryWindow, "No");
                textField.setText("");
            }
        });
    }

    private void startCommand() {
        textArea.appendText(Player.playerRed.getName() + " is Red\n");
        textArea.appendText(Player.playerBlue.getName() + " is Blue\n\n");
        textArea.appendText("Rolling Dice to see who goes first:\n");
        textField.setText("");

        do {
            Dice.rollDice();
            textArea.appendText(Player.playerRed.getName() + " rolled " + (Dice.roll1) + "\n");
            textArea.appendText(Player.playerBlue.getName() + " rolled " + (Dice.roll2) + "\n");

            if (Dice.roll1 > Dice.roll2) {
                Player.playerRed.setTurn(1); // Red = Player 1
                Player.playerBlue.setTurn(2); // Blue = Player 2
                currentPlayer = Player.playerRed.getTurn(); // Player 1 starts
                textArea.appendText(Player.playerRed.getName() + " goes first as they rolled the higher number.\n\n");
            }

            if (Dice.roll1 < Dice.roll2) {
                Player.playerBlue.setTurn(1); // Red = P2
                Player.playerRed.setTurn(2); // Blue = P1
                currentPlayer = Player.playerBlue.getTurn();
                textArea.appendText(Player.playerBlue.getName() + " goes first as they rolled the higher number.\n\n");
                BoardPanel.gameView.setImage(boardImageFlipped);
            }

            if (Dice.roll1 == Dice.roll2) {
                textArea.appendText("Both Players rolled a " + (Dice.roll1) + " so players must roll again\n\n");
            }
        } while (Dice.roll1 == Dice.roll2); // Roll to see who goes first

        ownerCheckers = GameLogic.findOwnCheckers(currentPlayer);
        GameLogic.findMoveTo(ownerCheckers, currentPlayer, moveCountTotal, 0);
    }

    private void nextCommand() {
        textField.setText("");
        Dice.rollDice();
        if (currentPlayer == Player.playerRed.getTurn()) { // If it was Red's turn, now blue's turn
            textArea.appendText("\n" + Player.playerBlue.getName() + "'s turn\n");
            rolledDouble(Player.playerBlue);
            BoardPanel.gameView.setImage(boardImageFlipped);
            currentPlayer = Player.playerBlue.getTurn();
        } else {
            textArea.appendText("\n" + Player.playerRed.getName() + "'s turn\n");
            rolledDouble(Player.playerRed);
            BoardPanel.gameView.setImage(BoardPanel.boardImage);
            currentPlayer = Player.playerRed.getTurn();
        }

        ownerCheckers = GameLogic.findOwnCheckers(currentPlayer);
        GameLogic.findMoveTo(ownerCheckers, currentPlayer, moveCountTotal, 0);
        if(LEGAL_MOVES.size() == 1) {
            getForcedPlayAlert();
            nextCommand();
        }

        if(LEGAL_MOVES.isEmpty()) {
            getNoMovesAlert();
            nextCommand();
        }
    }

    private void cheatCommand() {
        // Release all coordinates on board, bar and bear-off
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 30; j++) {
                BoardPanel.BOARD[i][j].releaseCoordinate();
            }
        }

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 30; j++) {
                BoardPanel.BEAR[i][j].releaseCoordinate();
            }
        }

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 10; j++) {
                BoardPanel.BAR[i][j].releaseCoordinate();
            }
        }

        int numReds = 0;
        int numBlues = 0;
        int currentPip = 1;
        int column = GameLogic.convertPipToColumn(currentPip);
        int row;

        for (int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) // Counts the checkers
        {
            row = GameLogic.nextRow(column, currentPip, 0);
            Checkers.moveCircle(BoardPanel.redCheckers[numReds].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate('R'); // Position now occupied
            numReds++;
        }

        currentPip = 24;
        column = GameLogic.convertPipToColumn(currentPip);
        for (int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) // Counts the checkers
        {
            row = GameLogic.nextRow(column, currentPip, 0);
            Checkers.moveCircle(BoardPanel.blueCheckers[numBlues].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate('B'); // Position now occupied
            numBlues++;
        }

        column = 0; // BEAR BLUE
        row = 0;
        for (int numCheckersInPip = 0; numCheckersInPip < 13; numCheckersInPip++) {
            Checkers.moveCircle(BoardPanel.blueCheckers[numBlues].getCircle(), column, row, 0);
            BoardPanel.BEAR[column][row].occupyCoordinate('B');
            numBlues++;
            row++;
        }

        column = 0; // BEAR RED
        row = 15;
        for (int numCheckersInPip = 0; numCheckersInPip < 13; numCheckersInPip++) {
            Checkers.moveCircle(BoardPanel.redCheckers[numReds].getCircle(), column, row, 0);
            BoardPanel.BEAR[column][row].occupyCoordinate('R');
            numReds++;
            row++;
        }
    }

    private int moveCommand(char option) {
        int diceUsed;
        textField.setText("");
        int index = option - 'A';
        if (index >= LEGAL_MOVES.size()) {
            getIllegalMoveAlert();
            diceUsed = 0;
        } else {
            moveCountTotal += MOVE_COUNT.get(index); // Get if the move is a double or single
            int fromPip = FROM_PIPS.get(index);
            int toPip = TO_PIPS.get(index);
            makeMove(fromPip, toPip, currentPlayer);
            diceUsed = fromPip - toPip;

            if (currentPlayer == Player.playerRed.getTurn()) {
                textArea.appendText(Player.playerRed.getColour() + ": " + Player.playerRed.getName() + " moved checker from pip " + fromPip + " to pip " + toPip + "\n");
                if(doubleRolled) {
                    if(moveCountTotal == 4) {
                        nextCommand();
                    }
                } else {
                    if (moveCountTotal == 2) {
                        nextCommand();
                    }
                }
            } else if (currentPlayer == Player.playerBlue.getTurn()) {
                textArea.appendText(Player.playerBlue.getColour() + ": " + Player.playerBlue.getName() + " moved checker from pip " + fromPip + " to pip " + toPip + "\n");
                if(doubleRolled) {
                    if(moveCountTotal == 4) {
                        nextCommand();
                    }
                } else {
                    if (moveCountTotal == 2) {
                        nextCommand();
                    }
                }
            }
        }
        return diceUsed;
    }

    private void doubleCommand(int currentPlayer) {
        if(Dice.canPlayerDouble(currentPlayer, currentHolder)) {
            if(currentPlayer == Player.playerRed.getTurn()) {
                textArea.appendText(Player.playerRed.getName() + " would like to double\nDo you accept?\n");
            } else {
                textArea.appendText(Player.playerBlue.getName() + " would like to double\nDo you accept?\n");
            }
        } else {
            textArea.appendText("You cannot double as you do not hold the cube!\n");
        }
    }

    private void announceGameWinner(Stage primaryWindow, String acceptDouble) {
        if (GameLogic.getWinner() == Player.playerBlue.getTurn()) {
            MatchFinish matchFinish = new MatchFinish(primaryWindow, Player.playerBlue.getTurn());
            primaryWindow.setScene(matchFinish.finishScene); // Show winner
        }

        if (GameLogic.getWinner() == Player.playerRed.getTurn()) {
            MatchFinish matchFinish = new MatchFinish(primaryWindow, Player.playerRed.getTurn());
            primaryWindow.setScene(matchFinish.finishScene); // Show winner
        }

        if (acceptDouble.equalsIgnoreCase("No")) {
            if(currentPlayer == Player.playerRed.getTurn()) {
                MatchFinish matchFinish = new MatchFinish(primaryWindow, Player.playerRed.getTurn());
                primaryWindow.setScene(matchFinish.finishScene); // Show winner
            }

            else {
                MatchFinish matchFinish = new MatchFinish(primaryWindow, Player.playerBlue.getTurn());
                primaryWindow.setScene(matchFinish.finishScene); // Show winner
            }
        }
    }

    private void getNoMovesAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No moves available");
        alert.setHeaderText("There are no moves available");
        alert.setContentText("Your turn will now be passed");
        alert.show();

        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
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

    private void getForcedPlayAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Play is forced");
        alert.setHeaderText("There is only one legal move");
        alert.setContentText("The move will now be executed");
        alert.show();

        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
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

    private void getIllegalMoveAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ILLEGAL MOVE");
        alert.setHeaderText("Entered move was not one of the options");
        alert.setContentText("Please enter a valid move");
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

    private static void rolledDouble(Player player) {
        if(Dice.roll1 == Dice.roll2) {
            textArea.appendText(player.getColour() + ": " + player.getName() + " rolled double " + Dice.roll1 + "\n");
            doubleRolled = true;
        } else {
            textArea.appendText(player.getColour() + ": " + player.getName() + " rolled " + Dice.roll1 + " and " + Dice.roll2 + "\n\n");
            doubleRolled = false;
        }
    }
}

