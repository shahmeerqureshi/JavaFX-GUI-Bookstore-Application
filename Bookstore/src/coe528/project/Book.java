package coe528.project;


public class Book {
    
     String name;
     double price;

    
    public Book() {
    this.name = "";
    this.price = 0;
               
    }
    
    public Book(String name, double price) {
        this.name = name;
        this.price = price;
        
    }
    
   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    @Override
    
    public String toString() {
        
        return "Name: " + name + " Price: " + price;
        
    }
    
    
}
