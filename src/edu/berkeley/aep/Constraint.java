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

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Constraint)) return false;
        var otherConstraint=(Constraint) other;
        return this.isMultipleOf(otherConstraint);
    }

    public boolean isMultipleOf(Constraint other){
        if (other.coefficients.length!=coefficients.length) return false;
        double factor=0;
        for (int i=0; i<coefficients.length;i++){
            if ((factor==0)&&(coefficients[i]!=0)&&(other.coefficients[i]!=0)){
                factor=coefficients[i]/other.coefficients[i];
            } else{
                if (Double.compare(factor*other.coefficients[i],coefficients[i])!=0) return false;
            }
        }
        if ((factor!=0)&&(Double.compare(factor*other.rhs,rhs)!=0)) return false;
        if (factor==0) return false;
        if ((factor>0)&&(!(this.sense.equals(other.sense)))) return false;
        if ((factor<0)&&(this.sense.equals(other.sense))) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode((int) (coefficients.length+rhs));
    }
}
