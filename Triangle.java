package com.shapesorter.model;

public class Triangle extends Shape {
    public Triangle(String color) {
        super(color);
    }

    @Override
    public void display() {
        System.out.print("^" + getColor().charAt(0));
    }

    @Override
    public void onInteract() {
        System.out.println("A " + getColor() + " triangle responded to your touch!");
    }
}