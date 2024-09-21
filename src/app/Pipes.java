package app;

import java.awt.Image;

public class Pipes {

	int pipeX = FlappyBird.boardWidth;
	int pipeY = 0;

	int pipeWidth = 64;
	int pipeHeight = 512;
	Image img;
	//compruba cuando el pajaro ha superado el obstaculo
	boolean passed = false;
	
	int velocityX =-4;

	public Pipes(Image img) {
		this.img = img;
	}
}
