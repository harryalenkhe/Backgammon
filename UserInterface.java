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
import java.util.stream.IntStream;

class UserInterface extends VBox {
    private static TextField textField;
    private static TextArea textArea;
    private BufferedImage emptyBoardImageFlipped;
    private static Image boardImageFlipped;
    private ArrayList<Integer> ownerCheckers;
    private ArrayList<Integer> MOVE_COUNT; // Has value of moves ( if a double move or single move )
    private ArrayList<String> LEGAL_MOVES;
    private ArrayList<Integer> FROM_PIPS;
    private ArrayList<Integer> TO_PIPS;
    private int[][] moveTo;
    private int moveCountTotal = 0;
    private int currentPlayer;
    private boolean canBearOff = false;

    UserInterface(Stage primaryWindow) {
        setBoardImageFlipped();
        setTextArea();
        setTextField();
        gamePlay(primaryWindow);
        getChildren().addAll(textField, textArea);
    }

    private void makeMove(int fromPip, int toPip, int currentPlayer) {
        textField.setText("");

        int fromColumn = GameLogic.convertPipToColumn(fromPip); // Convert pip to be moved from to column index
        int toColumn = GameLogic.convertPipToColumn(toPip); // Convert pip to be moved to, to column index
        int fromRow = GameLogic.topCheckerInPip(fromColumn, fromPip, currentPlayer); // Get top checker position in column to be moved from
        int toRow = GameLogic.nextRow(toColumn, toPip, currentPlayer); // Get free position in column to be moved to
        char currentPlayerColour; // Each position has a one of 3 colours RED, BLUE OR EMPTY ( R, B, E )

        Circle toBeMoved = BoardPanel.checkerAtStartingPip(fromColumn, fromRow, currentPlayer, fromPip);

        // 3 types of moves can be made
        if (fromPip == 0) { // Moving from bar to board
            currentPlayerColour = BoardPanel.BAR[fromColumn][fromRow].getPlayer();
            BoardPanel.BAR[fromColumn][fromRow].releaseCoordinate();
            Checkers.moveCircle(toBeMoved, toColumn, toRow, toPip);
            BoardPanel.BOARD[toColumn][toRow].occupyCoordinate(currentPlayerColour);
        }

        if (toPip == 25) { // Moving from board to bear
            currentPlayerColour = BoardPanel.BOARD[fromColumn][fromRow].getPlayer();
            BoardPanel.BOARD[fromColumn][fromRow].releaseCoordinate();
            Checkers.moveCircle(toBeMoved, toColumn, toRow, toPip);
            BoardPanel.BEAR[toColumn][toRow].occupyCoordinate(currentPlayerColour);
        } else { // Moving from board to board
            currentPlayerColour = BoardPanel.BOARD[fromColumn][fromRow].getPlayer();
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

            if (moveOption.trim().length() == 1 && moveOption.matches("\\w")) {
                char option = moveOption.charAt(0);

                moveCommand(option);
                if (moveCountTotal == 2) { // two moves have been made
                    moveCountTotal = 0;
                }

                if (moveCountTotal == 1) {
                    ownerCheckers = GameLogic.findOwnCheckers(currentPlayer);
                    moveTo = GameLogic.findMoveTo(ownerCheckers); // Display moves for next dice roll;
                    displayLegalMoves(moveTo);
                }

                if (GameLogic.getWinner() == Player.playerRed.getTurn() || GameLogic.getWinner() == Player.playerBlue.getTurn()) {
                    GameFinish gameFinish = new GameFinish();
                    primaryWindow.setScene(gameFinish.finishScene); // Show winner
                }
            }

            if (textField.getText().equalsIgnoreCase("cheat")) {
                textField.setText("");
                textArea.setText("");
                cheatCommand();
                ownerCheckers = GameLogic.findOwnCheckers(currentPlayer);
                moveTo = GameLogic.findMoveTo(ownerCheckers); // Display moves for next dice roll;
                displayLegalMoves(moveTo);
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
        displayLegalMoves(moveTo);
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
        displayLegalMoves(moveTo);
    }

    private void cheatCommand() {

        // Release all coordinates on board, bar and bear-off
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
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
        int currentPip = 1; // PIP TO BE CONVERTED TO COLUMN
        int column = GameLogic.convertPipToColumn(currentPip); // COLUMN OF PIP THAT WAS CONVERTED
        int row;

        for (int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) // Counts the checkers
        {
            row = GameLogic.nextRow(column, currentPip, 0);
            Checkers.moveCircle(BoardPanel.redCheckers[numReds].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate('R'); // Position now occupied
            numReds++;
        }

        currentPip = 2;
        column = GameLogic.convertPipToColumn(currentPip);
        for (int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) // Counts the checkers
        {
            row = GameLogic.nextRow(column, currentPip, 0);
            Checkers.moveCircle(BoardPanel.redCheckers[numReds].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate('R'); // Position now occupied
            numReds++;
        }

        currentPip = 3;
        column = GameLogic.convertPipToColumn(currentPip);
        for (int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) // Counts the checkers
        {
            row = GameLogic.nextRow(column, currentPip, 0);
            Checkers.moveCircle(BoardPanel.redCheckers[numReds].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate('R'); // Position now occupied
            numReds++;
        }

        currentPip = 4;
        column = GameLogic.convertPipToColumn(currentPip);
        for (int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) // Counts the checkers
        {
            row = GameLogic.nextRow(column, currentPip, 0);
            Checkers.moveCircle(BoardPanel.redCheckers[numReds].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate('R'); // Position now occupied
            numReds++;
        }

        currentPip = 5;
        column = GameLogic.convertPipToColumn(currentPip);
        for (int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) // Counts the checkers
        {
            row = GameLogic.nextRow(column, currentPip, 0);
            Checkers.moveCircle(BoardPanel.redCheckers[numReds].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate('R'); // Position now occupied
            numReds++;
        }

        column = 0; // BAR RED
        row = 0;
        for (int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++) {
            Checkers.moveCircle(BoardPanel.redCheckers[numReds].getCircle(), column, row, 0);
            BoardPanel.BAR[column][row].occupyCoordinate('R');
            numReds++;
            row++;
        }

        column = 0; // BEAR RED
        row = 15;
        for (int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) {
            Checkers.moveCircle(BoardPanel.redCheckers[numReds].getCircle(), column, row, 25);
            BoardPanel.BEAR[column][row].occupyCoordinate('R');
            numReds++;
            row++;
        }

        column = 0; // BAR BLUE
        row = 5;
        for (int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++) {
            Checkers.moveCircle(BoardPanel.blueCheckers[numBlues].getCircle(), column, row, 0);
            BoardPanel.BAR[column][row].occupyCoordinate('B');
            numBlues++;
            row++;
        }

        column = 0; // BEAR BLUE
        row = 0;
        for (int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++) {
            Checkers.moveCircle(BoardPanel.blueCheckers[numBlues].getCircle(), column, row, 25);
            BoardPanel.BEAR[column][row].occupyCoordinate('B');
            numBlues++;
            row++;
        }


        currentPip = 24;
        column = GameLogic.convertPipToColumn(currentPip);
        for (int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++) // Counts the checkers
        {
            row = GameLogic.nextRow(column, currentPip, 0);
            Checkers.moveCircle(BoardPanel.blueCheckers[numBlues].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate('B'); // Position now occupied
            numBlues++;
        }

        currentPip = 22;
        column = GameLogic.convertPipToColumn(currentPip);
        for (int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++) // Counts the checkers
        {
            row = GameLogic.nextRow(column, currentPip, 0);
            Checkers.moveCircle(BoardPanel.blueCheckers[numBlues].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate('B'); // Position now occupied
            numBlues++;
        }

        currentPip = 21;
        column = GameLogic.convertPipToColumn(currentPip);
        for (int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++) // Counts the checkers
        {
            row = GameLogic.nextRow(column, currentPip, 0);
            Checkers.moveCircle(BoardPanel.blueCheckers[numBlues].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate('B'); // Position now occupied
            numBlues++;
        }

    } // Cheat testing

    private void moveCommand(char option) {
        textField.setText("");
        int index = option - 'A';
        if (index >= LEGAL_MOVES.size()) {
            getIllegalMoveAlert();
        } else {
            moveCountTotal += MOVE_COUNT.get(index); // Get if the move is a double or single
            int fromPip = FROM_PIPS.get(index);
            int toPip = TO_PIPS.get(index);
            makeMove(fromPip, toPip, currentPlayer);

            if (currentPlayer == Player.playerRed.getTurn()) {
                textArea.appendText(Player.playerRed.getColour() + ": " + Player.playerRed.getName() + " moved checker from pip " + fromPip + " to pip " + toPip + "\n");
                if (moveCountTotal == 2) {
                    nextCommand();
                }
            } else if (currentPlayer == Player.playerBlue.getTurn()) {
                textArea.appendText(Player.playerBlue.getColour() + ": " + Player.playerBlue.getName() + " moved checker from pip " + fromPip + " to pip " + toPip + "\n");
                if (moveCountTotal == 2) {
                    nextCommand();
                }
            }
        }
    }

    private void displayLegalMoves(int[][] moveTo) {
        FROM_PIPS = new ArrayList<>();
        TO_PIPS = new ArrayList<>();
        LEGAL_MOVES = new ArrayList<>();
        MOVE_COUNT = new ArrayList<>();
        char letter = 'A'; // To be used as selection option for player

        textArea.appendText("Available moves:\n");
        for (int[] ints : moveTo) {
            if (canBearOff) { // CAN BEAR OFF
                if (ints[1] == 0 || ints[2] == 0) {  // SINGLE BEAR-OFF MOVE
                    LEGAL_MOVES.add(letter++ + ": " + ints[0] + " - Off");
                    FROM_PIPS.add(ints[0]);
                    TO_PIPS.add(25);
                    MOVE_COUNT.add(1);
                } else if (ints[3] == 0) { // DOUBLE BEAR-OFF MOVE
                    LEGAL_MOVES.add(letter++ + ": " + ints[0] + " - " + ints[1] + "  " + ints[1] + " - Off");
                    FROM_PIPS.add(ints[0]);
                    TO_PIPS.add(25); // Bear off pip positions is 25
                    MOVE_COUNT.add(2);

                    LEGAL_MOVES.add(letter++ + ": " + ints[0] + " - " + ints[2] + "  " + ints[2] + " - Off");
                    FROM_PIPS.add(ints[0]);
                    TO_PIPS.add(25);
                    MOVE_COUNT.add(2); // Move using two dice rolls counts as two moves
                } else { // NON BEAR-OFF MOVE
                    if (ints[0] == 0) { // BAR-TO-BOARD MOVE
                        LEGAL_MOVES.add(letter++ + ": BAR - " + ints[2] + "  " + ints[2] + " - " + ints[3]);
                        FROM_PIPS.add(ints[0]);
                        TO_PIPS.add(ints[3]);
                        MOVE_COUNT.add(2);

                        LEGAL_MOVES.add(letter++ + ": BAR - " + ints[1] + "  " + ints[1] + " - " + ints[3]);
                        FROM_PIPS.add(ints[0]);
                        TO_PIPS.add(ints[3]);
                        MOVE_COUNT.add(2);
                    } else { // BOARD-TO-BOARD MOVE
                        LEGAL_MOVES.add(letter++ + ": " + ints[0] + " - " + ints[2] + "  " + ints[2] + " - " + ints[3]);
                        FROM_PIPS.add(ints[0]);
                        TO_PIPS.add(ints[3]);
                        MOVE_COUNT.add(2);

                        LEGAL_MOVES.add(letter++ + ": " + ints[0] + " - " + ints[1] + "  " + ints[1] + " - " + ints[3]);
                        FROM_PIPS.add(ints[0]);
                        TO_PIPS.add(ints[3]);
                        MOVE_COUNT.add(2);
                    }
                }
            }

            if (!canBearOff) { // cant bear off
                if (ints[3] != 0) { // NON BEAR-OFF MOVE
                    if (ints[0] == 0) { // BAR-TO-BOARD MOVE
                        LEGAL_MOVES.add(letter++ + ": BAR - " + ints[2] + "  " + ints[2] + " - " + ints[3]);
                        FROM_PIPS.add(ints[0]);
                        TO_PIPS.add(ints[3]);
                        MOVE_COUNT.add(2);

                        LEGAL_MOVES.add(letter++ + ": BAR - " + ints[1] + "  " + ints[1] + " - " + ints[3]);
                        FROM_PIPS.add(ints[0]);
                        TO_PIPS.add(ints[3]);
                        MOVE_COUNT.add(2);
                    } else { // BOARD-TO-BOARD MOVE
                        LEGAL_MOVES.add(letter++ + ": " + ints[0] + " - " + ints[2] + "  " + ints[2] + " - " + ints[3]);
                        FROM_PIPS.add(ints[0]);
                        TO_PIPS.add(ints[3]);
                        MOVE_COUNT.add(2);

                        LEGAL_MOVES.add(letter++ + ": " + ints[0] + " - " + ints[1] + "  " + ints[1] + " - " + ints[3]);
                        FROM_PIPS.add(ints[0]);
                        TO_PIPS.add(ints[3]);
                        MOVE_COUNT.add(2);
                    }
                } else {
                    if (ints[1] != 0 && ints[2] != 0) {
                        LEGAL_MOVES.add(letter++ + ": " + ints[0] + " - " + ints[1]);
                        FROM_PIPS.add(ints[0]);
                        TO_PIPS.add(ints[1]);
                        MOVE_COUNT.add(1);

                        LEGAL_MOVES.add(letter++ + ": " + ints[0] + " - " + ints[2]);
                        FROM_PIPS.add(ints[0]);
                        TO_PIPS.add(ints[2]);
                        MOVE_COUNT.add(1);
                    } else if (ints[1] == 0) {
                        LEGAL_MOVES.add(letter++ + ": " + ints[0] + " - " + ints[2]);
                        FROM_PIPS.add(ints[0]);
                        TO_PIPS.add(ints[2]);
                        MOVE_COUNT.add(1);
                    } else {
                        LEGAL_MOVES.add(letter++ + ": " + ints[0] + " - " + ints[1]);
                        FROM_PIPS.add(ints[0]);
                        TO_PIPS.add(ints[1]);
                        MOVE_COUNT.add(1);
                    }
                }
            }
        }

        if (LEGAL_MOVES.size() == 0) {
            getNoMovesAlert();
            nextCommand();
        }

        if (LEGAL_MOVES.size() == 1) {
            getForcedPlayAlert();
            moveCommand('A'); // Make move with the forced legal move
            nextCommand(); // Pass
        } else {
            IntStream.range(0, LEGAL_MOVES.size()).forEach(index -> textArea.appendText(LEGAL_MOVES.get(index) + "\n"));
        }
        textArea.appendText("\n");
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
    } // Warning for no moves available

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
    } // Warning for forced play

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
    } // Warning for incorrect selection of moves

//    private boolean isCanBearOff(int currentPlayer) {
//        char currentPlayerColour;
//        int column;
//        int row;
//        int pip = 0;
//        if(currentPlayer == Player.playerBlue.getTurn()) {
//            currentPlayerColour = 'B';
//
//            if() {
//              canBearOff = false;
//            } else {
//                for(pip = 24; pip > 6; pip--) {
//                    if(position is occupied with blue) canBearOff = false;
//                }
//                canBearOff = true;
//              }
//        }
//
//        if(currentPlayer == Player.playerRed.getTurn()) {
//            currentPlayerColour = 'R';
//
//            if() {
//              canBearOff = false;
//            } else {
//                for(pip = 24; pip > 6; pip--) {
//                    if(position is occupied with blue) canBearOff = false;
//                }
//                canBearOff = true;
//              }
//        }
//        return canBearOff;
//    }
}


