package com.bespoke.zaghi;

import com.memetix.mst.language.Language;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;


public class About extends Activity{
	
	private Language lang = null;
	private Translator TranslatorClass = new Translator();
	private String translatedAbout = "";
	private String englishAbout;
	Spinner sLanguages;
	TextView tvAbout;
	
	LangSpinnerSelector LangSelectorClass = new LangSpinnerSelector();
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		sLanguages = (Spinner) findViewById(R.id.sLanguages);
		tvAbout = (TextView) findViewById(R.id.tvAbout);
		
		englishAbout = tvAbout.getText().toString();

		// inflates spinners
		//addListenerOnSpinnerItemSelection();
	
		
		sLanguages.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				// New Language Selection
				int index;
				String[] langArray = getResources().getStringArray(
						R.array.languages_array);
				index = sLanguages.getSelectedItemPosition();
				lang = LangSelectorClass.LangSelector(String
						.valueOf(langArray[index].trim()));
				
				if(lang != null){
					new MyAsyncTaskLocalization() {
						protected void onPostExecute(Boolean result) {
							if (result) {
								tvAbout.setText(translatedAbout);
							}
						}
					}.execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});
		
	}
	
	/*
	// populates the spinners with languages
	public void addListenerOnSpinnerItemSelection() {
		sLanguages = (Spinner) findViewById(R.id.sInLanguages);
		sLanguages
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}*/
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	class MyAsyncTaskLocalization extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
			translatedAbout = TranslatorClass.Translate(englishAbout, Language.ENGLISH, lang);
			return true;
		}
	}

	
}