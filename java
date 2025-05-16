import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

//Interface for interactive game elements
interface Interactive{
  void onInteract();
}
//Base class for all shapes
abstract class Shape implements Interactive{
  private String color;

  //Constructor for Shape
  public Shape(String color){
    this.color = color;
  }
  public String getColor(){
    return color;
  }
  //Abstract method to display the shape (for polymorphsm)
  public abstarct void display();
  
  //Default interaction for a shape
  @Override
  public void onInteract(){
    System.out.println("You interacted with a" +getColor()+ "shape.");
  }
}

//Concrete class for Circle,inheriting from Shape
class circle extends Shape{ 
  //Constructor for Circle 
  public circle(String color){
  super(color);
}

@Override
public void display(){
System.out.print("O"+getColor().charAt(0));
}

@Override
public void onInteract(){
  sytem.out.println("You tapped a"+getColor()+"Circle");
}
}

//Concrete class for Square, inheriting from Shape
class Square extends Shape{
  //Constructor for Square
  public Square(String color){
    super(color);
  }
  @Override
  public void display(){
    System.out.print("[" +getColor().charAt(0) +"]");
  }
  @Override
  public void onInteract(){
    System.out.println("You clicked a" +getColor() +"Square");
  }
}

//Concrete class for Triangle,inheriting from Shapes
class Triangle extends Shape{
  //Constructor for Triangle
  public Triangle(String color){
    super(color);
  }

  @Override 
  public void display(){
    System.out.print("^"+ getColor().charAt(0));
  }

  @Override
  public void onInteract(){
  System.out.println("A" + getColor() + "triangle responded at your touch!");
  }
}

//Class representing a container
class Container{
  private String color;

  //Constructor for Container
  public Container(String color){
    this.color = color;
  }

  public String getColor(){
    return color;
  }

  public void display(){
    System.out.println("Container: " + color);
  }

  public boolean canAccept(Shape shape){
    return this.color.equalsIgnoreCase(shape.getColor());
  }
}

class ShapeSorter{
  private Shape[] shapes; //Array to hold shapes
  private Container[] containers; //Array to hold containers
  private int Score;
  private Random random = new Random();
  private Scanner sacnner = new Scanner(System.in);
  private final String[] possibleColors = {"Red","Blue","Green"};
}
//Constructor for ShapeSorter
public ShapeSorter(int numShapes, int numContainers) {
  this.shapes = new Shape[numShapes];
  this.containers = new Container[numContainers];
  this.score = 0;
  initializaGame();
}

private void initializaGame(){
  //initializing shapes with random colors and types
  String[] possibleTypes = {"Circle","Square","Triangle"};
  for (int i = 0; i < shapes.length; i++) {
    String color = possibleColors[random.nextInt(possibleColors.length)];
    String type = possibleTypes[random.nextInt(possibleTypes.length)];
    switch (types){
      case "Circle":
           shapes[i] = new Circle(color);
           break;
      case "Square":
           shapes[i] = new Square(color);
           break;
      case "Triangle":
           shapes[i] = new Triangle(color);
           break;     
    }
  }

  //initialize containers with distinct colors
  List<String>availableContainerColors = new ArrayList<>(List of(possibleColors));
  for(int i = 0; i < container.length; i++){
    if(availableContainerColors.isEmpty()){
      System.out.println("Not enough distinct colors for the requested number of containers.");
      break;
    }
    int randomIndex = random.nextInt(availableContainerColors.remove(randomIndex));
  }
}

public void displayChallenge(){
  System.out.println("\n--- Shape Sorter Challenge ---");
  System.out.println("Score: " +score);
  System.out.println("\nShapes to sort:");
 
  for(int i = 0; i < shapes.length; i++){
    System.out.print((i + 1)+ ".");
    shapes[i].display(); //Polymorphic call to display
    System.out.print("");
  }
 
  System.out.println("\nContainers.");
  for(int i = 0; i < containers.lenght; i++){
    System.out.print((char)('A' + i)+ ".");
    containers[i].display();
  }
  System.out.println("\nEnter the shape number(1- "+shapes.length+) and container letter(A- "+(char('A'+containers.length- 1)+") 
                      to sort(e.g., 1A) or 'interact[shape number]' or 'quit. ");
}

public void processInput(String input){
if(input.equalsIgnoreCase("quit")){
System.out.println("Thanks for Playing! Final Score:" + score );
System.exit(0);
}
else if (input.startsWith("interact")){
  String numberStr = input.subString("interact".length());
if (isNumeric (numberStr)){
    int shapeIndex = Integer..parseInt(numberStr) -1;
    if(shapeIndex >= 0 && shapeIndex < shapes.length && shapes [shapeIndex] != null) {
        shapes[shapeIndex].onInteract(); // call to onInteract
}
}
else {
System.out.println("Invalid shape number for interaction.");
}
}
else if (input.length() == 2){
  char shapeChar =input.charAt(0);
  char containerChar = input.charAt(1);

  if (Character.isDigit(shapeChar) && Character.isLetter(containerChar)) {
    int shapeIndex = Character.getNumericValue(shapeChar) -1;
    int containerIndex = Character.toUpperCase(containerChar) -'A';

     if (shapeIndex >= 0 && shapeIndex < shapes.length && shapes[shapeIndex] != null containerIndex >= 0  containerIndex < containers.length) {
     
     if (containers[containerIndex].canAccept(shapes[shapeIndex])){
     System.out.println("Correctly sorted shape " + (shapeIndex + 1) + "into container" + (char) ('A' + containerIndex) + "!");
     score++;
     shapes[shapeIndex] = null;
     }
     else {
     System.out.println("Incorrect sort. That shape does not belong to that container.");
     }
}
else {
     System.out.println("Invalid shape number or container letter.");
}
}
else {
     System.out.println("Invalid input format. Use format '1A'.");
}
}
else {
     System.out.println("Invalid input. Please use correct input.");
}
}

public boolean is GameOver(){
      for(Shape shape : shape){
        if(shape != null){
           return false; //There are still unsorted shapes
           }
        }
        return true;
}
public void playGame(){
     System.out.println("Welcome to the Ultimate Shape Sorter Challenge!");
     while(!isGameOver()){
        displayChallenge();
        String input = scanner.nextLine().trim();
        processInput(input);
     }
     System.out.println("\n---Game Over!---);
     System.out.println("Final Score:" +score+ "out of" +shapes.length+ ".");
     scanner.close();
}

public static void main(String args[]){
    GameManager game = new GameManager(5,3); //Creating a game with 5 shapes and 3 containers
    game.playGame();
  }
}