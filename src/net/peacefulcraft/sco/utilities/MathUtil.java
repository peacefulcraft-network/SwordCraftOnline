package net.peacefulcraft.sco.utilities;

import java.util.Arrays;
import java.util.List;

public class MathUtil {

    /**
     * Calculates a,b,c values in parabolic equation.
     * Equation: y = ax^2 + bx + c
     * @return List of type int.
     */
    public static List<Double> lagrangeParabola(double x1, double y1, double x2, double y2, double x3, double y3) {
        double a = y1/((x1-x2) * (x1-x3)) + y2/((x2-x1) * (x2-x3)) + y3/((x3-x1) * (x3-x2));
        double b = -y1*(x2+x3) / ((x1-x2) * (x2-x3)) - y2*(x1+x3) / ((x2 - x1) * (x2-x3)) - y3*(x1+x2) / ((x3-x1)*(x3-x2));
        double c = y1*x2*x3 / ((x1-x2) * (x1-x3)) + y2*x1*x3 / ((x2-x1) * (x2-x3)) + y3*x1*x2 / ((x3-x1) * (x3-x2));

        return Arrays.asList(a, b, c);
    }
}