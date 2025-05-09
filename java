interface Interactive{
  void onInteract();
}
abstract class Shape implements Interactive{
  private String color;
  public Shape(String color){
    this.color = color;
  }
  public String getColor(){
    return color;
  }
  public abstarct void display();
  
  @Override
  public void onInteract(){
    System.out.println("You interacted with a" +getColor()+ "shape.");
  }
}

class circle extends Shape
{
  public circle(String color)
{
  super(color);
}
@Override
public void display()
{
system.out.print("O"+getColor().charAt(0));
}

@Override
public void onInteract()
{
  Ssytem.out.println("You tapped a"+getColor()+"Circle");
}
}