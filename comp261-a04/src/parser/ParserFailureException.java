package parser;
public class ParserFailureException extends RuntimeException{
    public ParserFailureException(String msg){
	super(msg);
    }
}
