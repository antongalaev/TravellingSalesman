package com.galaev.tsp.solver;

import com.galaev.tsp.model.Matrix;
import com.galaev.tsp.model.Route;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Class {@code SolverService} is a service, that
 * allows to perform tasks, that solve travelling salesman
 * problem. As it extends {@link javafx.concurrent.Service} tasks
 * are performed concurrently. A matrix must be set before starting
 * the service. Tasks return {@link com.galaev.tsp.model.Route} as
 * result.
 *
 * @author Anton Galaev
 * @see com.galaev.tsp.model.Matrix
 * @see com.galaev.tsp.solver.Solver
 */
public class SolverService extends Service<Route> {

    /* Private Matrix Property */
    private ObjectProperty<Matrix> matrixProperty = new SimpleObjectProperty<>();

    /**
     * Getter for the matrix.
     *
     * @return a Matrix object
     */
    public Matrix getMatrix() {
        return matrixProperty.get();
    }

    /**
     * Setter for the matrix.
     *
     * @param matrix a Matrix object
     */
    public void setMatrix(Matrix matrix) {
        matrixProperty.set(matrix);
    }

    /**
     * Getter for the matrix property itself.
     *
     * @return the matrix property
     */
    public ObjectProperty<Matrix> matrixProperty() {
        return matrixProperty;
    }

    /**
     * Implementation of the task creation method.
     * Creates a new task for TSP solving n the given matrix.
     *
     * @return new task
     */
    @Override
    protected Task<Route> createTask() {
        return new Task<Route>() {
            @Override
            protected Route call() throws Exception {
                Solver solver = Solver.getInstance();
                return solver.process(getMatrix());
            }
        };
    }
}
