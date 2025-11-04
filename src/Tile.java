public class Tile {

    //Attributes
    private int value; //The number in a tile


    //Constructors
    //Default constructor, create a tile with the value of 0
    public Tile() {
        this(0);
    }

    //Constructor that implements attributes from the user
    public Tile(int value) {
        this.value = value;
    }


    //Getters & Setters
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }


    //Main Methods
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Tile)) {
            return false;
        }
        Tile tile = (Tile) other;
        return value == tile.value;
    }

    @Override
    public String toString(){
        return ""+this.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}