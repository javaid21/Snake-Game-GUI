import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {
	
	static final int PANEL_WIDTH = 600;
	static final int PANEL_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (PANEL_WIDTH * PANEL_HEIGHT)/ UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	int bodyParts = 4;
	int score;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		
		//Creating new instance of the game
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if(running) {
			g.setColor(Color.RED);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i = 0; i < bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.GREEN);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + score *10, (PANEL_WIDTH - metrics.stringWidth("Score: " + score *10 ))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	
	public void move() {
		
		//Moving the snake
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void newApple() {
		
		//Create an apple at a random position on the grid
		appleX = random.nextInt((int)PANEL_WIDTH/UNIT_SIZE)*UNIT_SIZE;
		appleY = random.nextInt((int)PANEL_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
	}
	
	public void checkApple() {
		
		//Check to see if snake has swallowed the apple
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			score++;
			newApple();
		}
	}
	
	public void checkCollisions() {
		
		//Check if head touches other body parts
		for(int i = bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0]) == y[i]) {
				running = false;
			}
		}
		
		//Check if head touches left border
		if(x[0] < 0) {
			running = false;
		}
		
		//Check if head touches right border
		if(x[0] > PANEL_WIDTH) {
			running = false;
		}
		
		//Check if head touches top border
		if(y[0] < 0) {
			running = false;
		}
		
		//Check if head touches bottom border
		if(y[0] > PANEL_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		
		//Display Score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + score *10, (PANEL_WIDTH - metrics1.stringWidth("Score: " + score *10 ))/2, g.getFont().getSize());
		
		//Check if the game is over
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (PANEL_WIDTH - metrics2.stringWidth("Game Over"))/2, PANEL_HEIGHT/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e ) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}
