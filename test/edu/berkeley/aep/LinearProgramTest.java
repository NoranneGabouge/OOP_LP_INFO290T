package edu.berkeley.aep;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LinearProgramTest {

    //LPa : LP with objective function 0, binary variable x and no constraint
    LinearProgram LPa=new LinearProgram(VariableType.BINARY);

    @Test
    public void zeroIsFeasibleForLPa(){
        assertTrue(LPa.isFeasible(0));
    }
}
