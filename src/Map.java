import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map implements Drawable {
    private ArrayList<BufferedImage> mapSections; // Array of map images
    private int currentSectionIndex; // Index to track which map section is currently showing
    private ArrayList<Barrier> barriers;

    public static final int VILLAGE = 0;
    public static final int BANK_OUTSIDE = 1;
    public static final int BANK_INSIDE = 2;
    public static final int CAVOU = 3;

    public Map(BufferedImage villageImage, BufferedImage bankOutsideImage, BufferedImage bankInsideImage,
            BufferedImage cavouImage) {
        // Initialize the mapSections list with the four images
        mapSections = new ArrayList<>();
        mapSections.add(villageImage);
        mapSections.add(bankOutsideImage);
        mapSections.add(bankInsideImage);
        mapSections.add(cavouImage);

        currentSectionIndex = VILLAGE; // Start in the village

        barriers = new ArrayList<>();
        setupBarriersForCurrentSection();
    }

    public void setupBarriersForCurrentSection() {
        barriers.clear(); // Clear existing barriers

        // Set up barriers for the current section
        switch (currentSectionIndex) {
            case VILLAGE:
                setupBarriersForVillage();
                break;
            case BANK_OUTSIDE:
                setupBarriersForBankOutside();
                break;
            case BANK_INSIDE:
                setupBarriersForBankInside();
                break;
            case CAVOU:
                setupBarriersForCavou();
                break;
        }
    }

    private void setupBarriersForVillage() {
        // Define barriers for the village section
        barriers.add(new Barrier(0, 0, 200, 1000));
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

    private void setupBarriersForBankOutside() {
        // Define barriers for the bank outside section
        barriers.add(new Barrier(100, 100, 500, 2000));
        barriers.add(new Barrier(100, 0, 2000, 150));
        barriers.add(new Barrier(1200, 100, 500, 2000));
        barriers.add(new Barrier(0, 910, 2000, 50));
        barriers.add(new Barrier(500, 500, 100, 100));
        barriers.add(new Barrier(750, 500, 500, 100));
        // outside
    }

    private void setupBarriersForBankInside() {
        // Define barriers for the bank inside section
        barriers.add(new Barrier(600, 0, 50, 2000));
        barriers.add(new Barrier(1300, 0, 50, 2000));
        barriers.add(new Barrier(0, 170, 2000, 20));
        barriers.add(new Barrier(0, 750, 2000, 20)); // Example barrier for bank inside
    }

    private void setupBarriersForCavou() {
        // Define barriers for the cavou section
        barriers.add(new Barrier(600, 0, 50, 2000));
        barriers.add(new Barrier(1300, 0, 50, 2000));
        barriers.add(new Barrier(0, 170, 2000, 20));
        barriers.add(new Barrier(0, 750, 2000, 20)); // Example barrier for cavou
    }

    public void switchSection(int section) {
        if (section >= 0 && section < mapSections.size()) {
            currentSectionIndex = section;
            setupBarriersForCurrentSection(); // Update barriers for the new section
        }
    }

    public boolean checkCollision(Rectangle playerBounds) {
        for (Barrier barrier : barriers) {
            if (barrier.collidesWith(playerBounds)) {
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics2D g, int viewportX, int viewportY, int windowWidth, int windowHeight) {
        BufferedImage currentMapImage = mapSections.get(currentSectionIndex);

        int x;
        int y;
        if (currentSectionIndex > 1) {
            int imgWidth = currentMapImage.getWidth();
            int imgHeight = currentMapImage.getHeight();

            // Calculate the center position to start drawing the image
            x = (windowWidth - imgWidth) / 2;
            y = (windowHeight - imgHeight) / 2;

        } else {
            x = -viewportX;
            y = -viewportY;
        }

        // Draw the image at the calculated position
        g.drawImage(currentMapImage, x, y, null);

        // Draw barriers for debugging purposes
        for (Barrier barrier : barriers) {
            barrier.drawDebug(g, viewportX, viewportY);
        }
    }

    // Getters for map sections
    public BufferedImage getCurrentSectionImage() {
        return mapSections.get(currentSectionIndex);
    }

    public int getCurrentSectionIndex() {
        return currentSectionIndex;
    }

    public BufferedImage getBankInsideImage() {
        return mapSections.get(BANK_INSIDE);
    }

    public BufferedImage getBankOutsideImage() {
        return mapSections.get(BANK_OUTSIDE);
    }

    public BufferedImage getVillageImage() {
        return mapSections.get(VILLAGE);
    }

    public BufferedImage getCavouImage() {
        return mapSections.get(CAVOU);
    }
}
