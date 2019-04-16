import java.util.ArrayList;

public class CHM_BOT implements BotAPI {

    // The public API of Bot must not change
    // This is ONLY class that you can edit in the program
    // Rename Bot to the name of your team. Use camel case.
    // Bot may not alter the state of the game objects
    // It may only inspect the state of the board and the player objects

    private PlayerAPI me, opponent;
    private BoardAPI board;
    private CubeAPI cube;
    private MatchAPI match;
    private InfoPanelAPI info;

    CHM_BOT(PlayerAPI me, PlayerAPI opponent, BoardAPI board, CubeAPI cube, MatchAPI match, InfoPanelAPI info) {
        this.me = me;
        this.opponent = opponent;
        this.board = board;
        this.cube = cube;
        this.match = match;
        this.info = info;
    }

    public String getName() {
        return "CHM_BOT"; // must match the class name
    }

    public String getCommand(Plays possiblePlays) {
        ArrayList<Integer> winPercentForMoves = getWinPercentForMoves(possiblePlays);
        int bestMove = 0;
        for(int index = 0; index < possiblePlays.plays.size(); index++) {
            if(winPercentForMoves.get(index) > bestMove) {
                bestMove = winPercentForMoves.get(index);
            }
        }
        return Integer.toString(winPercentForMoves.indexOf(bestMove)+1);
    }

    public String getDoubleDecision() {
        if(me.getScore() > opponent.getScore()) {
            return "y";
        } else {
            return "n";
        }
    }

    private ArrayList<Integer> getWinPercentForMoves(Plays possiblePlays) {
        ArrayList<Integer> winPercentage = new ArrayList<>();
        for (Play move : possiblePlays) {
            int moveScore = 0;

            if(move.numberOfMoves() > 2) {
                moveScore += move.getMove(0).getPipDifference() * 4;
            }

            else if(move.numberOfMoves() == 2) {
                if(move.getMove(0).getFromPip() == 25 || move.getMove(1).getFromPip() == 25) { // Moving from Bar
                    if(move.getMove(0).isHit() && move.getMove(1).isHit()) {
                        moveScore += 2; // Bar - Board ( Double Hit )
                    }
                    else if(move.getMove(0).isHit() || move.getMove(1).isHit()) {
                        moveScore += 1; // Bar - Board ( Single Hit + Blot)
                    }
                    else if(move.getMove(0).getToPip() == move.getMove(1).getToPip()) {
                        moveScore += 4; // Bar - Board ( Block )
                    }
                    else if(isBlotToBlock(move.getMove(0).getToPip()) || isBlotToBlock(move.getMove(1).getToPip())) {
                        if(isBlotToBlock(move.getMove(0).getToPip()) && isBlotToBlock(move.getMove(1).getToPip())) {
                            moveScore += 5; // Bar - Board ( Double Blot to Block )
                        } else {
                            moveScore += 2; // Bar - Board ( Single Blot to Block )
                        }
                    }
                }

                else { // Moving from board
                    if(move.getMove(0).getToPip() == 0 || move.getMove(1).getToPip() == 0) { // Bear - off

                        if(move.getMove(0).getToPip() == 0 && move.getMove(1).getToPip() == 0) {
                            moveScore += 10; // Board - Bear off ( Two bear offs )
                            if(isBlockToBlot(move.getMove(0).getFromPip())) {
                                moveScore = 0;
                            }

                            if(isBlockToBlot(move.getMove(1).getFromPip())) {
                                moveScore = 0;
                            }
                        }
                        else {
                            if(move.getMove(0).isHit() || move.getMove(1).isHit()) {
                                moveScore += 3; // Board - Bear off ( Single Bear off + Hit)
                            }
                            else if(isBlotToBlock(move.getMove(0).getToPip()) || isBlotToBlock(move.getMove(1).getToPip())) {
                                moveScore += 5; // Board - Bear off ( Single Bear off + Blot to Block )
                            }
                        }

                    } else { // Non Bear - off
                        if(move.getMove(0).isHit() && move.getMove(1).isHit()) {
                            if(isAhead()) {
                                moveScore += 0;
                            } else {
                                moveScore += 1;
                            }
                        }
                        else if(move.getMove(0).isHit() || move.getMove(1).isHit()) {
                            if(move.getMove(0).getToPip() == move.getMove(1).getToPip()) {
                                moveScore += 5; // Board - Board ( Hit to a Block )
                            } else {
                                if(isAhead()) {
                                    moveScore += 0;
                                } else {
                                    moveScore += 1;
                                }
                            }
                        }
                        else if(move.getMove(0).getToPip() == move.getMove(1).getToPip()) {
                            moveScore += 3; // Board - Board ( Block )
                        }
                        else if(isBlotToBlock(move.getMove(0).getToPip()) || isBlotToBlock(move.getMove(1).getToPip())) {
                            if(isBlotToBlock(move.getMove(0).getToPip()) && isBlotToBlock(move.getMove(1).getToPip())) {
                                moveScore += 4; // Board - Board ( Double Blot to Block )
                            } else {
                                moveScore += 3; // Board - Board ( Single Blot to Block )
                            }
                        }
                    }
                }

                if(move.getMove(0).getFromPip() > 18) {
                    moveScore += 3;
                } else if(move.getMove(0).getFromPip() > 12) {
                    moveScore += 2;
                } else if(move.getMove(0).getFromPip() > 6) {
                    moveScore += 1;
                }

                if(move.getMove(1).getFromPip() > 18) {
                    moveScore += 3;
                } else if(move.getMove(1).getFromPip() > 12) {
                    moveScore += 2;
                } else if(move.getMove(1).getFromPip() > 6) {
                    moveScore += 1;
                }

                if(isPrimePip(move.getMove(0).getToPip()) || isPrimePip(move.getMove(1).getToPip())) {
                    if(isAhead()) {
                        moveScore += 2;
                    }
                }

                if(isBlockToBlot(move.getMove(0).getToPip()) || isBlockToBlot(move.getMove(1).getToPip())) {
                    moveScore = 0;
                }

                if(inHomeBoard(move.getMove(0).getToPip()) || inHomeBoard(move.getMove(1).getToPip())) {
                    if(move.getMove(0).isHit() || move.getMove(1).isHit()) {
                        moveScore = 0;
                    }
                }
            }

            else { // Forced play
                moveScore += move.getMove(0).getPipDifference();
            }
            winPercentage.add(moveScore);
        }
        return winPercentage;
    }

    private boolean isBlotToBlock(int pip) {
        return board.getNumCheckers(me.getId(), pip) == 1;
    }

    private int getTotalPipCount(int ID) {
        int totalPipCount = 0;
        for(int pip = 25; pip >= 0; pip--) {
            totalPipCount += pip * board.getNumCheckers(ID, pip);
        }
        return totalPipCount;
    }

    private boolean isBlockToBlot(int pip) {
        return board.getNumCheckers(me.getId(), pip) == 2;
    }

    private boolean isPrimePip(int pip) {
        return (pip >= 5 && pip <= 10);
    }

    private boolean isPrime() {
        for(int pip = 5; pip <= 10; pip++) {
            if(board.getNumCheckers(me.getId(), pip) == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isAhead() {
        return getTotalPipCount(me.getId())-50 < getTotalPipCount(opponent.getId());
    }

    private boolean inHomeBoard(int pip) {
        return pip <= 6 && pip >= 1;
    }
}
