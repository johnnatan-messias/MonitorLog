package util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
/**
 * @author Johnnatan Messias
 */
public class TransactionTask extends AsyncTask<Void, Void, Boolean> {
	private static String CATEGORY = "TransactionTask";
	private final Context context;
	private final Transaction transaction;
	private ProgressDialog progressDialog;
	private Throwable exceptionError;
	private int waitMsg;

	public TransactionTask(Context context, Transaction transaction, int waitMsg) {
		this.context = context;
		this.transaction = transaction;
		this.waitMsg = waitMsg;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		openProgressDialog();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			transaction.execute();
		} catch (Throwable e) {
			Log.e(CATEGORY, e.getMessage(), e);
			this.exceptionError = e;
			return false;
		} finally {
			try {
				closeProgressDialog();
			} catch (Exception e) {
				Log.e(CATEGORY, e.getMessage(), e);
			}
		}

		return true;
	}

	@Override
	protected void onPostExecute(Boolean ok) {
		if (ok) {
			transaction.updateView();
		} else {
			AndroidUtils.alertDialog(context,
					"Erro: " + exceptionError.getMessage(),
					android.R.drawable.alert_dark_frame);
		}
	}

	public void openProgressDialog() {
		try {
			progressDialog = ProgressDialog.show(context, "",
					context.getString(waitMsg));

		} catch (Throwable e) {
			Log.e(CATEGORY, e.getMessage(), e);
		}
	}

	public void closeProgressDialog() {
		try {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		} catch (Throwable e) {
			Log.e(CATEGORY, e.getMessage(), e);
		}
	}
}
