import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

// main game class which extends JPanel and implements ActionListener and MouseListener interfaces
public class Game extends JPanel implements ActionListener, MouseListener {
    private Timer timer; // timer to control game updates (60 frames per second)
    private Player player; // player character object
    private Robot robot; // robot character object that interacts with the player
    private Map map; // map object that holds game levels and images
    private JLabel messageLabel; // label for displaying messages to the player (e.g., instructions)
    private int viewportX = 0; // camera X offset
    private int viewportY = 0; // camera Y offset

    private BufferedImage policeImage; // image of the police character in the game

    public Game() {
        try {
            // load images
            BufferedImage playerRightImage = ImageIO.read(new File("src/graphics/playerRight.png"));
            BufferedImage playerLeftImage = ImageIO.read(new File("src/graphics/playerLeft.png"));
            BufferedImage robotImage = ImageIO.read(new File("src/graphics/robot.png"));
            BufferedImage villageImage = ImageIO.read(new File("src/graphics/villaggio.png"));
            BufferedImage bankOutsideImage = ImageIO.read(new File("src/graphics/banca.png"));
            BufferedImage bankInsideImage = ImageIO.read(new File("src/graphics/entrata banca.png"));
            BufferedImage cavouImage = ImageIO.read(new File("src/graphics/caveau.png"));
            BufferedImage victoryImage = ImageIO.read(new File("src/graphics/victory.png"));
            policeImage = ImageIO.read(new File("src/graphics/police.png"));

            // initialize game entities
            player = new Player(playerLeftImage, playerRightImage, 400, 300);
            robot = new Robot(robotImage, 320, 530);
            map = new Map(villageImage, bankOutsideImage, bankInsideImage, cavouImage, victoryImage, policeImage);

        } catch (IOException e) {
            e.printStackTrace(); // handle image loading errors
        }

        // timer for game updates (60 frames per second)
        timer = new Timer(1000 / 60, this);
        timer.start(); // start the game loop

        addMouseListener(this); // add mouse listener for interaction

        // key listener for movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int dx = 0, dy = 0; // movement in x and y directions
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        dy = -5; // move up
                        break;
                    case KeyEvent.VK_DOWN:
                        dy = 5; // move down
                        break;
                    case KeyEvent.VK_LEFT:
                        dx = -5; // move left
                        player.switchDirection(false); // player facing left
                        break;
                    case KeyEvent.VK_RIGHT:
                        dx = 5; // move right
                        player.switchDirection(true); // player facing right
                        break;
                    case KeyEvent.VK_SPACE: // space bar pressed
                        // check if the player is near the robot to interact
                        if (robot.getFollow()) {
                            interactWithRobot(); // interact with robot if nearby
                        }
                        break;
                }

                // if no collision detected, move the player
                if (!map.checkCollision(player.getBounds(dx, dy))) {
                    player.move(dx, dy); // move player by dx, dy
                    // messageLabel.setText("x:" + player.getX() + " | y:" + player.getY()); // optional for debugging
                } else {
                    messageLabel.setText("Player is intersecting with barriers"); // display message on collision
                }

                updateViewport(); // move the viewport to follow the player
            }
        });

        setFocusable(true); // make the panel focusable to receive key events

        messageLabel = new JLabel("Welcome to RoboHeist!"); // initial welcome message
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER); // center align the message
        messageLabel.setPreferredSize(new Dimension(800, 200)); // set preferred size for the label
    }

    // method to update the "camera" view based on player position
    private void updateViewport() {
        int viewWidth = getWidth(); // width of the game window (viewport)
        int viewHeight = getHeight(); // height of the game window (viewport)

        int mapWidth = 0; // initialize to zero, will be set based on the current map
        int mapHeight = 0;

        // check if the player reaches specific locations to change the game section
        if (map.getCurrentSectionIndex() == 0 && player.getY() <= 50 && robot.getFollow()) {
            map.switchSection(map.getCurrentSectionIndex() + 1); // switch to the next section of the map
            robot.setY(900); // set new robot position
            robot.setX(850);
            player.setX(900); // set new player position
            player.setY(850);
        } else if (map.getCurrentSectionIndex() == 1 && player.getY() <= 260 && player.getX() > 900
                && player.getX() < 950 && !map.getPolice()) {
            map.switchSection(map.getCurrentSectionIndex() + 1); // advance to the next section
            robot.setY(710); // set robot's new position in the map
            robot.setX(610);
            player.setX(800); // set player's new position
            player.setY(600);
        } else if (map.getCurrentSectionIndex() == 2 && player.getY() <= 200 && player.getX() > 900
                && player.getX() < 970 && robot.getPasswordFound()) {
            map.switchSection(map.getCurrentSectionIndex() + 1); // move to the vault section
            robot.setY(910); // set robot position in the new section
            robot.setX(610);
            player.setX(900); // move player to the next area
            player.setY(600);
            messageLabel.setText("Interact with the robot one more time to end the game."); // prompt player to interact
        }

        // check if the robot has won to switch to victory screen
        if (robot.getWon()) {
            map.switchSection(map.getCurrentSectionIndex() + 1); // move to the victory screen
        }

        // get the dimensions of the current map image
        switch (map.getCurrentSectionIndex()) {
            case 0:
                mapWidth = map.getVillageImage().getWidth(); // get width of the village map
                mapHeight = map.getVillageImage().getHeight(); // get height of the village map
                break;
            case 1:
                mapWidth = map.getBankOutsideImage().getWidth(); // get width of the bank exterior map
                mapHeight = map.getBankOutsideImage().getHeight(); // get height of the bank exterior map
                break;
            case 2:
                mapWidth = map.getBankInsideImage().getWidth(); // get width of the bank interior map
                mapHeight = map.getBankInsideImage().getHeight(); // get height of the bank interior map
                break;
            case 3:
                mapWidth = map.getCavouImage().getWidth(); // get width of the vault map
                mapHeight = map.getCavouImage().getHeight(); // get height of the vault map
                break;
        }

        // center the player in the viewport, if possible
        viewportX = player.getX() - viewWidth / 2;
        viewportY = player.getY() - viewHeight / 2;

        // clamp the viewport X position so it doesn't go beyond the map boundaries
        viewportX = Math.max(0, Math.min(viewportX, mapWidth - viewWidth));
        viewportY = Math.max(0, Math.min(viewportY, mapHeight - viewHeight));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        robot.followPlayer(player); // robot follows the player during the game
        repaint(); // redraw the scene (refresh game graphics)
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // call the parent paint method

        // check if the player has won the game
        if (robot.getWon()) {
            // clear the screen and show the "YOU WON" message
            clearScreen(g); // optional: a method to clear the screen if needed
            drawWinMessage(g); // display the win message
        } else {
            // regular game drawing logic
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK); // set the background color to black
            g2d.fillRect(0, 0, getWidth(), getHeight()); // fill the background

            int windowWidth = getWidth();
            int windowHeight = getHeight();

            // draw the map and other game objects if the game is not won
            map.draw(g2d, viewportX, viewportY, windowWidth, windowHeight); // draw map
            player.draw(g2d, viewportX, viewportY, windowWidth, windowHeight); // draw player
            robot.draw(g2d, viewportX, viewportY, windowWidth, windowHeight); // draw robot
        }
    }

    // optional method to clear the screen (set everything to black or another color)
    private void clearScreen(Graphics g) {
        g.setColor(Color.BLACK); // set the screen to black
        g.fillRect(0, 0, getWidth(), getHeight()); // fill the whole screen with black
    }

    // method to draw the "YOU WON" message
    private void drawWinMessage(Graphics g) {
        g.setColor(Color.WHITE); // set the color for the message to white
        g.setFont(new Font("Arial", Font.BOLD, 50)); // set font size and style
        FontMetrics metrics = g.getFontMetrics(g.getFont()); // measure the text for centering

        String message = "YOU WON!"; // message to display

        // center the message on the screen
        int x = (getWidth() - metrics.stringWidth(message)) / 2;
        int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

        g.drawString(message, x, y); // draw the message on the screen
        messageLabel.setText("GGs, see how helpful an AI robot could be? "); // update label message
    }

    // method to handle interaction between player and robot
    private void interactWithRobot() {
        try {
            robot.interact(map.getCurrentSectionIndex(), messageLabel, map); // robot interaction logic
            this.paintComponent(getGraphics()); // repaint the screen after interaction
        } catch (Exception e) {
            e.printStackTrace(); // handle interaction errors
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX() + viewportX; // adjust mouseX for viewportX
        int mouseY = e.getY() + viewportY; // adjust mouseY for viewportY

        // check if the click is inside the robot's bounds
        if (mouseX >= robot.getX() && mouseX <= robot.getX() + robot.getImage().getWidth() &&
                mouseY >= robot.getY() && mouseY <= robot.getY() + robot.getImage().getHeight()) {
            interactWithRobot(); // trigger robot interaction if clicked
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // no implementation needed for mouse entered event
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // no implementation needed for mouse exited event
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // no implementation needed for mouse pressed event
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // no implementation needed for mouse released event
    }

    public JLabel getMessageLabel() {
        return messageLabel; // return the message label
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RoboHeist"); // create a new game window

        Game game = new Game(); // game panel containing the map and player

        // use BorderLayout for the frame layout
        frame.setLayout(new BorderLayout());

        // add the game panel to the center
        frame.add(game, BorderLayout.CENTER);

        // ensure messageLabel is always at the bottom of the screen
        game.getMessageLabel().setPreferredSize(new Dimension(frame.getWidth(), 100)); // adjust size as needed
        frame.add(game.getMessageLabel(), BorderLayout.SOUTH); // add the message label to the bottom

        // frame settings
        frame.setSize(800, 600); // set frame size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close the program on exit
        frame.setVisible(true); // make the window visible

        GameMusic gameMusic = new GameMusic(); // create a new GameMusic object
        gameMusic.loadMusic("src/music/music-first.wav"); // load the background music
        gameMusic.playMusic(); // play the loaded music
    }
}
