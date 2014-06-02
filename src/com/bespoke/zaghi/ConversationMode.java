package com.bespoke.zaghi;

import java.util.ArrayList;

import org.ispeech.SpeechSynthesis;
import org.ispeech.SpeechSynthesisEvent;
import org.ispeech.error.BusyException;
import org.ispeech.error.InvalidApiKeyException;
import org.ispeech.error.NoNetworkException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.language.Language;

public class ConversationMode extends Activity implements OnInitListener {
	protected static final int RESULT_SPEECH = 1;

	private static final String TAG = "iSpeech API";

	private ImageButton topBtnSpeak;
	private ImageButton bottomBtnSpeak;

	private TextView tvTranslatedTextin;
	private TextView tvTranslatedTextout;
	private TextView tvTranslatedTextTop;
	private TextView tvTranslatedTextBottom;
	private TextView tvLangTop;
	private TextView tvLangBottom;
	private TextView tvInVoiceTop;
	private TextView tvInVoiceBottom;
	private TextView tvResultTop;
	private TextView tvResultBottom;

	private Translator TranslatorClass = new Translator();

	private String outText = "";
	private String transText = "";

	// iSpeech instance
	private SpeechSynthesis synthesis;
	private Context _context;

	private Language inLang = null;
	private Language outLang = null;
	private Language bottomLang = null;
	private Language topLang = null;

	private Spinner topLangSpinner;
	private Spinner bottomLangSpinner;
	
	// duplicates made to switch output and input classes when different users'
	// buttons are pressed
	// i.e. top user presses button => top user is input and bottom becomes
	// output
	// i.e. bottom user presses button => bottom user is input and top becomes
	// output
	LangSpinnerSelector inLangSelectorClass;
	LangSpinnerSelector outLangSelectorClass;
	LangSpinnerSelector topLangSelectorClass = new LangSpinnerSelector();;
	LangSpinnerSelector bottomLangSelectorClass = new LangSpinnerSelector();;

	// TTs object
	private TextToSpeech myTTS;
	// status check code
	private int MY_DATA_CHECK_CODE = 0;

	/** Called when the activity is first created. **/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation_mode);

		addListenerOnSpinnerItemSelection();

		tvTranslatedTextTop = (TextView) findViewById(R.id.tvTranslatedTextTop);
		tvTranslatedTextBottom = (TextView) findViewById(R.id.tvTranslatedTextBottom);
		tvLangTop = (TextView) findViewById(R.id.tvLangTop);
		tvLangBottom = (TextView) findViewById(R.id.tvLangBottom);
		tvInVoiceTop = (TextView) findViewById(R.id.tvInVoiceTop);
		tvInVoiceBottom = (TextView) findViewById(R.id.tvInVoiceBottom);
		tvResultTop = (TextView) findViewById(R.id.tvResultTop);
		tvResultBottom = (TextView) findViewById(R.id.tvResultBottom);

		topBtnSpeak = (ImageButton) findViewById(R.id.topBtnSpeak);
		bottomBtnSpeak = (ImageButton) findViewById(R.id.bottomBtnSpeak);

		prepareTTSEngine();

		StartTTS();
		
		topLangSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			// selected position of top spinner item is index
			int index;

			String[] langArray = getResources().getStringArray(
					R.array.languages_array);

			/*Controls the language selection for the top spinner.*/
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				index = topLangSpinner.getSelectedItemPosition();
				topLang = topLangSelectorClass.LangSelector(String
						.valueOf(langArray[index].trim()));

				// top localization
				tvLangTop.setText(topLangSelectorClass.getWordLang());
				tvInVoiceTop.setText(topLangSelectorClass.getWordVoiceIn());
				tvResultTop.setText(topLangSelectorClass.getResult());

				// Layout for upside-down toast
				LayoutInflater inflaterLayout = getLayoutInflater();
				View layout = inflaterLayout.inflate(R.layout.toast_layout,
						(ViewGroup) findViewById(R.id.toast_layout_root));
				TextView topToast = (TextView) layout.findViewById(R.id.text);
				topToast.setText(String.valueOf(langArray[index].trim()));

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 80);
				toast.setView(layout);
				toast.show();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}

		});

		
		bottomLangSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					// selected position of bottom spinner item is index
					int index;
					String[] langArray = getResources().getStringArray(
							R.array.languages_array);

					/*Controls the language selection for the bottom spinner.*/
					@Override
					public void onItemSelected(AdapterView<?> parentView,
							View selectedItemView, int position, long id) {
						index = bottomLangSpinner.getSelectedItemPosition();
						bottomLang = bottomLangSelectorClass
								.LangSelector(String.valueOf(langArray[index]
										.trim()));
						Toast t = Toast.makeText(getApplicationContext(),
								String.valueOf(langArray[index].trim()),
								Toast.LENGTH_SHORT);
						t.show();

						// bottom localization
						tvResultBottom.setText(bottomLangSelectorClass
								.getResult());
						tvLangBottom.setText(bottomLangSelectorClass
								.getWordLang());
						tvInVoiceBottom.setText(bottomLangSelectorClass
								.getWordVoiceIn());
					}

					@Override
					public void onNothingSelected(AdapterView<?> parentView) {

					}

				});

		/*Action done once the top speaking input button is pressed*/
		topBtnSpeak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				inLangSelectorClass = topLangSelectorClass;
				outLangSelectorClass = bottomLangSelectorClass;

				inLang = topLang;
				outLang = bottomLang;

				tvTranslatedTextin = tvTranslatedTextTop;
				tvTranslatedTextout = tvTranslatedTextBottom;

				if (inLang != null && outLang != null) {
					SpeechToTextMic();
				} else {
					if (outLang == null) {
						Toast t = Toast
								.makeText(getApplicationContext(),
										"Please select a language.",
										Toast.LENGTH_SHORT);
						t.show();
					}
					if (inLang == null) {
						// Layout for upside-down toast
						LayoutInflater inflaterLayout = getLayoutInflater();
						View layout = inflaterLayout
								.inflate(
										R.layout.toast_layout,
										(ViewGroup) findViewById(R.id.toast_layout_root));
						TextView topToast = (TextView) layout
								.findViewById(R.id.text);
						topToast.setText("Please select a language.");

						Toast toast = new Toast(getApplicationContext());
						toast.setGravity(Gravity.TOP, 0, 80);
						toast.setView(layout);
						toast.show();
					}
				}
			}
		});

		/*Action done once the bottom speaking input button is pressed*/
		bottomBtnSpeak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				inLangSelectorClass = bottomLangSelectorClass;
				outLangSelectorClass = topLangSelectorClass;

				inLang = bottomLang;
				outLang = topLang;

				tvTranslatedTextin = tvTranslatedTextBottom;
				tvTranslatedTextout = tvTranslatedTextTop;

				if (inLang != null && outLang != null) {
					SpeechToTextMic();
				} else {
					if (inLang == null) {
						Toast t = Toast
								.makeText(getApplicationContext(),
										"Please select a language.",
										Toast.LENGTH_SHORT);
						t.show();
					}
					if (outLang == null) {
						LayoutInflater inflaterLayout = getLayoutInflater();
						View layout = inflaterLayout
								.inflate(
										R.layout.toast_layout,
										(ViewGroup) findViewById(R.id.toast_layout_root));
						TextView topToast = (TextView) layout
								.findViewById(R.id.text);
						topToast.setText("Please select a language.");
						Toast toast = new Toast(getApplicationContext());
						toast.setGravity(Gravity.TOP, 0, 80);
						toast.setView(layout);
						toast.show();
					}
				}
			}

		});

		// action done when the Result text is pressed
		tvTranslatedTextTop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				outLangSelectorClass = topLangSelectorClass;
				speakOut(transText);
			}

		});
		tvTranslatedTextBottom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				outLangSelectorClass = bottomLangSelectorClass;
				speakOut(transText);
			}

		});

	}

	public void addListenerOnSpinnerItemSelection() {

		topLangSpinner = (Spinner) findViewById(R.id.topLanguages);
		// up-side-down attributes
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.localized_language_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.top_spinner);
		topLangSpinner.setAdapter(adapter);

		bottomLangSpinner = (Spinner) findViewById(R.id.bottomLanguages);
		// right-side-up attributes
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.localized_language_array,
				android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(R.layout.bottom_spinner);
		bottomLangSpinner.setAdapter(adapter2);

		topLangSpinner
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		bottomLangSpinner
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());
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
							ConversationMode.this);
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

	// STT Initiates the Microphone for voice input
	private void SpeechToTextMic() {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				"Please speak slowly and clearly.");

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

	// TTS Speaks the string input
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

				outText = text.get(0);

				new MyAsyncTask() {
					protected void onPostExecute(Boolean result) {
						speakOut(transText);
						tvTranslatedTextin.append(Html
								.fromHtml("<font color='#33AA99'>" + "\u2193 "
										+ outText + "</font>"));
						tvTranslatedTextin.append("\n");
						tvTranslatedTextout
								.append(Html
										.fromHtml("<font color='#FF5533' align='right'>"
												+ "\u2191 "
												+ transText
												+ "</font>"));
						tvTranslatedTextout.append("\n");

						// have results textviews scroll down
						((ScrollView) findViewById(R.id.tvScrollerTop))
								.post(new Runnable() {
									public void run() {
										((ScrollView) findViewById(R.id.tvScrollerTop))
												.fullScroll(View.FOCUS_DOWN);
									}
								});
						((ScrollView) findViewById(R.id.tvScrollerBottom))
								.post(new Runnable() {
									public void run() {
										((ScrollView) findViewById(R.id.tvScrollerBottom))
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

			transText = TranslatorClass.Translate(outText, inLang, outLang);
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
					Toast.LENGTH_LONG).show();
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