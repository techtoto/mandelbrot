package com.github.techtoto.mandelbrot;

/**
 * Implements a complex Number with basic arithmetic operations.
 * Code and concept adapted from "Uebung 04" from "Allgemeine Informatik I" at TU Darmstadt.
 *
 * @author Florian Brandherm, Tobias R.
 * @version 2021-04-12
 */
public record ComplexNumber(double re, double im) {
    /**
     * Adds two complex numbers.
     *
     * @param other the second summand.
     * @return a new ComplexNumber which equals to the addition of both complex numbers.
     */
    public ComplexNumber add(ComplexNumber other) { // 3.1 d)
        double new_re = re + other.re;
        double new_im = im + other.im;
        return new ComplexNumber(new_re, new_im);
    }

    /**
     * Multiplies two complex numbers.
     *
     * @param other the second factor.
     * @return a new ComplexNumber which equals to the multiplication of both complex numbers.
     */
    public ComplexNumber times(ComplexNumber other) { // 3.1 e)
        double new_re = re * other.re - im * other.im;
        double new_im = re * other.im + im * other.re;
        return new ComplexNumber(new_re, new_im);
    }
}