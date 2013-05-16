package com.tldr.tools;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

public final class ToolBox {
	
	
	public static void showErrorMessage(EditText view, String message) {
		int ecolor = Color.RED; // whatever color you want
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder;
		ssbuilder = new SpannableStringBuilder(message);
		ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
		view.setError(ssbuilder);
	}

}
