package com.bespoke.zaghi;

import java.util.Locale;

import android.util.Log;

import com.memetix.mst.language.Language;

public class LangSpinnerSelector {

	private Locale loc = null;
	private Language lang = null;
	
	private String voiceType = null;
	private String result = "Result:";
	private String wordInLang = "Input Language";
	private String wordOutLang = "Output Language";
	private String wordTextIn = "Text Input:";
	private String wordToBeTranslated = "To Be Translated...";
	private String wordTranslate = "Translate";
	private String wordVoiceIn = "Voice Input:";
	private String wordLang = "Language";
	private String wordStartRecord = "Start Recording:";
	private String wordTranscript = "Transcription:";
	
	public enum Lang {
		Arabic, Bulgarian, Catalan, Czech, Danish, Dutch, English, Estonian, Finnish, French, German, Greek, Hebrew, Hindi, Hungarian, Indonesian, Italian, Japanese, Korean, Latvian, Lithuanian, Norwegian, Polish, Portuguese, Romanian, Russian, Slovak, Slovenian, Spanish, Swedish, Thai, Turkish, Ukrainian, Vietnamese;
	}

	public Language LangSelector(String str) {

		if (str.equals("Haitian (Creole)")) {
			lang = Language.HAITIAN_CREOLE;
			loc = LocaleCreator.HUNGARIAN;
			voiceType = null;
			result = "Rezilta sa:";
			wordInLang = "D' lang";
			wordOutLang = "sortie lang";
			wordTextIn = "Tèks D':";
			wordToBeTranslated = "Pou fè ke...";
			wordTranslate = "Tradwi";
			wordVoiceIn = "Voix D':";
			wordLang = "Lang";
			wordStartRecord = "Kòmanse albòm li ki anrejistre:";
			wordTranscript = "Transcription:";
		} else if (str.equals("Choose a language...")){
			lang = null;
			loc = null;
			voiceType = null;
			result = "Result:";
			wordInLang = "Input Language";
			wordOutLang = "Output Language";
			wordTextIn = "Text Input:";
			wordToBeTranslated = "To be translated…";
			wordTranslate = "Translate";
			wordVoiceIn = "Voice Input:";
			wordLang = "Language";
			wordStartRecord = "Start Recording:";
			wordTranscript = "Transcription:";
		} else if (str.equals("Chinese (Simplified)")) {
			lang = Language.CHINESE_SIMPLIFIED;
			loc = Locale.SIMPLIFIED_CHINESE;
			voiceType = "chchinesemale";
			result = "结果：";
			wordInLang = "输入的语言";
			wordOutLang = "输出语言";
			wordTextIn = "输入文本：";
			wordToBeTranslated = "要翻译......";
			wordTranslate = "翻译";
			wordVoiceIn = "语音输入：";
			wordLang = "语言";
			wordStartRecord = "开始录制：";
			wordTranscript = "转录：";
		} else if (str.equals("Chinese (Traditional)")) {
			lang = Language.CHINESE_TRADITIONAL;
			voiceType = "chchinesefemale";
			loc = Locale.TRADITIONAL_CHINESE;
			result = "結果：";
			wordInLang = "輸入的語言";
			wordOutLang = "輸出語言";
			wordTextIn = "輸入文本：";
			wordToBeTranslated = "要翻譯......";
			wordTranslate = "翻譯";
			wordVoiceIn = "語音輸入：";
			wordLang = "語言";
			wordStartRecord = "開始錄製：";
			wordTranscript = "轉錄：";
		} else if (str.equals("Hmong Daw")) {
			lang = Language.HMONG_DAW;
			loc = LocaleCreator.HUNGARIAN;
			voiceType = "hkchinesefemale";
			result = "Raug:";
			wordInLang = "Cov lus input";
			wordOutLang = "Rau cov zis lus";
			wordTextIn = "Szövegbevitel:";
			wordToBeTranslated = "Muab txhais...";
			wordTranslate = "Txhais";
			wordVoiceIn = "Lub suab tawm tswv yim:";
			wordLang = "Hais lus";
			wordStartRecord = "Pib cov ntaubntawv povthawj siv:";
			wordTranscript = "Transcription:";
		} else if (str.equals("English (UK)")) {
			lang = Language.ENGLISH;
			loc = LocaleCreator.UK;
			voiceType = "ukenglishmale";
			result = "Result:";
			wordInLang = "Input Language";
			wordOutLang = "Output Language";
			wordTextIn = "Text Input:";
			wordToBeTranslated = "To be translated…";
			wordTranslate = "Translate";
			wordVoiceIn = "Voice Input:";
			wordLang = "Language";
			wordStartRecord = "Start Recording:";
			wordTranscript = "Transcription:";
		} else {

			switch (Lang.valueOf(str)) {
			case Arabic: {
				lang = Language.ARABIC;
				loc = LocaleCreator.ARABIC;
				voiceType = "arabicmale";
				result = "النتيجة:";
				wordInLang = "لغة الإدخال";
				wordOutLang = "لغة الإخراج";
				wordTextIn = "إدخال النص:";
				wordToBeTranslated = "لترجمة...";
				wordTranslate = "ترجمة";
				wordVoiceIn = "صوت الإدخال:";
				wordLang = "اللغة";
				wordStartRecord = "بدء التسجيل:";
				wordTranscript = "النسخ:";
				break;
			}

			case Bulgarian: {
				lang = Language.BULGARIAN;
				loc = LocaleCreator.BULGARIAN;
				voiceType = "rurussianmale";
				result = "Резултат:";
				wordInLang = "Език на въвеждане";
				wordOutLang = "Изходен език";
				wordTextIn = "Въвеждане на текст:";
				wordToBeTranslated = "Да бъдат преведени...";
				wordTranslate = "Превод";
				wordVoiceIn = "Въвеждане на глас:";
				wordLang = "Език";
				wordStartRecord = "Започнете записа:";
				wordTranscript = "Транскрипция:";
				break;
			}

			case Catalan: {
				lang = Language.CATALAN;
				loc = LocaleCreator.CATALAN;
				voiceType = "eurcatalanfemale";
				result = "Resultat:";
				wordInLang = "Llengua d'aportació";
				wordOutLang = "Idioma de sortida";
				wordTextIn = "Entrada de text:";
				wordToBeTranslated = "A traduir...";
				wordTranslate = "Traduir";
				wordVoiceIn = "Entrada de veu:";
				wordLang = "Llengua";
				wordStartRecord = "Inicia l'enregistrament:";
				wordTranscript = "Transcripció:";
				break;
			}

			case Czech: {
				lang = Language.CZECH;
				loc = LocaleCreator.CZECH;
				voiceType = "eurczechfemale";
				result = "Výsledek:";
				wordInLang = "Jazyk zadávání";
				wordOutLang = "Výstupní jazyk";
				wordTextIn = "Zadávání textu:";
				wordToBeTranslated = "Chcete-li přeložit...";
				wordTranslate = "Přeložit";
				wordVoiceIn = "Hlas vstup:";
				wordLang = "Jazyk";
				wordStartRecord = "Spuštění nahrávání:";
				wordTranscript = "Přepis:";
				break;
			}

			case Danish: {
				lang = Language.DANISH;
				loc = LocaleCreator.DANISH;
				voiceType = "eurdanishfemale";
				result = "Resultat:";
				wordInLang = "Inputsprog";
				wordOutLang = "Output sprog";
				wordTextIn = "Indtastning af tekst:";
				wordToBeTranslated = "Oversat...";
				wordTranslate = "Oversætte";
				wordVoiceIn = "Stemmeinput:";
				wordLang = "Sprog";
				wordStartRecord = "Start optagelse:";
				wordTranscript = "Transskription:";
				break;
			}

			case Dutch: {
				lang = Language.DUTCH;
				loc = LocaleCreator.DUTCH;
				voiceType = "eurdutchfemale";
				result = "Resultaat:";
				wordInLang = "Invoertaal";
				wordOutLang = "Uitvoer taal";
				wordTextIn = "Tekstinvoer:";
				wordToBeTranslated = "Te vertalen...";
				wordTranslate = "Vertalen";
				wordVoiceIn = "Stem Input:";
				wordLang = "Taal";
				wordStartRecord = "Opname te starten:";
				wordTranscript = "Transcriptie:";
				break;
			}

			case English: {
				lang = Language.ENGLISH;
				loc = LocaleCreator.US;
				voiceType = "usenglishfemale";
				result = "Result:";
				wordInLang = "Input Language";
				wordOutLang = "Output Language";
				wordTextIn = "Text Input:";
				wordToBeTranslated = "To be translated…";
				wordTranslate = "Translate";
				wordVoiceIn = "Voice Input:";
				wordLang = "Language";
				wordStartRecord = "Start Recording:";
				wordTranscript = "Transcription:";
				break;
			}

			case Estonian: {
				lang = Language.ESTONIAN;
				loc = LocaleCreator.ESTONIAN;
				voiceType = "eurfinnishfemale";
				result = "Tulemus:";
				wordInLang = "Sisestuskeel";
				wordOutLang = "Väljundi keel";
				wordTextIn = "Teksti sisestamine:";
				wordToBeTranslated = "Tõlkida...";
				wordTranslate = "Tõlkida";
				wordVoiceIn = "Voice sisend:";
				wordLang = "Keel";
				wordStartRecord = "Alusta salvestamist:";
				wordTranscript = "Transkriptsioon:";
				break;
			}

			case Finnish: {
				lang = Language.FINNISH;
				loc = LocaleCreator.FINNISH;
				voiceType = "eurfinnishfemale";
				result = "Tulos:";
				wordInLang = "Syöttökieli";
				wordOutLang = "Tuotoksen kieli";
				wordTextIn = "Tekstin kirjoittaminen:";
				wordToBeTranslated = "Käännettävä...";
				wordTranslate = "Kääntää";
				wordVoiceIn = "Äänisyötteet:";
				wordLang = "Kieli";
				wordStartRecord = "Aloita äänitys:";
				wordTranscript = "Transkriptio:";
				break;
			}
			case French: {
				lang = Language.FRENCH;
				loc = LocaleCreator.FRENCH;
				voiceType = "cafrenchfemale";
				result = "Résultat:";
				wordInLang = "Langue d'entrée";
				wordOutLang = "Langue sortie";
				wordTextIn = "Saisie de texte :";
				wordToBeTranslated = "À traduire...";
				wordTranslate = "Traduire";
				wordVoiceIn = "Entrée de voix :";
				wordLang = "Langue";
				wordStartRecord = "Démarrer l'enregistrement :";
				wordTranscript = "Transcription :";
				break;
			}

			case German: {
				lang = Language.GERMAN;
				loc = LocaleCreator.GERMANY;
				voiceType = "eurgermanfemale";
				result = "Ergebnis:";
				wordInLang = "Eingabesprache";
				wordOutLang = "Ausgabe-Sprache";
				wordTextIn = "Texteingabe:";
				wordToBeTranslated = "Übersetzt werden...";
				wordTranslate = "Übersetzen";
				wordVoiceIn = "Spracheingabe:";
				wordLang = "Sprache";
				wordStartRecord = "Aufnahme zu starten:";
				wordTranscript = "Transkription:";
				break;
			}

			case Greek: {
				lang = Language.GREEK;
				loc = LocaleCreator.GREEK;
				voiceType = "eurgreekfemale";
				result = "Αποτέλεσμα:";
				wordInLang = "Γλώσσα εισόδου";
				wordOutLang = "Γλώσσα εξόδου";
				wordTextIn = "Εισαγωγής κειμένου:";
				wordToBeTranslated = "Να μεταφραστεί...";
				wordTranslate = "Μεταφράσετε";
				wordVoiceIn = "Φωνή εισόδου:";
				wordLang = "Γλώσσα";
				wordStartRecord = "Ξεκινήστε την εγγραφή:";
				wordTranscript = "Μεταγραφή:";
				break;
			}

			case Hebrew: {
				lang = Language.HEBREW;
				loc = LocaleCreator.HEBREW;
				voiceType = "arabicmale";
				result = "התוצאה:";
				wordInLang = "שפת הקלט";
				wordOutLang = "פלט שפה";
				wordTextIn = "קלט טקסט:";
				wordToBeTranslated = "כדי להיות מתורגם...";
				wordTranslate = "לתרגם";
				wordVoiceIn = "כניסת קול:";
				wordLang = "שפה";
				wordStartRecord = "התחל הקלטה:";
				wordTranscript = "שעתוק:";
				break;
			}

			case Hindi: {
				lang = Language.HINDI;
				loc = LocaleCreator.HINDI;
				voiceType = null;
				result = "परिणाम:";
				wordInLang = "इनपुट भाषा";
				wordOutLang = "आउटपुट भाषा";
				wordTextIn = "पाठ इनपुट:";
				wordToBeTranslated = "अनुवाद किया जा करने के लिए...";
				wordTranslate = "का अनुवाद";
				wordVoiceIn = "आवाज़ इनपुट:";
				wordLang = "भाषा";
				wordStartRecord = "रिकॉर्डिंग प्रारंभ करें:";
				wordTranscript = "प्रतिलेखन:";
				break;
			}

			// Hmong_Daw
			case Hungarian: {
				lang = Language.HUNGARIAN;
				loc = LocaleCreator.HUNGARIAN;
				voiceType = "huhungarianfemale";
				result = "Eredmény:";
				wordInLang = "Beviteli nyelv";
				wordOutLang = "Kimeneti nyelvet";
				wordTextIn = "Masukan teks:";
				wordToBeTranslated = "Fordítandó...";
				wordTranslate = "Lefordít";
				wordVoiceIn = "Hang bemenet:";
				wordLang = "Nyelv";
				wordStartRecord = "Felvétel indítása:";
				wordTranscript = "Átírás:";
				break;
			}

			case Indonesian: {
				lang = Language.INDONESIAN;
				loc = LocaleCreator.INDONESIAN;
				voiceType = null;
				result = "Hasil:";
				wordInLang = "Bahasa masukan";
				wordOutLang = "bahasa keluaran";
				wordTextIn = "Input Text:";
				wordToBeTranslated = "Diterjemahkan...";
				wordTranslate = "Menerjemahkan";
				wordVoiceIn = "Input suara:";
				wordLang = "Bahasa";
				wordStartRecord = "Mulai merekam:";
				wordTranscript = "Transkripsi:";
				break;
			}

			case Italian: {
				lang = Language.ITALIAN;
				loc = LocaleCreator.ITALY;
				voiceType = "euritalianmale";
				result = "Risultato:";
				wordInLang = "Lingua di input";
				wordOutLang = "Linguaggio di output";
				wordTextIn = "Inserimento testo:";
				wordToBeTranslated = "Per essere tradotto...";
				wordTranslate = "Traduci";
				wordVoiceIn = "Input vocale:";
				wordLang = "Lingua";
				wordStartRecord = "Avviare la registrazione:";
				wordTranscript = "Trascrizione:";
				break;
			}

			case Japanese: {
				lang = Language.JAPANESE;
				loc = LocaleCreator.JAPAN;
				voiceType = "jpjapanesemale";
				result = "結果:";
				wordInLang = "入力言語";
				wordOutLang = "出力言語";
				wordTextIn = "テキスト入力:";
				wordToBeTranslated = "翻訳する.";
				wordTranslate = "翻訳";
				wordVoiceIn = "音声入力:";
				wordLang = "言語";
				wordStartRecord = "記録を開始します。";
				wordTranscript = "転写：";
				break;
			}

			case Korean: {
				lang = Language.KOREAN;
				loc = LocaleCreator.KOREA;
				voiceType = "krkoreanfemale";
				result = "결과:";
				wordInLang = "입력된 언어";
				wordOutLang = "출력 언어";
				wordTextIn = "텍스트 입력:";
				wordToBeTranslated = "번역을...";
				wordTranslate = "번역하기";
				wordVoiceIn = "음성 입력:";
				wordLang = "언어";
				wordStartRecord = "녹음 시작:";
				wordTranscript = "녹음 방송:";
				break;
			}

			case Latvian: {
				lang = Language.LATVIAN;
				loc = LocaleCreator.LATVIAN;
				voiceType = null;
				result = "Rezultāts:";
				wordInLang = "Ievades valodu";
				wordOutLang = "Izvades valoda";
				wordTextIn = "Teksta ievades metodi:";
				wordToBeTranslated = "Tulko...";
				wordTranslate = "Tulkot";
				wordVoiceIn = "Balss ievade:";
				wordLang = "Valodas";
				wordStartRecord = "Sāk reģistrēt:";
				wordTranscript = "Transkripcija:";
				break;
			}

			case Lithuanian: {
				lang = Language.LITHUANIAN;
				loc = LocaleCreator.LITHUANIAN;
				voiceType = null;
				result = "Rezultatas:";
				wordInLang = "Įvesties kalbą";
				wordOutLang = "Išvesties kalba";
				wordTextIn = "Teksto įvestis:";
				wordToBeTranslated = "Išversti...";
				wordTranslate = "Versti";
				wordVoiceIn = "Balso įrašus:";
				wordLang = "Kalba";
				wordStartRecord = "Pradėti įrašymą:";
				wordTranscript = "Transkripcija:";
				break;
			}

			case Norwegian: {
				lang = Language.NORWEGIAN;
				loc = LocaleCreator.NORWEGIAN;
				voiceType = null;
				result = "Resultat:";
				wordInLang = "Inndataspråk";
				wordOutLang = "Utgang språk";
				wordTextIn = "Skriving:";
				wordToBeTranslated = "Å bli oversatt...";
				wordTranslate = "Oversette";
				wordVoiceIn = "Talekommandoen:";
				wordLang = "Språk";
				wordStartRecord = "Starte opptak:";
				wordTranscript = "Transkripsjon:";
				break;
			}

			case Polish: {
				lang = Language.POLISH;
				loc = LocaleCreator.POLISH;
				voiceType = "eurpolishfemale";
				result = "Wynik:";
				wordInLang = "Język";
				wordOutLang = "Język wyjściowy";
				wordTextIn = "Wprowadzania tekstu:";
				wordToBeTranslated = "Aby przetłumaczyć...";
				wordTranslate = "Przetłumacz";
				wordVoiceIn = "Głosu:";
				wordLang = "Język";
				wordStartRecord = "Rozpocząć nagrywanie:";
				wordTranscript = "Transkrypcja:";
				break;
			}

			case Portuguese: {
				lang = Language.PORTUGUESE;
				loc = LocaleCreator.PORTUGUESE;
				voiceType = "brportuguesefemale";
				result = "Resultado:";
				wordInLang = "Idioma de entrada";
				wordOutLang = "Língua de saída";
				wordTextIn = "Entrada de texto:";
				wordToBeTranslated = "A traduzir...";
				wordTranslate = "Traduzir";
				wordVoiceIn = "Entrada de voz:";
				wordLang = "Língua";
				wordStartRecord = "Inicie a gravação:";
				wordTranscript = "Transcrição:";
				break;
			}

			case Romanian: {
				lang = Language.ROMANIAN;
				loc = LocaleCreator.ROMANIAN;
				voiceType = "eurpolishfemale";
				result = "Rezultatul:";
				wordInLang = "Limbă";
				wordOutLang = "Ieșire Limba";
				wordTextIn = "De introducere a textului:";
				wordToBeTranslated = "Pentru a fi tradus...";
				wordTranslate = "Traduce";
				wordVoiceIn = "Voce de intrare:";
				wordLang = "Limba";
				wordStartRecord = "Pornire înregistrare:";
				wordTranscript = "Transcrierea:";
				break;
			}

			case Russian: {
				lang = Language.RUSSIAN;
				loc = LocaleCreator.RUSSIAN;
				voiceType = "rurussianfemale";
				result = "Результат:";
				wordInLang = "Язык ввода";
				wordOutLang = "Выходной язык";
				wordTextIn = "Ввод текста:";
				wordToBeTranslated = "Для перевода...";
				wordTranslate = "Перевести";
				wordVoiceIn = "Голосовой ввод:";
				wordLang = "Язык";
				wordStartRecord = "Начните запись:";
				wordTranscript = "Транскрипция:";
				break;
			}

			case Slovak: {
				lang = Language.SLOVAK;
				loc = LocaleCreator.SLOVAK;
				voiceType = "eurczechfemale";
				result = "Výsledok:";
				wordInLang = "Vstupný jazyk";
				wordOutLang = "Výstup jazyk";
				wordTextIn = "Zadávania textu:";
				wordToBeTranslated = "Byť preložený...";
				wordTranslate = "Preložiť";
				wordVoiceIn = "Hlasový vstup:";
				wordLang = "Jazyk";
				wordStartRecord = "Spustenie nahrávania:";
				wordTranscript = "Prepis:";
				break;
			}

			case Slovenian: {
				lang = Language.SLOVENIAN;
				loc = LocaleCreator.SLOVENIAN;
				voiceType = null;
				result = "Rezultat:";
				wordInLang = "Jezik vnosa";
				wordOutLang = "Izhodni jezik";
				wordTextIn = "Vnos besedila:";
				wordToBeTranslated = "Treba prevesti...";
				wordTranslate = "Prevajanje";
				wordVoiceIn = "Glasovnih ukazov:";
				wordLang = "Jezik";
				wordStartRecord = "Začetek snemanja:";
				wordTranscript = "Prepis:";
				break;
			}

			case Spanish: {
				lang = Language.SPANISH;
				loc = LocaleCreator.SPANISH;
				voiceType = "usspanishfemale";
				result = "Resultado:";
				wordInLang = "Idioma de entrada";
				wordOutLang = "Idioma de salida";
				wordTextIn = "Entrada de texto:";
				wordToBeTranslated = "A traducir...";
				wordTranslate = "Traducir";
				wordVoiceIn = "Entrada de voz:";
				wordLang = "Idioma";
				wordStartRecord = "Iniciar grabación:";
				wordTranscript = "Transcripción:";
				break;
			}

			case Swedish: {
				lang = Language.SWEDISH;
				loc = LocaleCreator.SWEDISH;
				voiceType = "swswedishfemale";
				result = "Resultat:";
				wordInLang = "Inmatningsspråk";
				wordOutLang = "Utdata språk";
				wordTextIn = "Textinmatning:";
				wordToBeTranslated = "Att översätta...";
				wordTranslate = "Översätta";
				wordVoiceIn = "Voice Input:";
				wordLang = "Språk";
				wordStartRecord = "Starta inspelningen:";
				wordTranscript = "Transkription:";
				break;
			}

			case Thai: {
				lang = Language.THAI;
				loc = LocaleCreator.THAI;
				voiceType = null;
				result = "ผลลัพธ์:";
				wordInLang = "ภาษาสำหรับการป้อนค่า";
				wordOutLang = "แสดงผลภาษา";
				wordTextIn = "ข้อความที่ป้อนข้อมูล:";
				wordToBeTranslated = "จะแปล...";
				wordTranslate = "แปล";
				wordVoiceIn = "อินพุตเสียง:";
				wordLang = "ภาษา";
				wordStartRecord = "เริ่มต้นบันทึก:";
				wordTranscript = "การถอดความ:";
				break;
			}

			case Turkish: {
				lang = Language.TURKISH;
				loc = LocaleCreator.TURKISH;
				voiceType = "eurturkishfemale";
				result = "Sonuç:";
				wordInLang = "Giriş dili";
				wordOutLang = "Çıkış dili";
				wordTextIn = "Metin girişi:";
				wordToBeTranslated = "Çevrilmesi gereken...";
				wordTranslate = "Çevir";
				wordVoiceIn = "Ses girişi:";
				wordLang = "Dil";
				wordStartRecord = "Kaydı Başlat:";
				wordTranscript = "Transkripsiyon:";
				break;
			}

			case Ukrainian: {
				lang = Language.UKRAINIAN;
				loc = LocaleCreator.UKRANIAN;
				voiceType = null;
				result = "Результат:";
				wordInLang = "Мова вводу";
				wordOutLang = "Вихідний мова";
				wordTextIn = "Введення тексту:";
				wordToBeTranslated = "Щоб перекласти...";
				wordTranslate = "Перекласти";
				wordVoiceIn = "Голосового вводу:";
				wordLang = "Мова";
				wordStartRecord = "Почати записування:";
				wordTranscript = "Транскрипція:";
				break;
			}

			case Vietnamese: {
				lang = Language.VIETNAMESE;
				loc = LocaleCreator.VIETNAMESE;
				voiceType = null;
				result = "Kết quả:";
				wordInLang = "Ngôn ngữ nhập";
				wordOutLang = "Đầu ra ngôn ngữ";
				wordTextIn = "Kiểu nhập văn bản:";
				wordToBeTranslated = "Được dịch...";
				wordTranslate = "Dịch";
				wordVoiceIn = "Giọng nói:";
				wordLang = "Ngôn ngữ";
				wordStartRecord = "Bắt đầu ghi âm:";
				wordTranscript = "Sao chép:";
				break;
			}
			default:
				Log.e("TTS", "Error with Lang Spinner");
				lang = null;
				loc = null;
				voiceType = null;
				result = "Result:";
				wordInLang = "Input Language";
				wordOutLang = "Output Language";
				wordTextIn = "Text Input:";
				wordToBeTranslated = "To be translated…";
				wordTranslate = "Translate";
				wordVoiceIn = "Voice Input:";
				wordLang = "Language";
				wordStartRecord = "Start Recording:";
				wordTranscript = "Transcription:";
			}
		}
		
		return lang;
	}

	public String whichVoiceType() {
		return this.voiceType;
	}

	public Locale whichLoc() {

		return this.loc;

	}
	public Language whichLang(){
		return this.lang;
	}

	public String GetInputLocale() {
		String str = loc.toString();
		return str;
	}
	
	public String getResult(){
		return this.result;
	}
	public String getWordInLang(){
		return this.wordInLang;
	}
	public String getWordOutLang(){
		return this.wordOutLang;
	}
	public String getWordTextIn(){
		return this.wordTextIn;
	}
	public String getWordToBeTranslated(){
		return this.wordToBeTranslated;
	}
	public String getWordTranslate(){
		return this.wordTranslate;
	}
	public String getWordVoiceIn(){
		return this.wordVoiceIn;
	}
	public String getWordLang(){
		return this.wordLang;
	}
	public String getWordStartRecord(){
		return this.wordStartRecord;
	}
	public String getWordTranscript(){
		return this.wordTranscript;
	}
}
