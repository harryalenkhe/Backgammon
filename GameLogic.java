import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

class GameLogic {
    static ArrayList<Integer> findOwnCheckers(int currentPlayer) {
        double checkerX;
        double checkerY;
        int column;
        int pip;
        Set<Integer> ownCheckersSet = new HashSet<>(); // Used to store unique values only
        ArrayList<Integer> ownCheckersArrayList = new ArrayList<>();

        if (currentPlayer == Player.playerRed.getTurn()) { // If red players turn
            for (int index = 0; index < 15; index++) {
                checkerX = BoardPanel.redCheckers[index].getCircleX();
                checkerY = BoardPanel.redCheckers[index].getCircleY();

                if(checkerX == 325.775) { // If on the bar
                    pip = 25;
                    ownCheckersSet.add(pip);
                } else {
                    column = (int) (((checkerX - 109)/33.35) + 0.5d); // Get column from X coordinate
                    if(column > 5) { // To make up for skipping the bar
                        column -= 2;
                    }

                    if (checkerY <= 241) { // If top half of board
                        pip = column + 13;
                        ownCheckersSet.add(pip);
                    }

                    else if (checkerY > 241) { // If bottom half of board
                        pip = Math.abs(column - 12);
                        ownCheckersSet.add(pip);
                    }
                }
            }
        }

        if (currentPlayer == Player.playerBlue.getTurn()) { // If blue players turn
            for (int index = 0; index < 15; index++) {
                checkerX = BoardPanel.blueCheckers[index].getCircleX();
                checkerY = BoardPanel.blueCheckers[index].getCircleY();

                if(checkerX == 325.775) { // If checker on bar
                    pip = 25;
                    ownCheckersSet.add(pip);
                } else { // If checker on board
                    column = (int) (((checkerX - 109) / 33.35) + 0.5d);
                    if(column > 5) {
                        column -= 2;
                    }

                    if (checkerY <= 241) { // If top half of board
                        pip = Math.abs(column - 12);
                        ownCheckersSet.add(pip); // Store pip in array
                    }

                    else if (checkerY > 241) { // If bottom half of board
                        pip = column + 13;
                        ownCheckersSet.add(pip); // Store pip in array
                    }
                }
            }
        }

        // Transferred to arrayList as it does not maintain the order elements are stored
        for (Object eachObject : ownCheckersSet) {
            ownCheckersArrayList.add((int) eachObject);
        }
        return ownCheckersArrayList;
    }

    static void findMoveTo(ArrayList<Integer> ownerCheckers, int currentPlayer, int MOVE_COUNT, int diceUsed) {
        int[][] moveTo = new int[ownerCheckers.size()][6];
        UserInterface.FROM_PIPS = new ArrayList<>();
        UserInterface.TO_PIPS = new ArrayList<>();
        UserInterface.LEGAL_MOVES = new ArrayList<>();
        UserInterface.MOVE_COUNT = new ArrayList<>();
        char letter = 'A'; // To be used as selection option for player

        for (int index = 0; index < ownerCheckers.size(); index++) {
            // First assign variables to pips moved to from dice rolls
            int startPip = ownerCheckers.get(index);
            int singleToPip1 = startPip - (Dice.roll1);
            int singleToPip2 = startPip - (Dice.roll2);
            int doubleToPip = startPip - (Dice.roll1 + Dice.roll2);
            int tripleToPip = startPip - (Dice.roll1*3);
            int quadToPip = startPip - (Dice.roll1*4);
            moveTo[index][0] = startPip; // store starting pip


            if (canBearOff(ownerCheckers, currentPlayer)) {
                if(diceUsed == 0 || diceUsed == 2) {
                    if (isBearOffMove(singleToPip1)) { // Is a bear-off move
                        UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - Off");
                        UserInterface.FROM_PIPS.add(moveTo[index][0]);
                        UserInterface.TO_PIPS.add(0);
                        UserInterface.MOVE_COUNT.add(1);
                    } else { // Not a bear-off move
                        if (isLegalMove(singleToPip1, currentPlayer, true)) { // Is a legal move
                            moveTo[index][1] = singleToPip1;
                            if (isBearOffMove(doubleToPip)) { // Is a bear-off move
                                if (MOVE_COUNT == 0 || MOVE_COUNT == 2) {
                                    UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][1] + " " + moveTo[index][1] + " - Off");
                                    UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                    UserInterface.TO_PIPS.add(0);
                                    UserInterface.MOVE_COUNT.add(2);
                                }
                            } else { // Not a bear-off move
                                if (isLegalMove(doubleToPip, currentPlayer, true)) { // Is a legal move
                                    moveTo[index][3] = doubleToPip;

                                    if(UserInterface.doubleRolled) {
                                        if (isBearOffMove(tripleToPip)) { // Is a bear-off move
                                            if (MOVE_COUNT == 1 || MOVE_COUNT == 0) {
                                                UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][1] + " " + moveTo[index][1] + " - " + moveTo[index][3] + " " + moveTo[index][3] + " - Off");
                                                UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                UserInterface.TO_PIPS.add(0);
                                                UserInterface.MOVE_COUNT.add(3);
                                            }
                                        } else { // Not a bear off move
                                            if (isLegalMove(tripleToPip, currentPlayer, true)) { // Is a legal move
                                                moveTo[index][4] = tripleToPip;
                                                if (isBearOffMove(quadToPip)) {
                                                    if (MOVE_COUNT == 0) {
                                                        UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][1] + " " + moveTo[index][1] + " - " + moveTo[index][3] + " " + moveTo[index][3] + " - " + moveTo[index][4] + " " + moveTo[index][4] + " - Off");
                                                        UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                        UserInterface.TO_PIPS.add(0);
                                                        UserInterface.MOVE_COUNT.add(4);
                                                    }
                                                } else { // Not a bear off move
                                                    if (isLegalMove(quadToPip, currentPlayer, true)) {
                                                        moveTo[index][5] = quadToPip;
                                                        if (MOVE_COUNT == 0) {
                                                            UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][1] + " " + moveTo[index][1] + " - " + moveTo[index][3] + " " + moveTo[index][3] + " - " + moveTo[index][4] + " " + moveTo[index][4] + " - " + moveTo[index][5]);
                                                            UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                            UserInterface.TO_PIPS.add(moveTo[index][5]);
                                                            UserInterface.MOVE_COUNT.add(4);
                                                        }
                                                    } else { // Not a bear off move
                                                        if (MOVE_COUNT == 1 || MOVE_COUNT == 0) {
                                                            UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][1] + " " + moveTo[index][1] + " - " + moveTo[index][3] + " " + moveTo[index][3] + " - " + moveTo[index][4]);
                                                            UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                            UserInterface.TO_PIPS.add(moveTo[index][4]);
                                                            UserInterface.MOVE_COUNT.add(3);
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (MOVE_COUNT == 0 || MOVE_COUNT == 2) {
                                                    UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][1] + " " + moveTo[index][1] + " - " + moveTo[index][3]);
                                                    UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                    UserInterface.TO_PIPS.add(moveTo[index][3]);
                                                    UserInterface.MOVE_COUNT.add(2);
                                                }
                                            }
                                        }
                                    }
                                } else { // Not a legal move
                                    UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][1]);
                                    UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                    UserInterface.TO_PIPS.add(moveTo[index][1]);
                                    UserInterface.MOVE_COUNT.add(1);
                                }
                            }
                        }
                    }
                }

                if(diceUsed == 0 || diceUsed == 1) {
                    if (isBearOffMove(singleToPip2)) { // Is a bear-off move
                        UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - Off");
                        UserInterface.FROM_PIPS.add(moveTo[index][0]);
                        UserInterface.TO_PIPS.add(0);
                        UserInterface.MOVE_COUNT.add(1);
                    }
                    else { // Not a bear-off move
                        if (isLegalMove(singleToPip2, currentPlayer, true)) { // Is a legal move
                            moveTo[index][2] = singleToPip2;
                            if (isBearOffMove(doubleToPip)) { // Is a bear-off move
                                if(!isLegalMove(singleToPip1, currentPlayer, true)) {
                                    if(MOVE_COUNT == 0 || MOVE_COUNT == 2) {
                                        UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][2] + " " + moveTo[index][2] + " - Off");
                                        UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                        UserInterface.TO_PIPS.add(0);
                                        UserInterface.MOVE_COUNT.add(2);
                                    }
                                }
                            }
                            else { // Not a bear-off move
                                if (isLegalMove(doubleToPip, currentPlayer, true)) { // Is a legal move
                                    moveTo[index][3] = doubleToPip;
                                    if (isBearOffMove(tripleToPip)) { // Is a bear-off move
                                        if(!isLegalMove(singleToPip1, currentPlayer, true)) {
                                            if(UserInterface.doubleRolled && (MOVE_COUNT == 1 || MOVE_COUNT == 0)) {
                                                UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][2] + " " + moveTo[index][2] + " - " + moveTo[index][3] + " " + moveTo[index][3] + " - Off");
                                                UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                UserInterface.TO_PIPS.add(0);
                                                UserInterface.MOVE_COUNT.add(3);
                                            }
                                        }
                                    }
                                    else { // Not a bear off move
                                        if (isLegalMove(tripleToPip, currentPlayer, true)) { // Is a legal move
                                            moveTo[index][4] = tripleToPip;
                                            if (isBearOffMove(quadToPip)) {
                                                if(!isLegalMove(singleToPip1, currentPlayer, true)) {
                                                    if(UserInterface.doubleRolled && MOVE_COUNT == 0) {
                                                        UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][2] + " " + moveTo[index][2] + " - " + moveTo[index][3] + " " + moveTo[index][3] + " - " + moveTo[index][4] + " " + moveTo[index][4] + " - Off");
                                                        UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                        UserInterface.TO_PIPS.add(0);
                                                        UserInterface.MOVE_COUNT.add(4);
                                                    }
                                                }
                                            }
                                            else {
                                                if (isLegalMove(quadToPip, currentPlayer, true)) {
                                                    moveTo[index][5] = quadToPip;

                                                    if(!isLegalMove(singleToPip1, currentPlayer, true)) {
                                                        if(UserInterface.doubleRolled && MOVE_COUNT == 0) {
                                                            UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][2] + " " + moveTo[index][2] + " - " + moveTo[index][3] + " " + moveTo[index][3] + " - " + moveTo[index][4] + " " + moveTo[index][4] + " - " + moveTo[index][5]);
                                                            UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                            UserInterface.TO_PIPS.add(moveTo[index][5]);
                                                            UserInterface.MOVE_COUNT.add(4);
                                                        }
                                                    }
                                                }
                                                else {
                                                    if(!isLegalMove(singleToPip1, currentPlayer, true)) {
                                                        if(UserInterface.doubleRolled && (MOVE_COUNT == 1 || MOVE_COUNT == 0)) {
                                                            UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][2] + " " + moveTo[index][2] + " - " + moveTo[index][3] + " " + moveTo[index][3] + " - " + moveTo[index][4]);
                                                            UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                            UserInterface.TO_PIPS.add(moveTo[index][4]);
                                                            UserInterface.MOVE_COUNT.add(3);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else {
                                            if(!isLegalMove(singleToPip1, currentPlayer, true)) {
                                                if(MOVE_COUNT == 0 || MOVE_COUNT == 2) {
                                                    UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][2] + " " + moveTo[index][2] + " - " + moveTo[index][3]);
                                                    UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                    UserInterface.TO_PIPS.add(moveTo[index][3]);
                                                    UserInterface.MOVE_COUNT.add(2);
                                                }
                                            }
                                        }
                                    }
                                }
                                else { // Not a legal move
                                    UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][2]);
                                    UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                    UserInterface.TO_PIPS.add(moveTo[index][2]);
                                    UserInterface.MOVE_COUNT.add(1);
                                }
                            }
                        }
                    }
                }
            }

            else { // cant bear off
                if(ownerCheckers.contains(25)) { // Moving from bar, can only move from bar until all checkers are off bar
                    if(diceUsed == 0 || diceUsed == 2) {
                        if (isLegalMove(25 - Dice.roll1, currentPlayer, false)) {
                            moveTo[index][1] = 25 - Dice.roll1;
                            UserInterface.LEGAL_MOVES.add(letter++ + ": Bar - " + moveTo[index][1]);
                            UserInterface.FROM_PIPS.add(25);
                            UserInterface.TO_PIPS.add(moveTo[index][1]);
                            UserInterface.MOVE_COUNT.add(1);
                        }
                    }

                    if(diceUsed == 0 || diceUsed == 1) {
                        if (isLegalMove(25 - Dice.roll2, currentPlayer, false)) {
                            moveTo[index][2] = 25 - Dice.roll2;
                            UserInterface.LEGAL_MOVES.add(letter + ": Bar - " + moveTo[index][2]);
                            UserInterface.FROM_PIPS.add(25);
                            UserInterface.TO_PIPS.add(moveTo[index][2]);
                            UserInterface.MOVE_COUNT.add(1);
                        }
                    }

                    if(UserInterface.LEGAL_MOVES.size() == 0) {
                        UserInterface.textArea.appendText("No available moves\n");
                    } else if(UserInterface.LEGAL_MOVES.size() == 1) {
                        UserInterface.textArea.appendText("Only one available move\n");
                        IntStream.range(0, UserInterface.LEGAL_MOVES.size()).forEach(i -> UserInterface.textArea.appendText(UserInterface.LEGAL_MOVES.get(i) + "\n"));
                    } else {
                        UserInterface.textArea.appendText("Available moves: \n");
                        IntStream.range(0, UserInterface.LEGAL_MOVES.size()).forEach(i -> UserInterface.textArea.appendText(UserInterface.LEGAL_MOVES.get(i) + "\n"));
                        UserInterface.textArea.appendText("\n");
                    }
                    return;
                }

                else { // Moving from board
                    if(diceUsed == 0 || diceUsed == 2) {
                        if (isLegalMove(singleToPip1, currentPlayer, false)) { // If single is a legal move
                            moveTo[index][1] = singleToPip1; // Store
                            if (isLegalMove(doubleToPip, currentPlayer, false)) { // If double is a legal move
                                moveTo[index][3] = doubleToPip; // Store
                                if(UserInterface.doubleRolled) {
                                    if (isLegalMove(tripleToPip, currentPlayer, false)) { // legal
                                        moveTo[index][4] = tripleToPip; // store
                                        if (isLegalMove(quadToPip, currentPlayer, false)) { // legal
                                            moveTo[index][5] = quadToPip;
                                            if (UserInterface.doubleRolled && MOVE_COUNT == 0) {
                                                UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][1] + " " + moveTo[index][1] + " - " + moveTo[index][3] + " " + moveTo[index][3] + " - " + moveTo[index][4] + " " + moveTo[index][4] + " - " + moveTo[index][5]);
                                                UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                UserInterface.TO_PIPS.add(moveTo[index][5]);
                                                UserInterface.MOVE_COUNT.add(4);
                                            }
                                        } else { // not legal
                                            if (UserInterface.doubleRolled && (MOVE_COUNT == 1 || MOVE_COUNT == 0)) {
                                                UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][1] + " " + moveTo[index][1] + " - " + moveTo[index][3] + " " + moveTo[index][3] + " - " + moveTo[index][4]);
                                                UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                UserInterface.TO_PIPS.add(moveTo[index][4]);
                                                UserInterface.MOVE_COUNT.add(3);
                                            }
                                        }
                                    } else { // not legal
                                        if (MOVE_COUNT == 0 || MOVE_COUNT == 2) {
                                            UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][1] + " " + moveTo[index][1] + " - " + moveTo[index][3]);
                                            UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                            UserInterface.TO_PIPS.add(moveTo[index][3]);
                                            UserInterface.MOVE_COUNT.add(2);
                                        }
                                    }
                                }
                                else {
                                    if (MOVE_COUNT == 0 || MOVE_COUNT == 2) {
                                        UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][1] + " " + moveTo[index][1] + " - " + moveTo[index][3]);
                                        UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                        UserInterface.TO_PIPS.add(moveTo[index][3]);
                                        UserInterface.MOVE_COUNT.add(2);
                                    }
                                }
                            } else { // Store as single if double is not a legal move
                                UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][1]);
                                UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                UserInterface.TO_PIPS.add(moveTo[index][1]);
                                UserInterface.MOVE_COUNT.add(1);
                            }
                        }
                    }

                    if(diceUsed == 0 || diceUsed == 1) {
                        if (isLegalMove(singleToPip2, currentPlayer, false)) { // If legal
                            moveTo[index][2] = singleToPip2; // Store
                            if (isLegalMove(doubleToPip, currentPlayer, false)) { // legal
                                moveTo[index][3] = doubleToPip; // store
                                if(UserInterface.doubleRolled) {
                                    if (isLegalMove(tripleToPip, currentPlayer, false)) { // legal
                                        moveTo[index][4] = tripleToPip; // store
                                        if (isLegalMove(quadToPip, currentPlayer, false)) { // legal
                                            moveTo[index][5] = quadToPip;
                                            if (!isLegalMove(singleToPip1, currentPlayer, false)) {
                                                if (MOVE_COUNT == 0) {
                                                    UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][2] + " " + moveTo[index][2] + " - " + moveTo[index][3] + " " + moveTo[index][3] + " - " + moveTo[index][4] + " " + moveTo[index][4] + " - " + moveTo[index][5]);
                                                    UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                    UserInterface.TO_PIPS.add(moveTo[index][5]);
                                                    UserInterface.MOVE_COUNT.add(4);
                                                }
                                            }
                                        } else { // not legal
                                            if (!isLegalMove(singleToPip1, currentPlayer, false)) {
                                                if (MOVE_COUNT == 1 || MOVE_COUNT == 0) {
                                                    UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][2] + " " + moveTo[index][2] + " - " + moveTo[index][3] + " " + moveTo[index][3] + " - " + moveTo[index][4]);
                                                    UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                    UserInterface.TO_PIPS.add(moveTo[index][4]);
                                                    UserInterface.MOVE_COUNT.add(3);
                                                }
                                            }
                                        }
                                    } else { // not legal
                                        if (!isLegalMove(singleToPip1, currentPlayer, false)) {
                                            if (MOVE_COUNT == 0 || MOVE_COUNT == 2) {
                                                UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][2] + " " + moveTo[index][2] + " - " + moveTo[index][3]);
                                                UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                                UserInterface.TO_PIPS.add(moveTo[index][3]);
                                                UserInterface.MOVE_COUNT.add(2);
                                            }
                                        }
                                    }
                                }
                                else {
                                    if(!isLegalMove(singleToPip1, currentPlayer, false)) {
                                        if (MOVE_COUNT == 0) {
                                            UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][2] + " " + moveTo[index][2] + " - " + moveTo[index][3]);
                                            UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                            UserInterface.TO_PIPS.add(moveTo[index][3]);
                                            UserInterface.MOVE_COUNT.add(2);
                                        }
                                    }
                                }
                            } else { // not legal
                                UserInterface.LEGAL_MOVES.add(letter++ + ": " + moveTo[index][0] + " - " + moveTo[index][2]);
                                UserInterface.FROM_PIPS.add(moveTo[index][0]);
                                UserInterface.TO_PIPS.add(moveTo[index][2]);
                                UserInterface.MOVE_COUNT.add(1);
                            }
                        }
                    }
                }
            }
        }
        if(UserInterface.LEGAL_MOVES.size() == 0) {
            UserInterface.textArea.appendText("No available moves\n");
        } else if(UserInterface.LEGAL_MOVES.size() == 1) {
            UserInterface.textArea.appendText("Only one available move\n");
            IntStream.range(0, UserInterface.LEGAL_MOVES.size()).forEach(index -> UserInterface.textArea.appendText(UserInterface.LEGAL_MOVES.get(index) + "\n"));
        } else {
            UserInterface.textArea.appendText("Available moves: \n");
            IntStream.range(0, UserInterface.LEGAL_MOVES.size()).forEach(index -> UserInterface.textArea.appendText(UserInterface.LEGAL_MOVES.get(index) + "\n"));
            UserInterface.textArea.appendText("\n");
        }
    }

    static int convertPipToColumn(int pip) {
        int column;

        if(pip == 0 || pip == 25) { // BAR OR BEAR
            column = 0;
        }

        else if(pip > 12) { // TOP HALF OF BOARD
            column = pip-13;
        }

        else { // BOTTOM HALF OF BOARD
            column = Math.abs(pip-12);
        }

        return column;
    }

    static int nextRow(int column, int pip, int currentPlayer) {
        int freeRow = -1;

        if(currentPlayer == 0 || currentPlayer == Player.playerRed.getTurn()) { // IF BOARD IS NOT FLIPPED
            if(pip > 12) { // Top half of board
                if(pip == 25) { // bar
                    for(int rowCheck = 0; rowCheck < 5; rowCheck++) {
                        if(!BoardPanel.BAR[0][rowCheck].isTaken()) {
                            freeRow = rowCheck;
                            break;
                        }
                    }
                } else if(pip < 25){
                    for(int rowCheck = 0; rowCheck < 15; rowCheck++) { // Start at top and go down
                        if(!BoardPanel.BOARD[column][rowCheck].isTaken()) { // If position in column is free then break
                            freeRow = rowCheck;
                            break;
                        }
                        if(rowCheck == 14) {
                            freeRow = 14;
                            break;
                        }
                    }
                }
                return freeRow;
            }

            else { // Bottom half of board
                if(pip == 0) { // bear
                    for(int rowCheck = 15; rowCheck < 30; rowCheck++) {
                        if(!BoardPanel.BEAR[0][rowCheck].isTaken()) {
                            freeRow = rowCheck;
                            break;
                        }
                        if(rowCheck == 29) {
                            freeRow = 29;
                            break;
                        }
                    }
                } else if (pip > 0){
                    for(int rowCheck = 29; rowCheck > 14; rowCheck--) { // Start bottom and go up
                        if(!BoardPanel.BOARD[column][rowCheck].isTaken()) { // If position in column is free then break
                            freeRow = rowCheck;
                            break;
                        }
                        if(rowCheck == 15) {
                            freeRow = 15;
                            break;
                        }
                    }
                }
                return freeRow;
            }
        }

        else { // BLUE PLAYERS TURN
            if(pip > 12) { // Bottom half of the board
                if(pip == 25) { //BAR
                    for(int rowCheck = 5; rowCheck < 10; rowCheck++) {
                        if(!BoardPanel.BAR[0][rowCheck].isTaken()) {
                            freeRow = rowCheck;
                        }
                    }
                } else if(pip < 25) {
                    for(int rowCheck = 29; rowCheck > 14; rowCheck--) { // Start bottom and go up
                        if(!BoardPanel.BOARD[column][rowCheck].isTaken()) { // If position in column is free then break
                            freeRow = rowCheck;
                            break;
                        }
                        if(rowCheck == 15) {
                            freeRow = 15;
                            break;
                        }
                    }
                }
                return freeRow;
            }

            else  { // Top half of the board
                if(pip == 0) { // BEAR
                    for(int rowCheck = 0; rowCheck < 15; rowCheck++) {
                        if(!BoardPanel.BEAR[0][rowCheck].isTaken()) {
                            freeRow = rowCheck;
                            break;
                        }
                        if(rowCheck == 14) {
                            freeRow = 14;
                            break;
                        }
                    }
                } else if(pip > 0) {
                    for(int rowCheck = 0; rowCheck < 15; rowCheck++) { // Start at top and go down
                        if(!BoardPanel.BOARD[column][rowCheck].isTaken()) { // If position in column is free then break
                            freeRow = rowCheck;
                            break;
                        }
                        if(rowCheck == 14) {
                            freeRow = 14;
                            break;
                        }
                    }
                }
                return freeRow;
            }
        }
    }

    static int getWinner() { // Check if last position in Bear Off is taken
        if(BoardPanel.BEAR[0][29].isTaken()) { // If last position for reds Bear off is taken then red wins
            Player.playerRed.updateMatchScore(Player.playerRed.getTurn(), UserInterface.pointsWon);
            // PLAYER RED WINS
            return Player.playerRed.getTurn();
        }

        if(BoardPanel.BEAR[0][14].isTaken()) { // If last position for blues Bear off is taken then blue wins
            Player.playerBlue.updateMatchScore(Player.playerBlue.getTurn(), UserInterface.pointsWon);
            // PLAYER BLUE WINS
            return Player.playerBlue.getTurn();
        }
        return 0;
    }

    private static boolean isLegalMove(int pip, int currentPlayer, boolean canBearOff) {
        int columnOfPip;
        int freeRowInPip;
        char colourOfPosition;
        boolean legalMove = false;

        if(canBearOff) {
            if(pip == 0) {
                return true;
            }
        } else {
            if(pip == 0) {
                return false;
            }
        }

        // First convert pip to column
        columnOfPip = convertPipToColumn(pip);
        // Second, find free row in that pip
        freeRowInPip = nextRow(columnOfPip,pip,currentPlayer);
        if(freeRowInPip == -1) {
            return false;
        }

        // Third, Check if top half or bottom half
        if(freeRowInPip < 15) { // Top half
            if(freeRowInPip == 0) { // If pip is empty
                colourOfPosition = 'E';
            } else { // If not empty, check colour of checker
                // We check the position before the free row to get the colour of the checker
                colourOfPosition = BoardPanel.BOARD[columnOfPip][freeRowInPip - 1].getPlayerColour();
            }
        } else { // Bottom half
            if(freeRowInPip == 29) { // If pip is empty
                colourOfPosition = 'E';
            } else { // If not empty
                // We check the position before the free row to get the colour of the checker
                colourOfPosition = BoardPanel.BOARD[columnOfPip][freeRowInPip + 1].getPlayerColour();
            }
        }

        if(colourOfPosition == 'E') {
            return true;
        } else {
            // Fourth, Check if colour matches current players checker
            if (currentPlayer == Player.playerRed.getTurn()) {
                if (colourOfPosition == 'R') { // Own checker
                    legalMove = true;
                } else if (colourOfPosition == 'B') { // Not own checker
                    legalMove = (freeRowInPip == 28 || freeRowInPip == 1);
                }
            } else if (currentPlayer == Player.playerBlue.getTurn()) {
                if (colourOfPosition == 'B') { // Own checker
                    legalMove = true;
                } else if (colourOfPosition == 'R') {
                    legalMove = (freeRowInPip == 28 || freeRowInPip == 1);
                }
            }
            return legalMove;
        }
    }

    private static boolean isBearOffMove(int pip) {
        return (pip <= 0);
    }

    private static boolean canBearOff(ArrayList<Integer> ownCheckersArrayList, int currentPlayer) {
        if (currentPlayer == Player.playerRed.getTurn()) { // If red players turn
            ownCheckersArrayList = findOwnCheckers(Player.playerRed.getTurn());
            for (int pip : ownCheckersArrayList) {
                if(pip > 6) {
                    return false;
                }
            }
            return true;
        }

        else {
            ownCheckersArrayList = findOwnCheckers(Player.playerBlue.getTurn());
            for (int pip : ownCheckersArrayList) {
                if(pip > 6) {
                    return false;
                }
            }
            return true;
        }
    }
}
