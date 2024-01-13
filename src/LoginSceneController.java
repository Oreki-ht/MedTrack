import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LoginSceneController
{
    @FXML
    private TextField nameInput;

    @FXML
    private PasswordField passwordInput;

    static String username;
    static boolean matchFound;
    String userText;
    String pwdText;
    String user;

    static String getUsername()
    {
        return username;
    }
    @FXML
    void btnLoginClicked(ActionEvent event)
    {
            userText = nameInput.getText();
            pwdText = passwordInput.getText();
            try 
            {
                Class.forName("org.postgresql.Driver");
                Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:1412/medloc", "postgres", "conanendou23");
                String query = "SELECT name, password FROM phauth";
                PreparedStatement statement = con.prepareStatement(query);

                ResultSet resultSet = statement.executeQuery();

                // Step 3: Retrieve the result
                matchFound = false;
                while (resultSet.next()) 
                {
                user = resultSet.getString("name");
                String password = resultSet.getString("password");

                if(user.equals(userText) && password.equals(pwdText))
                {
                    JOptionPane.showMessageDialog(null, "Successfull, Redirecting to the dashboard....");
                    matchFound = true;
                    username = user;
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddMedicine.fxml"));
                        Parent root = loader.load();
        
                        // Create a new scene
                        Scene scene = new Scene(root);
        
                        // Get the Stage from the current event source
                        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        
                        // Set the new scene
                        stage.setScene(scene);
                        stage.show();
                        stage.setTitle("Add Medicine");

                    // Exit the loop once a match has been found
                    } catch (Exception e) {
                        e.printStackTrace();
                        
                    }
                } 
                }

                if(!matchFound)
                {
                    JOptionPane.showMessageDialog(null, "Incorrect name or password, Please try again.");
                }
                statement.close();
                con.close();
            }
            catch (Exception fe)
            {
                System.out.println(fe);
            }
        }
}