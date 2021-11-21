package pl.edu.agh.kis.pz1.exceptions;

public class TooManyClientsException extends Exception{
    public TooManyClientsException(String str) {
        super(str);
    }
}
