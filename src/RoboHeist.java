import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RoboHeist extends JPanel implements ActionListener, MouseListener {
    private Timer timer;
    private int playerX = 100;
    private int playerY = 100;

    private int robotX = 150;
    private int robotY = 100;

    private BufferedImage playerImage;
    private BufferedImage robotImage;

    private JLabel messageLabel; // Message label for displaying messages

    public RoboHeist() {
        try {
            playerImage = ImageIO.read(new File("src/graphics/player.png")); // Load player image
            robotImage = ImageIO.read(new File("src/graphics/robot.png")); // Load robot image
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer = new Timer(1000 / 60, this);
        timer.start();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        playerY -= 5;
                        break;
                    case KeyEvent.VK_DOWN:
                        playerY += 5;
                        break;
                    case KeyEvent.VK_LEFT:
                        playerX -= 5;
                        break;
                    case KeyEvent.VK_RIGHT:
                        playerX += 5;
                        break;
                }
                updateMessage(); // Update message based on new position
            }
        });

        addMouseListener(this);
        setFocusable(true);

        // Set up the message label
        messageLabel = new JLabel("Welcome to the RoboHeist!"); // Default message
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setPreferredSize(new Dimension(800, 30)); // Set preferred size for the message bar
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(playerImage, playerX, playerY, null); // Draw player image
        g.drawImage(robotImage, robotX, robotY, null); // Draw robot image
    }

    public void actionPerformed(ActionEvent e) {
        // Update robot behavior
        if (Math.abs(playerX - robotX) > 50) {
            robotX += (playerX > robotX) ? 4 : -4; // Move towards player
        }
        if (Math.abs(playerY - robotY) > 50) {
            robotY += (playerY > robotY) ? 4 : -4; // Move towards player
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Check if the click is within the robot's bounds
        if (mouseX >= robotX && mouseX <= robotX + robotImage.getWidth() &&
                mouseY >= robotY && mouseY <= robotY + robotImage.getHeight()) {
            interactWithRobot();
        }
    }

    private void interactWithRobot() {
        JOptionPane.showMessageDialog(this, "You interacted with the robot!");
    }

    // Unused mouse listener methods
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void updateMessage() {
        // Update messages based on player position
        if (playerX < 200 && playerY < 200) {
            messageLabel.setText("You're in the starting area.");
        } else if (playerX < 400) {
            messageLabel.setText("You're getting closer to the bank!");
        } else {
            messageLabel.setText("You're in the main heist area!");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RoboHeist");
        RoboHeist game = new RoboHeist();

        frame.add(game, BorderLayout.CENTER);
        frame.add(game.messageLabel, BorderLayout.SOUTH); // Add message label to the frame
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
