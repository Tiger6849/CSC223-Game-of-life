
/**
 * GameOf Life is a game based off conways game of life.
 *
 * @author Llew cahalane
 * @version 1 07/05/26
 */
import java.awt.event.*; // for events like mouse and keyboard stuff
import java.util.Scanner; // to read text input
import javax.swing.*; //for the windows GUI stuff
import java.awt.*; //also for GUI
import java.awt.geom.*; // for lines
import javax.swing.JButton;//for buttons
public class GameOfLife extends JFrame implements ActionListener,MouseListener
{
    //finals
    final int BOARDWIDTH = 20; 
    final int BOARDHEIGHT = 20;

    final int CELLWIDTH = 20;

    //board array
    String[][] gameBoard = new String[BOARDWIDTH][BOARDHEIGHT];
    String[][] pastFrame = new String[BOARDWIDTH][BOARDHEIGHT];

    //scanner for input
    Scanner keyboard = new Scanner(System.in);
    
    //menu
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem menuItem;
    /**
     * Constructor for objects of class GameOfLife
     */
    public GameOfLife()
    {
        //set up
        setUpBoard();

        //make window
        setTitle("Connect four");
        this.getContentPane().setPreferredSize(new Dimension(500,500));
        this.getContentPane().setLayout(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        
        //detects mouse clicks
        addMouseListener(this);
        
        //make menu  bar
        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        
        //add menus to the menu bar
        menu = new JMenu("Time control");
        menuBar.add(menu);
        
        menuItem = new JMenuItem("Move forward one frame");
        menuItem.addActionListener(this);
        menuItem.setAccelerator(KeyStroke.getKeyStroke('s'));
        menu.add(menuItem);
        
        add menu stuff!!! you were copying off menuWithShortcuts in the GUI project
    
        //packs everything together
        this.setVisible(true); 

        while(true){
            printBoard();
            //repaint();
            if(printQuestionAndCleanInput("Do you want to 1.Move forward some steps or 2.Change the state of a cell?", 1).equals("1")){
                if(printQuestionAndCleanInput("Do you want to 1.Move forward one step or 2.Move forward multiple step?" ,1).equals("1")){
                    cellCalculation(1);
                }else{
                    String answer = printQuestionAndCleanInput("How many steps?", 2);
                    cellCalculation(Integer.parseInt(answer));
                }
            }else{
                String answer = printQuestionAndCleanInput("What cell? please answer in x,y format", 3);
                changeStateOfCell(answer);
            }
        }
    }

    public void setUpBoard() {
        for(int x = 0;x < BOARDWIDTH;x++){
            for(int y = 0;y < BOARDHEIGHT;y++){
                gameBoard[x][y] = "#";
            }
        }
    }

    public void printBoard() {
        for(int y = 0;y < BOARDHEIGHT;y++){
            for(int x = 0;x < BOARDWIDTH;x++){
                System.out.print(gameBoard[x][y] + "  ");
            }
            System.out.println();
        }
    }

    public String printQuestionAndCleanInput(String question ,int questionType){
        boolean inputSanatized = false;
        String answer = "1";
        int answerX = 1;
        int answerY = 2;
        String answerXYString = "";

        while(!inputSanatized){
            System.out.println(question);
            //checks if you entered an integer
            if(questionType != 3){
                if(keyboard.hasNextInt()){
                    answer = keyboard.nextLine();
                    //checks if its 1 or 2
                    if(questionType == 1){
                        if(answer.equals("1") || answer.equals("2")){
                            inputSanatized = true;
                        }else{
                            System.out.println("Please input 1 or 2");
                        }
                    }else{
                        if(Integer.parseInt(answer) > 0){
                            inputSanatized = true;
                        }else{
                            System.out.println("Please input positive number");
                        }
                    }
                }else{
                    if(questionType == 1){
                        System.out.println("Please input 1 or 2");
                    }else{
                        System.out.println("Please input positive number");
                    }
                    keyboard.nextLine();
                }
            }else{
                answerXYString = sanatizeCoords(answer,answerX,answerY,inputSanatized);
                while(answerXYString.equals("retry")){
                    System.out.println(question);
                    answerXYString = sanatizeCoords(answer,answerX,answerY,inputSanatized);
                }
                inputSanatized = true;
            }
        }

        //returns the players answer
        if(questionType != 3){
            return answer;
        }else{
            return answerXYString;
        }
    }

    public String sanatizeCoords(String answer,int answerX,int answerY,boolean inputSanatized){
        answer = keyboard.nextLine();
        boolean isAnswerY = false;
        String answerXString = "";
        String answerYString = "";
        boolean fail = false;
        if(answer.length() >= 3){
            for(int i = 0;i < answer.length();i++){
                if(answer.charAt(i) == ',' && !isAnswerY){
                    isAnswerY = true;
                }else if(isAnswerY){
                    answerYString += answer.charAt(i);
                }else if(!isAnswerY){
                    answerXString += answer.charAt(i);
                }else{
                    System.out.println("Please use format x,y");
                    fail = true;
                }
                if(answer.charAt(i) != ','&&!Character.isDigit(answer.charAt(i))){
                    System.out.println("Please only input numbers and ,");
                    fail = true;
                }
            }
            if(!fail){
                if(Integer.parseInt(answerYString) < BOARDHEIGHT && Integer.parseInt(answerYString) >= 0){
                    answerY = Integer.parseInt(answerYString);
                    if(Integer.parseInt(answerXString) < BOARDWIDTH && Integer.parseInt(answerXString) >= 0){
                        answerX = Integer.parseInt(answerXString);
                        inputSanatized = true;
                    }else{
                        System.out.println("Please say a position on the board");
                    }
                }else{
                    System.out.println("Please say a position on the board");
                } 
            }
        }else{
            System.out.println("Please use format x,y");
        }

        if(inputSanatized){
            return answerX + ";" + answerY;
        }else{
            return "retry";
        }
    }

    public void cellCalculation(int steps){
        for(int x=0;x<BOARDWIDTH;x++){
            for(int y=0;y<BOARDHEIGHT;y++){
                pastFrame[x][y] = gameBoard[x][y];
            }
        }

        for(int x=0;x<BOARDWIDTH;x++){
            for(int y=0;y<BOARDHEIGHT;y++){
                int liveNeighbours = 0;

                //checks if they are to close to the left edge
                if(x > 0){
                    if(pastFrame[x-1][y].equals("#")){liveNeighbours++;};//checks left
                    if(y > 0)
                        if(pastFrame[x-1][y-1].equals("#")){liveNeighbours++;};//checks top left
                    if(y < BOARDHEIGHT-1)
                        if(pastFrame[x-1][y+1].equals("#")){liveNeighbours++;};//checks bottom left
                }
                //checks if they are to close to the top edge
                if(y > 0){
                    if(pastFrame[x][y-1].equals("#")){liveNeighbours++;};//checks above
                    if(x < BOARDWIDTH-1)
                        if(pastFrame[x+1][y-1].equals("#")){liveNeighbours++;};//checks top right
                }
                //checks if they are to close to the right edge
                if(x < BOARDWIDTH-1){
                    if(pastFrame[x+1][y].equals("#")){liveNeighbours++;};//checks right
                    if(y < BOARDHEIGHT-1)
                        if(pastFrame[x+1][y+1].equals("#")){liveNeighbours++;};//checks bottom right
                }
                //checks if they are to close to the bottom edge
                if(y < BOARDHEIGHT-1)
                    if(pastFrame[x][y+1].equals("#")){liveNeighbours++;};//checks below

                if(gameBoard[x][y].equals("#")){
                    if(liveNeighbours < 2){
                        gameBoard[x][y] = "="; //underpopulation
                    }
                    if(liveNeighbours > 3){
                        gameBoard[x][y] = "="; //overpopulation
                    }
                }else{
                    if(liveNeighbours == 3){
                        gameBoard[x][y] = "#"; //reproduction
                    }   
                }
            }
        }
    }

    public void changeStateOfCell(String answer){
        boolean isAnswerY = false;
        String answerXString = "";
        String answerYString = "";

        for(int i = 0;i < answer.length();i++){
            if(answer.charAt(i) == ';' && !isAnswerY){
                isAnswerY = true;
            }else if(isAnswerY){
                answerYString += answer.charAt(i);
            }else if(!isAnswerY){
                answerXString += answer.charAt(i);
            }
        }
        int answerY = Integer.parseInt(answerYString);
        int answerX = Integer.parseInt(answerXString);

        if(gameBoard[answerX][answerY].equals("#")){
            gameBoard[answerX][answerY] = "=";
        }else{
            gameBoard[answerX][answerY] = "#";
        }
    }

    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        //draw cells
        for(int y = 0;y < BOARDHEIGHT;y++){
            for(int x = 0;x < BOARDWIDTH;x++){
                if(gameBoard[x][y] == "#"){
                    g2.setColor(Color.WHITE);
                }else if(gameBoard[x][y] == "="){
                    g2.setColor(Color.BLACK);
                }

                g2.fillRect(CELLWIDTH * (x + 2),CELLWIDTH * (y + 2),CELLWIDTH,CELLWIDTH);
            }
        }

        g2.setColor(Color.GRAY);
        //draw horizontal grid lines
        for(int x=0;x<BOARDHEIGHT + 1;x++){
            g2.fillRect(CELLWIDTH * 2,(CELLWIDTH * x) + CELLWIDTH * 2,(CELLWIDTH * BOARDWIDTH),2);
        }

        //draw vertical grid lines
        for(int y=0;y<BOARDWIDTH + 1;y++){
            g2.fillRect((CELLWIDTH * y)+ CELLWIDTH * 2,CELLWIDTH * 2,2,(CELLWIDTH * BOARDHEIGHT));
        }
    }

    public void mouseExited(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        for(int y = 0;y < BOARDHEIGHT;y++){
            for(int x = 0;x < BOARDWIDTH;x++){
                int cellX = CELLWIDTH * (x + 2);
                int cellY = CELLWIDTH * (y + 2);
                int cellXAndWidth = CELLWIDTH * (x + 2) + CELLWIDTH;
                int cellYAndHeight = CELLWIDTH * (y + 2) + CELLWIDTH;

                if(mouseX > cellX && mouseX < cellXAndWidth && mouseY > cellY && mouseY < cellYAndHeight){
                    if(gameBoard[x][y].equals("=")){
                        gameBoard[x][y] = "#";
                    }else{
                        gameBoard[x][y] = "=";
                    }
                }
            }
        }
        repaint();
    }
}

