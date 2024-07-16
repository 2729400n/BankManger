module ui.bank {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    opens ui.bank to javafx.fxml;
    exports ui.bank;
}
