package uk.ac.rgu.cm2115;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class MainApp extends Application {

    private static Scene scene = null;
    private static final Map<String, Object> models = new HashMap<>();

    @Override
    public void start(Stage stage) throws IOException {

        /*
         * Change "primary" to the name of the FXML file of your first scene,
         * and "new Object()" to an object
         * representing the model class for that scene
         */
        MainApp.<AccountManager>setScene("AccountManager.fxml", new AccountManager());

        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        URL resUri;
        FXMLLoader fxmlLoader = new FXMLLoader(resUri=MainApp.class.getResource(fxml ));
        return fxmlLoader.load();
    }

    /**
     * Method to set the scene, with a given domain model
     * @param <T>
     * @param fxml
     * @param accountManager
     * @throws IOException
     */    
    public static <T> void setScene(String fxml, T accountManager) throws IOException {
        if (!fxml.endsWith(".fxml")) {
            fxml = fxml.substring(0, fxml.length()-5);
        }
        if(scene == null){
            scene = new Scene(loadFXML(fxml), 640, 480);
        }

        /* Strip off .fxml if it's been passed in the filename */
        

        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxml));
        Parent root = loader.load();

        @SuppressWarnings("unchecked")
        Controller<T> controller = (Controller<T>) loader.getController();
        controller.setModel(accountManager);

        models.put(fxml, accountManager);

        scene.setRoot(root);
    }

    /**
     * Method to set the scene. This can only be called if a domain model
     * has previously been used with this scene - otherwise an IOException is thrown
     * @param fxml
     * @throws IOException
     */
    public static void setScene(String fxml) throws IOException{
        /* Strip off .fxml if it's been passed in the filename */
        if (!fxml.endsWith(".fxml")) {
            fxml = fxml.substring(0, fxml.length()-5);
        }

        Object model = MainApp.models.get(fxml);

        if(model == null){
            throw new IOException("Scene cannot be used for the first time without a domain model");
        }

        MainApp.setScene(fxml, model);
    }


    public static void main(String[] args) {
        launch();
    }

}
