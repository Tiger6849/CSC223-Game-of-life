
/**
 * GameOf Life is a game based off conways game of life.
 *
 * @author Llew cahalane
 * @version 1 07/05/26
 */
import java.awt.event.*;
import java.util.Scanner;
public class GameOfLife
{
    
    final int BOARDWIDTH = 20; 
    final int BOARDHEIGHT = 20;
    
    String[][] gameBoard = new String[BOARDWIDTH][BOARDHEIGHT];
    /**
     * Constructor for objects of class GameOfLife
     */
    public GameOfLife()
    {
        setUpBoard();
        printBoard();
    }
    
    public void setUpBoard() {
        for(int x = 0;x < BOARDWIDTH;x++){
            for(int y = 0;y < BOARDHEIGHT;y++){
                gameBoard[x][y] = "=";
            }
        }
    }
    
    public void printBoard() {
        for(int x = 0;x < BOARDWIDTH;x++){
            for(int y = 0;y < BOARDHEIGHT;y++){
                System.out.print(gameBoard[x][y]+" ");
            }
            System.out.println();
        }
    }
}
