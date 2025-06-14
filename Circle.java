package com.shapesorter.model;

public class Circle extends Shape {
    public Circle(String color) {
        super(color);
    }

    @Override
    public void display() {
        System.out.print("O" + getColor().charAt(0));
    }

    @Override
    public void onInteract() {
        System.out.println("You tapped a " + getColor() + " circle!");
    }
}
