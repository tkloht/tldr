package com.tldr.tools;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

/**
 * Klasse, welche die Sprachausgabe ermöglicht.
 * 
 * @author Christoph
 * 
 */
public class KnightRider implements OnInitListener {

	private Context context;
	TextToSpeech tts;
	private boolean readytospeek;

	public KnightRider(Context c) {
		this.context = c;
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		c.startActivity(checkIntent);
		try {
			tts = new TextToSpeech(c, this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * Sagt den angegebenen String, wenn readytospeak. (Initialisierung des
	 * Knightriders vor Benutzung, am Besten in onCreate).
	 */
	public void say(String s) {
		if (!readytospeek) {

		} else {
			tts.speak(s, TextToSpeech.QUEUE_ADD, null);
		}
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.GERMAN);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {
				this.readytospeek = true;
				// Ab jetzt kann das Gerät sprechen
			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}
	}
}
