package com.shapesorter;

import com.shapesorter.dao.DatabaseConnection;
import com.shapesorter.dao.GameDAO;
import com.shapesorter.model.*;
import com.shapesorter.util.GameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method; // Added import
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ShapeSorterGameUI {
    private JFrame frame;
    private JPanel gamePanel;
    private JLabel scoreLabel;
    private int score = 0;
    private GameDAO gameDAO;
    private Object[] shapes;
    private Object[] containers;
    private String selectedShapeIndex = null;
    private Random random = new Random();

    public ShapeSorterGameUI() {
        gameDAO = new GameDAO();
        initializeGame();
        initializeUI();
    }

    private Color getColorFromString(String color) {
        if (color == null) return Color.LIGHT_GRAY;
        return switch (color.toLowerCase()) {
            case "red" -> Color.RED;
            case "blue" -> Color.BLUE;
            case "green" -> Color.GREEN;
            default -> Color.LIGHT_GRAY;
        };
    }

    private void initializeGame() {
        shapes = new Object[5];
        containers = new Object[3];

        String[] shapeTypes = {"Circle", "Square", "Triangle"};

        for (int i = 0; i < shapes.length; i++) {
            String color = GameUtils.getRandomColor();
            String type = shapeTypes[random.nextInt(shapeTypes.length)];
            shapes[i] = createShapeInstance(type, color);
        }

        Set<String> usedColors = new HashSet<>();
        for (int i = 0; i < containers.length; i++) {
            String color;
            do {
                color = GameUtils.getRandomColor();
            } while (usedColors.contains(color));

            usedColors.add(color);
            containers[i] = new Container();
        }
    }

    private Object createShapeInstance(String type, String color) {
        try {
            return Class.forName("com.shapesorter.model." + type)
                    .getConstructor(String.class)
                    .newInstance(color);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Reflection helper methods with proper imports and error handling
    private String getShapeColor(Object shape) {
        try {
            if (shape == null) return "gray";
            Method getColor = shape.getClass().getMethod("getColor");
            return (String) getColor.invoke(shape);
        } catch (Exception e) {
            e.printStackTrace();
            return "gray";
        }
    }

    private String getContainerColor(Object container) {
        try {
            if (container == null) return "gray";
            Method getColor = container.getClass().getMethod("getColor");
            return (String) getColor.invoke(container);
        } catch (Exception e) {
            e.printStackTrace();
            return "gray";
        }
    }

    private boolean canAccept(Object container, Object shape) {
        try {
            if (container == null || shape == null) return false;
            Method canAccept = container.getClass().getMethod("canAccept", shape.getClass());
            return (Boolean) canAccept.invoke(container, shape);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void onInteract(Object shape) {
        try {
            if (shape != null) {
                Method onInteract = shape.getClass().getMethod("onInteract");
                onInteract.invoke(shape);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        frame = new JFrame("Shape Sorter Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton scoresButton = new JButton("View Top Scores");
        scoresButton.addActionListener(e -> showTopScores());

        headerPanel.add(scoreLabel);
        headerPanel.add(scoresButton);

        // Game panel
        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());
        updateGamePanel();

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> newGame());

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> {
            saveScore();
            System.exit(0);
        });

        controlPanel.add(newGameButton);
        controlPanel.add(quitButton);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void updateGamePanel() {
        gamePanel.removeAll();

        // Shapes panel
        JPanel shapesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        shapesPanel.setBorder(BorderFactory.createTitledBorder("Shapes"));

        for (int i = 0; i < shapes.length; i++) {
            if (shapes[i] != null) {
                JButton shapeButton = new JButton(shapes[i].getClass().getSimpleName());
                shapeButton.setBackground(getColorFromString(getShapeColor(shapes[i])));
                int finalI = i;
                shapeButton.addActionListener(e -> selectedShapeIndex = String.valueOf(finalI + 1));
                shapesPanel.add(shapeButton);
            }
        }

        // Containers panel
        JPanel containersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        containersPanel.setBorder(BorderFactory.createTitledBorder("Containers"));

        for (int i = 0; i < containers.length; i++) {
            JButton containerButton = new JButton("Container " + (char)('A' + i));
            containerButton.setBackground(getColorFromString(getContainerColor(containers[i])));
            int finalI = i;
            containerButton.addActionListener(e -> {
                if (selectedShapeIndex != null) {
                    trySortShape(Integer.parseInt(selectedShapeIndex) - 1, finalI);
                }
            });
            containersPanel.add(containerButton);
        }

        // Interact button
        JButton interactButton = new JButton("Interact");
        interactButton.addActionListener(e -> {
            if (selectedShapeIndex != null) {
                interactWithShape(Integer.parseInt(selectedShapeIndex) - 1);
            }
        });

        // Layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(shapesPanel, BorderLayout.NORTH);
        centerPanel.add(containersPanel, BorderLayout.CENTER);
        centerPanel.add(interactButton, BorderLayout.SOUTH);

        gamePanel.add(centerPanel, BorderLayout.CENTER);
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    private void trySortShape(int shapeIndex, int containerIndex) {
        if (shapeIndex < 0 || shapeIndex >= shapes.length || shapes[shapeIndex] == null ||
                containerIndex < 0 || containerIndex >= containers.length) {
            return;
        }

        if (canAccept(containers[containerIndex], shapes[shapeIndex])) {
            score++;
            shapes[shapeIndex] = null;
            JOptionPane.showMessageDialog(frame, "Correct! +1 point");
        } else {
            JOptionPane.showMessageDialog(frame, "Wrong container!");
        }
        scoreLabel.setText("Score: " + score);
        updateGamePanel();
    }

    private void interactWithShape(int shapeIndex) {
        if (shapeIndex >= 0 && shapeIndex < shapes.length && shapes[shapeIndex] != null) {
            onInteract(shapes[shapeIndex]);
            score++;
            shapes[shapeIndex] = null;
            scoreLabel.setText("Score: " + score);
            updateGamePanel();
        }
    }

    private void showTopScores() {
        try {
            StringBuilder scores = new StringBuilder("Top Scores:\n");
            for (String score : gameDAO.getTopScores()) {
                scores.append(score).append("\n");
            }
            JOptionPane.showMessageDialog(frame, scores.toString(), "Top Scores", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading scores: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void newGame() {
        score = 0;
        scoreLabel.setText("Score: " + score);
        initializeGame();
        updateGamePanel();
    }

    private void saveScore() {
        String playerName = JOptionPane.showInputDialog(frame, "Enter your name:", "Save Score", JOptionPane.PLAIN_MESSAGE);
        if (playerName != null && !playerName.trim().isEmpty()) {
            try {
                gameDAO.saveScore(playerName.trim(), score);
                JOptionPane.showMessageDialog(frame, "Score saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to save score: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShapeSorterGameUI());
    }
}