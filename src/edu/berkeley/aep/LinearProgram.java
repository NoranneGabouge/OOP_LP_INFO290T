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
        if (costVector.length!=nbBinaryVar+nbIntegerVar+nbRealVar) throw new IllegalArgumentException("Dimension of cost vector does not match number of variables");
        this.objective=costVector;
    }

    public boolean hasFeasiblePoint(int[] binary,int[] integers,double[] scalars) {
        if ((binary.length!=nbBinaryVar)||(integers.length!=nbIntegerVar)||(scalars.length!=nbRealVar)) {
            return false;
        }
        for (int v : binary) {
            if ((v < 0) || (v > 1)) {
                return false;
            }
        }
        var iterator=this.constraints.iterator();
        while(iterator.hasNext()){
            var next=iterator.next();
            if (next.satisfiedBy(binary,integers,scalars)){
                iterator.remove();
            }
            else{
                return false;
            }
        }
        return true;
    }

    public LinearProgram relaxation() {
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof LinearProgram)) return false;
        LinearProgram LP=(LinearProgram) other;
        if ((!(LP.nbBinaryVar==nbBinaryVar)) || (!(LP.nbIntegerVar==nbIntegerVar))||(!(LP.nbRealVar==nbRealVar))||(!(LP.sense.equals(sense)))){
            return false;
        } else {
            for (int i=0; i<this.objective.length;i++){
                if (!(Double.compare(LP.objective[i],objective[i])==0)) return false;
            }
            var iterator=this.constraints.iterator();
            var otheriterator=LP.constraints.iterator();
            while((iterator.hasNext())&&(otheriterator.hasNext())){
                var next=iterator.next();
                var othernext=otheriterator.next();
                if (next.equals(othernext)){
                    iterator.remove();
                    otheriterator.remove();
                }
                else{
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(nbBinaryVar+nbIntegerVar+nbRealVar);
    }
}
