package edu.berkeley.aep;


//Understands a Linear optimization Program (LP)

import java.util.HashSet;
import java.util.Set;

public class LinearProgram {
    
    private final int nbBinaryVar;
    private final int nbIntegerVar;
    private final int nbRealVariables;
    private final Sense sense;
    private Set<Constraint> constraints;

    public LinearProgram(int nbBinaryVariables, int nbIntegerVariables, int nbRealVariables, Sense sense) {
        this.nbBinaryVar=nbBinaryVariables;
        this.nbIntegerVar=nbIntegerVariables;
        this.nbRealVariables=nbRealVariables;
        this.sense=sense;
        this.constraints=new HashSet<>();
    }

    public void addConstraints(Constraint constraint){
        this.constraints.add(constraint);
    }


    public boolean hasFeasiblePoint(int[] binary,int[] integers,double[] scalars) {
        if ((binary.length==nbBinaryVar)&&(integers.length==nbIntegerVar)&&(scalars.length==nbRealVariables)){
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
