package util;

/**
 * @author Johnnatan Messias
 */
public interface Transaction {

	public void execute() throws Exception;

	public void updateView();

}
