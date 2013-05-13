package com.galaev.tsp.gui.controllers;

import com.galaev.tsp.gui.Message;
import com.galaev.tsp.gui.Prompt;
import com.galaev.tsp.model.Cell;
import com.galaev.tsp.model.Matrix;
import com.galaev.tsp.model.Route;
import com.galaev.tsp.solver.SolverService;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.FillTransition;
import javafx.animation.FillTransitionBuilder;
import javafx.animation.StrokeTransition;
import javafx.animation.StrokeTransitionBuilder;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ContextMenuBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.Lighting;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Class represents a controller (in MVC model) for
 * the main window of the application.
 * 
 * @author Anton Galaev
 */
public class MainController implements Initializable {

    public static final double ANIMATION_DELAY = 0.5;
    public TabPane tabPane;
    public Tab canvasTab;
    public Tab tableTab;
    public Tab resultTab;
    public Pane canvas;
    public GridPane table;
    public TextArea result;
    public MenuItem saveMenuItem;
    public MenuItem randomMenuItem;
    public MenuItem newMenuItem;
    public MenuItem openMenuItem;
    public MenuItem clearMenuItem;
    public MenuItem solveMenuItem;
    public MenuItem abortMenuItem;
    public MenuItem maxRandomMenuItem;
    public CheckMenuItem symmetricMode;
    public RadioMenuItem tableMode;
    public RadioMenuItem canvasMode;
    public Button newButton;
    public Button openButton;
    public Button saveButton;
    public Button solveButton;
    private FileChooser chooser;
    private SolverService service;
    private Matrix matrix;
    private Map<Integer, StringProperty> names;
    private int maxRandomValue = 10;
    private int nodeCounter = 0;

    /**
     * Opens a file dialog, that allows
     * to choose a .txt or .tsp file.
     * Creates a table based on the file data.
     *
     * @param actionEvent action event
     * @throws FileNotFoundException
     */
    public void openFile(ActionEvent actionEvent)
            throws FileNotFoundException {
        File file = chooser.showOpenDialog(table.getScene().getWindow());
        if (file != null) {
            String fileName = file.getName();
            switch (fileName.substring(fileName.lastIndexOf("."))) {
                case ".txt":
                    Scanner inp = new Scanner(file);
                    matrix = new Matrix(inp);
                    break;
                case ".tsp":
                    try {
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                        matrix = (Matrix) ois.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    showMessage("Error message", "Can't open file you've chosen.");
            }
            // Switch to table mode
            tableModeOn(new ActionEvent());
            int number = matrix.getSize();
            // Clear the grid and the map
            table.getChildren().clear();
            names = new HashMap<>();
            // Populate the grid and the map
            for (int i = 0; i < number; ++ i) {
                names.put(i, new SimpleStringProperty(String.valueOf(i + 1)));
            }
            attachHeadings(number + 1);
            for (Cell cell : matrix) {
                attachCell(cell);
            }
            saveMenuItem.setDisable(false);
            saveButton.setDisable(false);
            solveButton.setDisable(false);
            randomMenuItem.setDisable(false);
            symmetricMode.setDisable(false);
            tableMode.setSelected(true);
        }
    }

    public void createNew(ActionEvent actionEvent) {
        Prompt prompt = new Prompt((Stage) table.getScene().getWindow(), "Number of Nodes",
                "Please provide the number of cities (nodes):");
        int number;
        if (prompt.show()) {
            try {
                number = Integer.parseInt(Prompt.result);
            } catch (NumberFormatException nfe) {
                showMessage("Info", "Please provide an integer number.");
                return;
            }
        } else {
            return;
        }
        // Switch to table mode
        tableModeOn(new ActionEvent());
        // Clear the grid and the map
        table.getChildren().clear();
        names = new HashMap<>();
        // Populate the grid and the map
        for (int i = 0; i < number; ++ i) {
            names.put(i, new SimpleStringProperty(String.valueOf(i + 1)));
        }
        attachHeadings(number + 1);
        for (int i = 0; i < number; ++ i) {
            for (int j = 0; j < number; ++ j) {
                Cell cell = new Cell(i == j ? -1 : 0, i, j);
                attachCell(cell);
            }
        }
        tableModeOn(new ActionEvent());
        saveMenuItem.setDisable(false);
        saveButton.setDisable(false);
        solveButton.setDisable(false);
        randomMenuItem.setDisable(false);
        symmetricMode.setDisable(false);
        tableMode.setSelected(true);
    }

    public void saveFile(ActionEvent actionEvent) {
        File file = chooser.showSaveDialog(table.getScene().getWindow());
        if (file != null) {
            List<Cell> cells = extractCells();
            if (cells.size() < 9) {
                showMessage("Info", "Please provide at least 3 nodes.");
                return;
            }
            matrix = new Matrix(cells);
            String fileName = file.getName();
            switch (fileName.substring(fileName.lastIndexOf("."))) {
                case ".txt":
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        PrintStream ps = new PrintStream(fos);
                        ps.print(matrix);
                        ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case ".tsp":
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                        oos.writeObject(matrix);
                        oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    showMessage("Error message", "Can't save file that way.");
            }
        }
    }

    public void useSymmetricTable(ActionEvent actionEvent) {
        ObservableList<Node> nodes = table.getChildren();
        int size = GridPane.getColumnIndex(nodes.get(nodes.size() - 1));
        TextField[][] textFields = new TextField[size][size];
        int k = 2 * size + 1;
        for (int i = 0; i < size; ++ i) {
            for (int j = 0; j < size; ++ j) {
                textFields[i][j] = (TextField) nodes.get(k++);
            }
        }
        if (symmetricMode.isSelected()) {
            for (int i = 0; i < size; ++ i) {
                for (int j = i + 1; j < size; ++ j) {
                    textFields[j][i].textProperty().bindBidirectional(textFields[i][j].textProperty());
                }
            }

        }
    }

    public void solve() {
        switchControls(true);
        if (canvasMode.isSelected()) {
            solveCanvas();
        } else {
            solveTable();
        }
    }

    public void abort(ActionEvent actionEvent) {
        service.cancel();
    }

    public void tableModeOn(ActionEvent actionEvent) {
        if (tableTab.isDisabled()) {
            // Clear the canvas
            canvas.getChildren().clear();
            names = new HashMap<>();
            // Set selections and disable buttons
            solveButton.setDisable(true);
            saveMenuItem.setDisable(true);
            saveButton.setDisable(true);
            tableTab.setDisable(false);
            tabPane.getSelectionModel().select(tableTab);
        }
    }

    public void canvasModeOn(ActionEvent actionEvent) {
        if (! tableTab.isDisabled()) {
            // Clear the canvas, the table and the names map
            canvas.getChildren().clear();
            table.getChildren().clear();
            names = new HashMap<>();
            nodeCounter = 0;
            // Set selections and disable / enable buttons
            tabPane.getSelectionModel().select(canvasTab);
            tableTab.setDisable(true);
            solveButton.setDisable(false);
            saveMenuItem.setDisable(false);
            saveButton.setDisable(false);
            randomMenuItem.setDisable(true);
            symmetricMode.setDisable(true);
        }
    }

    public void clearCanvas(ActionEvent actionEvent) {
        canvas.getChildren().clear();
    }

    public void addNode(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget().equals(canvas) && canvasMode.isSelected()) {
            double fontSize = canvas.getHeight() / 25;
            if (canvas.getWidth() < canvas.getHeight()) {
                fontSize = canvas.getWidth() / 25;
            }
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            names.put(nodeCounter, new SimpleStringProperty(String.valueOf(nodeCounter + 1)));
            Circle circle = createCircle(nodeCounter, x, y);
            Text text = createText(nodeCounter, fontSize, 0, circle);
            ++ nodeCounter;
            canvas.getChildren().addAll(circle, text);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", "*.txt"),
                new FileChooser.ExtensionFilter("Travelling Salesman Project", "*.tsp"));
        saveMenuItem.setDisable(true);
        saveButton.setDisable(true);
        solveButton.setDisable(true);
        randomMenuItem.setDisable(true);
        symmetricMode.setDisable(true);
        tableMode.setSelected(true);
        abortMenuItem.setDisable(true);
        ToggleGroup toggleGroup = new ToggleGroup();
        tableMode.setToggleGroup(toggleGroup);
        canvasMode.setToggleGroup(toggleGroup);
        canvasTab.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {

            }
        });

    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void setMaxRandomValue(ActionEvent actionEvent) {
        Prompt prompt = new Prompt((Stage) table.getScene().getWindow(), "Max Random Value",
                "Please provide the maximum value for random table fill");
        if (prompt.show()) {
            try {
                maxRandomValue = Integer.parseInt(Prompt.result);
            } catch (NumberFormatException nfe) {
                System.out.print(nfe);
            }
        }
    }

    public void randomFill(ActionEvent actionEvent) {
        Random random = new Random();
        ObservableList<Node> nodes = table.getChildren();
        int size = GridPane.getColumnIndex(nodes.get(nodes.size() - 1));
        for (int i = 2 * size + 1; i < nodes.size(); ++ i) {
            TextField textField = (TextField) nodes.get(i);
            if (! GridPane.getRowIndex(textField).equals(GridPane.getColumnIndex(textField))) {
                textField.setText(String.valueOf(random.nextInt(maxRandomValue)));
            }
        }
    }

    /**
     * Attaches a cell to the grid pane.
     * On the grid pane cell represented as
     * a text field. Besides, it has context menu
     * for blocking or allowing the transition.
     *
     * @param cell cell to attach
     * @see com.galaev.tsp.model.Cell
     */
    private void attachCell(Cell cell) {
        // Cell parameters
        int from = cell.getFrom();
        int to = cell.getTo();
        int value = cell.getValue();
        // Create a cell for the grid pane:
        final TextField textCell = TextFieldBuilder.create()
                .text(value == -1 ? "\u221e" : String.valueOf(value))
                .maxHeight(Double.MAX_VALUE)
                .maxWidth(Double.MAX_VALUE)
                .editable(from != to)
                .build();
        GridPane.setHgrow(textCell, Priority.ALWAYS);
        GridPane.setVgrow(textCell, Priority.ALWAYS);
        // Disabling the default context menu:
        final EventDispatcher initial = textCell.getEventDispatcher();
        textCell.setEventDispatcher(new EventDispatcher() {
            @Override
            public Event dispatchEvent(Event event, EventDispatchChain eventDispatchChain) {
                if (event instanceof MouseEvent) {
                    MouseEvent mouseEvent = (MouseEvent)event;
                    if (mouseEvent.getButton() == MouseButton.SECONDARY ||
                            (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.isControlDown())) {
                        event.consume();
                    }
                }
                return initial.dispatchEvent(event, eventDispatchChain);
            }
        });
        // If it's not a diagonal cell, add custom context menu
        if (from != to) {
            // Context menu item for blocking or allowing transition
            final MenuItem menuItem = new MenuItem("Block transition");
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                public boolean blocked = false;
                int value;
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (blocked) {
                        textCell.setText(String.valueOf(value));
                        menuItem.setText("Block transition");
                        blocked = false;
                    } else {
                        value = Integer.parseInt(textCell.getText());
                        textCell.setText("\u221e");
                        menuItem.setText("Allow transition");
                        blocked = true;
                    }
                }
            });
            // Creating and setting the context menu
            final ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().add(menuItem);
            textCell.setContextMenu(contextMenu);
        }
        // Attach the cell to the grid pane
        table.add(textCell, to + 1, from + 1);
    }

    /**
     * Attaches headings to the grid pane.
     * On the grid pane headings represented as
     * labels. Besides, it has context menu
     * for renaming the node.
     *
     * @see com.galaev.tsp.model.Cell
     */
    private void attachHeadings(int size) {
        table.add(LabelBuilder.create()
                .text("T")
                .minWidth(20)
                .maxWidth(Double.POSITIVE_INFINITY)
                .alignment(Pos.CENTER)
                .build(), 0, 0);
        for (int i = 1; i < size; ++ i) {
            final int nodeNumber = i;
            LabelBuilder labelBuilder  = LabelBuilder.create()
                    .alignment(Pos.CENTER)
                    .maxWidth(Double.POSITIVE_INFINITY)
                    .minWidth(20)
                    .contextMenu(ContextMenuBuilder.create()
                            .items(MenuItemBuilder.create()
                                    .text("Rename")
                                    .onAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent actionEvent) {
                                            Prompt prompt = new Prompt((Stage) table.getScene().getWindow(), "Rename a Node",
                                                    "Please provide new name for a city (node):");
                                            if (prompt.show()) {
                                                String newName = Prompt.result;
                                                names.get(nodeNumber - 1).set(newName);
                                            }
                                        }
                                    })
                                    .build())
                            .build());
            final Label label1 = labelBuilder.build();
            final Label label2 = labelBuilder.build();
            label1.textProperty().bind(names.get(nodeNumber - 1));
            label2.textProperty().bind(names.get(nodeNumber - 1));
            table.add(label1, i, 0);
            table.add(label2, 0, i);
            GridPane.setHgrow(label1, Priority.ALWAYS);
            GridPane.setVgrow(label1, Priority.ALWAYS);
            GridPane.setHgrow(label2, Priority.ALWAYS);
            GridPane.setVgrow(label2, Priority.ALWAYS);
        }

    }

    private void solveCanvas() {
        final List<Circle> circles = new ArrayList<>();
        final List<Text> titles = new ArrayList<>();
        for (Node node : canvas.getChildren()) {
            if (node instanceof Circle) {
                circles.add((Circle) node);
            }
            if (node instanceof Text) {
                titles.add((Text) node);
            }
        }
        final int size = circles.size();
        if (size < 3) {
            showMessage("Info", "Please provide at least 3 nodes.");
            return;
        }
        int[][] tempMatrix = new int[size][size];
        for (int i = 0; i < size; ++ i) {
            for (int j = i; j < size; ++ j) {
                if (i == j) {
                    tempMatrix[i][j] = -1;
                } else {
                    tempMatrix[i][j] = distance(circles.get(i), circles.get(j));
                    tempMatrix[j][i] = tempMatrix[i][j];
                }
            }
        }
        matrix = new Matrix(tempMatrix);
        service = new SolverService();
        service.setMatrix(matrix);
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Route route = (Route) event.getSource().getValue();
                result.appendText("\n" + route.toString());
                Line[] lines = new Line[size];
                createLines(lines, circles.toArray(new Circle[circles.size()]), route);
                canvas.getChildren().clear();
                canvas.getChildren().addAll(lines);
                canvas.getChildren().addAll(circles);
                canvas.getChildren().addAll(titles);
                switchControls(false);
            }
        });
        service.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                switchControls(false);
            }
        });
        service.start();
    }

    private void solveTable() {
        List<Cell> cells = extractCells();
        if (cells.size() < 9) {
            showMessage("Info", "Please provide at least 3 nodes.");
            return;
        }
        tabPane.getSelectionModel().select(canvasTab);
        matrix = new Matrix(cells);
        service = new SolverService();
        service.setMatrix(matrix);
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Route route = (Route) event.getSource().getValue();
                result.appendText("\n" + route.toString());
                drawRoute(route);
                switchControls(false);
            }
        });
        service.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                 switchControls(false);
            }
        });
        service.start();
    }

    private List<Cell> extractCells() {
        List<Cell> cells = new ArrayList<>();
        ObservableList<Node> nodes = table.getChildren();
        int size = GridPane.getColumnIndex(nodes.get(nodes.size() - 1));
        for (int i = 2 * size + 1; i < nodes.size(); ++ i) {
            TextField textField = (TextField) nodes.get(i);
            int from = GridPane.getRowIndex(textField) - 1;
            int to = GridPane.getColumnIndex(textField) - 1;
            int value = textField.getText().equals("\u221e") ? -1 : Integer.parseInt(textField.getText());
            cells.add(new Cell(value, from, to));
        }
        return cells;
    }

    private void drawRoute(Route route) {
        // Clear the "canvas"
        canvas.getChildren().clear();
        // Set all the drawing parameters
        int n = route.getRoute().size() - 1;
        double alpha = 2 * Math.PI / n;
        double x = canvas.getWidth() / 2;
        double y = canvas.getHeight() / 2;
        double rad = canvas.getHeight() / 3;
        double fontSize = canvas.getHeight() / 25;
        if (canvas.getWidth() < canvas.getHeight()) {
            rad = canvas.getWidth() / 3;
            fontSize = canvas.getWidth() / 25;
        }
        Circle[] circles = new Circle[n];
        Line[] lines = new Line[n];
        Text[] titles = new Text[n];
        // Circles and text creation
        for (int i = 0; i < n; ++ i) {
            // Create a circle
            circles[i] = createCircle(i, x + rad * Math.sin(alpha * i), y - rad * Math.cos(alpha * i)
            );
            // Create circle's text
            titles[i] = createText(i, fontSize, alpha, circles[i]);
        }
        // Lines creation
        createLines(lines, circles, route);
        // Add everything to the canvas
        canvas.getChildren().addAll(lines);
        canvas.getChildren().addAll(circles);
        canvas.getChildren().addAll(titles);
    }

    private void createLines(Line[] lines, Circle[] circles, Route route) {
        int n = route.getRoute().size() - 1;
        for (int i = 0; i < n; ++ i) {
            int node1 = route.getRoute().get(i);
            int node2 = route.getRoute().get(i + 1);
            // Create line
            lines[i] = createLine(circles[node1], circles[node2]);
            // Circle animation:
            FillTransition fill = FillTransitionBuilder.create()
                    .duration(Duration.seconds(ANIMATION_DELAY))
                    .shape(circles[node1])
                    .fromValue(Color.WHITE)
                    .toValue(Color.BLUE)
                    .delay(Duration.seconds(i * ANIMATION_DELAY))
                    .build();
            fill.play();
            // Lines animation:
            StrokeTransition stroke = StrokeTransitionBuilder.create()
                    .shape(lines[i])
                    .duration(Duration.seconds(ANIMATION_DELAY))
                    .fromValue(Color.BLUE)
                    .toValue(Color.RED)
                    .delay(Duration.seconds(i * ANIMATION_DELAY))
                    .build();
            FadeTransition fade = FadeTransitionBuilder.create()
                    .node(lines[i])
                    .duration(Duration.seconds(ANIMATION_DELAY))
                    .fromValue(0)
                    .toValue(1)
                    .delay(Duration.seconds(i * ANIMATION_DELAY))
                    .build();
            stroke.play();
            fade.play();
        }
    }

    private Circle createCircle(int i, double x, double y) {
        final int nodeNumber = i;
        // Create circle
        final Circle circle = new Circle(x, y, 10);
        circle.getStyleClass().add("circle");
        circle.setEffect(new Lighting());
        circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                circle.setCenterX(mouseEvent.getX());
                circle.setCenterY(mouseEvent.getY());
            }
        });
        // Set context menu for renaming
        circle.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent contextMenuEvent) {
                ContextMenu contextMenu = new ContextMenu();
                contextMenu.getItems().add(MenuItemBuilder.create()
                        .text("Rename")
                        .onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                Prompt prompt = new Prompt((Stage) table.getScene().getWindow(), "Rename a Node",
                                        "Please provide new name for a city (node):");
                                if (prompt.show()) {
                                    String newName = Prompt.result;
                                    names.get(nodeNumber).set(newName);
                                }
                            }
                        })
                        .build());
                contextMenu.show(circle, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
            }
        });
        // Set radius
        circle.radiusProperty().bind(Bindings.min(
                canvas.heightProperty().divide(25),
                canvas.widthProperty().divide(25)));
        return circle;
    }

    private Text createText(int i, final double size, double alpha, Circle circle) {
        final String name = names.get(i).get();
        VPos position = VPos.BOTTOM;
        final DoubleProperty offsetX = new SimpleDoubleProperty();
        final DoubleProperty offsetY = new SimpleDoubleProperty();
        final double beta = alpha * i;
        // Set appropriate offsets
        if (beta >= Math.PI / 2 && beta < 3 * Math.PI / 2)  {
            position = VPos.TOP;
        }
        setOffsets(beta, size, name, offsetX, offsetY);
        // Create text
        final Text text= TextBuilder.create()
                .id("mytext")
                .textOrigin(position)
                .font(Font.font("Times New Roman", size))
                .build();
        text.textProperty().bind(names.get(i));
        text.xProperty().bind(Bindings.add(circle.centerXProperty(), offsetX));
        text.yProperty().bind(Bindings.add(circle.centerYProperty(), offsetY));
        canvas.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                double size = number2.doubleValue() / 25;
                text.setFont(Font.font("Times New Roman", size));
                setOffsets(beta, size, name, offsetX, offsetY);
            }
        });
        text.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                setOffsets(beta, size, text.getText(), offsetX, offsetY);
            }
        });
        return text;
    }

    private void setOffsets(double beta, double size, String name, DoubleProperty offsetX, DoubleProperty offsetY) {
        if (beta < Math.PI / 4) {
            offsetY.set(- size);
        } else if (beta < Math.PI / 2) {
            offsetY.set(- size);
        } else if (beta < 3 * Math.PI / 4) {
            offsetY.set(size);
        } else if (beta < Math.PI) {
            offsetY.set(size);
        } else if (beta < 5 * Math.PI / 4) {
            offsetY.set(size);
            offsetX.set(- size * name.length() / 2);
        } else if (beta < 3 * Math.PI / 2) {
            offsetY.set(size);
            offsetX.set( - size * name.length() / 2);
        } else if (beta < 7 * Math.PI / 4) {
            offsetY.set(- size);
            offsetX.set(- size * name.length() / 2);
        } else {
            offsetY.set(- size);
            offsetX.set(- size * name.length() / 2);
        }
    }

    private Line createLine(Circle c1, Circle c2) {
        Line line = LineBuilder.create()
                .styleClass("line")
                .effect(new Lighting())
                .build();
        // Bind lines to circles
        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());
        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());
        line.strokeWidthProperty().bind(Bindings.min(
                canvas.heightProperty().divide(50),
                canvas.widthProperty().divide(50)));
        return line;
    }

    private int distance(Circle c1, Circle c2) {
        double dx2 = (c1.getCenterX() - c2.getCenterX()) * (c1.getCenterX() - c2.getCenterX());
        double dy2 = (c1.getCenterY() - c2.getCenterY()) * (c1.getCenterY() - c2.getCenterY());
        return (int) (Math.sqrt(dx2 + dy2) + 0.5);
    }

    private void showMessage(String title, String message) {
         new Message((Stage) table.getScene().getWindow(), title, message);
    }

    private void switchControls(boolean value) {
        saveMenuItem.setDisable(value);
        randomMenuItem.setDisable(value);
        newMenuItem.setDisable(value);
        openMenuItem.setDisable(value);
        clearMenuItem.setDisable(value);
        solveMenuItem.setDisable(value);
        maxRandomMenuItem.setDisable(value);
        symmetricMode.setDisable(value);
        tableMode.setDisable(value);
        canvasMode.setDisable(value);
        newButton.setDisable(value);
        openButton.setDisable(value);
        saveButton.setDisable(value);
        solveButton.setDisable(value);
        abortMenuItem.setDisable(! value);
    }
}