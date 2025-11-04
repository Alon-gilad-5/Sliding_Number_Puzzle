import java.util.Arrays;

public class Board {

    //Attributes
    private Tile[][] tiles; //the 2D array representing the board


    //Constructors
    //Constructor that creates a target/perfect board according to the size of the board
    public Board(int numOfRows , int numOfColumn){
        tiles = new Tile[numOfRows][numOfColumn];
        int counter = 1;
        for (int i=0 ; i<tiles.length ; i++){ //Run through all rows
            for (int j=0 ; j<tiles[i].length ; j++){ //Run through all columns
                if (i >= tiles.length-1 && j >= (tiles[i].length-1))
                    tiles[i][j] = new Tile(); //Assign the last tile to be 0
                else {
                    tiles[i][j] = new Tile(counter); //Assign the current tile according to the value it should hold
                    counter++; //Raise counter by 1 to adjust to the next tile
                }
            }
        }
    }

    //Constructor that creates a board according to a string representing the board
    //In the string, a space represents the end of a number, | represents the end of a row
    public Board(String board) {
        int row=0 , column=0 , num=0;
        this.tiles = new Tile[getNumOfRows(board)][getNumOfColumns(board)]; //Initialize board size
        for (int i=0 ; i<board.length() ; i++){ //Run through the string
            if (board.charAt(i) == '|') { //At the end of a row
                tiles[row][column] = new Tile(num); //Use num for the final tile in the row
                num=0; //Reset num
                row++; //Go to the next row
                column=0; //Reset the column counter, so the next row would start from the beginning
            }
            else if (board.charAt(i) == ' ') { //At the end of a number
                tiles[row][column] = new Tile(num); //Use num for the current tile
                num=0; //Reset num
                column++; //Go to the next column
            }
            else { //At a number
                num = num * 10 + (int)board.charAt(i)-48; //Add the current number to the end of the current number. -48 to compensate ascii
                if (i+1>=board.length()) //At the end of the string
                    tiles[row][column] = new Tile(num); //Use num for the last tile
            }
        }
    }

    //Default constructor, create a board with 1 tile in it, with 0 in it
    public Board() {
        this("0");
    }


    //Getters & Setters
    public Tile[][] getTiles() {
        return tiles;
    }
    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }


    //Main Methods
    //Find the number of rows of a board from the board's string representation
    public static int getNumOfRows (String board){
        int counter=1; //Default number of rows is 1
        for (int i=0 ; i<board.length() ; i++) { //Run through the string
            if (board.charAt(i) == '|') { //At the end of a row
                counter++; //Add 1 row
            }
        }
        return counter; //Return number of rows
    }

    //Find the number of columns of a board from the board's string representation
    public static int getNumOfColumns (String board){
        int counter=1; //Default number of columns is 1

        //Run through the string until the end of the first row
        for (int i=0 ; board.charAt(i) != '|' && i+1 < board.length() ; i++) {
            if (board.charAt(i) == ' '){ //At the end of a number
                counter++; //Add 1 column
            }
        }
        return counter; //Return number of columns
    }

    //Search for a tile's number on the board and return its location in an array formatted (row,column)
    public int[] findValueOnBoard(int value){
        int[] index = new int[2]; //Initialize return array

        for (int i = 0; i < this.tiles.length; i++) //Search through all rows
            for (int j = 0; j < this.tiles[i].length; j++){ //Search through all columns

                if(this.tiles[i][j].getValue() == value) { //When the value has been found
                    index[0] = i; //Set location's row
                    index[1] = j; //Set location's column
                    return index; //Return location array
                }
            }
        return null; // When the tile's number isn't on the board, return a default of null
    }

    //Override toString method
    //In the string, a space represents the end of a number, | represents the end of a row
    @Override
    public String toString(){
        String out=""; //Initialize return string
        for (int i=0 ; i < tiles.length; i++){ //Run through all rows
            for (int j=0 ; j < tiles[i].length; j++) { //Run through all columns

                if (j + 1 < tiles[i].length) //On "normal" columns
                    //Add the tile's value to the string with a space after it
                    out = out + tiles[i][j].getValue() + " ";

                else //At the end of a row
                    //Add the tile's value to the string
                    out = out + tiles[i][j].getValue();
            }
            if (i + 1 < tiles.length) //At the end of "normal" rows that aren't the last row
                out = out + "|"; //Add | to the string, representing the end of a normal row
        }
        return out; //Return result string
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Board)) {
            return false;
        }
        Board board = (Board) other;
        return Arrays.deepEquals(tiles, board.tiles);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tiles);
    }
}