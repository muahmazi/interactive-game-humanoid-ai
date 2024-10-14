import java.awt.*;
import java.awt.image.BufferedImage;

public class Player implements Drawable {
    private int x;
    private int y;
    private BufferedImage image;
    private BufferedImage leftImage;
    private BufferedImage rightImage;
    private boolean movingRight;

    public Player(BufferedImage leftImage, BufferedImage rightImage, int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.leftImage = leftImage;
        this.rightImage = rightImage;
        this.image = rightImage; // Start facing right
        this.movingRight = true;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void switchDirection(boolean right) {
        this.movingRight = right;
        this.image = right ? rightImage : leftImage;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth() / 10, image.getHeight() / 10);
    }

    public void draw(Graphics2D g, int viewportX, int viewportY) {
        g.drawImage(image, x - viewportX, y - viewportY, image.getWidth() / 10, image.getHeight() / 10, null);
    }

    // Getter and Setter methods for x and y
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
