package edu.berkeley.aep;

public enum VariableType {
    BINARY;

    public boolean hasInstance(double v) {
        if (this==BINARY){
            if ((v==0)||v==1){
                return true;
            }
            return false;
        }
        return false;
    }
}
