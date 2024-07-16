package ui.bank;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

import javafx.scene.control.Button;

/**
 * JavaFX App
 */
public class MainApp extends Application {

    private static Scene scene = null;
    private static final Map<String, ModelParent> models = new HashMap<String,ModelParent>();
    private static Stack<Parent> history = new Stack<>();
    private static Parent cache = null;
    public static int cacheName = 0;
    public static Stage mainWindow=null;

    public void start(Stage stage) throws IOException, URISyntaxException {

        /*
         * Change "primary" to the name of the FXML file of your first scene,
         * and "new Object()" to an object
         * representing the model class for that scene
         */
        MainApp.mainWindow=stage;
        MainApp.setScene("AccountManager.fxml",new AccountManager());
        stage.setScene(scene);

        stage.show();
    }

    private static void setScene(Scene scene) {
        MainApp.scene= scene==null?new Scene(new AnchorPane(),640,480):scene;
    }

    static void setRoot(String fxml) throws NoCacheException,IOException, URISyntaxException {
        /* Strip off .fxml if it's been passed in the filename */
        if (!fxml.endsWith(".fxml")) {
            fxml = fxml.substring(0, fxml.length() - 5);
        }

        
        Object model = MainApp.models.get(fxml);

        if (model == null) {
            throw new NoCacheException("Scene cannot be used for the first time without a domain model");
        }
        history.push(scene.getRoot());
        MainApp.scene.setRoot(cache);
    }

    static void setRoot(Parent page) throws IOException, URISyntaxException {
        

        history.push(scene.getRoot());
        MainApp.scene.setRoot(page);
    }

    private static FXMLLoader loadFXML(String fxml) throws IOException, URISyntaxException {
        URL resUri;
        FXMLLoader fxmlLoader = new FXMLLoader(resUri = MainApp.class.getResource(fxml));
        
        if (resUri != null)
            return fxmlLoader;
        return null;
    }

    /**
     * Method to set the scene, with a given domain model
     * 
     * @param <T>
     * @param fxml
     * @param accountManager
     * @throws IOException
     */
    public static <T> void setScene(String fxml, T accountManager) throws IOException, URISyntaxException {
        if (!fxml.endsWith(".fxml")) {
            fxml = fxml + ".fxml";
        }
        
        FXMLLoader loader = loadFXML(fxml);
        Parent rootView = loader.load();
        if (rootView == null)
            rootView = new AnchorPane();

        Bounds bd = rootView != null ? rootView.getLayoutBounds()
                : (Bounds) (new Rectangle(640, 480)).getLayoutBounds();

        Scene scene=null;

        @SuppressWarnings("unchecked")
        Controller<T> controller = (Controller<T>) loader.getController();

        controller.setModel(accountManager);
        controller.setRootView(rootView);

        if (!fxml.endsWith(".fxml")) {
            fxml = fxml.substring(0, fxml.length() - 5);
        }    
        if (MainApp.scene == null) {

            MainApp.scene = scene = new Scene(rootView, bd.getWidth(), bd.getHeight());
            mainWindow.setScene(scene);
            mainWindow.setTitle(fxml);

        } else {
            
            scene = MainApp.scene;
            history.push(scene.getRoot());
            scene.setRoot(rootView);
        }
        
        models.put(fxml, new ModelParent(rootView, controller, accountManager));

        
    }

    /**
     * Method to set the scene. This can only be called if a domain model
     * has previously been used with this scene - otherwise an IOException is thrown
     * 
     * @param fxml
     * @throws IOException
     */
    public static void setScene(String fxml) throws NoCacheException, IOException, URISyntaxException {
        /* Strip off .fxml if it's been passed in the filename */
        if (!fxml.endsWith(".fxml")) {
            fxml = fxml.substring(0, fxml.length() - 5);
        }

        history.push(scene.getRoot());
        Object model = MainApp.models.get(fxml);

        if (model == null) {
            throw new NoCacheException("Scene cannot be used for the first time without a domain model");
        }
        
        MainApp.setScene(fxml, model);
    }

    public static void goToPage(String page) throws  IOException,URISyntaxException {
        try{
        MainApp.setRoot(page);
        }catch(NoCacheException e){
            try {
                MainApp.setRootCreate(page);
            } catch (LoaderException ldexm  ) {
                MainApp.setRoot(PageNotFondScene.ROOTVIEW);
            }
            
        }
    }

    private static void setRootCreate(String page) throws LoaderException, IOException, URISyntaxException {
        if(MainApp.scene==null){
            throw new NullPointerException("There is no Main Scene To bind to.");
        }

        /* Strip off .fxml if it's been passed in the filename */
        if (!page.endsWith(".fxml")) {
            page = page + ".fxml";
        }

        FXMLLoader loader = loadFXML(page);
        Parent rootView = loader.load();
        
        if (rootView == null)
            throw new LoaderException();

        Bounds bd = rootView != null ? rootView.getLayoutBounds()
                : (Bounds) (new Rectangle(640, 480)).getLayoutBounds();

        @SuppressWarnings("unchecked")
        Controller<Object> controller = (Controller<Object>) loader.getController();

        controller.setModel(controller.createModel());
        controller.setRootView(rootView);

        if (!page.endsWith(".fxml")) {
            page = page.substring(0, page.length() - 5);
        }    
        
        history.push(MainApp.scene.getRoot());
        scene.setRoot(rootView);
        
        models.put(page, new ModelParent(rootView, controller, controller.model));
    }

    public static void goToPage(Parent page) {

    }

    public static void goBack() {
        Parent rootView = MainApp.history.pop();
        MainApp.scene.setRoot(rootView);

    }

    public static void main(String[] args) {
        launch();
    }

}
