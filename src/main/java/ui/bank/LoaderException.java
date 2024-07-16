package ui.bank;

public  class  LoaderException  extends Exception{
    public final long serialVersionUID = 3824983984911321L;
    private String detailMessage = "Thew was a loader Error";
    char errorCode = 500; 
    public LoaderException(){
        super();
    }
    public LoaderException(String var){
        super(var);
    }
    public LoaderException(char var){
        super();
        errorCode=var;
    }
}
