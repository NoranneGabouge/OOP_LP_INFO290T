package edu.berkeley.aep;

/**
 * Understands a constraint of a linear program
 */
public class Constraint {
    private final double[] coefficients;
    private final double rightHandSide;
    private final ConstraintDirection direction;

    public Constraint(double[] coefficients, ConstraintDirection direction, double rightHandSide) {
        this.coefficients = coefficients;
        this.rightHandSide = rightHandSide;
        this.direction = direction;
    }

    public int dimension(){
        return coefficients.length;
    }

    /**
     * Evaluates whether a constraint is satisfied by the variables provided
     */
    public boolean isSatisfiedByVariables(int[] binary, int[] integers, double[] scalars) {
        double leftHandSide = 0;
        for (int i = 0; i < coefficients.length; i++) {
            if (i < binary.length){
                leftHandSide = leftHandSide + coefficients[i] * binary[i];
            } else if (i < binary.length + integers.length) {
                leftHandSide = leftHandSide + coefficients[i] * integers[i - binary.length];
            } else {
                leftHandSide = leftHandSide + coefficients[i] * scalars[i - binary.length - integers.length];
            }
        }
        return (leftHandSide <= rightHandSide && direction == ConstraintDirection.LESSTHAN) ||
                (leftHandSide >= rightHandSide && direction == ConstraintDirection.GREATERTHAN) ||
                (leftHandSide == rightHandSide && direction == ConstraintDirection.EQUALS);
    }


    public boolean isMultipleOf(Constraint other){
        if (other.coefficients.length != coefficients.length) {
            return false;
        }
        double factor = 0;
        for (int i = 0; i < coefficients.length; i++){
            if (factor == 0 && coefficients[i] != 0 && other.coefficients[i] != 0){
                factor = coefficients[i] / other.coefficients[i];
            } else{
                if (Double.compare(factor * other.coefficients[i], coefficients[i]) != 0) {
                    return false;
                }
            }
        }
        if (factor != 0 && Double.compare(factor * other.rightHandSide, rightHandSide) != 0) {
            return false;
        }
        if (factor == 0) {
            return false;
        }
        if (factor > 0 && !(this.direction.equals(other.direction))) {
            return false;
        }
        return factor > 0 || !this.direction.equals(other.direction);
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Constraint)) {
            return false;
        }
        Constraint otherConstraint = (Constraint) other;
        return this.isMultipleOf(otherConstraint);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode((int) (coefficients.length + rightHandSide));
    }
}
