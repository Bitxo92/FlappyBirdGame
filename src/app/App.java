package app;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class App extends JFrame {
	
	private FlappyBird flappyBird;
	private ImageIcon icono;
	
	public App() {
		
		icono = new ImageIcon(getClass().getResource("/flappybird.png"));
		
		this.setIconImage(icono.getImage());
		this.setTitle("Flappy Bird");
		this.setSize(400,700);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		flappyBird = new FlappyBird();
		this.add(flappyBird);
		//permite redimensionar la ventana principal para asi alojar mejor sus subcomponentes
		this.pack();
		flappyBird.requestFocus();
		
		this.setVisible(true);
	}

}
