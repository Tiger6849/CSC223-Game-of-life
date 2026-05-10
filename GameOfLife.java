
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
    //finals
    final int BOARDWIDTH = 20; 
    final int BOARDHEIGHT = 20;

    //board array
    String[][] gameBoard = new String[BOARDWIDTH][BOARDHEIGHT];

    //scanner for input
    Scanner keyboard = new Scanner(System.in);
    /**
     * Constructor for objects of class GameOfLife
     */
    public GameOfLife()
    {
        //set up
        setUpBoard();

        printBoardAndMenu();
        if(printQuestionAndCleanInput("Do you want to 1.Move forward some steps or 2.Change the state of a cell?", 1) == "1"){
            if(printQuestionAndCleanInput("Do you want to 1.Move forward one step or 2.Move forward multiple step?" ,1) == "1"){
                System.out.println("placeholder for calculating code. move one step forward");
            }else{
                String answer = printQuestionAndCleanInput("How many steps?", 2);
                System.out.println("placeholder for calculating code. move "+ answer +" step/s forward");
            }
        }else{
                String answer = printQuestionAndCleanInput("What cell? please answer in x,y format", 3);
                System.out.println("placeholder for calculating code. fill in "+ answer);
        }
    }

    public void setUpBoard() {
        for(int x = 0;x < BOARDWIDTH;x++){
            for(int y = 0;y < BOARDHEIGHT;y++){
                gameBoard[x][y] = "";
            }
        }
    }

    public void printBoardAndMenu() {
        for(int x = 0;x < BOARDWIDTH;x++){
            for(int y = 0;y < BOARDHEIGHT;y++){
                System.out.print(gameBoard[x][y]);
            }
            System.out.println();
        }
    }

    public String printQuestionAndCleanInput(String question ,int questionType){
        boolean inputSanatized = false;
        int answer = 1;
        int answerX = 1;
        int answerY = 2;
        
        while(!inputSanatized){
            System.out.println(question);
            //checks if you entered an integer
            if(questionType != 3){
                if(keyboard.hasNextInt()){
                    answer = keyboard.nextInt();
                    keyboard.nextLine();
                    //checks if its 1 or 2
                    if(questionType == 1){
                        if(answer == 1 || answer == 2){
                            inputSanatized = true;
                        }else{
                            keyboard.nextLine();
                            System.out.println("Please input 1 or 2");
                        }
                    }else{
                        if(answer > 0){
                            inputSanatized = true;
                        }else{
                            keyboard.nextLine();
                            System.out.println("Please input positive number");
                        }
                    }
                }else{
                    keyboard.nextLine();
                    if(questionType == 1){
                        System.out.println("Please input 1 or 2");
                    }else{
                        System.out.println("Please input positive number");
                    }
                }
            }else{
                if(keyboard.hasNextInt()){
                    
                }
            }
        }

        //returns the players answer
        if(questionType != 3){
            return String.valueOf(answer);
        }else{
            return answerX + ";" + answerY;
        }
    }
}
