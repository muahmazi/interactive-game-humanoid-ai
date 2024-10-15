import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Map implements Drawable {
    private ArrayList<BufferedImage> mapSections; // array of map images
    private int currentSectionIndex; // index to track which map section is currently showing
    private ArrayList<Barrier> barriers;
    private ArrayList<Barrier> traps;
    private boolean showTraps = false; // flag to show traps

    private BufferedImage policeImage;
    private boolean policePresent = true; // flag to check if police is present
    private boolean explosionDone = false; // flag to ensure explosion happens once

    public static final int VILLAGE = 0;
    public static final int BANK_OUTSIDE = 1;
    public static final int BANK_INSIDE = 2;
    public static final int CAVOU = 3;
    public static final int END = 4;

    // constructor to initialize the map images and setup the first section
    public Map(BufferedImage villageImage, BufferedImage bankOutsideImage, BufferedImage bankInsideImage,
            BufferedImage cavouImage, BufferedImage victoryImage, BufferedImage policeImage) {

        this.policeImage = policeImage;

        // initialize the mapSections list with the four images
        mapSections = new ArrayList<>();
        mapSections.add(villageImage);
        mapSections.add(bankOutsideImage);
        mapSections.add(bankInsideImage);
        mapSections.add(cavouImage);
        mapSections.add(victoryImage);

        currentSectionIndex = VILLAGE; // start in the village

        barriers = new ArrayList<>();
        traps = new ArrayList<>();
        setupBarriersForCurrentSection(); // setup barriers for the initial section
    }

    // method to setup barriers for the current map section
    public void setupBarriersForCurrentSection() {
        barriers.clear(); // clear existing barriers
        traps.clear(); // clear traps

        // set up barriers based on the current section
        switch (currentSectionIndex) {
            case VILLAGE:
                setupBarriersForVillage();
                break;
            case BANK_OUTSIDE:
                setupBarriersForBankOutside();
                break;
            case BANK_INSIDE:
                setupBarriersForBankInside();
                setupTrapBarriers(); // add traps in the bank inside section
                break;
            case CAVOU:
                setupBarriersForCavou();
                break;
        }
    }

    // method to define barriers for the village section
    private void setupBarriersForVillage() {
        barriers.add(new Barrier(0, 0, 200, 1000));
        barriers.add(new Barrier(0, 0, 2000, 1));
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

    // method to define barriers for the bank outside section
    private void setupBarriersForBankOutside() {
        barriers.add(new Barrier(100, 100, 500, 2000));
        barriers.add(new Barrier(100, 0, 2000, 150));
        barriers.add(new Barrier(1200, 100, 500, 2000));
        barriers.add(new Barrier(0, 910, 2000, 50));
        barriers.add(new Barrier(500, 500, 100, 100));
        barriers.add(new Barrier(750, 500, 500, 100));
    }

    // method to define barriers for the bank inside section
    private void setupBarriersForBankInside() {
        barriers.add(new Barrier(600, 0, 50, 2000));
        barriers.add(new Barrier(0, 0, 2000, 180));
        barriers.add(new Barrier(1300, 0, 50, 2000));
        barriers.add(new Barrier(0, 170, 2000, 20));
        barriers.add(new Barrier(0, 750, 2000, 20));
    }

    // method to define barriers for the cavou section
    private void setupBarriersForCavou() {
        barriers.add(new Barrier(600, 0, 50, 2000));
        barriers.add(new Barrier(1300, 0, 50, 2000));
        barriers.add(new Barrier(0, 170, 2000, 20));
        barriers.add(new Barrier(0, 750, 2000, 20));
    }

    // method to define trap barriers for the bank inside section
    private void setupTrapBarriers() {
        traps.add(new Barrier(710, 610, 50, 50));
        traps.add(new Barrier(760, 660, 50, 50));
        traps.add(new Barrier(760, 510, 50, 50));
        traps.add(new Barrier(760, 410, 50, 50));
        traps.add(new Barrier(710, 310, 50, 50));
        traps.add(new Barrier(860, 510, 50, 50));
        traps.add(new Barrier(1060, 510, 50, 50));
        traps.add(new Barrier(1110, 410, 50, 50));
        traps.add(new Barrier(1060, 310, 50, 50));
        traps.add(new Barrier(1110, 610, 50, 50));
        traps.add(new Barrier(1210, 560, 50, 50));
        traps.add(new Barrier(960, 360, 50, 50));
        traps.add(new Barrier(810, 260, 50, 50));
        System.out.println("Number of traps: " + traps.size()); // debug output
    }

    // method to switch between map sections
    public void switchSection(int section) {
        if (section >= 0 && section < mapSections.size()) {
            currentSectionIndex = section;
            setupBarriersForCurrentSection(); // update barriers for the new section
        } else {
            System.out.println("debug"); // debug output for invalid section
        }
    }

    // method to check collision with barriers or traps
    public boolean checkCollision(Rectangle playerBounds) {
        // check collision with traps if in the bank inside section
        if (currentSectionIndex == 2) {
            for (Barrier trap : traps) {
                if (trap.collidesWith(playerBounds)) {
                    return true;
                }
            }
        }

        // check collision with regular barriers
        for (Barrier barrier : barriers) {
            if (barrier.collidesWith(playerBounds)) {
                return true;
            }
        }
        return false; // no collision detected
    }

    // method to draw the current map section, barriers, and other elements
    public void draw(Graphics2D g, int viewportX, int viewportY, int windowWidth, int windowHeight) {
        BufferedImage currentMapImage = mapSections.get(currentSectionIndex);

        int x;
        int y;
        if (currentSectionIndex > 1) {
            int imgWidth = currentMapImage.getWidth();
            int imgHeight = currentMapImage.getHeight();

            // center position to start drawing the image
            x = (windowWidth - imgWidth) / 2;
            y = (windowHeight - imgHeight) / 2;
        } else {
            x = -viewportX;
            y = -viewportY;
        }

        // draw the current map image
        g.drawImage(currentMapImage, x, y, null);

        // draw police or explosion if in the bank outside section
        if (currentSectionIndex == BANK_OUTSIDE && policePresent) {
            g.drawImage(policeImage, 890 - viewportX, 260 - viewportY, null);
            g.drawImage(policeImage, 940 - viewportX, 260 - viewportY, null);
        } else if (currentSectionIndex == BANK_OUTSIDE && !policePresent && !explosionDone) {
            BufferedImage expolosionImage = null;
            try {
                expolosionImage = ImageIO.read(new File("src/graphics/explosion.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            g.drawImage(expolosionImage, 940 - viewportX, 260 - viewportY, null);
            g.drawImage(expolosionImage, 940 - viewportX, 260 - viewportY, null);
            try {
                Thread.sleep(1000); // pause for 1 second to simulate explosion effect
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            explosionDone = true; // ensure explosion only happens once
        }

        // draw traps if showTraps is enabled
        if (showTraps) {
            for (Barrier trap : traps) {
                Color color = new Color(0, 255, 0, 120); // fully opaque green for traps
                trap.drawDebug(g, viewportX, viewportY, color); // draw traps
            }
        }
    }

    // getters for various map sections and attributes
    public BufferedImage getCurrentSectionImage() {
        return mapSections.get(currentSectionIndex);
    }

    public void removePolice() {
        this.policePresent = false; // remove police from the map
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

    public ArrayList<Barrier> getTraps() {
        return traps; // return the traps list
    }

    public void setShowTraps(boolean b) {
        showTraps = b; // set the flag to show or hide traps
    }

    public boolean getPolice() {
        return policePresent; // check if police are present
    }
}
