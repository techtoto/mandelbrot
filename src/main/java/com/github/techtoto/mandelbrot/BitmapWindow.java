package com.github.techtoto.mandelbrot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class implements a window to show a picture.
 * Code and concept adapted from "Uebung 04" from "Allgemeine Informatik I" at TU Darmstadt.
 *
 * @author Florian Brandherm, Tobias R.
 * @version 2021-04-12
 */
public class BitmapWindow extends JPanel {

    JFrame frame;
    BufferedImage img;

    /**
     * The Constructor creates a window which displays a Bitmap.
     *
     * @param bitmap  A 2D-Array of pixels
     * @param colored If true, the picture is created in pseudocolor. Otherwise the picture is in grayscale.
     */
    public BitmapWindow(int[][] bitmap, boolean colored) {
        frame = new JFrame("Mandelbrot set");
        frame.getContentPane().setBackground(Color.black);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        frame.add(this);

        int bmp_width = bitmap.length;
        int bmp_height = bitmap[0].length;
        this.img = new BufferedImage(bmp_width, bmp_height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < bmp_width; ++x) {
            for (int y = 0; y < bmp_height; ++y) {
                if (bitmap[x][y] == 255) {
                    this.img.setRGB(x, y, 0x000000);
                } else {
                    float value = bitmap[x][y] / 254.0f;
                    int col;
                    if (colored) {
                        col = Color.getHSBColor((float) Math.sqrt(value) + 0.5f, 1.0f, 1.0f).getRGB();
                    } else {
                        col = Color.getHSBColor(1.0f, 0.0f, value).getRGB();
                    }
                    this.img.setRGB(x, y, col);
                }
            }
        }

        this.setPreferredSize(new Dimension(this.img.getWidth(), this.img.getHeight()));

        this.frame.pack();

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }

    /**
     * Re-draws the image if the window is re-created.
     * This method is called automatically by the window manager.
     */
    public void paint(Graphics g) {
        g.drawImage(this.img, 0, 0, this.img.getWidth(), this.img.getHeight(), null);
    }

    /**
     * Saves the plotted image to the given filename.
     *
     * @param filename The name of the file where the picture is to be saved.
     */
    public void saveImage(String filename) {
        File file = new File(filename);
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException ex) {
            System.err.println("Konnte das Bild nicht speichern!");
            ex.printStackTrace();
        }
    }
}
