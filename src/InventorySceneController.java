import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class InventorySceneController {

    @FXML
    private TableView<Medicine> mTable;

    @FXML
    private Button sAddMedicine;

    @FXML
    private Button invModifyBtn;

    @FXML
    private Button sModify;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private Pane sidebar;

    @FXML
    private TextField searchField;
    
    @FXML
    private TableColumn<Medicine, String> tExpiry;

    @FXML
    private TableColumn<Medicine, String> tMedBrand;

    @FXML
    private TableColumn<Medicine, String> tMedCode;

    @FXML
    private TableColumn<Medicine, String> tMedName;

    @FXML
    private TableColumn<Medicine, String> tPrice;

    @FXML
    private TableColumn<Medicine, String> tQuantity;

    @FXML
    private TableColumn<Medicine, String> tType;

    @FXML
    private Label userLabel;

    String user = LoginSceneController.getUsername();

    @FXML
    private void initialize(){
      userLabel.setText(user);
      tMedName.setCellValueFactory(cellData -> cellData.getValue().medicineNameProperty());
      tMedCode.setCellValueFactory(cellData -> cellData.getValue().medicineCodeProperty());
      tMedBrand.setCellValueFactory(cellData -> cellData.getValue().medicineBrandProperty());
      tType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
      tExpiry.setCellValueFactory(cellData -> cellData.getValue().expiryDateProperty());
      tQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
      tPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

      fetchDataFromDatabase();

    }

    private void fetchDataFromDatabase(){
      try {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:1412/medloc", "postgres", "conanendou23");
        Statement st = con.createStatement();
        String query = "Select \"Medicine Code\", \"Medicine Name\", \"Medicine Brand\", \"Type\", \"Expiry date\", \"Quantity\", \"Price\" from medicine where \"PharmacyName\" = '" + user + "';";
        ResultSet rs = st.executeQuery(query);

        ObservableList<Medicine> medicineList = FXCollections.observableArrayList();
        while(rs.next()){
          Medicine medicine = new Medicine(
                rs.getString("Medicine Name"),
                rs.getString("Medicine Code"),
                rs.getString("Medicine Brand"),
                rs.getString("Type"),
                rs.getString("Expiry Date"),
                rs.getString("Quantity"),
                rs.getString("Price")
            );
            medicineList.add(medicine);
        }

        mTable.setItems(medicineList);
      } catch (Exception e) {
        System.err.println(e);
      }
    }

    @FXML
    void searchBtnClicked(ActionEvent event) {
      String searched = searchField.getText();
      try {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:1412/medloc", "postgres", "conanendou23");
        Statement st = con.createStatement();
        String query = "Select \"Medicine Code\", \"Medicine Name\", \"Medicine Brand\", \"Type\", \"Expiry date\", \"Quantity\", \"Price\" from medicine where \"PharmacyName\" = '" + user + "' and \"Medicine Code\" = '" + searched + "';";
        ResultSet rs = st.executeQuery(query);

        ObservableList<Medicine> medicineList = FXCollections.observableArrayList();
        while(rs.next()){
          Medicine medicine = new Medicine(
                rs.getString("Medicine Name"),
                rs.getString("Medicine Code"),
                rs.getString("Medicine Brand"),
                rs.getString("Type"),
                rs.getString("Expiry Date"),
                rs.getString("Quantity"),
                rs.getString("Price")
            );
            medicineList.add(medicine);
        }

        mTable.setItems(medicineList);
      } catch (Exception e) {
        System.err.println(e);
      }

    }

    @FXML
    void invModifyBtnClicked(ActionEvent event) {
      try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyScene.fxml"));
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
    void sidebarModifyClicked(ActionEvent event) {
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
    void logoutClicked(ActionEvent event) {
      showConfirmationDialog(event);
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
