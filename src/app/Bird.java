package app;

import java.awt.Image;

public class Bird {

//	poicionamento inicial de nuestro pajaro
	int birdX = FlappyBird.boardWidth / 8;
	int birdY = FlappyBird.boardHeight / 2;

	int birdWidth = 34;
	int birdHeight = 24;
	
	//establecer la velocidad de juego, es decir el movimiento del pajaro
		int velocityY = 0;
		int gravity =1;
		
		
	
	Image img;
	
	
	public Bird(Image img) {
		this.img = img;
		
	}
	

}
