package com.bespoke.zaghi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.language.Language;

public class RecordMode extends Activity implements OnInitListener {

	protected static final int RESULT_SPEECH = 1;

	private static final String TAG = "iSpeech API";

	// status check code
	private int MY_DATA_CHECK_CODE = 0;

	private ImageButton btnSpeak;
	private Button btnTranslate;
	private Button saveTextButton;
	private Button btnClearText;

	private TextView tvTranslatedText;
	private TextView tvOutputBox;
	private TextView tvInLang;
	private TextView tvOutLang;
	private TextView tvInVoice;
	private TextView tvInText;
	private TextView tvResult;

	private int startSelection = 0;
	private int endSelection = 0;

	private String outText = "";
	private String transText = "";
	private String selectedText = "";

	// localization
	private String wordClearText = "Clear Text";
	private String wordSaveText = "Save Text";
	private String wordSelectFile = "Select file";
	private String wordEnterFileName = "Enter File Name";
	private String wordOk = "Ok";
	private String wordCancel = "Cancel";
	private String wordOpen = "Open";
	private String wordDelete = "Delete";
	private String wordWarning = "Warning";
	private String wordAreYouSureMsg = "Are you sure you want to leave without saving text?";
	private String wordYes = "Yes";
	private String wordNo = "No";
	private String wordSelectLang = "Please select a language.";
	private String wordSelectText = "Select text to be translated.";
	private String wordRecordingNow = "Recording now in progress.";

	private String globalIndex = "";

	// iSpeech instance
	private SpeechSynthesis synthesis;
	private Context _context;

	private Language inLang = null;
	private Language outLang = null;

	private Spinner inLangSpinner;
	private Spinner outLangSpinner;
	private Spinner fileChooserSpinner;
	private ArrayAdapter<String> adapter;

	private LangSpinnerSelector inLangSelectorClass = new LangSpinnerSelector();
	private LangSpinnerSelector outLangSelectorClass = new LangSpinnerSelector();

	private Translator TranslatorClass = new Translator();

	// TTs object
	private TextToSpeech myTTS;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_mode);

		addListenerOnSpinnerItemSelection();

		inLangSpinner = (Spinner) findViewById(R.id.sInLanguages);
		outLangSpinner = (Spinner) findViewById(R.id.sOutLanguages);
		fileChooserSpinner = (Spinner) findViewById(R.id.fileChooser);

		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
		btnTranslate = (Button) findViewById(R.id.bTranslates);
		saveTextButton = (Button) findViewById(R.id.saveButton);
		btnClearText = (Button) findViewById(R.id.clearButton);

		tvTranslatedText = (TextView) findViewById(R.id.tvTranslatedText);
		tvOutputBox = (TextView) findViewById(R.id.tvInput);
		tvInLang = (TextView) findViewById(R.id.tvInLang);
		tvOutLang = (TextView) findViewById(R.id.tvOutLang);
		tvInVoice = (TextView) findViewById(R.id.tvInVoice);
		tvInText = (TextView) findViewById(R.id.tvInText);
		tvResult = (TextView) findViewById(R.id.tvResult);

		initFileSpinner();
		readFilesIntoSpinner();
		prepareTTSEngine();

		// display locales to console
		Log.i("-------------", Arrays.toString(Locale.getAvailableLocales()));

		StartTTS();

		inLangSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			// New Language Selection
			int index;
			String[] langArray = getResources().getStringArray(
					R.array.languages_array);

			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				index = inLangSpinner.getSelectedItemPosition();
				globalIndex = index + "\n";

				inLang = inLangSelectorClass.LangSelector(String
						.valueOf(langArray[index].trim()));
				Toast t = Toast.makeText(getApplicationContext(),
						String.valueOf(langArray[index].trim()),
						Toast.LENGTH_SHORT);
				t.show();

				// fast localization
				tvInLang.setText(inLangSelectorClass.getWordInLang());
				tvOutLang.setText(inLangSelectorClass.getWordOutLang());
				tvInVoice.setText(inLangSelectorClass.getWordVoiceIn());
				tvInText.setText(inLangSelectorClass.getWordTranscript());
				tvResult.setText(inLangSelectorClass.getResult());
				btnTranslate.setText(inLangSelectorClass.getWordTranslate());

				if (inLang != null) {
					// slower localization
					new MyAsyncTaskFastLocalization() {
						protected void onPostExecute(Boolean result) {
							if (result) {
								btnClearText.setText(wordClearText);
								saveTextButton.setText(wordSaveText);
								initFileSpinner();
								readFilesIntoSpinner();
							}
						}
					}.execute();
					// slowest localization
					new MyAsyncTaskLocalization() {
						protected void onPostExecute(Boolean result) {
						}
					}.execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}

		});

		outLangSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			// New Language Selection
			int index;
			String[] langArray = getResources().getStringArray(
					R.array.languages_array);

			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				index = outLangSpinner.getSelectedItemPosition();
				outLang = outLangSelectorClass.LangSelector(String
						.valueOf(langArray[index].trim()));
				Toast t = Toast.makeText(getApplicationContext(),
						String.valueOf(langArray[index].trim()),
						Toast.LENGTH_SHORT);
				t.show();

				// localization

			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}

		});

		// Speech to Text Button Listener
		btnSpeak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (inLang != null)
					SpeechToTextMic();
				else {
					Toast t = Toast.makeText(getApplicationContext(),
							wordSelectLang, Toast.LENGTH_SHORT);
					t.show();
				}
			}

		});

		// Button to translate the selected text
		btnTranslate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				startSelection = tvOutputBox.getSelectionStart();
				endSelection = tvOutputBox.getSelectionEnd();

				selectedText = tvOutputBox.getText().toString()
						.substring(startSelection, endSelection);

				Log.d(TAG, "selected text is " + selectedText);

				// this will change the color of the selected text when
				// translated
				// remove if non desired....
				tvOutputBox.setText(Html.fromHtml(tvOutputBox.getText()
						.toString().substring(0, startSelection)
						+ "<font color='#33AA99'>"
						+ tvOutputBox.getText().toString()
								.substring(startSelection, endSelection)
						+ "</font>"
						+ tvOutputBox.getText().toString()
								.substring(endSelection, tvOutputBox.length())));

				if (outLang == null) {

					Toast t = Toast.makeText(getApplicationContext(),
							wordSelectLang, Toast.LENGTH_SHORT);
					t.show();
				} else if (selectedText.isEmpty()) {
					Toast t = Toast.makeText(getApplicationContext(),
							wordSelectText, Toast.LENGTH_SHORT);
					t.show();
				} else {
					new MyAsyncTask() {
						protected void onPostExecute(Boolean result) {
							if (result) {

								speakOut(transText);
								tvTranslatedText.append(Html
										.fromHtml("<font color='#33AA99'>"
												+ selectedText + "</font>"));
								tvTranslatedText.append("\n");
								tvTranslatedText.append(Html
										.fromHtml("<font color='#FF5533'>"
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

		// Listener for the save button
		saveTextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				saveDialog(false);
			}
		});

	} // end onCreate

	// Save dialog box pops up and prompts user
	public void saveDialog(final boolean exit) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(wordEnterFileName);

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton(wordOk, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				saveTheFile(value);
				tvOutputBox.setText("");
				if (exit) {
					finish();
				}
			}
		});

		alert.setNegativeButton(wordCancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});

		alert.show();
		// Hides SoftKeyboard on activity start
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	public void openDeleteDialog(final String file) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(file);
		// Set an EditText view to get user input
		alert.setNegativeButton(wordOpen,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						openFile(file);
					}
				});

		alert.setNeutralButton(wordDelete,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						if (!file.equals(fileChooserSpinner.getPrompt())) {

							adapter.remove(file);
							adapter.notifyDataSetChanged();
							deleteFile(file);
						}
					}
				});

		alert.setPositiveButton(wordCancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						dialog.cancel();
					}
				});
		alert.show();
		setSpinnerToDefault();
	}

	// Saves the final to internal storage
	public void saveTheFile(String fileName) {
		String tvString = globalIndex;

		tvString += tvOutputBox.getText().toString();

		// Write text in output box to a file
		try {

			String filePath = this.getFilesDir().toString() + "/" + fileName;
			String UTF8 = "utf8";

			BufferedWriter fout = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filePath), UTF8));

			fout.write(tvString);
			fout.close();

		} catch (IOException e) {
			Log.e(TAG, "Error writing to file " + e);
		}

		adapter.add(fileName);
		adapter.notifyDataSetChanged();

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

	// set up adapter for FileSpinner
	public void initFileSpinner() {
		List<String> str = new ArrayList<String>();

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, str);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fileChooserSpinner.setAdapter(adapter);
		fileChooserSpinner.setOnItemSelectedListener(new FileOnItemSelect());
		fileChooserSpinner.setPrompt(wordSelectFile);
		// fileChooserSpinner.setPromptId(R.string.spinnerprompt);
		adapter.notifyDataSetChanged(); // for localization
	}

	// populates the file selection spinner
	public void readFilesIntoSpinner() {

		String[] files = this.fileList();
		// Add Choose File prompt first
		adapter.add(fileChooserSpinner.getPrompt().toString());

		if (files != null) {
			// load files into spinner
			for (int i = 0; i < files.length; i++) {

				String fileType = MimeTypeMap.getFileExtensionFromUrl(files[i]
						.toString());

				if (!fileType.equals("mp3")) {
					adapter.add(files[i]);
				}

			}

			// Sets the spinners starting item as select file
			setSpinnerToDefault();
			adapter.notifyDataSetChanged();

		}
	}

	// Sets the spinners starting item as "select file"
	public void setSpinnerToDefault() {
		String val = fileChooserSpinner.getPrompt().toString();
		int spinnerPos = adapter.getPosition(val);
		fileChooserSpinner.setSelection(spinnerPos);
		adapter.notifyDataSetChanged();
	}

	// listener for clear text button
	public void clearTV(View v) {
		tvOutputBox.setText("");
		setSpinnerToDefault();
	}

	// listener for file selection spinner
	private class FileOnItemSelect implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {

			String selectedFile = fileChooserSpinner.getSelectedItem()
					.toString();

			if (selectedFile.equals(fileChooserSpinner.getPrompt())) {
				return;
			}

			openDeleteDialog(selectedFile);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	// When file is selected, display contents
	public void openFile(String file) {
		try {

			String filePath = this.getFilesDir().toString() + "/" + file;
			String UTF8 = "utf8";

			BufferedReader fin = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath), UTF8));

			String langCode = "";
			langCode = fin.readLine();
			inLangSpinner.setSelection(Integer.parseInt(langCode));

			StringBuilder output = new StringBuilder();
			String s;
			while ((s = fin.readLine()) != null) {
				output.append(s);
				output.append("\n");
			}

			tvOutputBox.setText(output);
			adapter.notifyDataSetChanged();
			fin.close();

		} catch (IOException e) {
			e.printStackTrace();
			Toast t = Toast.makeText(getApplicationContext(),
					"Could not read file" + e, Toast.LENGTH_SHORT);
			t.show();
		}

	}

	// STT Initiates the Microphone for voice input
	private void SpeechToTextMic() {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, wordRecordingNow);

		Intent detailsIntent = new Intent(
				RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
		sendOrderedBroadcast(detailsIntent, null, new LanguageDetailsChecker(),
				null, Activity.RESULT_OK, null, null);

		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
				inLangSelectorClass.GetInputLocale());
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "");
		intent.putExtra(
				RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,
				"30000");
		intent.putExtra(
				RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
				"3000000");
		intent.putExtra(
				RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
				"30000");

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
							RecordMode.this);
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

	// Initiates Android's Text To Speech Speaker
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
		/*
		 * Toast t = Toast.makeText(getApplicationContext(),
		 * outLangSelectorClass.GetInputLocale(), Toast.LENGTH_SHORT); t.show();
		 */

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
				tvOutputBox.append(outText);
				tvOutputBox.append("\n");

			}
			break;
		}
		}
	}

	// Async to Translate the text in background
	// called by above onActivityResult
	class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
			transText = TranslatorClass
					.Translate(selectedText, inLang, outLang);
			return true;
		}
	}

	class MyAsyncTaskFastLocalization extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
			wordSaveText = TranslatorClass.Translate("Save Text",
					Language.ENGLISH, inLang);
			wordClearText = TranslatorClass.Translate("Clear Text",
					Language.ENGLISH, inLang);
			wordSelectFile = TranslatorClass.Translate("Select file",
					Language.ENGLISH, inLang);
			return true;
		}
	}

	class MyAsyncTaskLocalization extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
			wordSelectFile = TranslatorClass.Translate("Select File",
					Language.ENGLISH, inLang);
			wordEnterFileName = TranslatorClass.Translate("Enter File Name",
					Language.ENGLISH, inLang);
			wordOk = TranslatorClass.Translate("Ok", Language.ENGLISH, inLang);
			wordCancel = TranslatorClass.Translate("Cancel", Language.ENGLISH,
					inLang);
			wordOpen = TranslatorClass.Translate("Open", Language.ENGLISH,
					inLang);
			wordDelete = TranslatorClass.Translate("Delete", Language.ENGLISH,
					inLang);
			wordWarning = TranslatorClass.Translate("Warning",
					Language.ENGLISH, inLang);
			wordAreYouSureMsg = TranslatorClass.Translate(
					"Are you sure you want to leave without saving text?",
					Language.ENGLISH, inLang);
			wordYes = TranslatorClass
					.Translate("Yes", Language.ENGLISH, inLang);
			wordNo = TranslatorClass.Translate("No", Language.ENGLISH, inLang);
			wordSelectLang = TranslatorClass.Translate(
					"Please select a language.", Language.ENGLISH, inLang);
			wordSelectText = TranslatorClass.Translate(
					"Select text to be translated.", Language.ENGLISH, inLang);
			wordRecordingNow = TranslatorClass.Translate(
					"Recording now in progress.", Language.ENGLISH, inLang);
			return true;
		}
	}

	// Initializes TTS from android
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

	@Override
	public void onBackPressed() {
		if (!tvOutputBox.getText().toString().isEmpty()) {
			warningDialog();
		} else
			super.onBackPressed();

		return;
	}

	// Save dialog box when user leaves record mode
	public void warningDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(wordWarning);
		alert.setMessage(wordAreYouSureMsg);

		// Leave without saving
		alert.setNegativeButton(wordYes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});

		alert.setNeutralButton(wordNo, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveDialog(true);
			}
		});

		alert.setPositiveButton(wordCancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});

		alert.show();
	}

}