import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ModifySceneController {

    @FXML
    private Button deleteBtn;

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
    private Button sAddMedicineBtn;

    @FXML
    private Button sInventoryBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;

    @FXML
    private Pane sidebar;

    @FXML
    private Label codeLabel;

    @FXML
    private Button updateBtn;

    @FXML
    private AnchorPane fieldsPane;

    @FXML
    private Label userLabel;

    @FXML
    private Button salesBtn;

    String user = LoginSceneController.getUsername();

    @FXML
    private void initialize() {
        // Set the default visibility to false
        fieldsPane.setVisible(false);
        userLabel.setText(user);
        codeLabel.setVisible(false);
        mdCode.setVisible(false);
    }

    boolean found;
    String searched;
    String srTxt;

    @FXML
    void deleteBtnClicked(ActionEvent event) {
      try {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:1412/medloc", "postgres", "conanendou23");
        String query = "Delete from Medicine where \"Medicine Code\" = '" + searched +"' and \"PharmacyName\" = '" + user + "';";
        PreparedStatement ps = con.prepareStatement(query);
        int change = ps.executeUpdate();
        if(change>0){
          fieldsPane.setVisible(false);
          showAlert(Alert.AlertType.INFORMATION, "Acknowledgement", "Medicine Deleted");
          searchField.setText("");
          mdName.setText("");
          mdBrand.setText("");
          mdCode.setText("");
          mdExpiry.setText("");
          mdQuantity.setText("");
          mdPrice.setText("");
          mdType.setText("");
        }
        else{
          showAlert(Alert.AlertType.WARNING, "Error", "An error happened, please try again");
        }
         
      } catch (Exception e) {
      System.err.println(e);
      }

    }

    @FXML
    void searchBtnClicked(ActionEvent event) {
      srTxt = searchField.getText();
      try {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:1412/medloc", "postgres", "conanendou23");
        String query = "Select * from Medicine Where \"PharmacyName\" = '" + user +"';";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet r = ps.executeQuery();
        found = false;
        while(r.next())
        {
          searched = r.getString("Medicine Code");
          if(srTxt.equals(searched)){
            found = true;
            showAlert(Alert.AlertType.INFORMATION, "Acknowledgement", "Medicine Found");
          }
        }
        if(!found){
          showAlert(Alert.AlertType.WARNING, "Error", "No such medicine found with the specified Medicine Code");
        }
        if(found){
        fieldsPane.setVisible(true);
        String q = "Select \"Medicine Name\", \"Medicine Code\", \"Medicine Brand\", \"Type\", \"Expiry date\", \"Quantity\", \"Price\" from Medicine where \"PharmacyName\" = '" + user + "' and \"Medicine Code\" = '" + srTxt + "'; ";
        PreparedStatement stmt = con.prepareStatement(q);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
          defName = rs.getString("Medicine Name");
          defCode = rs.getString("Medicine Code");
          defBrand = rs.getString("Medicine Brand");
          defType = rs.getString("Type");
          defExpiry = rs.getString("Expiry date");
          defQuantity = rs.getString("Quantity");
          defPrice = rs.getString("Price");
        }
          mdBrand.setText(defBrand);
          mdName.setText(defName);
          mdCode.setText(defCode);
          mdExpiry.setText(defExpiry);
          mdQuantity.setText(defQuantity);
          mdType.setText(defType);
          mdPrice.setText(defPrice);
        }
        
      } catch (Exception e) {
        System.err.println(e);
      }

    }

    @FXML
    void salesBtnClicked(ActionEvent event) {
      try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SaleScene.fxml"));
          Parent root = loader.load();    
          Scene scene = new Scene(root);    
          Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();   
          stage.setScene(scene);
          stage.show();
          stage.setTitle("Sales Page");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @FXML
    void sidebarAddMedicineClicked(ActionEvent event) 
    {
      try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddMedicine.fxml"));
          Parent root = loader.load();
          Scene scene = new Scene(root);   
          Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();   
          stage.setScene(scene);
          stage.show();
          stage.setTitle("Add Medicine");
          } catch (Exception e) {
              System.err.println(e);
          }
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
    String defName, defBrand, defCode, defType, defExpiry, defQuantity, defPrice;
     
    @FXML
    void updateBtnClicked(ActionEvent event) {
      String defName = mdName.getText();
      String defBrand = mdBrand.getText();
      String defCode = mdCode.getText();
      String defType = mdType.getText();
      String defExpiry = mdExpiry.getText();
      String defQuantity = mdQuantity.getText();
      String defPrice = mdPrice.getText();
      if(defName==""||defBrand==""||defCode==""||defType==""||defExpiry==""||defQuantity==""||defPrice==""){
       showAlert(Alert.AlertType.WARNING, "Error", "Fill out all the fields and try again"); 
      } else{
        try {
      Class.forName("org.postgresql.Driver");
      Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:1412/medloc", "postgres", "conanendou23");
      String query = "UPDATE Medicine SET \"Medicine Name\" = '" + defName + "', \"Medicine Brand\" = '"+ defBrand + "', \"Type\" ='" + defType + "', \"Expiry date\" ='"+defExpiry + "', \"Quantity\" = '"+ defQuantity + "', \"Price\" = '" + defPrice + "' WHERE \"Medicine Code\" = '" + srTxt + "' AND \"PharmacyName\" = '" + user +"';";
      PreparedStatement ps = con.prepareStatement(query);
      //ps.setString(1, defName);KeneKe
      //ps.setString(2, defCode);
      //ps.setString(3, defBrand);
      //ps.setString(4, defType);
      //ps.setString(5, defExpiry);
      //ps.setString(6, defQuantity);
      //ps.setString(7, defPrice) 
      int change = ps.executeUpdate();
      ps.close();
      con.close();
      if(change>0){
        showAlert(Alert.AlertType.INFORMATION, "Acknowledgement", "Update Performed Successfully");
        fieldsPane.setVisible(false);
        searchField.setText("");
        mdName.setText("");
        mdBrand.setText("");
        mdCode.setText("");
        mdExpiry.setText("");
        mdQuantity.setText("");
        mdPrice.setText("");
        mdType.setText("");

        } else{
          showAlert(Alert.AlertType.WARNING, "Error", "An error happened, please try again");
        }
        
      } catch (Exception e) {
        System.err.println(e);
      }
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String contentText) {
      Alert alert = new Alert(alertType);
      alert.setTitle(title);
      alert.setHeaderText(null); // You can set a header text if needed
      alert.setContentText(contentText);
      alert.showAndWait();
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
    
    @FXML
    void logoutClicked(ActionEvent event) {
      showConfirmationDialog(event);
     }
  }
