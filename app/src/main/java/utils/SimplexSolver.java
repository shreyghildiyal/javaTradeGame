package utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimplexSolver {
    
    private Map<String, Float> costWeights;
    private int constraintCount = 0;

    public SimplexSolver(Map<String, Float> costWeights) {
        this.costWeights = costWeights;
    }
}
