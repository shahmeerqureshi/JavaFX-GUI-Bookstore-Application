
package coe528.project;

import javafx.collections.ObservableList;

public class Customer {

    private String username;
    private String password;
    private double points;
    private String status;
        
    public Customer() {
        this.username = " ";
        this.password = " ";
        this.points = 0;
    
    }
    public Customer(String username, String password, double points) {
        
        this.username = username;
        this.password = password;
        this.points = points;
        
    }
    public double getPoints() {
        return points;
    }

    public void setPoints(double pointss) {
        points = pointss;
    }

    public String getStatus() {
        return status;
    }
    
     public void setUsername(String username) {
        this.username = username;
    }

    
    public String getUsername() {
        return username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    
    public String getPassword() {
        return password;
    }

    
    public String PointsToStatus(double points) {
        
        if (points < 1000) {
            status = "silver";
        }
        
        else if (points >= 1000) {

            status = "gold";
        }
        
        return status;
    }
    
     public String PrintWelcomeMessage() {
       PointsToStatus(getPoints());
       return "Welcome" + " " + getUsername() + ". You have: " + getPoints() + " points. Your status is " + getStatus() + ".";
       
    }
    
     public String FileStringReader() {
         
         return getUsername() + "," + getPassword() + "," + getPoints();
              
     }
    
     public void Redeem(ObservableList<Book> SelectedBooks) {
       
          
          
          
          double TotalCost = 0;
          double updatedCost;
          for(int l = 0; l < SelectedBooks.size(); l++) {  
          
          TotalCost += SelectedBooks.get(l).getPrice();
          
          }
          
          if (getPoints()*0.01 >= TotalCost) {
              
               try {
              
          
          
          RedeemPoints(getPoints(), TotalCost);
          updatedCost = updateTC(getPoints(), TotalCost);
          PointsToStatus(getPoints());
          
          System.out.println(getPoints());
          System.out.println(updatedCost);
          
          }
          
          catch (Exception e) {
              System.out.println("Add a book to your cart before clicking redeeming points");
          }
        
    }
          else {
              
              System.out.println("Not enough points");
          }
        
    }
    
    public void Buy(ObservableList<Book> SelectedBooks) {
        try {
          
          
      
          double TotalCost = 0;
          for(int l = 0; l < SelectedBooks.size(); l++) {  
          
          TotalCost += SelectedBooks.get(l).getPrice();
          
          }
          double updatedpoints = UpdatePoints(getPoints(), TotalCost);
          setPoints(updatedpoints);
          PointsToStatus(updatedpoints);
          System.out.println(getPoints());
          System.out.println(getStatus());
          System.out.println(TotalCost); 
          }
          
          catch (Exception e) {
              System.out.println("Add a book to your cart before clicking buy");
          }
        
     
}
      

    
    public String PrintUpdatedInfo() {
        UpdatePoints(getPoints(), 100);
        PointsToStatus(getPoints());
       return UpdatedMessage();
       
    }
      public String TotalCostString(double TotalCost) {
        
        return "Total Cost: " + TotalCost;
    }
     public double UpdatePoints(double points, double totalcost) {
        
        double updatedPoints;
        updatedPoints = points + totalcost*10;
        return  updatedPoints;
    }
     
     public void RedeemPoints(double points, double totalcost) {
         
         double TotalCost = totalcost;
         double updatedPoints = 0;
         if(points > 100) {
             if(0.01*points > TotalCost) {
                 updatedPoints = points - (100*TotalCost);
                 setPoints(updatedPoints);
             }
             else if (0.01*points < TotalCost) {
                 setPoints(0);
             }
         
     
         } 
     }
   
     public static double updateTC(double points, double totalCost) {
         
            double totalcost = totalCost;
            if(points > 100) {
             if(0.01*points > totalCost) {
                 totalcost = 0;
                 
             }
             else if (0.01*points < totalCost) {
                 totalcost = totalCost - 0.01*points;
                 
             }
         
     }
            return totalcost;
     }
    
    @Override
    public String toString() {
        
        return "Welcome" + " " + username + ". You have: " + points + " points. Your status is " + status + ".";
        
    }
    
    
    public String UpdatedMessage() {
        
        return "You now have: " + points + " points. Your updated status is: " + status + ".";
        
    }

   
}


