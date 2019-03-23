import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

    static int[][] findMoveTo(ArrayList<Integer> ownerCheckers, int currentPlayer) {
        int[][] moveTo = new int[ownerCheckers.size()][6];
        for (int index = 0; index < ownerCheckers.size(); index++) {

            // First assign variables to pips moved to from dice rolls
            int startPip = ownerCheckers.get(index);
            int singleToPip1 = startPip - (Dice.roll1);
            int singleToPip2 = startPip - (Dice.roll2);
            int doubleToPip = startPip - (Dice.roll1 + Dice.roll2);
            int tripleToPip;
            int quadToPip;
            if(UserInterface.doubleRolled) {
                tripleToPip = startPip - (Dice.roll1*3);
                quadToPip = startPip - (Dice.roll1*4);
            } else {
                tripleToPip = -1;
                quadToPip = -1;
            }

            moveTo[index][0] = startPip; // store starting pip

            if (canBearOff()) {
                if (singleToPip1 <= 0) { // SINGLE bear off move
                    singleToPip1 = doubleToPip = tripleToPip = quadToPip = 0;
                    moveTo[index][1] = singleToPip1;
                    moveTo[index][3] = doubleToPip;
                    moveTo[index][4] = tripleToPip;
                    moveTo[index][5] = quadToPip;
                } else {
                    if (isLegalMove(singleToPip1, currentPlayer)) { // If legal
                        moveTo[index][1] = singleToPip1; // Store
                        if (doubleToPip <= 0) {
                            doubleToPip = tripleToPip = quadToPip = 0;
                            moveTo[index][3] = doubleToPip;
                            moveTo[index][4] = tripleToPip;
                            moveTo[index][5] = quadToPip;
                        } else {
                            if (isLegalMove(doubleToPip, currentPlayer)) {
                                moveTo[index][3] = doubleToPip;
                                if (tripleToPip <= 0) {
                                    tripleToPip = quadToPip = 0;
                                    moveTo[index][4] = tripleToPip;
                                    moveTo[index][5] = quadToPip;
                                } else {
                                    if (isLegalMove(tripleToPip, currentPlayer)) {
                                        moveTo[index][4] = tripleToPip;
                                        if (quadToPip <= 0) {
                                            quadToPip = 0;
                                            moveTo[index][5] = quadToPip;
                                        } else {
                                            if (isLegalMove(quadToPip, currentPlayer)) {
                                                moveTo[index][5] = quadToPip;
                                            } else {
                                                quadToPip = -1;
                                                moveTo[index][5] = quadToPip;
                                            }
                                        }
                                    } else {
                                        tripleToPip = quadToPip = -1;
                                        moveTo[index][4] = tripleToPip;
                                        moveTo[index][5] = quadToPip;
                                    }
                                }
                            } else {
                                doubleToPip = tripleToPip = quadToPip = -1;
                                moveTo[index][3] = doubleToPip;
                                moveTo[index][4] = tripleToPip;
                                moveTo[index][5] = quadToPip;
                            }
                        }
                    } else {
                        singleToPip1 = doubleToPip = tripleToPip = quadToPip = -1;
                        moveTo[index][1] = singleToPip1;
                        moveTo[index][3] = doubleToPip;
                        moveTo[index][4] = tripleToPip;
                        moveTo[index][5] = quadToPip;
                    }
                }


                if (singleToPip2 <= 0) { // SINGLE bear off move
                    singleToPip2 = doubleToPip = tripleToPip = quadToPip = 0;
                    moveTo[index][2] = singleToPip2;
                    moveTo[index][3] = doubleToPip;
                    moveTo[index][4] = tripleToPip;
                    moveTo[index][5] = quadToPip;
                } else {
                    if (isLegalMove(singleToPip2, currentPlayer)) { // If legal
                        moveTo[index][2] = singleToPip2; // Store
                        if (doubleToPip <= 0) {
                            doubleToPip = tripleToPip = quadToPip = 0;
                            moveTo[index][3] = doubleToPip;
                            moveTo[index][4] = tripleToPip;
                            moveTo[index][5] = quadToPip;
                        } else {
                            if (isLegalMove(doubleToPip, currentPlayer)) {
                                moveTo[index][3] = doubleToPip;
                                if (tripleToPip <= 0) {
                                    tripleToPip = quadToPip = 0;
                                    moveTo[index][4] = tripleToPip;
                                    moveTo[index][5] = quadToPip;
                                } else {
                                    if (isLegalMove(tripleToPip, currentPlayer)) {
                                        moveTo[index][4] = tripleToPip;
                                        if (quadToPip <= 0) {
                                            quadToPip = 0;
                                            moveTo[index][5] = quadToPip;
                                        } else {
                                            if (isLegalMove(quadToPip, currentPlayer)) {
                                                moveTo[index][5] = quadToPip;
                                            } else {
                                                quadToPip = -1;
                                                moveTo[index][5] = quadToPip;
                                            }
                                        }
                                    } else {
                                        tripleToPip = quadToPip = -1;
                                        moveTo[index][4] = tripleToPip;
                                        moveTo[index][5] = quadToPip;
                                    }
                                }
                            } else {
                                doubleToPip = tripleToPip = quadToPip = -1;
                                moveTo[index][3] = doubleToPip;
                                moveTo[index][4] = tripleToPip;
                                moveTo[index][5] = quadToPip;
                            }
                        }
                    } else {
                        singleToPip2 = doubleToPip = tripleToPip = quadToPip = -1;
                        moveTo[index][2] = singleToPip2;
                        moveTo[index][3] = doubleToPip;
                        moveTo[index][4] = tripleToPip;
                        moveTo[index][5] = quadToPip;
                    }
                }
            }

            else { //CANT BEAR OFF
                if(startPip != 25) { // Moving from board
                    if (singleToPip1 <= 0) { // bear off move
                        singleToPip1 = doubleToPip = tripleToPip = quadToPip = -1;
                        moveTo[index][1] = singleToPip1;
                        moveTo[index][3] = doubleToPip;
                        moveTo[index][4] = tripleToPip;
                        moveTo[index][5] = quadToPip;
                    } else { // Non bear off move
                        if (isLegalMove(singleToPip1, currentPlayer)) { // If legal
                            moveTo[index][1] = singleToPip1; // Store
                            if (doubleToPip <= 0) { // If bear off
                                doubleToPip = tripleToPip = quadToPip = -1; // illegal
                                moveTo[index][3] = doubleToPip;
                                moveTo[index][4] = tripleToPip;
                                moveTo[index][5] = quadToPip;
                            } else { // non bear off
                                if (isLegalMove(doubleToPip, currentPlayer)) { // legal
                                    moveTo[index][3] = doubleToPip; // store
                                    if (tripleToPip <= 0) { // if bear off
                                        tripleToPip = quadToPip = -1; // illegal
                                        moveTo[index][4] = tripleToPip;
                                        moveTo[index][5] = quadToPip;
                                    } else { // non bear off
                                        if (isLegalMove(tripleToPip, currentPlayer)) { // legal
                                            moveTo[index][4] = tripleToPip; // store
                                            if (quadToPip <= 0) { // if bear off
                                                quadToPip = -1;
                                                moveTo[index][5] = quadToPip;
                                            } else { // not bear off
                                                if (isLegalMove(quadToPip, currentPlayer)) { // legal
                                                    moveTo[index][5] = quadToPip;
                                                } else { // not legal
                                                    quadToPip = -1;
                                                    moveTo[index][5] = quadToPip;
                                                }
                                            }
                                        } else { // not legal
                                            tripleToPip = quadToPip = -1;
                                            moveTo[index][4] = tripleToPip;
                                            moveTo[index][5] = quadToPip;
                                        }
                                    }
                                } else { // not legal
                                    doubleToPip = tripleToPip = quadToPip = -1;
                                    moveTo[index][3] = doubleToPip;
                                    moveTo[index][4] = tripleToPip;
                                    moveTo[index][5] = quadToPip;
                                }
                            }
                        } else { // not legal
                            singleToPip1 = doubleToPip = tripleToPip = quadToPip = -1;
                            moveTo[index][1] = singleToPip1;
                            moveTo[index][3] = doubleToPip;
                            moveTo[index][4] = tripleToPip;
                            moveTo[index][5] = quadToPip;
                        }
                    }

                    if (singleToPip2 <= 0) { // bear off move
                        singleToPip2 = doubleToPip = tripleToPip = quadToPip = -1;
                        moveTo[index][2] = singleToPip2;
                        moveTo[index][3] = doubleToPip;
                        moveTo[index][4] = tripleToPip;
                        moveTo[index][5] = quadToPip;
                    } else { // Non bear off move
                        if (isLegalMove(singleToPip2, currentPlayer)) { // If legal
                            moveTo[index][2] = singleToPip2; // Store
                            if (doubleToPip <= 0) { // If bear off
                                doubleToPip = tripleToPip = quadToPip = -1; // illegal
                                moveTo[index][3] = doubleToPip;
                                moveTo[index][4] = tripleToPip;
                                moveTo[index][5] = quadToPip;
                            } else { // non bear off
                                if (isLegalMove(doubleToPip, currentPlayer)) { // legal
                                    moveTo[index][3] = doubleToPip; // store
                                    if (tripleToPip <= 0) { // if bear off
                                        tripleToPip = quadToPip = -1; // illegal
                                        moveTo[index][4] = tripleToPip;
                                        moveTo[index][5] = quadToPip;
                                    } else { // non bear off
                                        if (isLegalMove(tripleToPip, currentPlayer)) { // legal
                                            moveTo[index][4] = tripleToPip; // store
                                            if (quadToPip <= 0) { // if bear off
                                                quadToPip = -1;
                                                moveTo[index][5] = quadToPip;
                                            } else { // not bear off
                                                if (isLegalMove(quadToPip, currentPlayer)) { // legal
                                                    moveTo[index][5] = quadToPip;
                                                } else { // not legal
                                                    quadToPip = -1;
                                                    moveTo[index][5] = quadToPip;
                                                }
                                            }
                                        } else { // not legal
                                            tripleToPip = quadToPip = -1;
                                            moveTo[index][4] = tripleToPip;
                                            moveTo[index][5] = quadToPip;
                                        }
                                    }
                                } else { // not legal
                                    doubleToPip = tripleToPip = quadToPip = -1;
                                    moveTo[index][3] = doubleToPip;
                                    moveTo[index][4] = tripleToPip;
                                    moveTo[index][5] = quadToPip;
                                }
                            }
                        } else { // not legal
                            singleToPip2 = doubleToPip = tripleToPip = quadToPip = -1;
                            moveTo[index][2] = singleToPip2;
                            moveTo[index][3] = doubleToPip;
                            moveTo[index][4] = tripleToPip;
                            moveTo[index][5] = quadToPip;
                        }
                    }
                }
                
                else { // Moving from bar, can only move from bar until all checkers are off bar
                    if(isLegalMove(singleToPip1, currentPlayer)) {
                        moveTo[index][1] = singleToPip1;
                        doubleToPip = tripleToPip = quadToPip = -1;
                        moveTo[index][3] = doubleToPip;
                        moveTo[index][4] = tripleToPip;
                        moveTo[index][5] = quadToPip;
                    } else {
                        singleToPip1 = doubleToPip = tripleToPip = quadToPip = -1;
                        moveTo[index][1] = singleToPip1;
                        moveTo[index][3] = doubleToPip;
                        moveTo[index][4] = tripleToPip;
                        moveTo[index][5] = quadToPip;
                    }

                    if(isLegalMove(singleToPip2, currentPlayer)) {
                        moveTo[index][2] = singleToPip2;
                        doubleToPip = tripleToPip = quadToPip = -1;
                        moveTo[index][3] = doubleToPip;
                        moveTo[index][4] = tripleToPip;
                        moveTo[index][5] = quadToPip;
                    } else {
                        singleToPip2 = doubleToPip = tripleToPip = quadToPip = -1;
                        moveTo[index][2] = singleToPip2;
                        moveTo[index][3] = doubleToPip;
                        moveTo[index][4] = tripleToPip;
                        moveTo[index][5] = quadToPip;
                    }
                }

            }
        }
        return moveTo;
    }

    // Convert pip to a column integer which is used as X index in array
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

    // Look for available row in pip to place checker, checks who's turn it is
    static int nextRow(int column, int pip, int currentPlayer) {
        int freeRow = -1;

        if(currentPlayer == 0 || currentPlayer == Player.playerRed.getTurn()) { // IF BOARD IS NOT FLIPPED
            if(pip > 12) { // Top half of board
                for(int rowCheck = 0; rowCheck < 6; rowCheck++) { // Max 6 checkers in each column // Start at top and go down
                    if(!BoardPanel.BOARD[column][rowCheck].isTaken()) { // If position in column is free then break
                        freeRow = rowCheck;
                        break;
                    }

                    if(rowCheck == 5) { // Pip is full with 6 checkers
                        break;
                    }
                }
            }

            if(pip <= 12){ // Bottom half of board
                for(int rowCheck = 11; rowCheck > 5; rowCheck--) { // Max 6 checkers in each column // Start bottom and go up
                    if(!BoardPanel.BOARD[column][rowCheck].isTaken()) { // If position in column is free then break
                        freeRow = rowCheck;
                        break;
                    }

                    if(rowCheck == 6) { // Pip is full with 6 checkers
                        break;
                    }
                }
            }

            if(pip == 0) { // bear
                for(int rowCheck = 15; rowCheck < 30; rowCheck++) {
                    if(!BoardPanel.BEAR[0][rowCheck].isTaken()) {
                        freeRow = rowCheck;
                        break;
                    }
                }
            }

            if(pip == 25) { // bar
                for(int rowCheck = 0; rowCheck < 10; rowCheck++) {
                    if(!BoardPanel.BAR[0][rowCheck].isTaken()) {
                        freeRow = rowCheck;
                        break;
                    }
                }
            }
        }

        else if(currentPlayer == Player.playerBlue.getTurn()) { // IF BOARD IS FLIPPED
            if(pip > 12) { // Bottom half of the board
                for(int rowCheck = 11; rowCheck > 5; rowCheck--) { // Max 6 checkers in each column // Start bottom and go up
                    if(!BoardPanel.BOARD[column][rowCheck].isTaken()) { // If position in column is free then break
                        freeRow = rowCheck;
                        break;
                    }

                    if(rowCheck == 6) { // Pip is full with 6 checkers
                        break;
                    }
                }
            }

            if(pip <= 12) { // Top half of the board
                for(int rowCheck = 0; rowCheck < 6; rowCheck++) { // Max 6 checkers in each column // Start at top and go down
                    if(!BoardPanel.BOARD[column][rowCheck].isTaken()) { // If position in column is free then break
                        freeRow = rowCheck;
                        break;
                    }

                    if(rowCheck == 5) { // Pip is full with 6 checkers
                        break;
                    }
                }
            }

            if(pip == 0) { // BEAR
                for(int rowCheck = 0; rowCheck < 15; rowCheck++) {
                    if(!BoardPanel.BEAR[0][rowCheck].isTaken()) {
                        freeRow = rowCheck;
                        break;
                    }
                }
            }

            else if(pip == 25) { //BAR
                for(int rowCheck = 0; rowCheck < 10; rowCheck++) {
                    if(!BoardPanel.BAR[0][rowCheck].isTaken()) {
                        freeRow = rowCheck;
                        break;
                    }
                }
            }
        }
        return freeRow; // Return position in column that is free
    }

    static int getWinner() { // Check if last position in Bear Off is taken
        if(BoardPanel.BEAR[0][29].isTaken()) { // If last position for reds Bear off is taken then red wins
            // PLAYER RED WINS
            return Player.playerRed.getTurn();
        }

        if(BoardPanel.BEAR[0][14].isTaken()) { // If last position for blues Bear off is taken then blue wins
            // PLAYER BLUE WINS
            return Player.playerBlue.getTurn();
        }
        return 0;
    }

    private static boolean isLegalMove(int pip, int currentPlayer) {
        int columnOfPip;
        int freeRowInPip;
        char colourOfPosition;
        boolean legalMove = false;

        // First convert pip to column
        columnOfPip = convertPipToColumn(pip);
        // Second, find free row in that pip
        freeRowInPip = nextRow(columnOfPip,pip,currentPlayer);

        // Third, Check if top half or bottom half
        if(freeRowInPip < 6) { // Top half
            if(freeRowInPip == 0) { // If pip is empty
                colourOfPosition = 'E';
            } else { // If not empty, check colour of checker
                // We check the position before the free row to get the colour of the checker
                colourOfPosition = BoardPanel.BOARD[columnOfPip][freeRowInPip - 1].getPlayerColour();
            }
        } else { // Bottom half
            if(freeRowInPip == 11) { // If pip is empty
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
                    legalMove = (freeRowInPip == 10 || freeRowInPip == 1);
                    if(legalMove) { // If its a hit

                    }
                }
            } else if (currentPlayer == Player.playerBlue.getTurn()) {
                if (colourOfPosition == 'B') { // Own checker
                    legalMove = true;
                } else if (colourOfPosition == 'R') {
                    legalMove = (freeRowInPip == 10 || freeRowInPip == 1);
                    if(legalMove) { // If its a hit

                    }
                }
            }
            return legalMove;
        }
    }

    static boolean canBearOff() {
        return false;
    }
}
