import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

class UserInterface extends VBox {

    private static TextField textField;
    private static TextArea textArea;
    private BufferedImage emptyBoardImageFlipped;
    private static Image boardImageFlipped;
    private int currentPlayer;
    private int[] ownerCheckers;
    private int[] moveTo;

    UserInterface(Stage primaryWindow) {
        setBoardImageFlipped();
        setTextArea();
        setTextField();
        setUserInputConditions(primaryWindow);
        getChildren().addAll(textField, textArea);
    }

    private int makeMove(int fromPip, int toPip, int currentPlayer) {
        int error = 0; // If there is an error then returned value will be -1
        textField.setText("");

        int fromColumn = BoardPanel.convertPipToColumn(fromPip); // Convert pip to be moved from to column index
        int toColumn = BoardPanel.convertPipToColumn(toPip); // Convert pip to be moved to, to column index
        int fromRow = BoardPanel.topCheckerInPip(fromColumn, fromPip, currentPlayer); // Get top checker position in column to be moved from


        int toRow = BoardPanel.nextRow(toColumn, toPip, currentPlayer); // Get free position in column to be moved to


        Circle toBeMoved = BoardPanel.checkerAtStartingPip(fromColumn, fromRow, currentPlayer, fromPip);
        if (fromPip == 0) { // Moving from bar to board
            BoardPanel.BAR[fromColumn][fromRow].releaseCoordinate();
            Checkers.moveCircle(toBeMoved, toColumn, toRow, toPip);
            BoardPanel.BOARD[toColumn][toRow].occupyCoordinate(); // Change position of checker to occupied
        }

        if (toPip == 25) { // Moving from board to bear
            BoardPanel.BOARD[fromColumn][fromRow].releaseCoordinate(); // Change position of checker to be moved to unoccupied
            BoardPanel.BEAR[toColumn][toRow].releaseCoordinate();
            Checkers.moveCircle(toBeMoved, toColumn, toRow, toPip);
            BoardPanel.BEAR[toColumn][toRow].occupyCoordinate(); // Change position of checker to occupied
        } else { // Moving from board to board
            BoardPanel.BOARD[fromColumn][fromRow].releaseCoordinate(); // Change position of checker to be moved to unoccupied
            BoardPanel.BOARD[toColumn][toRow].releaseCoordinate(); // Pre-caution, make sure position to be moved is unoccupied
            Checkers.moveCircle(toBeMoved, toColumn, toRow, toPip);
            BoardPanel.BOARD[toColumn][toRow].occupyCoordinate(); // Change position of checker to occupied
        }

        return error;
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

    private void setUserInputConditions(Stage primaryWindow) {
        textField.setOnAction(E -> {
            // Takes input of two numbers and splits into two indexes of array
            String moveOption = textField.getText().toUpperCase();

            if (textField.getText().equalsIgnoreCase("start")) {
                startCommand();
            }

            if (textField.getText().equalsIgnoreCase("quit")) {
                System.exit(0);
            }

//            if(textField.getText().equalsIgnoreCase("double")) {
//                textField.setText("");
//                if(Dice.x == 1) { // Cube in no one's possession
//                    Dice.firstDoubleOdds(); // Player who prompted double now holds dice
//                    currentHolder = currentPlayer;
//                }
//
//                if(Dice.x < 64 && Dice.x > 1) { // After first double prompt
//                    String acceptOrNot = textField.getText();
//                    if(Dice.acceptDouble(acceptOrNot)) {
//                        currentHolder = Dice.doubleOdds(currentPlayer, currentHolder);
//                    }
//
//                    else {
//                        textArea.appendText("Player did not accept double\n");
//                    }
//                }
//
//                else {
//                    textArea.appendText("Odds can not be doubled anymore\n");
//                }
//
//            } // Doubles the odds and flips cube //

            if (textField.getText().equalsIgnoreCase("next")) {
                nextCommand();
            }

            if (moveOption.trim().length() == 1) {
                char option = moveOption.charAt(0);
                moveCommand(option, ownerCheckers, moveTo, primaryWindow);
            }

            if (textField.getText().equalsIgnoreCase("cheat")) {
                textField.setText("");
                BoardPanel.setUpCheat();
            }
        });
    }

    private void startCommand() {
        textArea.appendText(Player.playerRed.getName() + " is using red\n");
        textArea.appendText(Player.playerBlue.getName() + " is using blue\n\n");
        textArea.appendText("Rolling Dice to see who goes first:\n");
        textField.setText("");
        do {
            Dice.rollDice();
            textArea.appendText(Player.playerRed.getName() + " rolled " + (Dice.roll1 + 1) + "\n");
            textArea.appendText(Player.playerBlue.getName() + " rolled " + (Dice.roll2 + 1) + "\n");
            if (Dice.roll1 > Dice.roll2) {
                Player.playerRed.setTurn(1); // Red = Player 1
                Player.playerBlue.setTurn(2); // Blue = Player 2
                currentPlayer = Player.playerRed.getTurn(); // Player 1 starts
                textArea.appendText(Player.playerRed.getName() + " is player " + Player.playerRed.getTurn() + " as they rolled the higher number.\n\n");
            }

            if (Dice.roll1 < Dice.roll2) {
                Player.playerBlue.setTurn(1); // Red = P2
                Player.playerRed.setTurn(2); // Blue = P1
                currentPlayer = Player.playerBlue.getTurn();
                textArea.appendText(Player.playerBlue.getName() + " is player " + Player.playerBlue.getTurn() + " as they rolled the higher number.\n\n");
                BoardPanel.gameView.setImage(boardImageFlipped);
            }

            if (Dice.roll1 == Dice.roll2) {
                textArea.appendText("Both Players rolled a " + (Dice.roll1 + 1) + " so players must move again\n\n");
            }
        } while (Dice.roll1 == Dice.roll2); // Roll to see who goes first

        ownerCheckers = GameLogic.findOwnCheckers(currentPlayer);
        moveTo = GameLogic.findMoveTo(ownerCheckers);
        displayLegalMoves(ownerCheckers, moveTo);
    }

    private void nextCommand() {
        textField.setText("");
        Dice.rollDice();
        if (currentPlayer == Player.playerRed.getTurn()) { // If it was Red's turn, now blue's turn
            textArea.appendText("\n" + Player.playerBlue.getName() + "'s turn\n");
            textArea.appendText(Player.playerBlue.getColour() + ": " + Player.playerBlue.getName() + " rolled " + (Dice.roll1 + 1) + " and " + (Dice.roll2 + 1) + "\n\n");
            BoardPanel.gameView.setImage(boardImageFlipped);
            currentPlayer = Player.playerBlue.getTurn();
        } else {
            textArea.appendText("\n" + Player.playerRed.getName() + "'s turn\n");
            textArea.appendText(Player.playerRed.getColour() + ": " + Player.playerRed.getName() + " rolled " + (Dice.roll1 + 1) + " and " + (Dice.roll2 + 1) + "\n\n");
            BoardPanel.gameView.setImage(BoardPanel.boardImage);
            currentPlayer = Player.playerRed.getTurn();
        }
        ownerCheckers = GameLogic.findOwnCheckers(currentPlayer);
        moveTo = GameLogic.findMoveTo(ownerCheckers);
        displayLegalMoves(ownerCheckers, moveTo);
    }

    private void moveCommand(char option, int[] ownerCheckers, int[] moveTo, Stage primaryWindow) {
        int index = option - 'A';
        int fromPip = ownerCheckers[index];
        int toPip = moveTo[index];
        textField.setText("");
        if (makeMove(fromPip, toPip, currentPlayer) != -1) { // If no errors we can print the checkers movement as a string
            if (currentPlayer == Player.playerRed.getTurn()) {
                textArea.appendText(Player.playerRed.getColour() + ": " + Player.playerRed.getName() + " moved checker from pip " + fromPip + " to pip " + toPip + "\n");
                ownerCheckers = GameLogic.findOwnCheckers(currentPlayer);
                moveTo = GameLogic.findMoveTo(ownerCheckers);
                displayLegalMoves(ownerCheckers, moveTo);
            }
            if (currentPlayer == Player.playerBlue.getTurn()) {
                textArea.appendText(Player.playerBlue.getColour() + ": " + Player.playerBlue.getName() + " moved checker from pip " + fromPip + " to pip " + toPip + "\n");
                ownerCheckers = GameLogic.findOwnCheckers(currentPlayer);
                moveTo = GameLogic.findMoveTo(ownerCheckers);
                displayLegalMoves(ownerCheckers, moveTo);
            }
            if (BoardPanel.getWinner() == Player.playerRed.getTurn() || BoardPanel.getWinner() == Player.playerBlue.getTurn()) {
                GameFinish gameFinish = new GameFinish();
                primaryWindow.setScene(gameFinish.finishScene);
            }
        }
    }

    private void displayLegalMoves(int[] ownerCheckers, int[] moveTo) {
        String[] LEGAL_MOVES = new String[15];
        char letter = 'A';

        textArea.appendText("Available moves:\n");
        for (int index = 0; index < moveTo.length; index++) {
            LEGAL_MOVES[index] = ownerCheckers[index] + " - " + moveTo[index];
            if (LEGAL_MOVES[0] == null) {
                textArea.appendText("There are no moves available, You pass your turn.\n");
                nextCommand();
                break;
            }
            if (moveTo[index] > 0) {
                textArea.appendText(letter++ + ": " + LEGAL_MOVES[index] + "\n");
            }
        }
        textArea.appendText("\n");
    }
}


