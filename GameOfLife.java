
/**
 * GameOfLife is a game based off conways game of life.
 *
 * @author Llew cahalane
 * 
 */
import java.awt.event.*; // for events like mouse and keyboard stuff
import java.util.Scanner; // to read text input
import javax.swing.*; //for the windows GUI stuff
import java.awt.*; //also for GUI
import java.awt.geom.*; // for lines
import javax.swing.JButton;//for buttons
import java.io.File; //to make files
import java.io.IOException; // to handle file errors
import java.io.FileWriter; // to write to files
public class GameOfLife extends JFrame implements ActionListener,MouseListener
{
    //finals
    final int BOARDWIDTH = 70; 
    final int BOARDHEIGHT = 70;

    final int CELLWIDTH = 15;

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

        setUpWindow();
    }

    public void setUpBoard() {
        for(int x = 0;x < BOARDWIDTH;x++){
            for(int y = 0;y < BOARDHEIGHT;y++){
                gameBoard[x][y] = "=";
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
        boolean isNumber = false;
        int answerX = 1;
        int answerY = 2;
        String answer = "1";
        String answerXYString = "";

        while(!inputSanatized){
            InD questionBox = new InD(question);
            questionBox.setLocationRelativeTo(this);
            questionBox.setVisible(true);
            String reply=questionBox.getText();

            //checks if you entered an integer
            for(int i = 0;i < reply.length();i++){
                if(Character.isDigit(reply.charAt(i))){
                    isNumber = true;
                }
            }
            if(questionType != 3){
                if(isNumber){
                    if(questionType == 1){
                        if(reply.equals("1") || reply.equals("2")){
                            inputSanatized = true;
                        }else{
                            System.out.println("Please input 1 or 2");
                        }
                    }else{
                        if(Integer.parseInt(reply) > 0){
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
                }
            }else{
                answerXYString = sanatizeCoords(answer,answerX,answerY,inputSanatized);
                while(answerXYString.equals("retry")){
                    System.out.println(question);
                    answerXYString = sanatizeCoords(reply,answerX,answerY,inputSanatized);
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

                g2.fillRect(CELLWIDTH * (x + 2),CELLWIDTH * (y + 5),CELLWIDTH,CELLWIDTH);
            }
        }

        g2.setColor(Color.GRAY);
        //draw horizontal grid lines
        for(int x=0;x<BOARDHEIGHT + 1;x++){
            g2.fillRect(CELLWIDTH * 2,(CELLWIDTH * x) + CELLWIDTH * 5,(CELLWIDTH * BOARDWIDTH),1);
        }

        //draw vertical grid lines
        for(int y=0;y<BOARDWIDTH + 1;y++){
            g2.fillRect((CELLWIDTH * y)+ CELLWIDTH * 2,CELLWIDTH * 5,1,(CELLWIDTH * BOARDHEIGHT));
        }
    }

    //these are here so there arent any errors
    public void mouseExited(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    // to detect mouse clicks
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        for(int y = 0;y < BOARDHEIGHT;y++){
            for(int x = 0;x < BOARDWIDTH;x++){
                int cellX = CELLWIDTH * (x + 2);
                int cellY = CELLWIDTH * (y + 5);
                int cellXAndWidth = CELLWIDTH * (x + 2) + CELLWIDTH;
                int cellYAndHeight = CELLWIDTH * (y + 5) + CELLWIDTH;

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

    //for detecting menu interactions
    public void actionPerformed(ActionEvent e){
        System.out.println(e.getActionCommand());
        switch(e.getActionCommand()){
            case "Move forward one frame":
                cellCalculation(1);
                repaint();
                break;
            case "Move forward multiple frames":
                cellCalculation(Integer.parseInt(printQuestionAndCleanInput("How many frames?",2)));
                repaint();
                break;
            case "Quit":
                System.exit(0);
                break;
            case "Save to file":
                writeSave();
                break;
            case "Create file for save":
                createSaveFile();
        }
    }

    public void setUpWindow(){
        //make window
        setTitle("Connect four");
        this.getContentPane().setPreferredSize(new Dimension(BOARDWIDTH* CELLWIDTH + 100,BOARDHEIGHT * CELLWIDTH + 100));
        this.getContentPane().setLayout(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

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
        menuItem.setAccelerator(KeyStroke.getKeyStroke('1'));
        menu.add(menuItem);

        menuItem = new JMenuItem("Move forward multiple frames");
        menuItem.addActionListener(this);
        menuItem.setAccelerator(KeyStroke.getKeyStroke('2'));
        menu.add(menuItem);

        menu = new JMenu("Extras");
        menuBar.add(menu);

        menuItem = new JMenuItem("Quit");
        menuItem.addActionListener(this);
        menuItem.setAccelerator(KeyStroke.getKeyStroke('2'));
        menu.add(menuItem);

        menuItem = new JMenuItem("Save to file");
        menuItem.addActionListener(this);
        menuItem.setAccelerator(KeyStroke.getKeyStroke('2'));
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Create file for save");
        menuItem.addActionListener(this);
        menuItem.setAccelerator(KeyStroke.getKeyStroke('2'));
        menu.add(menuItem);

        //put it onto the screen
        this.pack();
        this.toFront();
        this.setVisible(true); 

    }

    public void createSaveFile(){
        //makes a file. the try is to catch if there is an error
        try {
            //creates the file object
            File myObj = new File("GameOfLifeSave.txt");
            //checks if the file already exists
            if(myObj.createNewFile()) {
                System.out.println("File saved");
            } else{
                System.out.println("File alrerady exists");
            }
            //catches the errors
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace(); //print error details
        }
        
    }
    
    public void writeSave() {
        for(int y = 0;y < BOARDHEIGHT;y++){
            for(int x = 0;x < BOARDWIDTH;x++){
                add a string here that you will put in the myWriter write thing below. also use this link: https://www.w3schools.com/java/java_files_write.asp
            }
        }
        
        try {
            FileWriter myWriter = new FileWriter("GameOfLifeSave.txt");
            myWriter.write("
        }
    }
}

