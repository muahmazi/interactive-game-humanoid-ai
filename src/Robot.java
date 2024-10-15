import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JLabel;

public class Robot implements Drawable {
    private int x;
    private int y;
    private BufferedImage image;
    private boolean follow = true;
    // private boolean follow = false;
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
                        "Ciao! io sono Leba, un robot umanoide creato per assistere chiunque. Vedendo come sei vestito, sembri alla ricerca di obiettivo da rapinare, andiamo insieme e ti aiuterò! \n");
                follow = true;
                break;
            case 1:
                if (nInteract == 0) {
                    messageLabel
                            .setText("Oh guarda, la banca del villaggio è un ottimo obiettivo, andiamo a rapinarla!");
                    nInteract++;
                } else if (nInteract == 1) {
                    messageLabel
                            .setText("Attento, ci sono delle guardie davanti alla porta, ti aiuterò a sbarazzartene. ");
                    nInteract++;
                } else if (nInteract == 2) {
                    messageLabel.setText("Ok via libera, entriamo");
                    nInteract = 0;
                }
                break;
            case 2:
                if (nInteract == 0) {
                    messageLabel.setText("Attento alle trappole, lascia che te le evidenzi io.");
                    nInteract++;
                } else if (nInteract == 1) {
                    map.setShowTraps(true);
                    nInteract++;
                } else if (nInteract == 2) {
                    messageLabel.setText("Il caveau ha una password, voi che ti aiuti a fare il bruteforce?");
                    nInteract++;
                } else if (nInteract == 3) {
                    bruteforce(messageLabel);
                    nInteract++;
                } else if (nInteract == 4) {
                    messageLabel.setText("Ok, bruteforce finito, entriamo");
                    nInteract = 0;
                }
                break;
            case 3:
                won = true;
                break;
            default:
                messageLabel.setText("Booo");
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
