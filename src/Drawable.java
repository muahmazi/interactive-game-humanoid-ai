import java.awt.*;

public interface Drawable {
    // this does not need many comments i guess
    void draw(Graphics2D g, int viewportX, int viewportY, int windowWidth, int windowHeight);
}