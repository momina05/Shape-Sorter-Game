package com.shapesorter.model;

public class Square extends Shape {
    public Square(String color) {
        super(color);
    }

    @Override
    public void display() {
        System.out.print("[" + getColor().charAt(0) + "]");
    }

    @Override
    public void onInteract() {
        System.out.println("You clicked a " + getColor() + " square!");
    }
}