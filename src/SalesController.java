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

public class SalesController {

    @FXML
    private AnchorPane fieldsPane;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label mdBrandLabel;

    @FXML
    private Label mdNameLabel;

    @FXML
    private Label mdPriceLabel;

    @FXML
    private TextField mdQuantity;

    @FXML
    private Button sAddMedicineBtn;

    @FXML
    private Button sInventoryBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;

    @FXML
    private Pane sidebar;

    @FXML
    private Button updateBtn;

    @FXML
    private Label userLabel;

    String user = LoginSceneController.getUsername();
    String srTxt, searched;
    boolean found;

    @FXML
    private void initialize() {
        // Set the default visibility to false
        fieldsPane.setVisible(false);
        userLabel.setText(user);   
    }


    @FXML
    void logoutClicked(ActionEvent event) {
      showConfirmationDialog(event);
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
        String q = "Select \"Medicine Name\", \"Medicine Brand\", \"Price\" from Medicine where \"PharmacyName\" = '" + user + "' and \"Medicine Code\" = '" + srTxt + "'; ";
        PreparedStatement stmt = con.prepareStatement(q);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
          mdNameLabel.setText(rs.getString("Medicine Name"));
          mdBrandLabel.setText(rs.getString("Medicine Brand"));
          mdPriceLabel.setText(rs.getString("Price") + "$");
        }
        }
        
      } catch (Exception e) {
        System.err.println(e);
      }
    }

    @FXML
    void sidebarAddMedicineClicked(ActionEvent event) {
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

    @FXML
    void updateBtnClicked(ActionEvent event) {
      int medQuantity = 0; 
      int x = 0;
      int unitPrice = 0;
      int oldSales = 0;
      int oldMedQuantity = 0;
      boolean proceed = true;
      try {
        medQuantity = Integer.parseInt(mdQuantity.getText());
      } catch (Exception e) {
        showAlert(Alert.AlertType.WARNING, "Error", "Enter a valid positive integer");
        proceed = false;
        System.err.println(e);
        mdQuantity.setText("");
      }
      try {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:1412/medloc", "postgres", "conanendou23");
        String q = "Select \"Quantity\", \"Price\" from Medicine where \"PharmacyName\" = '" + user + "' and \"Medicine Code\" = '" + srTxt + "'; ";
        PreparedStatement stmt = con.prepareStatement(q);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
          x = rs.getInt("Quantity");
          unitPrice = rs.getInt("Price");
        }
        String qr = "Select SoldMedicines, TotalSales from phauth where name = '" + user + "';";
        PreparedStatement st = con.prepareStatement(qr);
        ResultSet rs2 = st.executeQuery();
        if(rs2.next()){
          oldSales = rs2.getInt("TotalSales");
          oldMedQuantity = rs2.getInt("SoldMedicines");
        }

        if(proceed && medQuantity>0 && x>=medQuantity){
          int profit = unitPrice * medQuantity;
          int remaining = x - medQuantity;
          String query = "Update Medicine set \"Quantity\" = '" + remaining + "' WHERE \"Medicine Code\" = '" + srTxt + "' AND \"PharmacyName\" = '" + user +"';";
          PreparedStatement ps = con.prepareStatement(query);
          int change = ps.executeUpdate();
          ps.close();
          if(change>0){
            showAlert(Alert.AlertType.INFORMATION, "Acknowledgement", "Sale Recorded Successfully");
            fieldsPane.setVisible(false);
            searchField.setText("");
          }
          int newSales = oldSales + profit;
          int newMedicineAmount = medQuantity + oldMedQuantity;
          String qry = "Update phauth set TotalSales = '" + newSales + "', SoldMedicines = '" + newMedicineAmount + "' WHERE name = '" + user +"';";
          PreparedStatement ps2 = con.prepareStatement(qry);
          ps2.executeUpdate();
          con.close();
          mdQuantity.setText("");
          fieldsPane.setVisible(false);
          }
          else if (proceed && medQuantity<=0){
          showAlert(Alert.AlertType.WARNING, "Error", "Only positive integers are allowed");
          mdQuantity.setText("");
          } else if(proceed){
          showAlert(Alert.AlertType.WARNING, "Error", "Number of sales can't be more than the available quantity");
          mdQuantity.setText("");
          }
          
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
  private void showConfirmationDialog(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation Dialog");
        alert.setContentText("Do you want to logout?");

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
