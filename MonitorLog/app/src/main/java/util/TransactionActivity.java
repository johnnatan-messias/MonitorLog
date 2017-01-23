package util;

import android.app.Activity;
import android.os.Bundle;

import com.monitorlog.R;

/**
 * @author Johnnatan Messias
 */
public class TransactionActivity extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public void startTransaction(Transaction transaction) {
		TransactionTask transactionTask = new TransactionTask(this,
				transaction, R.string.wait);
		transactionTask.execute();
	}
}
