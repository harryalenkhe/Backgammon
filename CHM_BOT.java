import java.util.ArrayList;

public class CHM_BOT implements BotAPI {

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
        return "CHM_BOT";
    }

    public String getCommand(Plays possiblePlays) {
        ArrayList<Integer> moveScoresArrayList = primingGame(possiblePlays);
        int bestMove = -10;
        for (Integer moveScore : moveScoresArrayList) {
            if (moveScore > bestMove) bestMove = moveScore;
        }
        return Integer.toString(moveScoresArrayList.indexOf(bestMove) + 1);
    }

    public String getDoubleDecision() {
        if (!isVeryAhead()) {
            return "n";
        } else {
            if (isAhead()) {
                return "y";
            } else {
                if (board.getNumCheckers(opponent.getId(), 0) > 0 && board.getNumCheckers(me.getId(), 0) == 0) {
                    return "n";
                } else {
                    return "y";
                }
            }
        }
    }

    private ArrayList<Integer> primingGame(Plays possiblePlays) {
        ArrayList<Integer> winPercentage = new ArrayList<>();

        for (Play move : possiblePlays) {
            int moveScore = 0;

            if (move.numberOfMoves() > 2) {
                Move firstMove = move.getMove(0);
                Move secondMove = move.getMove(1);
                Move thirdMove = move.getMove(2);
                Move fourthMove = move.getMove(3);
                moveScore += move.getMove(0).getPipDifference() * 4;
                if (firstMove.getFromPip() == 25 || secondMove.getFromPip() == 25 || thirdMove.getFromPip() == 25 || fourthMove.getFromPip() == 25) { // Moving from Bar
                    if (firstMove.isHit()) {
                        moveScore += 2; // Bar - Board ( Double Hit )
                    }

                    if (secondMove.isHit()) {
                        moveScore += 2;
                    }

                    if (thirdMove.isHit()) {
                        moveScore += 2;
                    }

                    if (fourthMove.isHit()) {
                        moveScore += 2;
                    }

                    if (isBlotToBlock(firstMove.getToPip())) {
                        moveScore += 3;
                    }

                    if (isBlotToBlock(secondMove.getToPip())) {
                        moveScore += 3;
                    }

                    if (isBlotToBlock(thirdMove.getToPip())) {
                        moveScore += 3;
                    }

                    if (isBlotToBlock(fourthMove.getToPip())) {
                        moveScore += 3;
                    }
                } else { // Moving from board
                    if (firstMove.getToPip() == 0 || secondMove.getToPip() == 0 || thirdMove.getToPip() == 0 || fourthMove.getToPip() == 0) { // Bear - off
                        if (firstMove.getToPip() == 0) {
                            moveScore += 5;
                        }

                        if (secondMove.getToPip() == 0) {
                            moveScore += 5;
                        }

                        if (thirdMove.getToPip() == 0) {
                            moveScore += 5;
                        }

                        if (fourthMove.getToPip() == 0) {
                            moveScore += 5;
                        }

                        if (isOppOnBar()) {
                            if (isBlockToBlot(firstMove.getFromPip())) {
                                moveScore -= 3;
                            }

                            if (isBlockToBlot(secondMove.getFromPip())) {
                                moveScore -= 3;
                            }

                            if (isBlockToBlot(thirdMove.getFromPip())) {
                                moveScore -= 3;
                            }

                            if (isBlockToBlot(fourthMove.getFromPip())) {
                                moveScore -= 3;
                            }

                            if (isBlotToBlock(firstMove.getToPip())) {
                                moveScore += 3;
                            }

                            if (isBlotToBlock(secondMove.getToPip())) {
                                moveScore += 3;
                            }

                            if (isBlotToBlock(thirdMove.getToPip())) {
                                moveScore += 3;
                            }

                            if (isBlotToBlock(fourthMove.getToPip())) {
                                moveScore += 3;
                            }
                        }

                    } else { // Non Bear - off
                        if (firstMove.isHit()) {
                            moveScore += 2; // Bar - Board ( Double Hit )
                        }

                        if (secondMove.isHit()) {
                            moveScore += 2;
                        }

                        if (thirdMove.isHit()) {
                            moveScore += 2;
                        }

                        if (fourthMove.isHit()) {
                            moveScore += 2;
                        }

                        if (isBlotToBlock(firstMove.getToPip())) {
                            moveScore += 3;
                        }

                        if (isBlotToBlock(secondMove.getToPip())) {
                            moveScore += 3;
                        }

                        if (isBlotToBlock(thirdMove.getToPip())) {
                            moveScore += 3;
                        }

                        if (isBlotToBlock(fourthMove.getToPip())) {
                            moveScore += 3;
                        }
                    }
                }

                if (firstMove.getFromPip() > 18) {
                    moveScore += 2;
                } else if (firstMove.getFromPip() > 12) {
                    moveScore += 1;
                }

                if (secondMove.getFromPip() > 18) {
                    moveScore += 2;
                } else if (secondMove.getFromPip() > 12) {
                    moveScore += 1;
                }

                if (thirdMove.getFromPip() > 18) {
                    moveScore += 2;
                } else if (thirdMove.getFromPip() > 12) {
                    moveScore += 1;
                }

                if (fourthMove.getFromPip() > 18) {
                    moveScore += 2;
                } else if (fourthMove.getFromPip() > 12) {
                    moveScore += 1;
                }

                if (isPrimePip(firstMove.getToPip())) {
                    moveScore += 2;
                }

                if (isPrimePip(secondMove.getToPip())) {
                    moveScore += 2;
                }

                if (isPrimePip(thirdMove.getToPip())) {
                    moveScore += 2;
                }

                if (isPrimePip(fourthMove.getToPip())) {
                    moveScore += 2;
                }


                if (oppCheckerToBeHit(firstMove.getFromPip())) {
                    if (firstMove.isHit()) {
                        moveScore += 3;
                    }
                }

                if (oppCheckerToBeHit(secondMove.getFromPip())) {
                    if (secondMove.isHit()) {
                        moveScore += 3;
                    }
                }

                if (oppCheckerToBeHit(thirdMove.getFromPip())) {
                    if (thirdMove.isHit()) {
                        moveScore += 3;
                    }
                }

                if (oppCheckerToBeHit(fourthMove.getFromPip())) {
                    if (fourthMove.isHit()) {
                        moveScore += 3;
                    }
                }
            } else if (move.numberOfMoves() == 2) {
                Move firstMove = move.getMove(0);
                Move secondMove = move.getMove(1);
                if (firstMove.getFromPip() == 25 || secondMove.getFromPip() == 25) { // Moving from Bar
                    if (firstMove.isHit() && secondMove.isHit()) {
                        moveScore += 2; // Bar - Board ( Double Hit )
                    } else if (firstMove.isHit() || secondMove.isHit()) {
                        moveScore += 1; // Bar - Board ( Single Hit + Blot)
                    } else if (firstMove.getToPip() == secondMove.getToPip()) {
                        moveScore += 4; // Bar - Board ( Block )
                    } else if (isBlotToBlock(firstMove.getToPip()) || isBlotToBlock(secondMove.getToPip())) {
                        if (isBlotToBlock(firstMove.getToPip()) && isBlotToBlock(secondMove.getToPip())) {
                            moveScore += 5; // Bar - Board ( Double Blot to Block )
                        } else {
                            moveScore += 2; // Bar - Board ( Single Blot to Block )
                        }
                    }
                } else { // Moving from board
                    if (firstMove.getToPip() == 0 || secondMove.getToPip() == 0) { // Bear - off
                        if (firstMove.getToPip() == 0 && secondMove.getToPip() == 0) {
                            moveScore += 10; // Board - Bear off ( Two bear offs )
                        } else {
                            moveScore += 5;
                        }

                        if (isOppOnBar()) {
                            if (isBlockToBlot(firstMove.getFromPip())) {
                                moveScore -= 3;
                            }

                            if (isBlockToBlot(secondMove.getFromPip())) {
                                moveScore -= 3;
                            }

                            if (isBlotToBlock(firstMove.getToPip())) {
                                moveScore += 5;
                            }

                            if (isBlotToBlock(secondMove.getToPip())) {
                                moveScore += 5;
                            }
                        }

                    } else { // Non Bear - off
                        if (firstMove.isHit() && secondMove.isHit()) {
                            moveScore += 2;
                        } else if (firstMove.isHit() || secondMove.isHit()) {
                            if (firstMove.getToPip() == secondMove.getToPip()) {
                                moveScore += 5; // Board - Board ( Hit to a Block )
                            } else {
                                moveScore += 1;
                            }
                        } else if (firstMove.getToPip() == secondMove.getToPip()) {
                            moveScore += 3;
                            if (isBlockToBlot(firstMove.getToPip())) {
                                moveScore -= 1;
                            }

                            if (isBlockToBlot(secondMove.getToPip())) {
                                moveScore -= 1;
                            }
                        } else if (isBlotToBlock(firstMove.getToPip()) || isBlotToBlock(secondMove.getToPip())) {
                            if (isBlotToBlock(firstMove.getToPip()) && isBlotToBlock(secondMove.getToPip())) {
                                moveScore += 2; // Board - Board ( Double Blot to Block )
                            } else {
                                moveScore += 1; // Board - Board ( Single Blot to Block )
                            }
                        }
                    }
                }

                if (firstMove.getFromPip() > 18) {
                    moveScore += 2;
                } else if (firstMove.getFromPip() > 12) {
                    moveScore += 1;
                }

                if (secondMove.getFromPip() > 18) {
                    moveScore += 2;
                } else if (secondMove.getFromPip() > 12) {
                    moveScore += 1;
                }

                if (isPrimePip(firstMove.getToPip()) || isPrimePip(secondMove.getToPip())) {
                    if (isPrimePip(firstMove.getToPip()) && isPrimePip(secondMove.getToPip())) {
                        moveScore += 4;
                    } else {
                        moveScore += 2;
                    }
                }

                if (isBlockToBlot(firstMove.getToPip()) || isBlockToBlot(secondMove.getToPip())) {
                    if (isBlockToBlot(firstMove.getToPip()) && isBlockToBlot(secondMove.getToPip())) {
                        moveScore -= 2;
                    } else {
                        moveScore += 1;
                    }
                }

                if (oppCheckerToBeHit(firstMove.getFromPip())) {
                    if (firstMove.isHit()) {
                        moveScore += 3;
                    }

                    if (firstMove.getToPip() == secondMove.getToPip()) {
                        moveScore += 6;
                    }
                }

                if (oppCheckerToBeHit(secondMove.getFromPip())) {
                    if (secondMove.isHit()) {
                        moveScore += 3;
                    }

                    if (secondMove.getToPip() == firstMove.getToPip()) {
                        moveScore += 6;
                    }
                }

                if (vulnerableCheckers()) {
                    if (isBlockToBlot(firstMove.getFromPip()) || isBlockToBlot(secondMove.getFromPip())) {
                        if (isBlockToBlot(firstMove.getFromPip()) && isBlockToBlot(secondMove.getFromPip())) {
                            moveScore -= 1;
                        } else {
                            moveScore -= 1;
                        }

                        if (firstMove.isHit()) {
                            moveScore += 2;
                        }

                        if (secondMove.isHit()) {
                            moveScore += 2;
                        }
                    }

                    if (isBlotToBlock(firstMove.getToPip()) || isBlotToBlock(secondMove.getToPip())) {
                        if (isBlotToBlock(firstMove.getToPip()) && isBlotToBlock(secondMove.getToPip())) {
                            moveScore += 2;
                        } else {
                            moveScore += 1;
                        }
                    }
                }

                if (!isAhead()) {
                    if (firstMove.isHit()) {
                        moveScore += 5;
                    }

                    if (secondMove.isHit()) {
                        moveScore += 5;
                    }
                }
            } else { // Forced play
                moveScore += move.getMove(0).getPipDifference();
            }
            winPercentage.add(moveScore); // Add to ArrayList
        }
        return winPercentage;
    }

    private boolean isBlotToBlock(int pip) { // Turns a blot into a block
        return board.getNumCheckers(me.getId(), pip) == 1;
    }

    private int getTotalPipCount(int ID) {
        int totalPipCount = 0;
        for (int pip = 25; pip >= 0; pip--) {
            totalPipCount += pip * board.getNumCheckers(ID, pip);
        }
        return totalPipCount;
    }

    private boolean isBlockToBlot(int pip) { // Leaves a 2 checker block as a blot
        return board.getNumCheckers(me.getId(), pip) == 2;
    }

    private boolean isPrimePip(int pip) {
        return (pip >= 3 && pip <= 7);
    }

    private boolean isAhead() {
        return getTotalPipCount(me.getId()) - 10 < getTotalPipCount(opponent.getId());
    }

    private boolean oppCheckerToBeHit(int Pip) {
        int pipsToBeHit = 0;
        for (int pip = Pip; pip >= 1; pip--) {
            if (board.getNumCheckers(opponent.getId(), pip) == 1) {
                pipsToBeHit++;
            }
        }
        return pipsToBeHit >= 1;
    }

    private boolean vulnerableCheckers() {
        int numOfPips = 0;
        for (int pip = 7; pip <= 18; pip++) {
            if (board.getNumCheckers(me.getId(), pip) == 1) {
                numOfPips++;
            }
        }
        return numOfPips >= 2;
    }

    private boolean isVeryAhead() {
        return getTotalPipCount(me.getId()) - 100 < getTotalPipCount(opponent.getId());
    }

    private boolean isOppOnBar() {
        return board.getNumCheckers(opponent.getId(), 25) > 0;
    }
}
