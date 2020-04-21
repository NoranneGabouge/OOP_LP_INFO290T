package edu.berkeley.aep;

public enum Sense {
    MIN,MAX;

    Sense reverse(){
        if (this.equals(MIN)) return MAX;
        return MIN;
    }
}
