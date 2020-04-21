package edu.berkeley.aep;


//Understands a Linear optimization Program (LP)

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LinearProgram {
    
    private final int nbBinaryVar;
    private final int nbIntegerVar;
    private final int nbRealVar;
    private final Sense sense;
    private double globalcoefficient;
    private Set<Constraint> constraints;
    private double[] objective;

    public LinearProgram(int nbBinaryVariables, int nbIntegerVariables, int nbRealVariables, Sense sense) {
        this.nbBinaryVar=nbBinaryVariables;
        this.nbIntegerVar=nbIntegerVariables;
        this.nbRealVar=nbRealVariables;
        this.sense=sense;
        this.constraints=new HashSet<>();//initialized to empty set of constraints
        this.objective=new double[nbBinaryVar+nbIntegerVar+nbRealVar];//initialized to 0
        this.globalcoefficient=1;
    }

    public void addConstraints(Constraint constraint){
        if (constraint.dimension()!=nbBinaryVar+nbIntegerVar+nbRealVar) throw new IllegalArgumentException("Number of constraint coefficients does not match number of variables");
        this.constraints.add(constraint);
    }

    public void setObjective(double[] costVector){
        if (costVector.length!=nbBinaryVar+nbIntegerVar+nbRealVar) throw new IllegalArgumentException("Dimension of cost vector does not match number of variables");
        this.objective=costVector;
    }
    public void setObjective(double[] costVector,double globalcoeff){
        if (costVector.length!=nbBinaryVar+nbIntegerVar+nbRealVar) throw new IllegalArgumentException("Dimension of cost vector does not match number of variables");
        this.objective=costVector;
        this.globalcoefficient=globalcoeff;
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
        LinearProgram LPRelaxation=new LinearProgram(0,0,nbBinaryVar+nbIntegerVar+nbRealVar,sense);
        LPRelaxation.setObjective(objective);
        for (int i=0; i<nbBinaryVar;i++){
            var selectBinaryVariable=new double[nbBinaryVar+nbIntegerVar+nbRealVar];
            selectBinaryVariable[i]=1;
            Constraint nonnegative=new Constraint(selectBinaryVariable,ConstraintSense.GREATERTHAN, 0);
            Constraint lessthanOne=new Constraint(selectBinaryVariable,ConstraintSense.LESSTHAN, 1);
            LPRelaxation.addConstraints(nonnegative);
            LPRelaxation.addConstraints(lessthanOne);
        }
        return LPRelaxation;
    }

    public static boolean areCollinear(double[] c1,double a1,double[] c2, double a2){
        if (c1.length!=c2.length) return false;
        boolean areCollinear=true;
        for (int i=0; i<c1.length;i++){
            double comb=a1*c1[i]-a2*c2[i];
            if (!((0 == Double.compare(comb, 0)) || (0 == Double.compare(comb, (double) -1 * 0)))) {
                areCollinear = false;
                break;
            }
        }
        return areCollinear;
    }

    public static int nonZeroElement(double[] c,double a){//return -1 if a*c is the zero vector, returns the index of a nonzero coefficient otherwise
        for (int i=0;i<c.length;i++){
            if (!((0== Double.compare(a*c[i], 0))||(0== Double.compare(a*c[i], (double)-1*0)))) return i;
        }
        return -1;
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof LinearProgram)) return false;

        LinearProgram LP=(LinearProgram) other;
        if ((LP.nbBinaryVar!=nbBinaryVar) || (LP.nbIntegerVar!=nbIntegerVar)||(LP.nbRealVar!=nbRealVar)) return false;
        if (areCollinear(LP.objective,LP.globalcoefficient,objective,globalcoefficient)){
            if ((nonZeroElement(LP.objective,LP.globalcoefficient)>=0)&&(nonZeroElement(LP.objective,LP.globalcoefficient)>=0)) {//the sense of the model matters
                int i1 = nonZeroElement(objective, globalcoefficient);
                int i2 = nonZeroElement(LP.objective, LP.globalcoefficient);
                boolean shouldHaveSameSense = (globalcoefficient * objective[i1] * LP.globalcoefficient * objective[i2] > 0);
                if (LP.sense.equals(sense) != shouldHaveSameSense) return false;
            }
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

    @Override
    public int hashCode() {
        return Integer.hashCode(nbBinaryVar+nbIntegerVar+nbRealVar);
    }

}
