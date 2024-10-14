import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map implements Drawable {
    private BufferedImage currentMapImage;
    private ArrayList<Barrier> barriers;

    public Map(BufferedImage villageImage, BufferedImage bankOutsideImage, BufferedImage bankInsideImage) {
        this.currentMapImage = villageImage;
        barriers = new ArrayList<>();
        setupBarriersForVillage();
    }

    public void setupBarriersForVillage() {
        barriers.clear();
        barriers.add(new Barrier(0, 0, 200,1000));
        barriers.add(new Barrier(20, 580, 2000, 500));
        barriers.add(new Barrier(0, 0, 800, 100));
        barriers.add(new Barrier(0, 0, 830, 50));
        barriers.add(new Barrier(0, 900, 1000, 50)); 
        barriers.add(new Barrier(0, 950, 1000, 100));
        barriers.add(new Barrier(0, 400, 450, 100));
        barriers.add(new Barrier(1150, 530, 450, 50));
        barriers.add(new Barrier(1350, 430, 450, 100));
        barriers.add(new Barrier(1520, 0, 50, 1000));
        barriers.add(new Barrier(1000, 0, 1000, 100));
        barriers.add(new Barrier(900, 0, 1000, 50));
    }

    public void switchSection(String section) {
        // Load the appropriate image and barriers based on the section
    }

    public boolean checkCollision(Rectangle playerBounds) {
        for (Barrier barrier : barriers) {
            if (barrier.collidesWith(playerBounds)) {
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics2D g, int viewportX, int viewportY) {
        g.drawImage(currentMapImage, -viewportX, -viewportY, null);
        for (Barrier barrier : barriers) {
            barrier.drawDebug(g, viewportX, viewportY); // Debug rendering of barriers
        }
    }
}

