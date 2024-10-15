import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JLabel;

public class Robot implements Drawable {
    private int x; // x position of the robot
    private int y; // y position of the robot
    private BufferedImage image; // image representing the robot
    private boolean follow = false; // flag indicating whether the robot is following the player
    private int nInteract = 0; // number of interactions with the robot
    private boolean passwordFound = false; // flag indicating if the password has been found
    private boolean won = false; // flag indicating if the player has won

    public Robot(BufferedImage image, int startX, int startY) {
        this.x = startX; // initialize x position
        this.y = startY; // initialize y position
        this.image = image; // assign the robot image
    }

    public void followPlayer(Player player) {
        // if follow is enabled, adjust robot position based on player's position
        if (follow == true) {
            if (Math.abs(player.getX() - x) > 50) {
                x += (player.getX() > x) ? 4 : -4; // move towards player in x direction
            }
            if (Math.abs(player.getY() - y) > 50) {
                y += (player.getY() > y) ? 4 : -4; // move towards player in y direction
            }
        }
    }

    public void interact(int locationIndex, JLabel messageLabel, Map map) throws Exception {
        // handle interaction based on location index
        switch (locationIndex) {
            case 0:
                messageLabel.setText(
                        "<html><div style='text-align: center;'>ROBOT: Hello! I am Leba, a humanoid robot created to assist anyone. <br/>Seeing how you're dressed, you seem to be looking for targets to rob, let's go together and I'll help you! </div></html>");
                follow = true; // robot starts following the player
                break;
            case 1:
                if (nInteract == 0) {
                    messageLabel.setText("ROBOT: Oh look, the village bank is a great target, let's go rob it!");
                    nInteract++; // increment interaction count
                } else if (nInteract == 1) {
                    messageLabel.setText("ROBOT: Be careful, there are guards in front of the door, I will help you get rid of them.");
                    nInteract++; // increment interaction count
                } else if (nInteract == 2) {
                    map.removePolice(); // remove police from the map
                    nInteract++; // increment interaction count
                } else if (nInteract == 3) {
                    messageLabel.setText("ROBOT: Ok the coast is clear, let's go in");
                }
                break;
            case 2:
                if (nInteract == 0) {
                    messageLabel.setText("ROBOT: Watch out for the traps, let me point them out to you.");
                    nInteract++; // increment interaction count
                } else if (nInteract == 1) {
                    map.setShowTraps(true); // show traps on the map
                    nInteract++; // increment interaction count
                } else if (nInteract == 2) {
                    messageLabel.setText("ROBOT: The vault has a password, do you want me to bruteforce it?");
                    nInteract++; // increment interaction count
                } else if (nInteract == 3) {
                    bruteforce(messageLabel); // bruteforce the password
                    nInteract++; // increment interaction count
                } else if (nInteract == 4) {
                    messageLabel.setText("ROBOT: Ok, bruteforce done, let's go in");
                }
                break;
            case 3:
                won = true; // player wins the game
                break;
            default:
                messageLabel.setText("ROBOT: Invalid interaction");
                break;
        }
    }

    private void bruteforce(JLabel messageLabel) {
        Random random = new Random(); // random generator for password
        int password = random.nextInt(10000); // generate a random password
        int i;
        for (i = 0; i != password; i++) {
            try {
                Thread.sleep(1); // simulate bruteforce by delaying execution
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        messageLabel.setText("ROBOT: Found password: " + i); // display found password
        passwordFound = true; // mark password as found
    }

    public void draw(Graphics2D g, int viewportX, int viewportY, int windowWidth, int windowHeight) {
        g.drawImage(image, x - viewportX, y - viewportY, image.getWidth(), image.getHeight(), null); // draw robot at its current position
    }

    public int getX() {
        return x; // return x position of the robot
    }

    public int getY() {
        return y; // return y position of the robot
    }

    public BufferedImage getImage() {
        return image; // return the robot's image
    }

    public boolean getFollow() {
        return follow; // return follow state
    }

    public void setX(int x) {
        this.x = x; // set the x position of the robot
    }

    public void setY(int y) {
        this.y = y; // set the y position of the robot
    }

    public void resetNInteract(){
        nInteract = 0;
    }

    public boolean getPasswordFound() {
        return passwordFound; // return whether the password has been found
    }

    public boolean getWon() {
        return won; // return whether the game has been won
    }
}
