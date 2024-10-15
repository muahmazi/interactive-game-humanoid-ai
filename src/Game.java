import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, MouseListener {
    private Timer timer;
    private Player player;
    private Robot robot;
    private Map map;
    private JLabel messageLabel;
    private int viewportX = 0; // Camera X offset
    private int viewportY = 0; // Camera Y offset

    public Game() {
        try {
            // Load images
            BufferedImage playerRightImage = ImageIO.read(new File("src/graphics/playerRight.png"));
            BufferedImage playerLeftImage = ImageIO.read(new File("src/graphics/playerLeft.png"));
            BufferedImage robotImage = ImageIO.read(new File("src/graphics/robot.png"));
            BufferedImage villageImage = ImageIO.read(new File("src/graphics/villaggio.png"));
            BufferedImage bankOutsideImage = ImageIO.read(new File("src/graphics/banca.png"));
            BufferedImage bankInsideImage = ImageIO.read(new File("src/graphics/entrata banca.png"));
            BufferedImage cavouImage = ImageIO.read(new File("src/graphics/caveau.png"));
            BufferedImage victoryImage = ImageIO.read(new File("src/graphics/victory.png"));

            // Initialize game entities
            player = new Player(playerLeftImage, playerRightImage, 400, 300);
            robot = new Robot(robotImage, 320, 530);
            map = new Map(villageImage, bankOutsideImage, bankInsideImage, cavouImage, victoryImage);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Timer for game updates
        timer = new Timer(1000 / 60, this);
        timer.start();

        addMouseListener(this);

        // Key listener for movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int dx = 0, dy = 0;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        dy = -5;
                        break;
                    case KeyEvent.VK_DOWN:
                        dy = 5;
                        break;
                    case KeyEvent.VK_LEFT:
                        dx = -5;
                        player.switchDirection(false);
                        break;
                    case KeyEvent.VK_RIGHT:
                        dx = 5;
                        player.switchDirection(true);
                        break;
                }
                if (!map.checkCollision(player.getBounds(dx, dy))) {
                    player.move(dx, dy);
                    messageLabel.setText("x:" + player.getX() + " | y:" + player.getY());
                } else {
                    messageLabel.setText("Player is intersecting with barriers");
                }
                updateViewport(); // Move the viewport to follow the player
            }
        });

        setFocusable(true);

        messageLabel = new JLabel("Welcome to RoboHeist!");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setPreferredSize(new Dimension(800, 200));
    }

    // Method to update the "camera" view based on player position
    private void updateViewport() {
        int viewWidth = getWidth(); // Width of the game window (viewport)
        int viewHeight = getHeight(); // Height of the game window (viewport)

        int mapWidth = 0; // Initialize to zero, will be set based on the current map
        int mapHeight = 0;

        if (map.getCurrentSectionIndex() == 0 && player.getY() <= 50 && robot.getFollow()) {
            map.switchSection(map.getCurrentSectionIndex() + 1);
            player.setX(800);
            player.setY(600);
            robot.setY(810);
            robot.setX(610);
        } else if (map.getCurrentSectionIndex() == 1 && player.getY() <= 260 && player.getX() > 900
                && player.getX() < 950) {
            map.switchSection(map.getCurrentSectionIndex() + 1);
            player.setX(800);
            player.setY(600);
            robot.setY(810);
            robot.setX(610);
        } else if (map.getCurrentSectionIndex() == 2 && player.getY() <= 200 && player.getX() > 900
                && player.getX() < 970 && robot.getPasswordFound()) {
            map.switchSection(map.getCurrentSectionIndex() + 1);
            player.setX(900);
            player.setY(600);
            robot.setY(910);
            robot.setX(610);
            messageLabel.setText("Interact with the robot one more time to end the game.");
        }
        if (robot.getWon()) {
            map.switchSection(map.getCurrentSectionIndex() + 1);
        }

        // Get the dimensions of the current map image
        switch (map.getCurrentSectionIndex()) {
            case 0:
                mapWidth = map.getVillageImage().getWidth();
                mapHeight = map.getVillageImage().getHeight();
                break;
            case 1:
                mapWidth = map.getBankOutsideImage().getWidth();
                mapHeight = map.getBankOutsideImage().getHeight();
                break;
            case 2:
                mapWidth = map.getBankInsideImage().getWidth();
                mapHeight = map.getBankInsideImage().getHeight();
                break;
            case 3:
                mapWidth = map.getCavouImage().getWidth();
                mapHeight = map.getCavouImage().getHeight();
                break;
        }

        // Center the player in the viewport, if possible
        viewportX = player.getX() - viewWidth / 2;
        viewportY = player.getY() - viewHeight / 2;

        // Clamp the viewport X position so it doesn't go beyond the map
        viewportX = Math.max(0, Math.min(viewportX, mapWidth - viewWidth));
        viewportY = Math.max(0, Math.min(viewportY, mapHeight - viewHeight));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        robot.followPlayer(player); // Robot follows the player
        repaint(); // Redraw the scene
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int windowWidth = getWidth();
        int windowHeight = getHeight();

        // Draw the map first
        map.draw(g2d, viewportX, viewportY, windowWidth, windowHeight);
        player.draw(g2d, viewportX, viewportY, windowWidth, windowHeight);
        robot.draw(g2d, viewportX, viewportY, windowWidth, windowHeight);
    }

    private void interactWithRobot() {
        try {
            robot.interact(map.getCurrentSectionIndex(), messageLabel, map);
            this.paintComponent(getGraphics());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX() + viewportX; // Adjust mouseX for viewportX
        int mouseY = e.getY() + viewportY; // Adjust mouseY for viewportY

        // Check if the click is inside the robot's bounds
        if (mouseX >= robot.getX() && mouseX <= robot.getX() + robot.getImage().getWidth() &&
                mouseY >= robot.getY() && mouseY <= robot.getY() + robot.getImage().getHeight()) {
            interactWithRobot();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public JLabel getMessageLabel() {
        return messageLabel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RoboHeist");

        Game game = new Game(); // Game panel containing the map and player

        // Use BorderLayout for the frame
        frame.setLayout(new BorderLayout());

        // Add the game panel to the center
        frame.add(game, BorderLayout.CENTER);

        // Ensure messageLabel is always at the bottom of the screen
        game.getMessageLabel().setPreferredSize(new Dimension(frame.getWidth(), 100)); // Adjust size as needed
        frame.add(game.getMessageLabel(), BorderLayout.SOUTH);

        // Frame settings
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
