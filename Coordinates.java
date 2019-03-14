class Coordinates {

    private double x; // column
    private double y; // row
    private boolean taken;

    Coordinates(double column, double row) {
        this.x = column;
        this.y = row;
        this.taken = false;
    }

    double getX() { return this.x; }

    double getY() { return this.y; }

    void occupyCoordinate()
    {
        this.taken = true;
    }

    void releaseCoordinate()
    {
        this.taken = false;
    }

    boolean isTaken() { return this.taken; }
}
