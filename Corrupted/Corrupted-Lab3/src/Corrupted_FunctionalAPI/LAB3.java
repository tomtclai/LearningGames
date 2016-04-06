package Corrupted_FunctionalAPI;
/**
 * LAB 3: If/Else Lab
 * 
 * @author Rachel Horton
 */
public class LAB3 extends CorruptedFunctionalAPI
{
    
   public void buildWorld()
   {
       
   }
   
   public void updateWorld()
   {
       if(pressingRight())
       {
           drawTile(22,5, "green");
       }
       else if(pressingLeft())
       {
           drawTile(1,5, "red"); 
       }
       else if (pressingUp())
       {
           drawTile(8,8,"yellow");
       }
       else if (pressingDown())
       {
           drawTile(1,1,"blue");
       }
    }
}
