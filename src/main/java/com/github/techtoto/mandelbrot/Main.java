package com.github.techtoto.mandelbrot;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        MandelbrotSet mandel = new MandelbrotSet(1280);

        mandel.setCenter(-0.747162, -0.087584);
        mandel.scale(10000);
        mandel.plotMandelbrotSection();
        //mandel.saveImage("Mandelbrot_10K.png");
    }
}
