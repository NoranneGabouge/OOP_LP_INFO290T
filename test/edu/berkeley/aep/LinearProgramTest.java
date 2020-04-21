package edu.berkeley.aep;

import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

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
    @Test
    public void linearRelaxationOfLPdIsEqualToACopyOfLPdAfterMultiplyingConstraintByMinusTwo(){
        LinearProgram LPd=new LinearProgram(0,0,1,Sense.MAX);
        LPd.setObjective(new double[]{3});
        Constraint constr1=new Constraint(new double[]{1},ConstraintSense.LESSTHAN, 2);
        LPd.addConstraints(constr1);
        LinearProgram LPdMultiplied=new LinearProgram(0,0,1,Sense.MAX);
        LPdMultiplied.setObjective(new double[]{3});
        Constraint constr1Multiplied=new Constraint(new double[]{-2},ConstraintSense.GREATERTHAN, -4);
        LPdMultiplied.addConstraints(constr1Multiplied);
        assertEquals(LPdMultiplied,LPd.relaxation());
    }

    @Test
    public void twoCopiesOfTheSameConstraintShouldBeEqual(){
        Constraint constr1=new Constraint(new double[]{1},ConstraintSense.LESSTHAN, 2);
        Constraint constr1Copy=new Constraint(new double[]{1},ConstraintSense.LESSTHAN, 2);
        assertEquals(constr1,constr1Copy);
    }

    @Test
    public void anLPShouldBeEqualToTheLPObtainedAfterChangingTheOrderOfTheConstraints(){
        LinearProgram LPc=new LinearProgram(2,3,2,Sense.MIN);
        LPc.setObjective(new double[]{3,2,-1,-4,-5,8,-1});
        LinearProgram LPcShuffle=new LinearProgram(2,3,2,Sense.MIN);
        LPcShuffle.setObjective(new double[]{3,2,-1,-4,-5,8,-1});
        Constraint constr1=new Constraint(new double[]{1,1,0,0,0,0,0},ConstraintSense.LESSTHAN, 1);
        Constraint constr2=new Constraint(new double[]{0,0,1,-2,1,0,0},ConstraintSense.GREATERTHAN, 5);
        Constraint constr3=new Constraint(new double[]{0,0,0,10,0,-1,0},ConstraintSense.LESSTHAN, 3);
        Constraint constr4=new Constraint(new double[]{0,0,0,0,0,0,1},ConstraintSense.GREATERTHAN, 9);
        LPc.addConstraints(constr1);
        LPc.addConstraints(constr2);
        LPc.addConstraints(constr3);
        LPc.addConstraints(constr4);
        LPcShuffle.addConstraints(constr3);
        LPcShuffle.addConstraints(constr1);
        LPcShuffle.addConstraints(constr4);
        LPcShuffle.addConstraints(constr2);
        assertEquals(LPc, LPcShuffle);
    }

    @Test
    /*
    LPe max 3x
    such that x binary, y integer
    LPeRelax max 3x
    such that 0<=x<=1 and x,y in R
     */
    public void linearRelaxationOfLPeIsEqualToLPeRelax(){
        LinearProgram LPe=new LinearProgram(1,1,0,Sense.MAX);
        LPe.setObjective(new double[]{3,0});
        LinearProgram LPeRelax=new LinearProgram(0,0,2,Sense.MAX);
        LPeRelax.setObjective(new double[]{3,0});
        Constraint xnonnegative=new Constraint(new double[]{1,0},ConstraintSense.GREATERTHAN, 0);
        Constraint xlessthanOne=new Constraint(new double[]{1,0},ConstraintSense.LESSTHAN, 1);
        LPeRelax.addConstraints(xnonnegative);
        LPeRelax.addConstraints(xlessthanOne);
        assertEquals(LPeRelax,LPe.relaxation());
    }


    @Test
    public void senseDoesNotMatterIfObjectiveIsZero(){
        LinearProgram LP1=new LinearProgram(0,0,1,Sense.MAX);
        LP1.setObjective(new double[]{0});
        LinearProgram LP2=new LinearProgram(0,0,1,Sense.MIN);
        LP2.setObjective(new double[]{0});
        assertEquals(LP1,LP2);
    }

    @Test
    public void senseDoesNotMatterIfgobalCoeffIsZero(){
        LinearProgram LP1=new LinearProgram(0,0,1,Sense.MAX);
        LP1.setObjective(new double[]{0});
        LinearProgram LP2=new LinearProgram(0,0,1,Sense.MAX);
        LP2.setObjective(new double[]{2},0);
        assertEquals(LP1,LP2);
    }


    @Test
    public void reversingObjectiveDoesNotChangeTheOptimizationProblem(){
        LinearProgram LPe=new LinearProgram(0,0,2,Sense.MAX);
        LPe.setObjective(new double[]{3,0});
        LinearProgram LPeRelaxReverse=new LinearProgram(0,0,2,Sense.MIN);
        LPeRelaxReverse.setObjective(new double[]{-3,0},-1);
        assertEquals(LPeRelaxReverse,LPe);
    }
    @Test
    /*
    LPeRelaxReverse min -3x
    such that 0<=x<=1 and x,y in R
     */
    public void linearRelaxationOfLPeIsEqualToLPeRelaxReverse(){
        LinearProgram LPe=new LinearProgram(1,1,0,Sense.MAX);
        LPe.setObjective(new double[]{3,0});
        LinearProgram LPeRelaxReverse=new LinearProgram(0,0,2,Sense.MIN);
        LPeRelaxReverse.setObjective(new double[]{-3,0});
        Constraint xnonnegative=new Constraint(new double[]{1,0},ConstraintSense.GREATERTHAN, 0);
        Constraint xlessthanOne=new Constraint(new double[]{1,0},ConstraintSense.LESSTHAN, 1);
        LPeRelaxReverse.addConstraints(xnonnegative);
        LPeRelaxReverse.addConstraints(xlessthanOne);
        assertEquals(LPeRelaxReverse,LPe.relaxation());
    }


}
