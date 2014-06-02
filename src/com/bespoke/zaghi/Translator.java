package com.bespoke.zaghi;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import android.app.Activity;
import android.widget.Toast;

public class Translator extends Activity {

	String transText = "Translated output shown here...";

	String Translate(String s1, Language l1, Language l2) {

		if (l1 == null || l2 == null) {
			Toast t = Toast.makeText(getApplicationContext(),
					"Please select a language.", Toast.LENGTH_SHORT);
			t.show();
		} else {
			Translate.setClientId("bzzaghi");
			Translate
					.setClientSecret("XXpH4BXWh4UMsomuCBZWNIgm5fs4oIms0bR3ihsjIZs=");
			try {
				transText = Translate.execute(s1, l1, l2);
			} catch (Exception e) {
				transText = e.toString();
			}
			// translatedText.setText(transText);
		}
		return transText;
	}

}