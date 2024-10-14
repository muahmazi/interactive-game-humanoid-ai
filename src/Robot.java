import java.awt.*;
import java.awt.image.BufferedImage;

public class Robot implements Drawable {
    private int x;
    private int y;
    private BufferedImage image;

    public Robot(BufferedImage image, int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.image = image;
    }

    public void followPlayer(Player player) {
        if (Math.abs(player.getX() - x) > 50) {
            x += (player.getX() > x) ? 4 : -4;
        }
        if (Math.abs(player.getY() - y) > 50) {
            y += (player.getY() > y) ? 4 : -4;
        }
    }

    public void draw(Graphics2D g, int viewportX, int viewportY) {
        g.drawImage(image, x - viewportX, y - viewportY, image.getWidth() / 10, image.getHeight() / 10, null);
    }
}
