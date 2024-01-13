import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AddMedicineController {

    @FXML
    private Button addBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private TextField mdBrand;

    @FXML
    private TextField mdCode;

    @FXML
    private TextField mdExpiry;

    @FXML
    private TextField mdName;

    @FXML
    private TextField mdPrice;

    @FXML
    private TextField mdQuantity;

    @FXML
    private TextField mdType;

    @FXML
    private Button sInventoryBtn;

    @FXML
    private Button sModify;

    @FXML
    private Label userLabel;

    @FXML
    private Pane sidebar;

    @FXML
    private Button logoutBtn;

    String user = LoginSceneController.getUsername();
    
    boolean success;
    int medPrice, medQuantity;

    @FXML
    private void initialize(){
      userLabel.setText(user);
    }

    @FXML
    void addBtnClicked(ActionEvent event) {
      String medName = mdName.getText();
      String medCode = mdCode.getText();
      String medType = mdType.getText();
      String medBrand = mdBrand.getText();
      String medExpiry = mdExpiry.getText();
      try {
        medPrice = Integer.parseInt(mdPrice.getText());
        medQuantity = Integer.parseInt(mdQuantity.getText());
      } catch (Exception e) {
      }
      if(medName==""||medCode==""||medType==""||medBrand==""||medExpiry=="")
      {
      showAlert(Alert.AlertType.WARNING, "Error", "You didn't enter all the required fields, please enter them all and try again");
      } else if(!(isValidInteger(mdPrice.getText()) && isValidInteger(mdQuantity.getText()))){
        showAlert(Alert.AlertType.WARNING, "Error", "Enter Integer numbers in price and quantity please");
        
      } else {
        try{
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:1412/medloc", "postgres", "conanendou23");
        String query = "INSERT INTO Medicine (\"PharmacyName\", \"Medicine Name\" , \"Medicine Code\" , \"Medicine Brand\", \"Type\", \"Expiry date\", \"Quantity\", \"Price\" ) VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1,user);
        ps.setString(2, medName);
        ps.setString(3, medBrand);
        ps.setString(4, medCode);
        ps.setString(5, medType);
        ps.setString(6, medExpiry);
        ps.setInt(7, medQuantity);
        ps.setInt(8, medPrice);
        
        int change = ps.executeUpdate();
        ps.close();
        con.close();

            if(change > 0){
              showAlert(Alert.AlertType.INFORMATION, "Acknowledgement", "Medicine Added Successfully");
              mdName.setText("");
              mdBrand.setText("");
              mdQuantity.setText("");
              mdCode.setText("");
              mdExpiry.setText("");
              mdPrice.setText("");
              mdType.setText("");
            }

      }
      catch(Exception e){
        System.err.println(e);
      showAlert(Alert.AlertType.WARNING, "Error", "You have entered a duplicate item or a duplicated Medicine Code, try again");}
      }
    }

    @FXML
    void clearBtnClicked(ActionEvent event) {
      mdName.setText("");
      mdBrand.setText("");
      mdQuantity.setText("");
      mdCode.setText("");
      mdExpiry.setText("");
      mdPrice.setText("");
      mdType.setText("");
    }

    @FXML
    void sidebarInventoryClicked(ActionEvent event) {
      try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InventoryScene.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
                stage.setTitle("Inventory Page");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @FXML
    void sidebarModifyClicked(ActionEvent event) {
      try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SaleScene.fxml"));
           Parent root = loader.load();
           Scene scene = new Scene(root);
           Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
           stage.setScene(scene);
           stage.show();
           stage.setTitle("Modify Page");
    } catch (Exception e) {
        System.err.println(e);
    }
    }

    private void showAlert(Alert.AlertType alertType, String title, String contentText) {
      Alert alert = new Alert(alertType);
      alert.setTitle(title);
      alert.setHeaderText(null); // You can set a header text if needed
      alert.setContentText(contentText);
      alert.showAndWait();
  }

  @FXML
  void logoutClicked(ActionEvent event){
    showConfirmationDialog(event);
  }

  public boolean isValidInteger(String text) {
    try {
        Integer.parseInt(text);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}
private void showConfirmationDialog(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation Dialog");
        alert.setContentText("Do you want to logout?");

        // Adding custom buttons (in addition to the default OK and Cancel buttons)
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        // Handling the user's choice
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == yesButton) {
                try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScene.fxml"));
                  Parent root = loader.load();
                  Scene scene = new Scene(root);
                  Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                  stage.setScene(scene);
                  stage.show();
                } catch (Exception e) {
                    System.err.println(e);
                }
            } else if (buttonType == noButton) {
                //System.out.println("User clicked No");
            }
        });
    }

}
