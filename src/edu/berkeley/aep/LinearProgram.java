package edu.berkeley.aep;


//Understands a Linear optimization Program (LP)

import java.util.HashSet;
import java.util.Set;

public class LinearProgram {
    
    private final int nbBinaryVar;
    private final int nbIntegerVar;
    private final int nbRealVar;
    private final Sense sense;
    private Set<Constraint> constraints;
    private double[] objective;

    public LinearProgram(int nbBinaryVariables, int nbIntegerVariables, int nbRealVariables, Sense sense) {
        this.nbBinaryVar=nbBinaryVariables;
        this.nbIntegerVar=nbIntegerVariables;
        this.nbRealVar=nbRealVariables;
        this.sense=sense;
        this.constraints=new HashSet<>();//initialized to empty set of constraints
        this.objective=new double[nbBinaryVar+nbIntegerVar+nbRealVar];//initialized to 0
    }

    public void addConstraints(Constraint constraint){
        if (constraint.dimension()!=nbBinaryVar+nbIntegerVar+nbRealVar) throw new IllegalArgumentException("Number of constraint coefficients does not match number of variables");
        this.constraints.add(constraint);
    }

    public void setObjective(double[] costVector){
        this.objective=costVector;
    }

    public boolean hasFeasiblePoint(int[] binary,int[] integers,double[] scalars) {
        if ((binary.length==nbBinaryVar)&&(integers.length==nbIntegerVar)&&(scalars.length==nbRealVar)){
            for (int v : binary) {
                if ((v < 0) || (v > 1)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
