package edu.berkeley.aep;

import org.junit.Test;

import static org.junit.Assert.*;

public class LinearProgramTest {

    //LPa : LP with objective function 0, binary variable x and no constraint
    LinearProgram LPa=new LinearProgram(1,0,0,Sense.MAX);

    @Test
    public void zeroIsFeasibleForLPa(){
        assertTrue(LPa.hasFeasiblePoint(new int[]{0}, new int[0],new double[0]));
    }

    @Test
    public void twoIsNotFeasibleForLPa(){
        assertFalse(LPa.hasFeasiblePoint(new int[]{2}, new int[0],new double[0]));
    }

    @Test(expected = IllegalArgumentException.class )
    public void addingAConstraintToLPaWithTwoCoeffsShouldThrowAnException(){
        Constraint constra=new Constraint(new double[2],ConstraintSense.GREATERTHAN, 0);
        LPa.addConstraints(constra);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingACostVectorToLPaWithTwoCoeffsForBinaryVarsShouldThrowAnException(){
        LPa.setObjective(new double[2]);
    }


    @Test
    /*LPb : min of x+y, integer variable x, real variable y, with constraints x>=0 and y>=0
     */
    public void minusOneminusOneNotFeasibleForLPb(){
        LinearProgram LPb=new LinearProgram(0,1,1,Sense.MIN);
        LPb.setObjective(new double[]{1,1});
        Constraint xnonnegative=new Constraint(new double[]{1,0},ConstraintSense.GREATERTHAN, 0);
        Constraint ynonnegative=new Constraint(new double[]{0,1},ConstraintSense.GREATERTHAN,  0);
        LPb.addConstraints(xnonnegative);
        LPb.addConstraints(ynonnegative);
        assertFalse(LPb.hasFeasiblePoint(new int[0], new int[]{-1}, new double[]{-1}));
    }


}
