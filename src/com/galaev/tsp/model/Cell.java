package com.galaev.tsp.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Class {@code Cell} represents a cell in the matrix.
 * A cell, in turn, represents the cost of transition
 * from one node to another. Thus, a cell
 * contains not only the indices of start and end points,
 * but also a value, that represents
 * the cost of the transition.
 *
 * @author Anton Galaev
 * @see com.galaev.tsp.model.Matrix
 */
public class Cell implements Serializable {

    /*
     * Start point index.
     * Read-only property.
     */
    private int from;

    /*
     * End point index.
     * Read-only property.
     */
    private int to;

    /*
     * Value of the cell, that represents
     * the cost of the transition.
     * Read-write property.
     */
    private transient IntegerProperty value = new SimpleIntegerProperty(0);

    /**
     * Getter for the value of a cell.
     *
     * @return cell value
     */
    public int getValue() {
        return value.get();
    }

    /**
     * Setter for the value of a cell.
     *
     * @param value cell value
     */
    public void setValue(int value) {
        this.value.set(value);
    }

    /**
     * Getter for the start index of the transition.
     * Returns row index of the cell.
     *
     * @return start point index
     */
    public int getFrom() {
        return from;
    }

    /**
     * Getter for the end index of the transition.
     * Returns column index of the cell.
     *
     * @return end point index
     */
    public int getTo() {
        return to;
    }

    /**
     * Public constructor for a cell, with 3 parameters.
     * Creates a cell with specified value and matrix indices.
     *
     * @param from start point index
     * @param to end point index
     */
    public Cell(int value, int from, int to) {
        this.value.set(value);
        this.from = from;
        this.to = to;
    }

    /**
     * Copy constructor for a cell.
     * Creates a cell with the same matrix indices,
     * but the value is specified separately.
     *
     * @param source old cell
     * @param value new value for a new cell
     */
    public Cell(Cell source, int value) {
        this.value.set(value);
        this.from = source.getFrom();
        this.to = source.getTo();
    }

    /**
     * Overridden {@code equals} method.
     * Cells are considered equal, if all their fields
     * are equal.
     *
     * @param other other object
     * @return true, if all fields of cells are equal,
     *         false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other){
            return true;
        }
        if (other instanceof Cell) {
            Cell cell = (Cell) other;
            if (this.value.get() == cell.value.get() &&
                this.from == cell.from &&
                this.to == cell.to) {
                return true;
            }
        }
        return false;
    }

    /**
     * Overridden {@code hashCode} method.
     * Generates unique hash value for a cell.
     *
     * @return hash value
     */
    @Override
    public int hashCode() {
        int hash = 1;
        hash = 13 * hash + from;
        hash = 13 * hash + to;
        hash = 13 * hash + value.get();
        return hash;
    }

    /**
     * Writes cell object properly during the serialization.
     *
     * @param oos object output stream
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(value.get());
    }

    /**
     * Reads cell object properly during the serialization.
     *
     * @param ois object input stream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        value = new SimpleIntegerProperty(ois.readInt());
    }
}