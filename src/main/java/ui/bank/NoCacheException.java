package ui.bank;

public class NoCacheException extends Throwable{
   static final long serialVersionUID = -4387526993124269948L;

    NoCacheException(){
        super();
    }
    NoCacheException(String string){
        super(string);
    }
}
