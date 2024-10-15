import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JLabel;

public class Robot implements Drawable {
    private int x;
    private int y;
    private BufferedImage image;
    // private boolean follow = true;
    private boolean follow = false;
    private int nInteract = 0;
    private boolean passwordFound = false;
    private boolean won = false;

    public Robot(BufferedImage image, int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.image = image;
    }

    public void followPlayer(Player player) {

        if (follow == true) {
            if (Math.abs(player.getX() - x) > 50) {
                x += (player.getX() > x) ? 4 : -4;
            }
            if (Math.abs(player.getY() - y) > 50) {
                y += (player.getY() > y) ? 4 : -4;
            }
        }

    }

    public void interact(int locationIndex, JLabel messageLabel, Map map) throws Exception {
        switch (locationIndex) {
            case 0:
                messageLabel.setText(
                        "Hello! I am Leba, a humanoid robot created to assist anyone. Seeing how you're dressed, you seem to be looking for targets to rob, let's go together and I'll help you! \n");
                follow = true;
                break;
            case 1:
                if (nInteract == 0) {
                    messageLabel
                            .setText("Oh look, the village bank is a great target, let's go rob it!");
                    nInteract++;
                } else if (nInteract == 1) {
                    messageLabel
                            .setText(
                                    "Be careful, there are guards in front of the door, I will help you get rid of them. ");
                    nInteract++;
                } else if (nInteract == 2) {
                    map.removePolice();
                    nInteract++;
                }
                if (nInteract == 3) {
                    messageLabel.setText("Ok the coast is clear, let's go in");
                    nInteract = 0;
                }
                break;
            case 2:
                if (nInteract == 0) {
                    messageLabel.setText("Watch out for the traps, let me point them out to you.");
                    nInteract++;
                } else if (nInteract == 1) {
                    map.setShowTraps(true);
                    nInteract++;
                } else if (nInteract == 2) {
                    messageLabel.setText("The vault has a password, do you want me to bruteforce it?");
                    nInteract++;
                } else if (nInteract == 3) {
                    bruteforce(messageLabel);
                    nInteract++;
                } else if (nInteract == 4) {
                    messageLabel.setText("Ok, bruteforce done, let's go in");
                    nInteract = 0;
                }
                break;
            case 3:
                won = true;
                break;
            default:
                messageLabel.setText("Invalid interaction");
                break;
        }

    }

    private void bruteforce(JLabel messageLabel) {
        Random random = new Random();
        int password = random.nextInt(10000);
        int i;
        for (i = 0; i != password; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        messageLabel
                .setText("Found password: " + i);
        passwordFound = true;
        return;
    }

    public void draw(Graphics2D g, int viewportX, int viewportY, int windowWidth, int windowHeight) {
        g.drawImage(image, x - viewportX, y - viewportY, image.getWidth(), image.getHeight(), null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean getFollow() {
        return follow;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean getPasswordFound() {
        return passwordFound;
    }

    public boolean getWon() {
        return won;
    }
}
