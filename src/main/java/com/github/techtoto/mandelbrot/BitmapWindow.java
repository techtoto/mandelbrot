package com.github.techtoto.mandelbrot;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class implements a window to show a picture.
 * Code and concept adapted from "Uebung 04" from "Allgemeine Informatik I" at TU Darmstadt.
 *
 * @author Florian Brandherm, Tobias R.
 * @version 2021-04-12
 */
public class BitmapWindow extends JPanel {

    private final BufferedImage img;
    private final Logger logger;

    /**
     * The Constructor creates an image from a given bitmap.
     *
     * @param bitmap  A 2D-Array of pixels
     * @param colored If true, the picture is created in pseudocolor. Otherwise the picture is in grayscale.
     */
    public BitmapWindow(int[][] bitmap, boolean colored) {
        logger = LoggerFactory.getLogger(BitmapWindow.class);
        int bmp_width = bitmap.length;
        int bmp_height = bitmap[0].length;
        img = new BufferedImage(bmp_width, bmp_height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < bmp_width; ++x) {
            for (int y = 0; y < bmp_height; ++y) {
                if (bitmap[x][y] == 255) {
                    img.setRGB(x, y, 0x000000);
                } else {
                    float value = bitmap[x][y] / 254.0f;
                    int col;
                    if (colored) {
                        col = Color.getHSBColor((float) Math.sqrt(value) + 0.5f, 1.0f, 1.0f).getRGB();
                    } else {
                        col = Color.getHSBColor(1.0f, 0.0f, value).getRGB();
                    }
                    img.setRGB(x, y, col);
                }
            }
        }
    }

    /**
     * Opens a window to show the created image.
     */
    public void showPlot() {
        JFrame frame = new JFrame("Mandelbrot set");
        frame.getContentPane().setBackground(Color.black);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        frame.add(this);
        this.setPreferredSize(new Dimension(this.img.getWidth(), this.img.getHeight()));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Re-draws the image if the window is re-created.
     * This method is called automatically by the window manager.
     * I actually don't know if I need this
     */
    @Override
    public void paint(@NotNull Graphics g) {
        g.drawImage(this.img, 0, 0, this.img.getWidth(), this.img.getHeight(), null);
    }

    /**
     * Saves the created image to the given filename.
     *
     * @param filename The name of the file where the picture is to be saved.
     */
    public void saveImage(@NotNull String filename) {
        File file = new File(filename);
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException ex) {
            logger.error("Image could not be saved!", ex);
        }
    }
}
