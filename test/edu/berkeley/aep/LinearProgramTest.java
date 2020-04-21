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

    @Test
    /*LPc : min of 3x1+2x2-y1-4y2-5y3+8w1-w2,
    such that
    x1+x2<=1
    y1-2y2+y3>=5
    10y2-w1<=3
    w2>=9
    with
    x1,x2 binary ; y1,y2,y3 integer ; w1,w2 real
    Test for point=(1,0,6,-1,2,4.5,12.1)
     */
    public void pointIsFeasibleForLPc(){
        LinearProgram LPc=new LinearProgram(2,3,2,Sense.MIN);
        LPc.setObjective(new double[]{3,2,-1,-4,-5,8,-1});
        Constraint constr1=new Constraint(new double[]{1,1,0,0,0,0,0},ConstraintSense.LESSTHAN, 1);
        Constraint constr2=new Constraint(new double[]{0,0,1,-2,1,0,0},ConstraintSense.GREATERTHAN, 5);
        Constraint constr3=new Constraint(new double[]{0,0,0,10,0,-1,0},ConstraintSense.LESSTHAN, 3);
        Constraint constr4=new Constraint(new double[]{0,0,0,0,0,0,1},ConstraintSense.GREATERTHAN, 9);
        LPc.addConstraints(constr1);
        LPc.addConstraints(constr2);
        LPc.addConstraints(constr3);
        LPc.addConstraints(constr4);
        assertTrue(LPc.hasFeasiblePoint(new int[]{1,0}, new int[]{6,-1,2}, new double[]{4.5,12.1}));
    }

    @Test
    /*
    LPd=max 3x
    such that x<=2
    x in R
     */
    public void linearRelaxationOfLPdIsEqualToLPd(){
        LinearProgram LPd=new LinearProgram(0,0,1,Sense.MAX);
        LPd.setObjective(new double[]{3});
        Constraint constr1=new Constraint(new double[]{1},ConstraintSense.LESSTHAN, 2);
        LPd.addConstraints(constr1);
        assertEquals(LPd,LPd.relaxation());
    }
    @Test
    public void linearRelaxationOfLPdIsEqualToACopyOfLPd(){
        LinearProgram LPd=new LinearProgram(0,0,1,Sense.MAX);
        LPd.setObjective(new double[]{3});
        Constraint constr1=new Constraint(new double[]{1},ConstraintSense.LESSTHAN, 2);
        LPd.addConstraints(constr1);
        LinearProgram LPdCopy=new LinearProgram(0,0,1,Sense.MAX);
        LPdCopy.setObjective(new double[]{3});
        Constraint constr1Copy=new Constraint(new double[]{1},ConstraintSense.LESSTHAN, 2);
        LPdCopy.addConstraints(constr1Copy);
        assertEquals(LPdCopy,LPd.relaxation());
    }


}
