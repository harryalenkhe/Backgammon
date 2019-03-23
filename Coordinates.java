class Coordinates {

    private double x; // column
    private double y; // row
    private char playerColour;
    private boolean taken;


    Coordinates(double column, double row, char playerColour) {
        this.x = column;
        this.y = row;
        this.playerColour = playerColour;
        this.taken = false;
    }

    double getX() { return this.x; }

    double getY() { return this.y; }

    char getPlayerColour() {
        return this.playerColour;
    }

    void occupyCoordinate(char playerColour)
    {
        this.taken = true;
        this.playerColour = playerColour;
    }

    void releaseCoordinate() {
        this.taken = false;
        this.playerColour = 'E';
    }

    boolean isTaken() { return this.taken; }
}
