package com.bespoke.zaghi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.ispeech.SpeechSynthesis;
import org.ispeech.SpeechSynthesisEvent;
import org.ispeech.error.BusyException;
import org.ispeech.error.InvalidApiKeyException;
import org.ispeech.error.NoNetworkException;

import com.memetix.mst.language.Language;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Html;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalMode extends Activity implements OnInitListener {
	protected static final int RESULT_SPEECH = 1;

	private static final String TAG = "iSpeech API";

	// status check code
	private int MY_DATA_CHECK_CODE = 0;

	private ImageButton btnSpeak;
	private Button btnTranslate;

	private TextView tvTranslatedText;
	private TextView tvResult;
	private TextView tvInLang;
	private TextView tvOutLang;
	private TextView tvInVoice;
	private TextView tvInText;

	private EditText etInput;

	private String inText = "";
	private String transText = "";

	// localization
	private String wordSelectLang = "Please select a language.";
	private String wordPleaseAddText = "Please add text to be translated.";
	private String wordPleaseSpeakSlow = "Please speak slowly and clearly.";

	// iSpeech instance
	private SpeechSynthesis synthesis;
	private Context _context;

	private Language inLang = null;
	private Language outLang = null;

	private Spinner inLangSpinner;
	private Spinner outLangSpinner;

	private LangSpinnerSelector inLangSelectorClass = new LangSpinnerSelector();
	private LangSpinnerSelector outLangSelectorClass = new LangSpinnerSelector();
	private Translator TranslatorClass = new Translator();

	// TTs object
	private TextToSpeech myTTS;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_mode);

		// Hides SoftKeyboard on activity start
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// inflates spinners
		addListenerOnSpinnerItemSelection();

		inLangSpinner = (Spinner) findViewById(R.id.sInLanguages);
		outLangSpinner = (Spinner) findViewById(R.id.sOutLanguages);
		
		btnTranslate = (Button) findViewById(R.id.bTranslates);
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
		
		tvTranslatedText = (TextView) findViewById(R.id.tvTranslatedText);
		tvResult = (TextView) findViewById(R.id.tvResult);
		tvInLang = (TextView) findViewById(R.id.tvInLang);
		tvOutLang = (TextView) findViewById(R.id.tvOutLang);
		tvInVoice = (TextView) findViewById(R.id.tvInVoice);
		tvInText = (TextView) findViewById(R.id.tvInText);
		
		etInput = (EditText) findViewById(R.id.etInput);
		
		prepareTTSEngine();

		// display locales to console
		Log.i("-------------", Arrays.toString(Locale.getAvailableLocales()));

		StartTTS();

		// action done once the speaking input button is pressed
		btnSpeak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if ((inLang != null) && (outLang != null)) {
					SpeechToTextMic();
				} else {
					Toast t = Toast.makeText(getApplicationContext(),
							wordSelectLang, Toast.LENGTH_SHORT);
					t.show();
				}
			}

		});

		inLangSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				// New Language Selection
				int index;
				String[] langArray = getResources().getStringArray(
						R.array.languages_array);
				index = inLangSpinner.getSelectedItemPosition();
				inLang = inLangSelectorClass.LangSelector(String
						.valueOf(langArray[index].trim()));

				// fast localization
				tvResult.setText(inLangSelectorClass.getResult());
				tvInLang.setText(inLangSelectorClass.getWordInLang());
				tvOutLang.setText(inLangSelectorClass.getWordOutLang());
				tvInText.setText(inLangSelectorClass.getWordTextIn());
				etInput.setHint(inLangSelectorClass.getWordToBeTranslated());
				// etInput.setText();
				btnTranslate.setText(inLangSelectorClass.getWordTranslate());
				tvInVoice.setText(inLangSelectorClass.getWordVoiceIn());

				// slower localization
				if (inLang != null) {
					new MyAsyncTaskLocalization() {
						protected void onPostExecute(Boolean result) {
						}
					}.execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				tvResult.setText("Result:");
			}
		});

		outLangSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				// New Language Selection
				int index;
				String[] langArray = getResources().getStringArray(
						R.array.languages_array);
				index = outLangSpinner.getSelectedItemPosition();
				outLang = outLangSelectorClass.LangSelector(String
						.valueOf(langArray[index].trim()));

			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});

		// action done once Translate button is pressed
		btnTranslate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				inText = etInput.getText().toString();

				if (outLang == null) {

					Toast t = Toast.makeText(getApplicationContext(),
							wordSelectLang, Toast.LENGTH_SHORT);
					t.show();
				} else if (inText.isEmpty()) {
					Toast t = Toast.makeText(getApplicationContext(),
							wordPleaseAddText, Toast.LENGTH_SHORT);
					t.show();
				} else {
					new MyAsyncTask() {
						protected void onPostExecute(Boolean result) {
							if (result) {
								speakOut(transText);
								tvTranslatedText.append(Html
										.fromHtml("<font color='#33AA99'>"
												+ inText + "</font>"));
								tvTranslatedText.append("\n");
								tvTranslatedText.append(Html
										.fromHtml("<font color='#FF5533'>"
												+ "-> " + transText + "</font>"));
								tvTranslatedText.append("\n");
								((ScrollView) findViewById(R.id.tvScroller))
										.post(new Runnable() {
											public void run() {
												((ScrollView) findViewById(R.id.tvScroller))
														.fullScroll(View.FOCUS_DOWN);
											}
										});
							}
						}
					}.execute();
				}
			}

		});

		// action done when the Result text is pressed
		tvTranslatedText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// New Language Selection
				int index;
				String[] langArray = getResources().getStringArray(
						R.array.languages_array);
				index = inLangSpinner.getSelectedItemPosition();
				inLang = inLangSelectorClass.LangSelector(String
						.valueOf(langArray[index].trim()));
				speakOut(transText);
			}

		});

	}

	// populates the spinners with languages
	public void addListenerOnSpinnerItemSelection() {
		inLangSpinner = (Spinner) findViewById(R.id.sInLanguages);
		inLangSpinner
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		outLangSpinner = (Spinner) findViewById(R.id.sOutLanguages);
		outLangSpinner
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	// STT Initiates the Microphone for voice input
	private void SpeechToTextMic() {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, wordPleaseSpeakSlow);

		Intent detailsIntent = new Intent(
				RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
		sendOrderedBroadcast(detailsIntent, null, new LanguageDetailsChecker(),
				null, Activity.RESULT_OK, null, null);

		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
				inLangSelectorClass.GetInputLocale());
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "");

		try {
			startActivityForResult(intent, RESULT_SPEECH);
			Toast t = Toast.makeText(getApplicationContext(),
					inLangSelectorClass.GetInputLocale(), Toast.LENGTH_SHORT);
			t.show();

		} catch (ActivityNotFoundException a) {
			Toast t = Toast
					.makeText(getApplicationContext(),
							"Device doesn't support Speech to Text",
							Toast.LENGTH_SHORT);
			t.show();
		}

	}

	// iSpeech TTS Setup
	private void prepareTTSEngine() {

		try {
			synthesis = SpeechSynthesis.getInstance(this);
			synthesis.setSpeechSynthesisEvent(new SpeechSynthesisEvent() {

				public void onPlaySuccessful() {
					Log.i(TAG, "onPlaySuccessful");
				}

				public void onPlayStopped() {
					Log.i(TAG, "onPlayStopped");
				}

				public void onPlayFailed(Exception e) {
					Log.e(TAG, "onPlayFailed");

					AlertDialog.Builder builder = new AlertDialog.Builder(
							PersonalMode.this);
					builder.setMessage("Error[TTSActivity]: " + e.toString())
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}

				public void onPlayStart() {
					Log.i(TAG, "onPlayStart");
				}

				@Override
				public void onPlayCanceled() {
					Log.i(TAG, "onPlayCanceled");
				}

			});

		} catch (InvalidApiKeyException e) {
			Log.e(TAG, "Invalid API key\n" + e.getStackTrace());
			Toast.makeText(_context, "ERROR: Invalid API key",
					Toast.LENGTH_LONG).show();
		}

	}

	// Initiates Text To Speech Speaker
	private void StartTTS() {
		// check for TTS data

		Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
		// create TTS
		myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {

				} else {
					Log.e("TTS", "Initialization failed!");
				}
			}
		});

	}

	// Speaks the string input using iSpeech API unless there is no language
	// available. Then it resorts to Android's TTS
	private void speakOut(String str) {

		Toast t = Toast.makeText(getApplicationContext(),
				outLangSelectorClass.GetInputLocale(), Toast.LENGTH_SHORT);
		t.show();

		// if there is no iSpeech dialect, use android default Locale
		if (outLangSelectorClass.whichVoiceType() == null) {

			// myTTS.setPitch((float) 1.1); myTTS.setSpeechRate((float) 1.0);
			myTTS.setLanguage(outLangSelectorClass.whichLoc());
			myTTS.speak(str, TextToSpeech.QUEUE_FLUSH, null);

		} else { // use iSpeech TTS

			synthesis.setVoiceType(outLangSelectorClass.whichVoiceType());
			try {
				synthesis.speak(str);
			} catch (BusyException e) {
				Log.e(TAG, "SDK is busy");
				e.printStackTrace();
				Toast.makeText(_context, "ERROR: SDK is busy",
						Toast.LENGTH_LONG).show();
			} catch (NoNetworkException e) {
				Log.e(TAG, "Network is not available\n" + e.getStackTrace());
				Toast.makeText(_context, "ERROR: Network is not available",
						Toast.LENGTH_LONG).show();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.translator, menu);
		return true;
	}

	// On the Result of SpeechToTextMic start the Async to Translate
	// Then set the tvTranslatedText to display and Speakout
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				inText = text.get(0);

				new MyAsyncTask() {
					protected void onPostExecute(Boolean result) {
						speakOut(transText);
						tvTranslatedText.append(Html
								.fromHtml("<font color='#33AA99'>" + inText
										+ "</font>"));
						tvTranslatedText.append("\n");
						tvTranslatedText.append(Html
								.fromHtml("<font color='#FF5533'>" + "-> "
										+ transText + "</font>"));
						tvTranslatedText.append("\n");
						((ScrollView) findViewById(R.id.tvScroller))
								.post(new Runnable() {
									public void run() {
										((ScrollView) findViewById(R.id.tvScroller))
												.fullScroll(View.FOCUS_DOWN);
									}
								});
					}
				}.execute();
			}
			break;
		}
		}
	}

	// Async to select the out Language and Translate the text in background
	// called by above onActivityResult
	class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {

			// New Language Selection
			int index;
			String[] langArray = getResources().getStringArray(
					R.array.languages_array);
			index = outLangSpinner.getSelectedItemPosition();
			outLang = outLangSelectorClass.LangSelector(String
					.valueOf(langArray[index].trim()));

			// Old Language Selection
			// outLang = outLangSelectorClass.LangSelector(String.valueOf(
			// outLangSpinner.getSelectedItem()).trim());

			transText = TranslatorClass.Translate(inText, inLang, outLang);
			return true;
		}
	}

	class MyAsyncTaskLocalization extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
			wordSelectLang = TranslatorClass.Translate(
					"Please select a language.", Language.ENGLISH, inLang);
			wordPleaseAddText = TranslatorClass.Translate(
					"Please add text to be translated.", Language.ENGLISH,
					inLang);
			wordPleaseSpeakSlow = TranslatorClass.Translate(
					"Please speak slowly and clearly.", Language.ENGLISH,
					inLang);
			return true;
		}
	}

	// setup TTS from android
	public void onInit(int initStatus) {
		// check for successful instantiation
		if (initStatus == TextToSpeech.SUCCESS) {
			if (myTTS.isLanguageAvailable(outLangSelectorClass.whichLoc()) == TextToSpeech.LANG_AVAILABLE)
				myTTS.setLanguage(outLangSelectorClass.whichLoc());
		} else if (initStatus == TextToSpeech.ERROR) {
			Toast.makeText(this, "Sorry! Text To Speech failed...",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Toast.makeText(_context, "landscape", Toast.LENGTH_SHORT).show();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Toast.makeText(_context, "portrait", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onDestroy() {
		if (myTTS != null) {
			myTTS.stop();
			myTTS.shutdown();
		}
		super.onDestroy();
	}

}