import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrationSceneController {

    @FXML
    private TextField pLocation;

    @FXML
    private TextField pLicense;

    @FXML
    private TextField pName;

    @FXML
    private PasswordField pPassword;

    @FXML
    private TextField pPhone;

    @FXML
    private Button registerBtn;

    boolean success;

    @FXML
    void registerBtnClicked(ActionEvent event) 
    {
        String nameTxt, pwdTxt, locationTxt, phoneTxt, licenseTxt;
        nameTxt = pName.getText();
        pwdTxt = pPassword.getText();
        locationTxt = pLocation.getText();
        phoneTxt = pPhone.getText();
        licenseTxt = pLicense.getText();
        if(nameTxt == "" || pwdTxt == "" || locationTxt == "" || phoneTxt == "" || licenseTxt == ""){
         JOptionPane.showMessageDialog(null, "Please enter all elements and try again.");
         pName.setText("");
         pPassword.setText("");
         pPhone.setText("");
         pLocation.setText("");
         pLicense.setText("");
        }else {
            try{
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:1412/medloc", "postgres", "conanendou23");
            String query = "INSERT INTO phauth (name, location, phonenum, password, license) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, nameTxt);
            ps.setString(2, locationTxt);
            ps.setString(3, phoneTxt);
            ps.setString(4, pwdTxt); 
            ps.setString(5, licenseTxt);

            int change = ps.executeUpdate();
            ps.close();
            con.close();

            if(change > 0){
                success = true;
            } else 
            {
                showAlert(Alert.AlertType.WARNING, "Warning", "This is a warning dialog.");     
            }
            
        }catch(Exception e)
        {System.err.println(e);
        showAlert(Alert.AlertType.WARNING, "Error", "You have entered a duplicated value, Enter the correct information and try again");}
        }
        if(success){
             try {
                 FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScene.fxml"));
                 Parent root = loader.load();
                 // Create a new scene
                 Scene scene = new Scene(root);
                 // Get the Stage from the current event source
                 Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                 // Set the new scene
                 stage.setScene(scene);
                 stage.show();
             } catch (Exception e) {
                System.err.println(e);
             }
            JOptionPane.showMessageDialog(null, "Pharmacy registered successfully...");
        }
        }
        

    private void showAlert(Alert.AlertType alertType, String title, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // You can set a header text if needed
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
