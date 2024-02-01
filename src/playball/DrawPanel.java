package playball;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

class DrawPanel extends JPanel {
	
    private int oneX = 7;
    private int oneY = 7;
    boolean up = false;
    boolean down = true;
    boolean left = false;
    boolean right = true;

    private int[][] obstacles = new int[3][3]; // Each obstacle has x, y, and size
    private int score = 0;

    public DrawPanel() {
        // Initialize obstacles at random positions
        Random rand = new Random();
        for (int i = 0; i < obstacles.length; i++) {
            obstacles[i][0] = rand.nextInt(280) + 10; // x position
            obstacles[i][1] = rand.nextInt(250) + 10; // y position
            obstacles[i][2] = 20; // size
        }
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.RED);
        g.fillRect(3, 3, this.getWidth()-6, this.getHeight()-6);
        g.setColor(Color.WHITE);
        g.fillRect(6, 6, this.getWidth()-12, this.getHeight()-12);
        g.setColor(Color.GREEN);
        g.fillOval(oneX, oneY, 16, 16);

        // Draw obstacles
        g.setColor(Color.BLACK);
        for (int[] obstacle : obstacles) {
            g.fillRect(obstacle[0], obstacle[1], obstacle[2], obstacle[2]);
        }

        // Draw score
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 10);
        
    }
   



    public void moveIt()
    {

        if(oneX >= 283){
            right = false;
            left = true;
        }
        if(oneX <= 7){
            right = true;
            left = false;
        }
        if(oneY >= 259){
            up = true;
            down = false;
        }
        if(oneY <= 7){
            up = false;
            down = true;
        }
        if(up){
            oneY--;
        }
        if(down){
            oneY++;
        }
        if(left){
            oneX--;
        }
        if(right){
            oneX++;
        }
        
        moveObstacles();
        checkCollision();
        repaint();
    }
    private void moveObstacles() {
        // Simple movement logic for obstacles
        Random rand = new Random();
        for (int[] obstacle : obstacles) {
            obstacle[0] += rand.nextInt(5) - 2; // Random horizontal movement
            obstacle[1] += rand.nextInt(5) - 2; // Random vertical movement

            // Keep obstacles within bounds
            if (obstacle[0] < 0) obstacle[0] = 0;
            if (obstacle[0] > this.getWidth() - obstacle[2]) obstacle[0] = this.getWidth() - obstacle[2];
            if (obstacle[1] < 0) obstacle[1] = 0;
            if (obstacle[1] > this.getHeight() - obstacle[2]) obstacle[1] = this.getHeight() - obstacle[2];
        }
    }
    private void checkCollision() {
        // Check if the ball collides with any obstacle
        Rectangle ballRect = new Rectangle(oneX, oneY, 16, 16);
        for (int[] obstacle : obstacles) {
            Rectangle obstacleRect = new Rectangle(obstacle[0], obstacle[1], obstacle[2], obstacle[2]);
            if (ballRect.intersects(obstacleRect)) {
                // Collision detected, reset game
                resetGame();
            }
        }
    }
    private void resetGame() {
        // Reset ball position
        oneX = 7;
        oneY = 7;

        // Reset obstacles
        Random rand = new Random();
        for (int i = 0; i < obstacles.length; i++) {
            obstacles[i][0] = rand.nextInt(280) + 10;
            obstacles[i][1] = rand.nextInt(250) + 10;
        }

        // Reset score
        score = 0;
    }
    public void increaseScore() {
        score++;
        repaint(); // Repaint to update the score display
    }

 }