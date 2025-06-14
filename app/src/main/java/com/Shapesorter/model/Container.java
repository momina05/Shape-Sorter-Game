package com.shapesorter.model;

public class Container {
    private final String color;

    public Container(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void display() {
        System.out.println("Container: " + color);
    }

    public boolean canAccept(Shape shape) {
        return this.color.equalsIgnoreCase(shape.getColor());
    }
}