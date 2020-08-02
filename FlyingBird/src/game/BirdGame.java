 
package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * 游戏界面
 */
public class BirdGame extends JPanel {

    // 背景图片
    BufferedImage background;
    
    BufferedImage startImage;
    BufferedImage gameOverImage;
    
    Ground ground;
    Column column1, column2;
    Bird bird;
    
    int score;
    
    int state;
    
    public static final int START = 0; //开始
    public static final int RUNNING = 1; //运行
    public static final int GAME_OVER = 2; //结束

    /**
     * 初始化游戏
     */
    public BirdGame() throws Exception {
        // 初始化背景图片
        background = ImageIO.read(getClass().getResource("/resources/bg.png"));
        
        startImage = ImageIO.read(getClass().getResource("/resources/start.png"));
        gameOverImage = ImageIO.read(getClass().getResource("/resources/gameover.png"));

        // 初始化地面、柱子、小鸟
        ground = new Ground();
        column1 = new Column(1);
        column2 = new Column(2);
        bird = new Bird();
        
        // 初始化分数
        score = 0;

        // 初始化状态
        state = START;
    }

    /**
     * 绘制界面
     */
    public void paint(Graphics g) {
        // 绘制背景
        g.drawImage(background, 0, 0, null);
        
        g.drawImage(ground.image, ground.x, ground.y, null);
        
        g.drawImage(column1.image, column1.x - column1.width / 2, column1.y - column1.height / 2, null);
        
        g.drawImage(column2.image, column2.x - column2.width / 2, column2.y - column2.height / 2, null);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.rotate(-bird.alpha, bird.x, bird.y);
        g.drawImage(bird.image, bird.x - bird.width / 2, bird.y - bird.height / 2, null);
        g2.rotate(bird.alpha, bird.x, bird.y);
        
        Font f = new Font(Font.SERIF, Font.BOLD, 40);
        g.setFont(f);
        g.drawString("" + score, 40, 60);
        g.setColor(Color.WHITE);
        g.drawString("" + score, 40 - 3, 60 - 3);
        
        switch(state) {
        case START:
        	g.drawImage(startImage, 0, 0, null);
            break;
        case GAME_OVER:
        	g.drawImage(gameOverImage, 0, 0, null);
            break;
        }
    }

    public void action() throws Exception {
    	MouseListener l = new MouseAdapter() {
    		public void mousePressed(MouseEvent e) {
    			try {
    				switch (state) {
    				case START:
    					state = RUNNING;
    					break;
    				case RUNNING:
    					bird.flappy();
    					break;
    				case GAME_OVER:
    					state = START;
    					column1 = new Column(1);
    					column2 = new Column(2);
    					score = 0;
    					bird = new Bird();
    					break;
    				}
    			}
    			catch(Exception ex) {
    				ex.printStackTrace();
    			}
    		}
    	};
    	
    	addMouseListener(l);
    	
    	while(true) {
    		switch (state) {
    		case START:
    			bird.fly();
    			ground.step();
    			break;
    		case RUNNING:
    			ground.step();
    			column1.step();
    			column2.step();
    			bird.fly();
    			bird.step();
    			
    			if (bird.x == column1.x || bird.x == column2.x) {
    				score++;
    			}
    			
    			if (bird.hit(column1) || bird.hit(column2) || bird.hit(ground))
    				state = GAME_OVER;
    		}
    		repaint();
    		Thread.sleep(1000/60);
    	}
    }
    
    /**
     * 启动方法
     */
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        BirdGame game = new BirdGame();
        frame.add(game);
        frame.setSize(440, 670);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.action();
    }

}