package edu.berkeley.aep;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Understands a Linear optimization Program (LP)
 */
public class LinearProgram {
    
    private final int nbBinaryVar;
    private final int nbIntegerVar;
    private final int nbRealVar;
    private final Direction direction;
    private double globalcoefficient;
    private Set<Constraint> constraints;
    private double[] objective;

    public LinearProgram(int nbBinaryVariables, int nbIntegerVariables, int nbRealVariables, Direction direction) {
        this.nbBinaryVar = nbBinaryVariables;
        this.nbIntegerVar = nbIntegerVariables;
        this.nbRealVar = nbRealVariables;
        this.direction = direction;
        this.constraints = new HashSet<>();//initialized to empty set of constraints
        this.objective = new double[getTotalNumVariables()];//initialized to 0
        this.globalcoefficient = 1;
    }

    public void addConstraints(Constraint constraint) {
        if (constraint.dimension() != getTotalNumVariables()) {
            throw new IllegalArgumentException("Number of constraint coefficients does not match number of variables");
        }
        this.constraints.add(constraint);
    }

    public void setObjective(double[] costVector) {
        if (costVector.length != getTotalNumVariables()) {
            throw new IllegalArgumentException("Dimension of cost vector does not match number of variables");
        }
        this.objective = costVector;
    }

    public void setObjective(double[] costVector,double globalcoeff){
        if (costVector.length != getTotalNumVariables()) {
            throw new IllegalArgumentException("Dimension of cost vector does not match number of variables");
        }
        this.objective = costVector;
        this.globalcoefficient = globalcoeff;
    }

    public boolean hasFeasiblePoint(int[] binary,int[] integers,double[] scalars) {
        if (binary.length != nbBinaryVar ||
                integers.length != nbIntegerVar ||
                scalars.length != nbRealVar) {
            return false;
        }
        for (int v : binary) {
            if (v < 0 || v > 1) {
                return false;
            }
        }
        Iterator<Constraint> iterator = this.constraints.iterator();
        while (iterator.hasNext()) {
            Constraint next = iterator.next();
            if (next.isSatisfiedByVariables(binary, integers, scalars)) {
                iterator.remove();
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the LP-relaxation of the linear program
     */
    public LinearProgram relaxation() {
        LinearProgram LPRelaxation = new LinearProgram(0,0,
                getTotalNumVariables(), direction);
        LPRelaxation.setObjective(objective);

        for (int i = 0; i < nbBinaryVar; i++) {
            double[] selectBinaryVariable = new double[getTotalNumVariables()];
            selectBinaryVariable[i] = 1;
            Constraint nonNegative = new Constraint(selectBinaryVariable, ConstraintDirection.GREATERTHAN, 0);
            Constraint lessThanOne = new Constraint(selectBinaryVariable, ConstraintDirection.LESSTHAN, 1);
            LPRelaxation.addConstraints(nonNegative);
            LPRelaxation.addConstraints(lessThanOne);
        }
        return LPRelaxation;
    }

    /**
     * Evaluates whether two vectors vector1 and vector2 (multiplied respectively by coefficients coeff1 and coeff2)
     * are collinear
     */
    public static boolean areCollinear(double[] vector1,double coeff1,double[] vector2, double coeff2){
        if (vector1.length != vector2.length) {
            return false;
        }
        boolean areCollinear = true;
        for (int i = 0; i < vector1.length; i++) {
            double comb = coeff1 * vector1[i] - coeff2 * vector2[i];
            if (!((0 == Double.compare(comb, 0)) ||
                    (0 == Double.compare(comb, (double) -1 * 0)))) {
                areCollinear = false;
                break;
            }
        }
        return areCollinear;
    }

    /**
     * Return -1 if a * c is the zero vector, returns the index of a nonzero coefficient otherwise
     */
    public static int getNonZeroElementOfProduct(double[] c, double a){
        for (int i = 0; i < c.length; i++) {
            if (!((0 == Double.compare(a * c[i], 0)) ||
                    (0 == Double.compare(a * c[i], -1.0d * 0)))) {
                return i;
            }
        }
        return -1;
    }

    private int getTotalNumVariables() {
        return nbBinaryVar + nbIntegerVar + nbRealVar;
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof LinearProgram)) return false;

        LinearProgram LP = (LinearProgram) other;
        if (LP.nbBinaryVar != nbBinaryVar || LP.nbIntegerVar != nbIntegerVar || LP.nbRealVar != nbRealVar ) {
            return false;
        }

        if (areCollinear(LP.objective, LP.globalcoefficient, objective, globalcoefficient)){
            if ((getNonZeroElementOfProduct(LP.objective, LP.globalcoefficient) >= 0) &&
                    (getNonZeroElementOfProduct(LP.objective,LP.globalcoefficient) >= 0)) {//the direction of the model matters
                int i1 = getNonZeroElementOfProduct(objective, globalcoefficient);
                int i2 = getNonZeroElementOfProduct(LP.objective, LP.globalcoefficient);
                boolean shouldHaveSameDirection = (globalcoefficient * objective[i1] * LP.globalcoefficient * objective[i2] > 0);
                if (LP.direction.equals(direction) != shouldHaveSameDirection) {
                    return false;
                }
            }
        }
        Iterator<Constraint> constraintIterator = this.constraints.iterator();
        Iterator<Constraint> otherConstraintIterator = LP.constraints.iterator();
        while (constraintIterator.hasNext() && otherConstraintIterator.hasNext()) {
            Constraint nextConstraint = constraintIterator.next();
            Constraint otherNextConstraint = otherConstraintIterator.next();
            if (nextConstraint.equals(otherNextConstraint)){
                constraintIterator.remove();
                otherConstraintIterator.remove();
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getTotalNumVariables());
    }

}
