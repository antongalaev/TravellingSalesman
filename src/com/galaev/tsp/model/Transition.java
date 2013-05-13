package com.galaev.tsp.model;

import java.io.Serializable;

/**
 * Class {@code Transition} represents
 * a transition from one node to another.
 * Class instance contains the indices of
 * the start node and the end node.
 * Thus, it may be considered as a single
 * edge in the graph.
 *
 * @author Anton Galaev
 * @see com.galaev.tsp.model.Matrix
 * @see com.galaev.tsp.model.Cell
 */
public class Transition implements Serializable {

    /* Start node */
    private int from;

    /* End node */
    private int to;

    /**
     * Getter for the start node of the transition.
     *
     * @return start node index
     */
    public int getFrom() {
        return from;
    }

    /**
     * Getter for the end node of the transition
     *
     * @return end node index
     */
    public int getTo() {
        return to;
    }

    /**
     * Public constructor for the class.
     *
     * @param from index of the start node
     * @param to index of the end node
     */
    public Transition(int from, int to) {
        this.from = from;
        this.to = to;
    }
}