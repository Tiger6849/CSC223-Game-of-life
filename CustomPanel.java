import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;

public class CustomPanel extends JPanel  {
    
    final int BOARDHEIGHT;
    final int BOARDWIDTH;
    final int CELLWIDTH;
    
    String[][] gameBoard;
    
    //only here to get the variables
    public CustomPanel (int BOARDHEIGHT2, int BOARDWIDTH2 ,int CELLWIDTH2, String[][] gameBoard2){
        
        
        
        BOARDHEIGHT = BOARDHEIGHT2;
        BOARDWIDTH = BOARDWIDTH2;
        CELLWIDTH = CELLWIDTH2;
        gameBoard = gameBoard2;
    }
    
    //painting in the cells and the outlines for the cells
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        //draw cells
        for(int y = 0;y < BOARDHEIGHT;y++){
            for(int x = 0;x < BOARDWIDTH;x++){
                if(gameBoard[x][y] == "#"){
                    g2.setColor(Color.WHITE);
                }else if(gameBoard[x][y] == "="){
                    g2.setColor(Color.GRAY);
                }
            
                g2.fillRect(CELLWIDTH * (x + 2),CELLWIDTH * (y + 2),CELLWIDTH,CELLWIDTH);
            }
        }
    
        g2.setColor(Color.LIGHT_GRAY);
        //draw horizontal grid lines
        for(int x=0;x<BOARDHEIGHT + 1;x++){
            g2.fillRect(CELLWIDTH * 2,(CELLWIDTH * x) + CELLWIDTH * 2,(CELLWIDTH * BOARDWIDTH),1);
        }
    
        //draw vertical grid lines
        for(int y=0;y<BOARDWIDTH + 1;y++){
            g2.fillRect((CELLWIDTH * y)+ CELLWIDTH * 2,CELLWIDTH * 2,1,(CELLWIDTH * BOARDHEIGHT));
        }
    
        //for the play/pause button
        g2.setColor(Color.GRAY);
        g2.drawString("Icons by Debi Alpa Nugraha and IYAHICON. Based on Conways Game Of Life", 10,1200);
    }  
}


