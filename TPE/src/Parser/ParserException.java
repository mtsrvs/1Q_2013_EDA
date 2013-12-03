package Parser;

public class ParserException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ParserException(String error) {
		super(error);
	}
}
