package edu.berkeley.aep;

public class Constraint {
    private final double[] coefficients;
    private final double rhs;
    private final ConstraintSense sense;

    public Constraint(double[] coefficients, ConstraintSense sense, double rhs) {
        this.coefficients=coefficients;
        this.rhs=rhs;
        this.sense=sense;
    }

    public int dimension(){
        return coefficients.length;
    }
}
