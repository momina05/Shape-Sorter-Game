package com.shapesorter.model;

public class Containers {
    private final String color;

    public Containers(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void display() {
        System.out.println("Container: " + color);
    }

    public boolean canAccept(Shapes shape) {
        return this.color.equalsIgnoreCase(shape.getColor());
    }
}
