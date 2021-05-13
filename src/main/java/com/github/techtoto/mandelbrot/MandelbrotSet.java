package com.github.techtoto.mandelbrot;

/**
 * Visualizes the Mandelbrot set.
 * Code and concept adapted from "Uebung 04" from "Allgemeine Informatik I" at TU Darmstadt.
 *
 * @author Tobias R.
 * @version 2021-05-13
 */
public class MandelbrotSet {

    private BitmapWindow window;
    private final int width;
    private final double[] xLimits;
    private final double[] yLimits;


    /**
     * Initializes a Mandelbrot set plot.
     *
     * @param width Picture width in pixels. Picture height is automatically calculated.
     */
    public MandelbrotSet(int width) {
        xLimits = new double[2];
        yLimits = new double[2];
        setLimits(-2, 1, -1, 1);

        if (width > 0) {
            this.width = width;
        } else {
            System.out.println("Invalid picture width: " + width);
            System.out.println("Assuming a picture width of 1920 pixels.");
            this.width = 1920;
        }
    }

    /**
     * Plots the complete Mandelbrot set.
     * x from -2 to 1, y from -1 to 1.
     * This results are in an aspect ratio of 3:2.
     */
    public void plotFullMandelbrot() {

        window = new BitmapWindow(calculateBitmap(width, getHeightByAspectRatio()), true);
    }

    /**
     * Plots a section of the Mandelbrot set.
     * Only callable if the limits have been previously set by setLimits().
     */
    public void plotMandelbrotSection() {
        if (xLimits != null && yLimits != null) {
            window = new BitmapWindow(calculateBitmap(width, getHeightByAspectRatio()), true);
        } else {
            System.out.println("Plot limits not set!");
        }
    }

    /**
     * Saves the plotted image to the given filename.
     *
     * @param filename The file path where the picture is to be saved
     */
    public void saveImage(String filename) {
        if (window != null) window.saveImage(filename);
    }

    public void setLimits(double xMin, double xMax, double yMin, double yMax) {
        if (xMax > xMin) {
            xLimits[0] = xMin;
            xLimits[1] = xMax;
        } else {
            System.out.println("Invalid x limits: xMin = " + xMin + " and xMax = " + xMax);
        }

        if (yMax > yMin) {
            yLimits[0] = yMin;
            yLimits[1] = yMax;
        } else {
            System.out.println("Invalid y limits: yMin = " + yMin + " and yMax = " + yMax);
        }
    }

    /**
     * @return An Array with the limits of the current plot (xMin, xMax, yMin, yMax).
     */
    public double[] getLimits() {
        double[] limits = new double[4];
        limits[0] = xLimits[0];
        limits[1] = xLimits[1];
        limits[2] = yLimits[0];
        limits[3] = yLimits[1];
        return limits;
    }

    /**
     * Moves the current plot.
     *
     * @param axis  The axis where the Plot is to be moved. Valid characters are 'x' and 'y'.
     * @param value The value by which the plot is to be moved.
     */
    public void move(char axis, double value) {
        switch (axis) {
            case 'x' -> setLimits(xLimits[0] + value, xLimits[1] + value, yLimits[0], yLimits[1]);
            case 'y' -> setLimits(xLimits[0], xLimits[1], yLimits[0] + value, yLimits[1] + value);
            default -> System.out.println("Invalid char!");
        }
    }

    /**
     * Scales the plot by the given factor,
     * where factor > 1 is zooming in and factor < 1 is zooming out.
     * @param factor the scaling factor.
     */
    public void scale(double factor) {
        double[] currentCenter = getCenter();
        double[] currentLimits = getLimits();
        double deltaX = currentLimits[1] - currentCenter[0];
        double deltaY = currentLimits[3] - currentCenter[1];

        setLimits(currentCenter[0] - (deltaX / factor),currentCenter[0] + (deltaX / factor), currentCenter[1] - (deltaY / factor),currentCenter[1] + (deltaY / factor));
    }

    /**
     *
     * @return The center of current plot.
     */
    public double[] getCenter() {
        double[] center = new double[2];
        double[] limits = getLimits();

        center[0] = (limits[0] + limits[1]) / 2;
        center[1] = (limits[2] + limits[3]) / 2;

        return center;
    }

    /**
     * Sets the center of the plot, keeping the current scaling.
     * @param x x coordinate of the center point
     * @param y y coordinate of the center point
     */
    public void setCenter(double x, double y) {
        double[] oldLimits = getLimits();
        double deltaX = oldLimits[1] - oldLimits[0];
        double deltaY = oldLimits[3] - oldLimits[2];

        setLimits(x - (deltaX / 2), x + (deltaX / 2), y - (deltaY / 2), y + (deltaY / 2));
    }

    /**
     * @return The height of the image based on the width and the given limits.
     */
    private int getHeightByAspectRatio() {
        return (int) (width / ((xLimits[1] - xLimits[0]) / (yLimits[1] - yLimits[0])));
    }

    /**
     * Maps a x coordinate to their respective real value,
     * defined by the given x limits.
     *
     * @param x     x-coordinate of the pixel.
     * @param width picture width in pixels.
     * @param xLims an array with the lower and higher x limits.
     */
    private double xToRe(double x, double width, double[] xLims) {
        return ((xLims[1] - xLims[0]) / width) * x + xLims[0];
    }

    /**
     * Maps a y coordinate to their respective imaginary value,
     * defined by the given y limits.
     *
     * @param y      y-coordinate of the pixel.
     * @param height picture height in pixels.
     * @param yLims  an array with the lower and higher y limits.
     */
    private double yToIm(double y, double height, double[] yLims) {
        return ((yLims[0] - yLims[1]) / height) * y + yLims[1];
    }

    /**
     * Calculates a bitmap of the Mandelbrot set.
     *
     * @param width  picture width in pixels
     * @param height picture height in pixels
     * @return 2D-bitmap of the Mandelbrot set
     */
    private int[][] calculateBitmap(int width, int height) {
        int[][] bitmap = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap[x][y] = calcPoint(new ComplexNumber(xToRe(x, width, xLimits), yToIm(y, height, yLimits)));
            }
        }

        return bitmap;
    }

    /**
     * Calculates for a given complex number c if it's part of the Mandelbrot set.
     * If not, it calculates how many iterations of the recursive progression are needed
     * to reach a defined threshold.
     *
     * @param c the complex number
     * @return 255 if the complex number is part of the Mandelbrot set. Otherwise, the number of iterations (0 to 255) until the threshold is reached.
     */
    private int calcPoint(ComplexNumber c) {
        double threshold = 42;
        double currentZLength;
        ComplexNumber currentZ = new ComplexNumber(0, 0);

        for (int i = 0; i <= 255; i++) {
            currentZ = currentZ.times(currentZ).add(c);
            currentZLength = Math.sqrt(Math.pow(currentZ.re(), 2) + Math.pow(currentZ.im(), 2));
            if (currentZLength > threshold) return i;
        }

        return 255;
    }
}
