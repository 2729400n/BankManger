package ui.bank;

import javafx.scene.Parent;
import javafx.stage.Screen;

public class PageNotFondScene  extends javafx.scene.Scene {
    final static Parent ROOTVIEW = new javafx.scene.layout.AnchorPane();
    static Screen primaryScreen  = javafx.stage.Screen.getPrimary(); 
   public PageNotFondScene(){
        super(ROOTVIEW,primaryScreen.getOutputScaleX()*640,primaryScreen.getOutputScaleY()*480);
        
        
    }
}
