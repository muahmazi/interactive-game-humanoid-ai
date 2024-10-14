import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class RoboHeist extends JPanel implements ActionListener, MouseListener {
    private Timer timer;
    private int playerX = 400; // Start the player at the center of the view
    private int playerY = 300;
    private int robotX = 300;
    private int robotY = 300;

    private BufferedImage playerImage;
    private BufferedImage playerLeftImage;
    private BufferedImage playerRightImage;
    private BufferedImage robotImage;

    // Map images
    private BufferedImage villageImage;
    private BufferedImage bankOutsideImage;
    private BufferedImage bankInsideImage;
    private BufferedImage cavouImage;

    // Current map section
    private String currentSection = "village"; // Could be "village", "bankOutside", "bankInside", or "cavou"

    private boolean movingRight = true; // Tracks the direction of player movement
    private int viewportX = 0; // The current "camera" position
    private int viewportY = 0;

    private JLabel messageLabel;

    // Define barriers (no-go zones) as rectangles (example for water areas)
    private ArrayList<Rectangle> barriers;

    public RoboHeist() {
        try {
            // Load images for player and robot
            playerRightImage = ImageIO.read(new File("src/graphics/playerRight.png"));
            playerLeftImage = ImageIO.read(new File("src/graphics/playerLeft.png"));
            playerImage = playerRightImage; // Start with the player facing right

            robotImage = ImageIO.read(new File("src/graphics/robot.jfif"));

            // Load map images
            villageImage = ImageIO.read(new File("src/graphics/villaggio.png"));
            bankOutsideImage = ImageIO.read(new File("src/graphics/banca.png"));
            bankInsideImage = ImageIO.read(new File("src/graphics/entrata banca.png"));
            cavouImage = ImageIO.read(new File("src/graphics/caveau.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer = new Timer(1000 / 60, this);
        timer.start();

        barriers = new ArrayList<>();
        setupBarriersForVillage(); // Initialize barriers for the current map section

        // Add key listener for movement
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int newX = playerX;
                int newY = playerY;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        newY -= 5;
                        break;
                    case KeyEvent.VK_DOWN:
                        newY += 5;
                        break;
                    case KeyEvent.VK_LEFT:
                        newX -= 5;
                        movingRight = false;
                        playerImage = playerLeftImage; // Switch to left-facing image
                        break;
                    case KeyEvent.VK_RIGHT:
                        newX += 5;
                        movingRight = true;
                        playerImage = playerRightImage; // Switch to right-facing image
                        break;
                }

                // Check if the player can move (avoid barriers)
                if (!isCollidingWithBarrier(newX, newY)) {
                    playerX = newX;
                    playerY = newY;
                    updateMessage();
                } else {
                    messageLabel.setText("Player is intersecting with barriers");
                }

                // Update the "camera" to follow the player
                updateViewport();
                // updateMessage();
            }
        });

        addMouseListener(this);
        setFocusable(true);

        // Message label
        messageLabel = new JLabel("Welcome to RoboHeist!");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setPreferredSize(new Dimension(800, 30));
    }

    private void setupBarriersForVillage() {
        // Example: Add a rectangular barrier (e.g., water) for the village map section
        barriers.clear(); // Clear previous barriers when changing sections

        if (currentSection.equals("village")) {
            barriers.add(new Rectangle(0, 0, 200,1000));
            barriers.add(new Rectangle(20, 580, 2000, 500));
            barriers.add(new Rectangle(0, 0, 800, 100));
            barriers.add(new Rectangle(0, 0, 830, 50));
            barriers.add(new Rectangle(0, 900, 1000, 50)); 
            barriers.add(new Rectangle(0, 950, 1000, 100));
            barriers.add(new Rectangle(0, 400, 450, 100));
            barriers.add(new Rectangle(1150, 530, 450, 50));
            barriers.add(new Rectangle(1350, 430, 450, 100));
            barriers.add(new Rectangle(1520, 0, 50, 1000));
            barriers.add(new Rectangle(1000, 0, 1000, 100));
            barriers.add(new Rectangle(900, 0, 1000, 50));
            // barriers.add(new Rectangle(100, 200, 150, 50));
            // barriers.add(new Rectangle(100, 200, 150, 50));
            // barriers.add(new Rectangle(100, 200, 150, 50));
        }
        // Add more barriers for other sections (bankOutside, bankInside, etc.) as needed
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
    
        // Draw the map section at its original size without stretching
        switch (currentSection) {
            case "village":
                g2d.drawImage(villageImage, -viewportX, -viewportY, villageImage.getWidth(), villageImage.getHeight(), null);
                break;
            case "bankOutside":
                g2d.drawImage(bankOutsideImage, -viewportX, -viewportY, bankOutsideImage.getWidth(), bankOutsideImage.getHeight(), null);
                break;
            case "bankInside":
                g2d.drawImage(bankInsideImage, -viewportX, -viewportY, bankInsideImage.getWidth(), bankInsideImage.getHeight(), null);
                break;
            case "cavou":
                g2d.drawImage(cavouImage, -viewportX, -viewportY, cavouImage.getWidth(), cavouImage.getHeight(), null);
                break;
        }
    
        // Draw the player and robot at their fixed size
        int scaledWidth = playerImage.getWidth() / 10;  // Example scaling factor
        int scaledHeight = playerImage.getHeight() / 10;
        g2d.drawImage(playerImage, playerX - viewportX, playerY - viewportY, scaledWidth, scaledHeight, null);
    
        int robotScaledWidth = robotImage.getWidth() / 10;
        int robotScaledHeight = robotImage.getHeight() / 10;
        g2d.drawImage(robotImage, robotX - viewportX, robotY - viewportY, robotScaledWidth, robotScaledHeight, null);
    
        // Optionally, draw debug barriers
        drawDebugBarriers(g2d);
    }
    
    private void drawDebugBarriers(Graphics2D g2d) {
        // Enable/Disable debug barrier rendering
        boolean debugMode = true; // Set to false to turn off barrier rendering
    
        if (debugMode) {
            g2d.setColor(new Color(255, 0, 0, 128)); // Semi-transparent red
    
            // Loop through all barriers and draw them
            for (Rectangle barrier : barriers) {
                g2d.fillRect(barrier.x - viewportX, barrier.y - viewportY, barrier.width, barrier.height);
            }
        }
    }
    
    

    // Method to update the "camera" view based on player position
    private void updateViewport() {
        // Keep the player at the center of the screen as much as possible
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        viewportX = playerX - viewWidth / 2;
        viewportY = playerY - viewHeight / 2;

        // Clamp viewport to the map boundaries (assuming map size is 1600x1200 for example)
        viewportX = Math.max(0, Math.min(viewportX, 1600 - viewWidth));
        viewportY = Math.max(0, Math.min(viewportY, 1200 - viewHeight));
    }

    // Collision detection method to check if the player is colliding with any barrier
    private boolean isCollidingWithBarrier(int x, int y) {
        Rectangle playerBounds = new Rectangle(x, y, (playerImage.getWidth())/10, (playerImage.getHeight())/10);

        for (Rectangle barrier : barriers) {
            if (playerBounds.intersects(barrier)) {
                return true;
            }
        }
        return false;
    }

    public void actionPerformed(ActionEvent e) {
        if (Math.abs(playerX - robotX) > 50) {
            robotX += (playerX > robotX) ? 4 : -4;
        }
        if (Math.abs(playerY - robotY) > 50) {
            robotY += (playerY > robotY) ? 4 : -4;
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX() + viewportX;
        int mouseY = e.getY() + viewportY;

        // Example: transition from bank outside to inside
        if (currentSection.equals("bankOutside") && mouseX >= 300 && mouseX <= 400 && mouseY >= 200 && mouseY <= 300) {
            currentSection = "bankInside";
            setupBarriersForVillage(); // Update barriers for new section
        }

        // Example: transition from bank inside to cavou
        if (currentSection.equals("bankInside") && mouseX >= 150 && mouseX <= 250 && mouseY >= 100 && mouseY <= 200) {
            currentSection = "cavou";
            setupBarriersForVillage(); // Update barriers for new section
        }
    }

    // Unused MouseListener methods
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    private void updateMessage() {
        // Update messages based on player's location or actions
        messageLabel.setText("x:"+playerX+" | y:"+playerY);

        // if (playerX < 200 && playerY < 200) {
        //     messageLabel.setText("You're in the starting area.");
        // } else if (playerX < 400) {
        //     messageLabel.setText("You're getting closer to the bank!");
        // } else {
        //     messageLabel.setText("You're in the main heist area!");
        // }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RoboHeist");
        RoboHeist game = new RoboHeist();

        frame.add(game, BorderLayout.CENTER);
        frame.add(game.messageLabel, BorderLayout.SOUTH);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
