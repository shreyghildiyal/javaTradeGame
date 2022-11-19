package utils;

import java.util.Map;

public class SimplexSolver {

    private Map<String, Float> costWeights;
    private int constraintCount = 0;

    public SimplexSolver(Map<String, Float> costWeights) {
        this.costWeights = costWeights;
        System.out.println(this.costWeights.size());
        System.out.println(constraintCount);
    }
}
