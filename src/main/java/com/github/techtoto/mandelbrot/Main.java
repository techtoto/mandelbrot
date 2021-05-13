package com.github.techtoto.mandelbrot;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        MandelbrotSet mandel = new MandelbrotSet(1920);

        mandel.setLimits(-0.1793,-0.1724,0.6585,0.6625);
        mandel.plotMandelbrot();
        mandel.showMandelbrot();
    }
}
