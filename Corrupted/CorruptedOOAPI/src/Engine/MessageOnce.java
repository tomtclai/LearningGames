
package Engine;

import javax.swing.JOptionPane;

public class MessageOnce
{
  private static int numAlerts = 1;

  public static void showAlert(String message)
  {
    if(numAlerts > 0)
    {
      JOptionPane.showMessageDialog(null, message);

      numAlerts -= 1;
    }
  }
}
