import java.awt.*;

public interface Drawable {
    void draw(Graphics2D g, int viewportX, int viewportY, int windowWidth, int windowHeight);
}