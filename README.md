# Linear Optimization Program Implementation

Completed: Spring 2020

The goal of this project is to implement (with Object Oriented Programming) instances of a **Linear Optimization Program (LP)** using Test-Driven Development

A **Linear Optimization Program (LP)** is a problem that can formulated as follows:   
```(LP) max/min c^T x``` 
*such that*   
         ```a_1^T x <= b_1,```  
         ```a_2^T x <= b_2,```  
         ```...```  
         ```a_m^T x <= b_m```  
    **x in R^n / x in Z^n**

In the formulation above, the model optimization direction is either ***max*** or ***min***;
```c``` is the ***cost vector*** (in R^n or Z^n), ```x``` is the ***variable***, and (```a_i^T x <= b_i```) are the ***constraints*** (the direction of the inequality can be ```>=```,```<=```,```=```)

A special case of (LP) is a Mixed Integer Program ***(MIP)***, where some coefficients of x are integers and the other coefficients are in R.
Therefore, in the code, a distinction is made between ***real***, ***integer*** and ***binary*** variables (binary=x in {0,1}).   
Note that having a binary variable is equivalent to defining this variable in Z and adding the constraints >=0 and <=1.   

Definition : x is a ***feasible solution*** for (LP) if it satisfies all the constraints   
Definition : the ***linear relaxation*** of an (MIP) (also called LP relaxation) is obtained by relaxing the integrity constraints
(i.e. by replacing x in Z by x in R, and x binary by 0<=x<=1)

The implementation can be found at ```src/edu/berkeley/aep/LinearProgram.java``` and unit tests at ```test/edu/berkeley/aep/LinearProgram.java```

