import java.awt.*;

public class Barrier {
    private Rectangle rectangle; // the rectangle representing the barrier's position and size

    public Barrier(int x, int y, int width, int height) {
        this.rectangle = new Rectangle(x, y, width, height); // create a new rectangle with given dimensions
    }

    public boolean collidesWith(Rectangle playerBounds) {
        // check if the player's bounds intersect with the barrier
        if (playerBounds.intersects(rectangle)) {
            return true; // collision detected
        }
        return false; // no collision
    }

    public void drawDebug(Graphics2D g, int viewportX, int viewportY, Color color) {
        g.setColor(color); // set color for debugging
        // draw the barrier as a filled rectangle, adjusting for viewport position
        g.fillRect(rectangle.x - viewportX, rectangle.y - viewportY, rectangle.width, rectangle.height);
    }

    public int getX() {
        return (int) rectangle.getMinX(); // return the x-coordinate of the barrier
    }

    public int getY() {
        return (int) rectangle.getMinY(); // return the y-coordinate of the barrier
    }
}
