package com.github.techtoto.mandelbrot;

/**
 * Visualizes the Mandelbrot set.
 * Code and concept adapted from "Uebung 04" from "Allgemeine Informatik I" at TU Darmstadt.
 *
 * @author Tobias R.
 * @version 2021-05-13
 */
public class MandelbrotSet {

    private BitmapWindow bitmap;
    private final int width;
    private final double[] reLimits;
    private final double[] imLimits;


    /**
     * Initializes a Mandelbrot set plot.
     * By default, real part goes from -2 to 1 and imaginary part from -1 to 1,
     * so this results in an aspect ratio of 3:2.
     *
     * @param width Picture width in pixels. Picture height is automatically calculated.
     */
    public MandelbrotSet(int width) {
        reLimits = new double[2];
        imLimits = new double[2];
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
     * Plots the Mandelbrot set.
     */
    public void plotMandelbrot() {
        bitmap = new BitmapWindow(calculateBitmap(width, getHeightByAspectRatio()), true);
    }

    public void showMandelbrot() {
        bitmap.showPlot();
    }

    /**
     * Saves the plotted image to the given filename.
     *
     * @param filename The file path where the picture is to be saved
     */
    public void saveImage(String filename) {
        if (bitmap != null) bitmap.saveImage(filename);
        else System.out.println("No image to save!");
    }

    public void setLimits(double reMin, double reMax, double imMin, double imMax) {
        if (reMax > reMin) {
            reLimits[0] = reMin;
            reLimits[1] = reMax;
        } else {
            System.out.println("Invalid x limits: reMin = " + reMin + " and reMax = " + reMax);
        }

        if (imMax > imMin) {
            imLimits[0] = imMin;
            imLimits[1] = imMax;
        } else {
            System.out.println("Invalid y limits: imMin = " + imMin + " and imMax = " + imMax);
        }
    }

    /**
     * @return An Array with the limits of the current plot (reMin, reMax, imMin, imMax).
     */
    public double[] getLimits() {
        double[] limits = new double[4];

        limits[0] = reLimits[0];
        limits[1] = reLimits[1];
        limits[2] = imLimits[0];
        limits[3] = imLimits[1];

        return limits;
    }

    /**
     * Moves the current plot.
     *
     * @param axis  The axis where the Plot is to be moved. Valid characters are 'x' (real part) and 'y' (imaginary part).
     * @param value The value by which the plot is to be moved.
     */
    public void move(char axis, double value) {
        switch (axis) {
            case 'x' -> setLimits(reLimits[0] + value, reLimits[1] + value, imLimits[0], imLimits[1]);
            case 'y' -> setLimits(reLimits[0], reLimits[1], imLimits[0] + value, imLimits[1] + value);
            default -> System.out.println("Invalid char!");
        }
    }

    /**
     * Scales the plot by the given factor,
     * where factor > 1 is zooming in and factor < 1 is zooming out.
     *
     * @param factor the scaling factor.
     */
    public void scale(double factor) {
        double[] currentCenter = getCenter();
        double[] currentLimits = getLimits();
        double deltaX = currentLimits[1] - currentCenter[0];
        double deltaY = currentLimits[3] - currentCenter[1];

        setLimits(currentCenter[0] - (deltaX / factor), currentCenter[0] + (deltaX / factor), currentCenter[1] - (deltaY / factor), currentCenter[1] + (deltaY / factor));
    }

    /**
     * @return The center of current plot, split in real and imaginary value.
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
     *
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
        return (int) (width / ((reLimits[1] - reLimits[0]) / (imLimits[1] - imLimits[0])));
    }

    /**
     * Maps a x coordinate to their respective real value,
     * defined by the given x limits.
     *
     * @param x        x-coordinate of the pixel.
     * @param width    picture width in pixels.
     * @param reLimits an array with the lower and higher x limits.
     */
    private double xToRe(double x, double width, double[] reLimits) {
        return ((reLimits[1] - reLimits[0]) / width) * x + reLimits[0];
    }

    /**
     * Maps a y coordinate to their respective imaginary value,
     * defined by the given y limits.
     *
     * @param y        y-coordinate of the pixel.
     * @param height   picture height in pixels.
     * @param imLimits an array with the lower and higher y limits.
     */
    private double yToIm(double y, double height, double[] imLimits) {
        return ((imLimits[0] - imLimits[1]) / height) * y + imLimits[1];
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
                bitmap[x][y] = calculatePoint(new ComplexNumber(xToRe(x, width, reLimits), yToIm(y, height, imLimits)));
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
    private int calculatePoint(ComplexNumber c) {
        double threshold = 42;
        double currentZLength;
        ComplexNumber currentZ = new ComplexNumber(0, 0);

        for (int i = 0; i <= 255; i++) {
            currentZ = currentZ.times(currentZ).add(c);
            currentZLength = (Math.pow(currentZ.re(), 2) + Math.pow(currentZ.im(), 2));
            if (currentZLength > threshold) return i;
        }

        return 255;
    }
}
