import java.awt.Dimension;

import javax.swing.JFrame;

public class movingWindow {

	public static void main(String[] args) throws InterruptedException {
		JFrame win = new JFrame();
		
		win.setSize(new Dimension(100,100));
		
		win.setVisible(true);
		
		win.setLocation(0, 200);
		
		//to make the window move horizontal I will have to change
		//the x value to whatever
		
		int x = 0;
		int sleepTime = 1000;
		for (int i = 0; i < 10; i++){
			//need a pause here
			Thread.currentThread().sleep(sleepTime); 	//sleepTime is an int and represent time in Milliseconds
			
			x += 50;
			
			win.setLocation(x, 200);
		}
		
	}

}
