import java.awt.*;

public class Barrier {
    private Rectangle rectangle;

    public Barrier(int x, int y, int width, int height) {
        this.rectangle = new Rectangle(x, y, width, height);
    }

    public boolean collidesWith(Rectangle playerBounds) {
        if (playerBounds.intersects(rectangle)) {
            return true;
        }
        return false;

    }

    public void drawDebug(Graphics2D g, int viewportX, int viewportY, Color color) {
        g.setColor(color);
        g.fillRect(rectangle.x - viewportX, rectangle.y - viewportY, rectangle.width, rectangle.height);
    }

    public int getX() {
        return (int) rectangle.getMinX();
    }

    public int getY() {
        return (int) rectangle.getMinY();
    }

}
