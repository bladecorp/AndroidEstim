package com.ipn.estim_v1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

public class DialogoCargando {

	private static ProgressDialog pd;

	public static final void mostrarDialogo(Activity activity) {
		// pd = new ProgressDialog(activity, R.style.StyledDialog);
		pd = new ProgressDialog(activity);
		pd.setMessage("Conectando...");
		pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		pd.show();
	}

	public static final void ocultarDialogo() {
		pd.dismiss();
	}

}
