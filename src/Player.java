import java.awt.*;
import java.awt.image.BufferedImage;

public class Player implements Drawable {
    private int x; // x coordinate of the player
    private int y; // y coordinate of the player
    private BufferedImage image; // current image representing the player
    private BufferedImage leftImage; // image when the player is facing left
    private BufferedImage rightImage; // image when the player is facing right

    public Player(BufferedImage leftImage, BufferedImage rightImage, int startX, int startY) {
        this.x = startX; // set the initial x position
        this.y = startY; // set the initial y position
        this.leftImage = leftImage; // set the left-facing image
        this.rightImage = rightImage; // set the right-facing image
        this.image = rightImage; // start facing right
    }

    public void move(int dx, int dy) {
        x += dx; // update the x position by dx
        y += dy; // update the y position by dy
    }

    public void switchDirection(boolean right) {
        this.image = right ? rightImage : leftImage; // switch image based on the direction
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth() / 10, image.getHeight() / 10); // get the player's bounding box
    }

    public Rectangle getBounds(int dx, int dy) {
        int width = image.getWidth() / 10; // scale the player's bounding box width
        int height = image.getHeight() / 10; // scale the player's bounding box height
        return new Rectangle(x + dx, y + dy, width, height); // return new bounds after moving
    }

    public void draw(Graphics2D g, int viewportX, int viewportY, int windowWidth, int windowHeight) {
        g.drawImage(image, x - viewportX, y - viewportY, image.getWidth() / 10, image.getHeight() / 10, null); // draw the player image
    }

    // getter and setter methods for x and y
    public int getX() {
        return x; // return the x position
    }

    public int getY() {
        return y; // return the y position
    }

    public void setX(int x) {
        this.x = x; // set the x position
    }

    public void setY(int y) {
        this.y = y; // set the y position
    }
}
