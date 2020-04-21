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


    public boolean satisfiedBy(int[] binary, int[] integers, double[] scalars) {
        double lhs=0;
        for (int i=0;i<coefficients.length;i++){
            if (i<binary.length){
                lhs=lhs+coefficients[i]*binary[i];
            } else if(i<binary.length+integers.length){
                lhs=lhs+coefficients[i]*integers[i-binary.length];
            } else {
                lhs=lhs+coefficients[i]*scalars[i-binary.length-integers.length];
            }
        }
        return (lhs <= rhs && sense == ConstraintSense.LESSTHAN) || (lhs >= rhs && sense == ConstraintSense.GREATERTHAN) || (lhs == rhs && sense == ConstraintSense.EQUALS);
    }
}
