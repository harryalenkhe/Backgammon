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
                    pip = 0;
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
                    pip = 0;
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

        for (Object eachObject : ownCheckersSet) { // Transferred to arrayList as it does not maintain the order elements are stored
            ownCheckersArrayList.add((int) eachObject);
            System.out.println(eachObject);
        }
        System.out.println("------------");

        return ownCheckersArrayList;
    }

    static int[][] findMoveTo(ArrayList<Integer> ownerCheckers) {
        int[][] moveTo = new int[ownerCheckers.size()][4];
        for (int index = 0; index < ownerCheckers.size(); index++) {

            int startPip = ownerCheckers.get(index);
            int singleToPip1; // Dice roll 1 move
            int singleToPip2; // Dice roll 2 move
            int doubleToPip; // Both dice rolls combined move

            if(startPip == 0) { // If starting on the bar
                singleToPip1 = 25 - (Dice.roll1+1);
                singleToPip2 = 25 - (Dice.roll2+1);
                doubleToPip = 25 - (Dice.roll1 + Dice.roll2 + 2);
            } else { // If starting on the board
                singleToPip1 = startPip - (Dice.roll1+1);
                singleToPip2 = startPip - (Dice.roll2+1);
                doubleToPip = startPip - (Dice.roll1 + Dice.roll2 + 2);
            }

            moveTo[index][0] = startPip; // store starting pip

            // Store any moves with pips less than 0 as 0
            if(singleToPip1 <= 0) moveTo[index][1] = 0;
            else moveTo[index][1] = singleToPip1;

            if(singleToPip2 <= 0) moveTo[index][2] = 0;
            else moveTo[index][2] = singleToPip2;

            if(doubleToPip <= 0) moveTo[index][3] = 0;
            else moveTo[index][3] = doubleToPip;
        }
        return moveTo;
    }

    // Convert pip to a column integer which is used as X index in array
    static int convertPipToColumn(int pip) {
        int column = 0;

        if(pip>12) { // TOP HALF OF BOARD
            column = pip-13;
        }

        if(pip <= 12) { // BOTTOM HALF OF BOARD
            column = Math.abs(pip-12);
        }

        if(pip == 0 || pip == 25) { // BAR OR BEAR
            column = 0;
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

            if(pip <= 12) { // Bottom half of board
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

            if(pip == 0) {
                for(int rowCheck = 0; rowCheck < 10; rowCheck++) {
                    if(!BoardPanel.BAR[0][rowCheck].isTaken()) {
                        freeRow = rowCheck;
                        break;
                    }
                }
            }

            if(pip == 25) {
                for(int rowCheck = 15; rowCheck < 30; rowCheck++) {
                    if(!BoardPanel.BEAR[0][rowCheck].isTaken()) {
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

            if(pip == 0) {
                for(int rowCheck = 0; rowCheck < 10; rowCheck++) {
                    if(!BoardPanel.BAR[0][rowCheck].isTaken()) {
                        freeRow = rowCheck;
                        break;
                    }
                }
            }

            if(pip == 25) {
                for(int rowCheck = 0; rowCheck < 15; rowCheck++) {
                    if(!BoardPanel.BEAR[0][rowCheck].isTaken()) {
                        freeRow = rowCheck;
                        break;
                    }
                }
            }
        }
        return freeRow; // Return position in column that is free
    }

    static int topCheckerInPip(int column, int pip, int currentPlayer) {
        int top = 0;

        if(currentPlayer == Player.playerRed.getTurn()) {
            if(pip >= 13) { // Top half of board
                for(int nextRow = 0; nextRow <= 6; nextRow++) { // Max 6 checkers in each column // Start at top and go down
                    if(!BoardPanel.BOARD[column][nextRow].isTaken()) { // If position in column is free then break
                        top = nextRow - 1; // Decrement by 1 to get top checker, as counting down from the top
                        break;
                    }
                    if(nextRow == 5) { // If 6 checkers in a column,
                        top = 5;
                        break;
                    }
                }
            }

            if(pip <= 12) { // Bottom half of board
                for(int nextRow = 11; nextRow >= 6; nextRow--) { // Max 6 checkers in each column // Start bottom and go up
                    if(!BoardPanel.BOARD[column][nextRow].isTaken()) { // If position in column is free then break and return checker before it
                        top = nextRow + 1; // Increment by 1 to get top checker, as counting up from the bottom
                        break;
                    }
                    if(nextRow == 6) { // If 6 checkers in a column, return top checker
                        top = 6;
                        break;
                    }
                }
            }

            if(pip == 0) {
                for(int nextRow = 0; nextRow < 5; nextRow++) {
                    if(!BoardPanel.BAR[0][nextRow].isTaken()) {
                        top = nextRow - 1;
                        break;
                    }
                }
            }
        }

        else if(currentPlayer == Player.playerBlue.getTurn()) {
            if(pip >= 13) { // Bottom half of board
                for(int nextRow = 11; nextRow >= 6; nextRow--) { // Max 6 checkers in each column // Start bottom and go up
                    if(!BoardPanel.BOARD[column][nextRow].isTaken()) { // If position in column is free then break and return checker before it
                        top = nextRow + 1; // Increment by 1 to get top checker, as counting up from the bottom
                        break;
                    }
                    if(nextRow == 6) { // If 6 checkers in a column, return top checker
                        top = 6;
                        break;
                    }
                }
            }

            if(pip <= 12) { // Top half of board
                for(int nextRow = 0; nextRow <= 6; nextRow++) { // Max 6 checkers in each column // Start at top and go down
                    if(!BoardPanel.BOARD[column][nextRow].isTaken()) { // If position in column is free then break
                        top = nextRow - 1; // Decrement by 1 to get top checker, as counting down from the top
                        break;
                    }
                    if(nextRow == 5) { // If 6 checkers in a column,
                        top = 5;
                        break;
                    }
                }
            }

            if(pip == 0) {
                for(int nextRow = 5; nextRow < 10; nextRow++) {
                    if(!BoardPanel.BAR[0][nextRow].isTaken()) {
                        top = nextRow - 1;
                        break;
                    }
                }
            }
        }
        return top; // Return position in column that is free
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
}
