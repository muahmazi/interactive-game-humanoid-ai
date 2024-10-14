import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Game extends JPanel implements ActionListener {
    private Timer timer;
    private Player player;
    private Robot robot;
    private Map map;
    private JLabel messageLabel;
    private int viewportX = 0;  // Camera X offset
    private int viewportY = 0;  // Camera Y offset

    public Game() {
        try {
            // Load images
            BufferedImage playerRightImage = ImageIO.read(new File("src/graphics/playerRight.png"));
            BufferedImage playerLeftImage = ImageIO.read(new File("src/graphics/playerLeft.png"));
            BufferedImage robotImage = ImageIO.read(new File("src/graphics/robot.jfif"));
            BufferedImage villageImage = ImageIO.read(new File("src/graphics/villaggio.png"));
            BufferedImage bankOutsideImage = ImageIO.read(new File("src/graphics/banca.png"));
            BufferedImage bankInsideImage = ImageIO.read(new File("src/graphics/entrata banca.png"));

            // Initialize game entities
            player = new Player(playerLeftImage, playerRightImage, 400, 300);
            robot = new Robot(robotImage, 300, 300);
            map = new Map(villageImage, bankOutsideImage, bankInsideImage);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Timer for game updates
        timer = new Timer(1000 / 60, this);
        timer.start();

        // Key listener for movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int dx = 0, dy = 0;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: dy = -5; break;
                    case KeyEvent.VK_DOWN: dy = 5; break;
                    case KeyEvent.VK_LEFT: dx = -5; player.switchDirection(false); break;
                    case KeyEvent.VK_RIGHT: dx = 5; player.switchDirection(true); break;
                }
                if (!map.checkCollision(player.getBounds())) {
                    player.move(dx, dy);
                } else {
                    messageLabel.setText("Player is intersecting with barriers");
                }
                updateViewport();  // Move the viewport to follow the player
            }
        });

        setFocusable(true);

        messageLabel = new JLabel("Welcome to RoboHeist!");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setPreferredSize(new Dimension(800, 30));
    }

    // Update the viewport based on player's position
    private void updateViewport() {
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        viewportX = player.getX() - viewWidth / 2;
        viewportY = player.getY() - viewHeight / 2;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        robot.followPlayer(player);  // Robot follows the player
        repaint();  // Redraw the scene
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw map and game objects
        map.draw(g2d, viewportX, viewportY);  // Map is drawn first (background)
        player.draw(g2d, viewportX, viewportY);
        robot.draw(g2d, viewportX, viewportY);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RoboHeist");
        Game game = new Game();
        frame.add(game);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
