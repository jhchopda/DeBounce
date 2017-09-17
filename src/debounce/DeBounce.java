package debounce;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

public class DeBounce extends Frame implements Runnable  {
    Random random= new Random();
    int flag=0, score, a;
    int minGap=200;
    int[] x, y;
    int y2=390,x2=70,x22=80;
    boolean gameStatus=true;
    public DeBounce(){
        
        //regester Listeners
        addKeyListener(new MyKeyAdapter(this));    
        addWindowListener(new MyWindowAdapter());
      
        restart();
    }    
     
    //initiallizing all variables whenever game is starting
    public void restart(){
        x=new int[3];
        y=new int[3];
        x[0]=950;
        x[1]=650;
        x[2]=350;
        y[0]=350;
        y[1]=370;
        y[2]=390;
        gameStatus=true;
        flag=0;
        score=0;
    }

    @Override
    public void paint(Graphics g){
        
        if(flag==0){
        	g.setFont(new Font("TimesRoman", Font.BOLD, 20));
        	g.drawString("to start the game press SPACE",350,200);
        	g.drawString("to quit the game press Q",375,250);
        }
        //rectangles
        g.setColor(Color.blue);
        for(int i=0; i<3; i++){
            g.fillRect(x[i],y[i],20,400-y[i]);
        }
        
        //ground or horizontal line
        g.setColor(Color.black);
        g.drawLine(0, 400, 1000, 400);
        
        //ball
        g.setColor(Color.red);
        g.fillOval(x2, y2, 10, 10);
        
        //display score
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman", Font.ITALIC, 20));
        g.drawString("Score: "+score, 700, 100);
     
        //display when one loose game
        if(gameStatus==false){
            g.setColor(Color.black);
            g.setFont(new Font("TimesRoman", Font.BOLD, 40)); 
            g.drawString("GAME OVER", 300, 300);
			g.setFont(new Font("TimesRoman", Font.BOLD, 20));        
        	g.drawString("to restart the game press SPACE",275,200);
        }
       
    }
    
     public static void main(String ar[]){
        DeBounce frm= new DeBounce();
        
        frm.setSize(new Dimension(1000,700));
        frm.setTitle("frame based App");
        frm.setVisible(true);
        
    } 

    //call when game start
    public void method(){
        new Thread(this).start();
    }
    
    //call for jumping ball
    public void method1(){
        new Thread(this).start();
    }
   
    @Override
    public void run(){
        if(flag==1){ //when game is start
            for(int i=0; ; i++){
                    
                //change x values of rect
                for(int j=0; j<3; j++){
                    
                    //disappearing nd regenerating rect when it reach at left edge of frame(x==0)
                    if(x[j]==0){
                        if(j==2)
                            x[j]=random.nextInt(1050-(x[0]+minGap))+x[0]+minGap;
                        else
                            x[j]=random.nextInt(1050-(x[j+1]+minGap))+x[j+1]+minGap;
                        
                        //give random value to y coordinate when new rect generate at right edge
                        a= random.nextInt(30)+350;
                        y[j]=a;
                        score += 5;
                    }
                    else
                        x[j] -=1;
                
                    //check if any rect is overlaped with ball (condition for loosing the game)
                    if(y2>y[j]){
                        if(x22>=x[j] && x22<=x[j]+20 || x2>=x[j] && x2<=x[j]+20){
                            gameStatus=false;
                            repaint();
                            return;
                        }
                    }
                }
                
                try {
                    Thread.sleep(3);
                    repaint();
                } catch (InterruptedException ex) {
                }
            }
        }
        else{  //thread for jumping ball
            for(int i=0; i<70; i++){  //in upward direction
                y2=y2-1;
               
                try {
                    Thread.sleep(5);
                    repaint();
                } catch (InterruptedException ex) {
                }    
            }
            for(int i=0; i<70; i++){  //in downward direction
                y2=y2+1;
                
                try {
                    Thread.sleep(5);
                    repaint();
                } catch (InterruptedException ex) {
                }    
            }
        }
    }
}

class MyWindowAdapter extends WindowAdapter {
    
    //on closing window
    @Override
    public void windowClosing(WindowEvent e){
        System.exit(0);
    }
}

class MyKeyAdapter extends KeyAdapter{
    DeBounce cgObj;
    public MyKeyAdapter(DeBounce cg){
        cgObj=cg;
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        int ch= e.getKeyCode();
        if(ch==KeyEvent.VK_SPACE){
            
            
            if(cgObj.y2<=360)
                return;
            
            //if space pressed 1st time: initiate the game 
            if(cgObj.flag==0){
                cgObj.flag=1;
                cgObj.method();
            }
            else{  //else let the ball jump
                cgObj.flag=2;
                cgObj.method1();
            }
            
            //space for restarting game
            if(cgObj.gameStatus==false)
                cgObj.restart();
        }

        if(ch==KeyEvent.VK_Q)
        	System.exit(0);
    }
}
