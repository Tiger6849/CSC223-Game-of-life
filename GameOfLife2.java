
/**
 * GameOfLife2 is a game based off conways game of life.
 *
 * @author Llew Cahalane
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
import java.io.FileNotFoundException; // to handle file reading errors
import java.util.concurrent.TimeUnit;// for a sleep command
import java.awt.image.BufferedImage; //for buffering
import javax.swing.ImageIcon; // for the images on buttons
import javax.swing.event.*;
import javax.swing.JFrame;//for JFrame
import javax.swing.SwingUtilities;// for swing utilities
import java.awt.Component;
import java.util.Random;//for generating random numbers

public class GameOfLife2 extends JFrame implements ActionListener,MouseListener,ChangeListener
{
    //finals
    final int BOARDWIDTH = 200; 
    final int BOARDHEIGHT = 200;

    final int CELLWIDTH = 5;

    //icons. the bunch of code is so that they are sized correctly.
    ImageIcon pauseIconTemp= new ImageIcon("images/pause.png");
    Image pauseImage = pauseIconTemp.getImage();
    Image pauseImageTemp = pauseImage.getScaledInstance(50,50,java.awt.Image.SCALE_SMOOTH);
    ImageIcon pauseIcon = new ImageIcon(pauseImageTemp);

    ImageIcon playIconTemp= new ImageIcon("images/play.png");
    Image playImage = playIconTemp.getImage();
    Image playImageTemp = playImage.getScaledInstance(50,50,java.awt.Image.SCALE_SMOOTH);
    ImageIcon playIcon = new ImageIcon(playImageTemp);

    //board array
    String[][] gameBoard = new String[BOARDWIDTH][BOARDHEIGHT];
    String[][] pastFrame = new String[BOARDWIDTH][BOARDHEIGHT];

    //scanner for input
    Scanner keyboard = new Scanner(System.in);

    //swing setup
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem menuItem;
    JButton myButton;
    CustomPanel panel;

    

    //variables
    boolean playing = false;

    int framesPerSecond;
    /**
     * Constructor for objects of class GameOfLife2
     */
    public GameOfLife2()
    {
        //set up
        setUpBoard();
        setUpWindow();
        playControl(false);
    }

    public void setUpBoard() {
        for(int x = 0;x < BOARDWIDTH;x++){
            for(int y = 0;y < BOARDHEIGHT;y++){
                gameBoard[x][y] = "=";
            }
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
            if(inputSanatized)
                answer = reply;
        }

        System.out.println(answer);
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
        for(int i = 0;i<steps;i++){
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
    }

    public void mouseExited(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}
    // to detect mouse clicks
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX() - 8;
        int mouseY = e.getY() - 53;
        
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
        
        panel.repaint(0,0,1080,1080);
    }

    //for detecting menu interactions
    public void actionPerformed(ActionEvent e){
        switch(e.getActionCommand()){
                case"Next frame":
                cellCalculation(1);
                panel.repaint(0,0,1080,1080);
                break;
            case "Move multiple frames":
                cellCalculation(Integer.parseInt(printQuestionAndCleanInput("How many frames?",2)));
                panel.repaint(0,0,1080,1080);
                break;
            case "Quit":
                System.exit(0);
                break;
            case "Save to file":
                writeSave();
                break;
            case "Create file for save":
                createSaveFile();
                break;
            case "Load save":
                loadSave();
                panel.repaint(0,0,1080,1080);
                break;
            case "":
                playControl(true);
                if(playing){
                    myButton.setIcon(pauseIcon);
                }else{
                    myButton.setIcon(playIcon);
                }
                break;
            case "Random":
                System.out.println("heklO");
                for(int y = 0;y < BOARDHEIGHT;y++){
                    for(int x = 0;x < BOARDWIDTH;x++){
                        Random r = new Random();
                        if(r.nextInt(2) == 1){
                            gameBoard[x][y] = "#";
                        }else{
                            gameBoard[x][y] = "=";
                        }
                    }
                }
                panel.repaint(0,0,1080,1080);
                break;
        }
    }

    public void setUpWindow(){

        JFrame frame = new JFrame("Game of life");
        panel = new CustomPanel(BOARDHEIGHT,BOARDWIDTH,CELLWIDTH,gameBoard);
        
        //detects mouse clicks
        frame.addMouseListener(this);

        //make menu  bar
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        //add menu to the menu bar
        menu = new JMenu("Extras");
        menuBar.add(menu);

        menuItem = new JMenuItem("Quit");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Save to file");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Create file for save");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Load save");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //buttons
        myButton = new JButton();
        myButton.setText("Next frame");
        myButton.setBounds (100,1100,120,20);
        myButton.addActionListener(this);
        myButton.setFocusable(false);
        frame.add(myButton);

        myButton = new JButton();
        myButton.setText("Move multiple frames");
        myButton.setBounds (240,1100,170,20);
        myButton.addActionListener(this);
        myButton.setFocusable(false);
        frame.add(myButton);
        
        myButton = new JButton();
        myButton.setText("Random");
        myButton.setBounds (720,1100,100,20);
        myButton.addActionListener(this);
        myButton.setFocusable(false);
        frame.add(myButton);

        myButton = new JButton();
        myButton.setBounds (430,1100,50,50);
        myButton.addActionListener(this);
        myButton.setFocusable(false);
        myButton.setIcon(playIcon);
        frame.add(myButton);
        
        

        //label for the slider
        JLabel sliderLabel = new JLabel("Speed",JLabel.CENTER);
        sliderLabel.setLocation(550,1085);
        sliderLabel.setSize(100,10);
        frame.add(sliderLabel);

        //add speed slider
        JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL,0,200,7);
        framesPerSecond.addChangeListener(this);
        framesPerSecond.setBounds(500,1095,200,60);
        framesPerSecond.setPaintTrack(true);
        framesPerSecond.setPaintTicks(true);
        framesPerSecond.setPaintLabels(true);
        framesPerSecond.setMajorTickSpacing(50);
        framesPerSecond.setMinorTickSpacing(5);
        frame.add(framesPerSecond);
        
        frame.add(panel);

        //put it onto the screen
        frame.setSize(new Dimension(BOARDWIDTH* CELLWIDTH + 100,BOARDHEIGHT * CELLWIDTH + 200));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center window on screen
        frame.setVisible(true); 
    }

    public void createSaveFile(){
        //makes a file. the try is to catch if there is an error
        try {
            //creates the file object
            File myObj = new File("GameOfLife2Save.txt");
            //checks if the file already exists
            if(myObj.createNewFile()) {
                System.out.println("File saved");
            } else{
                System.out.println("File already exists");
            }
            //catches the errors
        } catch (IOException e) {
            System.out.println("An error occurred. (IOException)");
            e.printStackTrace(); //print error details
        }

    }
    //you were doing save stuff. make sure to update testing doc
    public void writeSave() {
        String saveString = "";
        for(int x = 0;x < BOARDWIDTH;x++){
            for(int y = 0;y < BOARDHEIGHT;y++){
                if(gameBoard[x][y].equals("#")){
                    saveString += "1";
                }else if(gameBoard[x][y].equals("=")){
                    saveString += "0";
                }
                if(y == BOARDWIDTH-1){
                    saveString += ";";
                }
            }
        }

        try {
            FileWriter myWriter = new FileWriter("GameOfLife2Save.txt");
            myWriter.write(saveString);
            myWriter.close();
            System.out.println("File saved");
        } catch(IOException e) {
            System.out.println("An error occurred.(IOException)");
            e.printStackTrace();
        }
    }

    public void loadSave() {
        File myObj = new File("GameOfLife2Save.txt");
        try (Scanner fileReader = new Scanner(myObj)){
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                int x = 0;
                int y = 0;
                for(int i = 0;i < data.length();i++){
                    switch(data.charAt(i)){
                        case '1':
                            gameBoard[x][y] = "#";
                            System.out.println(data.charAt(i)+" "+data.length());;
                            y++;
                            break;
                        case '0':
                            gameBoard[x][y] = "=";
                            System.out.println(data.charAt(i)+" "+data.length());
                            y++;
                            break;
                        case ';':
                            x++;
                            y = 0;
                            System.out.println(data.charAt(i)+" "+data.length());
                    }
                }
            }
        }catch (FileNotFoundException e){
            System.out.println("An error occured (FileNotFoundException)");
            e.printStackTrace();
        }
    }

    //controls wether its going automatically or not
    public void playControl(boolean changing){
        if(changing){
            if(playing){
                playing = false;
            }else{
                playing = true;
            }
        }else{
            while(true){
                try{
                    TimeUnit.MILLISECONDS.sleep(1000/(1+framesPerSecond));
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread was interrupted while sleeping.");
                }

                if(playing){
                    cellCalculation(1);
                    panel.repaint(0,0,1080,1080);
                }
            }
        }

    }

    public void stateChanged(ChangeEvent e){
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            framesPerSecond = (int)source.getValue();
        }
    }
}