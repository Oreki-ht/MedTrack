import javafx.beans.property.SimpleStringProperty;

public class Medicine {

    private final SimpleStringProperty medicineName;
    private final SimpleStringProperty medicineCode;
    private final SimpleStringProperty medicineBrand;
    private final SimpleStringProperty type;
    private final SimpleStringProperty expiryDate;
    private final SimpleStringProperty quantity;
    private final SimpleStringProperty price;

    public Medicine(String medicineName, String medicineCode,
        String medicineBrand, String type, String expiryDate, String quantity, String price) {
        this.medicineName = new SimpleStringProperty(medicineName);
        this.medicineCode = new SimpleStringProperty(medicineCode);
        this.medicineBrand = new SimpleStringProperty(medicineBrand);
        this.type = new SimpleStringProperty(type);
        this.expiryDate = new SimpleStringProperty(expiryDate);
        this.quantity = new SimpleStringProperty(quantity);
        this.price = new SimpleStringProperty(price);
    }

    // Getter methods

    public String getMedicineName() {
        return medicineName.get();
    }

    public String getMedicineCode() {
        return medicineCode.get();
    }

    public String getMedicineBrand() {
        return medicineBrand.get();
    }

    public String getType() {
        return type.get();
    }

    public String getExpiryDate() {
        return expiryDate.get();
    }

    public String getQuantity() {
        return quantity.get();
    }

    public String getPrice() {
        return price.get();
    }

    // Property methods

    public SimpleStringProperty medicineNameProperty() {
        return medicineName;
    }

    public SimpleStringProperty medicineCodeProperty() {
        return medicineCode;
    }

    public SimpleStringProperty medicineBrandProperty() {
        return medicineBrand;
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public SimpleStringProperty expiryDateProperty() {
        return expiryDate;
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }
}
