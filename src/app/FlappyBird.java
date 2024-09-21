package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    // Dimensiones del tablero
    static final int boardWidth = 400;
    static final int boardHeight = 700;
    
    // Objetos principales
    private Bird bird;
    private Timer gameLoop;          // Timer principal del juego
    private Timer placePipesTimer;   // Timer para colocar tubos periódicamente
    boolean gameOver = false;        // Bandera para verificar si el juego terminó
    boolean birdFalling = false;     // Bandera para controlar si el pájaro está cayendo tras la colisión
    double score = 0;                // Puntuación del jugador
    ArrayList<Pipes> pipes;          // Lista de tubos en el juego
    
    // Botón de reinicio
    private JButton restartButton;

    // Imágenes
    private Image backgroundImg;
    private Image birdImg;
    private Image topPipeImg;
    private Image bottomPipeImage;

    // Constructor
    public FlappyBird() {
        // Establece el tamaño del tablero de juego
        this.setPreferredSize(new Dimension(boardWidth, boardHeight));
        this.setFocusable(true);
        this.addKeyListener(this);  // Agregar el KeyListener para controlar el juego con el teclado

        // Cargar las imágenes del juego
        this.backgroundImg = new ImageIcon(getClass().getResource("/flappybirdbg.png")).getImage();
        this.birdImg = new ImageIcon(getClass().getResource("/flappybird.png")).getImage();
        this.topPipeImg = new ImageIcon(getClass().getResource("/toppipe.png")).getImage();
        this.bottomPipeImage = new ImageIcon(getClass().getResource("/bottompipe.png")).getImage();
        
        // Inicializar el pájaro
        bird = new Bird(birdImg);
        
        // Inicializar la lista de tubos
        pipes = new ArrayList<>();

        // Timer para colocar los tubos cada 1.5 segundos
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();  // Método para agregar tubos al juego
            }
        });
        placePipesTimer.start();  // Iniciar el timer para los tubos
        
        // Timer del bucle principal del juego (60 FPS)
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();  // Iniciar el juego

        // Crear el botón de reinicio y posicionarlo debajo del mensaje de "Game Over"
        restartButton = new JButton("Reiniciar");
        restartButton.setBounds(boardWidth / 2 - 75, boardHeight / 2 + 50, 150, 60);  // Centrar el botón debajo del puntaje
        restartButton.setVisible(false);  // Al principio, ocultar el botón
        restartButton.setBackground(Color.GREEN);  // Color de fondo del botón
        restartButton.setFont(new Font("Arial", Font.BOLD, 25));  // Fuente del texto del botón
        restartButton.setForeground(Color.BLACK);  // Color del texto del botón
        this.setLayout(null);
        this.add(restartButton);

        // Acción del botón de reinicio
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();  // Reiniciar el juego cuando se presiona el botón
            }
        });
    }

    // Método que dibuja los componentes del juego en la pantalla
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);  // Llamar al método que dibuja cada componente
    }

    // Método que dibuja el fondo, el pájaro, los tubos y la puntuación
    public void draw(Graphics g) {
        // Dibujar el fondo
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        // Dibujar el pájaro
        g.drawImage(bird.img, bird.birdX, bird.birdY, bird.birdWidth, bird.birdHeight, null);

        // Dibujar los tubos
        for (Pipes pipe : pipes) {
            g.drawImage(pipe.img, pipe.pipeX, pipe.pipeY, pipe.pipeWidth, pipe.pipeHeight, null);
        }

        // Dibujar la puntuación
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 32));

        if (gameOver) {
            // Mostrar "Game Over" y la puntuación en el centro de la pantalla cuando el juego termine
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.setColor(Color.RED);
            String gameOverText = "Game Over";
            String scoreText = "Score: " + (int) score;

            // Dibujar "Game Over" y la puntuación centrada
            g.drawString(gameOverText, boardWidth / 2 - g.getFontMetrics().stringWidth(gameOverText) / 2, boardHeight / 2 - 100);
            g.drawString(scoreText, boardWidth / 2 - g.getFontMetrics().stringWidth(scoreText) / 2, boardHeight / 2 - 30);

        } else {
            // Si el juego sigue en marcha, mostrar solo la puntuación
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    // Método para agregar tubos en el juego
    public void placePipes() {
        int gapHeight = 150; // Altura del espacio entre los tubos
        int randomY = (int) (Math.random() * (boardHeight - gapHeight - 200)) + 100;  // Posición aleatoria para el tubo superior

        // Crear el tubo superior
        Pipes topPipe = new Pipes(topPipeImg);
        topPipe.pipeX = boardWidth;
        topPipe.pipeY = randomY - topPipe.pipeHeight;

        // Crear el tubo inferior
        Pipes bottomPipe = new Pipes(bottomPipeImage);
        bottomPipe.pipeX = boardWidth;
        bottomPipe.pipeY = randomY + gapHeight;

        // Agregar los tubos a la lista
        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }

    // Método que maneja el movimiento de los elementos en el juego
    public void move() {
        if (!gameOver) {
            // Movimiento del pájaro
            bird.velocityY += bird.gravity;
            bird.birdY += bird.velocityY;
            bird.birdY = Math.max(bird.birdY, 0); // Evitar que el pájaro salga por la parte superior de la pantalla

            // Movimiento de los tubos
            for (Pipes pipe : pipes) {
                pipe.pipeX += pipe.velocityX;

                // Verificar si el pájaro ha pasado el tubo para aumentar la puntuación
                if (!pipe.passed && bird.birdX > pipe.pipeX + pipe.pipeWidth) {
                    pipe.passed = true;
                    score += 500;  // Aumentar la puntuación
                }

                // Comprobar si hay una colisión entre el pájaro y un tubo
                if (collision(bird, pipe)) {
                    gameOver = true;
                    birdFalling = true;  // Hacer que el pájaro caiga después de la colisión
                }
            }
        } else if (birdFalling) {
            // Si el juego ha terminado, permitir que el pájaro siga cayendo
            bird.velocityY += bird.gravity;
            bird.birdY += bird.velocityY;
        }

        // Cuando el pájaro cae fuera de la pantalla, detener la caída
        if (bird.birdY > boardHeight) {
            birdFalling = false;
        }
    }

    // Método que comprueba si el pájaro colisiona con un tubo
    public boolean collision(Bird a, Pipes b) {
        return a.birdX < b.pipeX + b.pipeWidth &&
               a.birdX + a.birdWidth > b.pipeX &&
               a.birdY < b.pipeY + b.pipeHeight &&
               a.birdY + a.birdHeight > b.pipeY;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.move();  // Mover los elementos del juego
        repaint();  // Volver a dibujar los elementos

        if (gameOver && !birdFalling) {
            placePipesTimer.stop();  // Detener la creación de tubos
            gameLoop.stop();  // Detener el bucle principal del juego
            restartButton.setVisible(true);  // Mostrar el botón de reinicio
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // Si se presiona la barra espaciadora
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameOver) {
                bird.velocityY = -9;  // Hacer que el pájaro salte cuando se presiona la barra espaciadora
            } else if (!birdFalling) {
                resetGame();  // Si el juego terminó, reiniciarlo cuando se presiona la barra espaciadora
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    // Método para reiniciar el juego
    public void resetGame() {
        bird = new Bird(birdImg);  // Restablecer la posición del pájaro
        pipes.clear();  // Limpiar los tubos
        score = 0;  // Restablecer la puntuación
        gameOver = false;  // Restablecer el estado del juego
        birdFalling = false;  // Detener la caída del pájaro

        placePipesTimer.start();  // Iniciar el temporizador de tubos
        gameLoop.start();  // Iniciar el bucle principal del juego

        restartButton.setVisible(false);  // Ocultar el botón de reinicio
    }
}
