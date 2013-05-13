package com.galaev.tsp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class {@code Route} represents a solution of
 * Travelling Salesman Problem. It contains
 * the complete route through all the cities (nodes of
 * the graph) and the cost of this route (in other words,
 * its length).
 *
 * @author Anton Galaev
 * @see com.galaev.tsp.model.Transition
 */
public class Route {

    /* The cost (length) of the route */
    private int cost;

    /* The route itself as ordered list of node indexes */
    private List<Integer> route;

    /**
     * Getter for the cost (length) of the route.
     *
     * @return cost of the route
     */
    public int getCost() {
        return cost;
    }

    /**
     * Getter for route itself.
     * Returns ordered list of node indexes.
     *
     * @return route list
     */
    public List<Integer> getRoute() {
        return route;
    }

    /**
     * Public constructor for class {@code Route}.
     * Creates an instance with the given cost and
     * route, extracted from transitions list.
     *
     * @param cost cost of the route
     * @param transitions performed transitions
     */
    public Route(int cost, List<Transition> transitions) {
        this.cost = cost;
        route = new ArrayList<>(transitions.size() / 2);
        route.add(transitions.get(0).getFrom());
        for (Transition transition : transitions) {
            route.add(transition.getTo());
        }
    }

    /**
     * Returns a string representation of the object.
     * That is route cost and the description of the route.
     *
     * @return string, containing route description and its cost
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("The shortest possible route is the following:\n");
        for (Integer i : route) {
            result.append("City #" + i + " ->\n");
        }
        result.append("\nTotal cost of the route is " + cost);
        return result.toString();
    }
}
