import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener {

    private int boardWidth, boardHeight, tileSize = 25;
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    Tile food;
    Random random;
    Timer gameLoop;
    private int velocityX, velocityY;
    private boolean isGameOver = false;

    public SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);

        addKeyListener(new AL());
        setFocusable(true);

        random = new Random();

        snakeHead = new Tile(1, 1);
        snakeBody = new ArrayList<Tile>();
        randomStart();

        food = new Tile(1, 1);
        placeFood();

        velocityX = 0;
        velocityX = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        g.setColor(Color.RED);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        g.setColor(Color.GREEN);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (isGameOver) {
            g.setColor(Color.BLACK);
            setBackground(Color.RED);
            g.setFont(new Font("Arial", Font.PLAIN, 64));
            g.drawString("Game Over!", boardWidth / 2 - 176, boardHeight / 2 - 48);
            g.setFont(new Font("Arial", Font.PLAIN, 42));
            g.drawString("Score: " + String.valueOf(snakeBody.size()), boardWidth / 2 - 76, boardHeight / 2 + 16);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Press SPACE to restart", boardWidth / 2 - 128,
                    boardHeight - tileSize);
        } else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), boardWidth / 2 - 32, tileSize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    public void randomStart() {
        snakeHead.x = random.nextInt(boardWidth / tileSize);
        snakeHead.y = random.nextInt(boardHeight / tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile previousSnakePart = snakeBody.get(i - 1);
                snakePart.x = previousSnakePart.x;
                snakePart.y = previousSnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {
                isGameOver = true;
            }
        }

        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth - tileSize
                || snakeHead.y * tileSize < 0
                || snakeHead.y * tileSize > boardHeight - tileSize) {
            isGameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (isGameOver) {
            gameLoop.stop();
        }
    }

    public class AL extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_UP && velocityY != 1) {
                velocityX = 0;
                velocityY = -1;
            } else if (key == KeyEvent.VK_DOWN && velocityY != -1) {
                velocityX = 0;
                velocityY = 1;
            } else if (key == KeyEvent.VK_LEFT && velocityX != 1) {
                velocityX = -1;
                velocityY = 0;
            } else if (key == KeyEvent.VK_RIGHT && velocityX != -1) {
                velocityX = 1;
                velocityY = 0;
            } else if (key == KeyEvent.VK_W && velocityY != 1) {
                velocityX = 0;
                velocityY = -1;
            } else if (key == KeyEvent.VK_S && velocityY != -1) {
                velocityX = 0;
                velocityY = 1;
            } else if (key == KeyEvent.VK_A && velocityX != 1) {
                velocityX = -1;
                velocityY = 0;
            } else if (key == KeyEvent.VK_D && velocityX != -1) {
                velocityX = 1;
                velocityY = 0;
            }

            if (key == KeyEvent.VK_SPACE && isGameOver) {
                snakeHead = new Tile(5, 5);
                snakeBody = new ArrayList<Tile>();
                velocityX = 0;
                velocityY = 0;
                isGameOver = false;
                setBackground(Color.BLACK);
                randomStart();
                placeFood();
                gameLoop.start();
            }
        }
    }
}