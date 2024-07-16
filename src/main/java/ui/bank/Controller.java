package ui.bank;

import javafx.scene.Parent;

public abstract class Controller<T> {

    protected T model;
    protected Parent rootView;

    public abstract void setModel(T model);
    public  void setRootView(Parent rootView){
        this.rootView = rootView;
    }
    public abstract  T createModel(); 
    
}
