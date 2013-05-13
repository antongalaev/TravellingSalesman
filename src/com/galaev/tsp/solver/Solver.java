package com.galaev.tsp.solver;

import com.galaev.tsp.model.Matrix;
import com.galaev.tsp.model.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class {@code Solver} provides
 * branch & bound method to solve the
 * Travelling Salesman Problem.
 * Class instance processes a matrix
 * of costs of transitions between nodes.
 *
 * @author Anton Galaev
 * @see com.galaev.tsp.model.Matrix
 * @see com.galaev.tsp.model.Cell
 */
public class Solver {

    /* The only instance of Solver */
    private static final Solver instance = new Solver();

    /**
     * Private default constructor.
     * No one can instantiate Solver.
     */
    private Solver() { }

    /**
     * Returns the solver instance for the application.
     *
     * @return the Solver instance
     */
    public static Solver getInstance() {
        return instance;
    }

    /**
     * A method for processing the input matrix.
     * Implements Branch & Bound algorithm to solve
     * the Travelling Salesman Problem.
     *
     * @param mx input matrix
     * @return result route and its cost
     */
    public Route process(Matrix mx) {

        // List of possible solutions to choose from.
        List<Matrix> waiting = new ArrayList<>();

        while (true) { // repeat until the solution is found
            if (mx.getSize() == 1) { // matrix is processed
                if (mx.getCell(0).getValue() == -1) { // bad matrix, no solution
                    mx.setCost(Integer.MAX_VALUE);
                }
                else { // solution is found
                    mx.addTransition(mx.getCell(0).getFrom(), mx.getCell(0).getTo());
                    return new Route(mx.getCost(), mx.getTransitions());
                }
            }
            // Find minimum cost in waiting list
            Matrix minMx;
            if (waiting.isEmpty()) {
                minMx = mx;
            } else {
                minMx = Collections.min(waiting);
            }
            // If current matrix is not the best,
            // add it to the waiting list
            if (minMx.getCost() < mx.getCost()) {
                waiting.add(mx);
                mx = minMx;
            }
            // Current size
            int s = mx.getSize();
            // Subtracting minimums
            // through rows:
            for (int i = 0; i < s; ++ i) {
                int min = Integer.MAX_VALUE;
                // find a minimum
                for (int j = 0; j < s; ++ j) {
                    if (mx.getCell(i * s + j).getValue() == -1) {
                        continue;
                    }
                    if (mx.getCell(i * s + j).getValue() < min) {
                        min = mx.getCell(i * s + j).getValue();
                    }
                    if (min == 0) {
                        break;
                    }
                }
                // subtract a minimum
                if (min > 0 && min != Integer.MAX_VALUE) {
                    for (int j = 0; j < s; ++ j) {
                        if (mx.getCell(i * s + j).getValue() == -1) {
                            continue;
                        }
                        mx.decreaseCellValue(i * s + j, min);
                    }
                    mx.setCost(mx.getCost() + min);
                }
            }
            // Subtracting minimums
            // through columns:
            for (int i = 0; i < s; ++ i) {
                int min = Integer.MAX_VALUE;
                // find a minimum
                for (int j = 0; j < s; ++ j) {
                    if (mx.getCell(i + j * s).getValue() == -1) {
                        continue;
                    }
                    if (mx.getCell(i + j * s).getValue() < min) {
                        min = mx.getCell(i + j * s).getValue();
                    }
                    if (min == 0) {
                        break;
                    }
                }
                // subtract a minimum
                if (min > 0 && min != Integer.MAX_VALUE) {
                    for (int j = 0; j < s; ++ j) {
                        if (mx.getCell(i + j * s).getValue() == -1) {
                            continue;
                        }
                        mx.decreaseCellValue(i + j * s, min);
                    }
                    mx.setCost(mx.getCost() + min);
                }
            }
            // Now looking for a zero
            int f = mx.getCurrent();
            int t = mx.findNextNode();
            if (t == -1) { // nowhere to go now with this matrix
                mx.setCost(Integer.MAX_VALUE);
                continue;
            }
            // Memorize the alternative way (not going to 't'-node from 'f'-node)
            Matrix alternative = new Matrix(mx);
            alternative.blockCell(f, t);
            waiting.add(alternative);
            // Update the answer
            mx.addTransition(f, t);
            // Update the current node
            mx.setCurrent(t);
            // Block used cell (transition)
            mx.blockCell(t, f);
            // Update the size
            mx.setSize(mx.getSize() - 1);
            // If we still have a lot nodes to go,
            // we can't go to the start point
            if (mx.getSize() > 1) {
                mx.blockCell(t, 0);
            }
            // Remove redundant row and column
            for (int i = 0; i < mx.getListLength(); ++ i) {
                if (mx.getCell(i).getFrom() == f || mx.getCell(i).getTo() == t) {
                    mx.removeCell(i);
                    -- i;
                }
            }
        }
    }
}