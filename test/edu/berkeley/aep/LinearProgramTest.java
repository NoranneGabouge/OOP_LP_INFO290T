package edu.berkeley.aep;

import org.junit.Test;

import static org.junit.Assert.*;

public class LinearProgramTest {

    //LPa : LP with objective function 0, binary variable x and no constraint
    LinearProgram LPa=new LinearProgram(1,0,0);

    @Test
    public void zeroIsFeasibleForLPa(){
        assertTrue(LPa.hasFeasiblePoint(new int[]{0}, new int[0],new double[0]));
    }

    @Test
    public void twoIsNotFeasibleForLPa(){
        assertFalse(LPa.hasFeasiblePoint(new int[]{2}, new int[0],new double[0]));
    }
}
