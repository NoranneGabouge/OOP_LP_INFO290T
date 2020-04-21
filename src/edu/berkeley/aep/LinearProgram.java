package edu.berkeley.aep;


//Understands a Linear optimization Program (LP)

public class LinearProgram {
    
    private final int nbBinaryVar;
    private final int nbIntegerVar;
    private final int nbRealVariables;

    public LinearProgram(int nbBinaryVariables, int nbIntegerVariables, int nbRealVariables) {
        this.nbBinaryVar=nbBinaryVariables;
        this.nbIntegerVar=nbIntegerVariables;
        this.nbRealVariables=nbRealVariables;
    }

    public boolean hasFeasiblePoint(int[] binary,int[] integers,double[] scalars) {
        if ((binary.length==nbBinaryVar)&&(integers.length==nbIntegerVar)&&(scalars.length==nbRealVariables)){
            for (int i=0;i<binary.length;i++){
                if ((binary[i]<0)||(binary[i]>1)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }


}
