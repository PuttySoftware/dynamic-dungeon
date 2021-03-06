package net.dynamicdungeon.sound;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Music {
    // Fields
    private final URL url;
    private AudioInputStream stream;
    private AudioInputStream decodedStream;
    private AudioFormat format;
    private AudioFormat decodedFormat;
    private boolean stop;

    public Music(final URL loc) {
	this.url = loc;
	this.stop = false;
    }

    public static void play() {
	final Music m = new Music(Music.class.getResource("/assets/music/dungeon.ogg"));
	final Thread t = new Thread() {
	    @Override
	    public void run() {
		m.playLoop();
	    }
	};
	t.setName("Music Player");
	t.setDaemon(true);
	t.start();
    }

    public void playLoop() {
	while (!this.stop) {
	    try {
		// Get AudioInputStream from given file.
		this.stream = AudioSystem.getAudioInputStream(this.url);
		this.decodedStream = null;
		if (this.stream != null) {
		    this.format = this.stream.getFormat();
		    this.decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, this.format.getSampleRate(),
			    16, this.format.getChannels(), this.format.getChannels() * 2, this.format.getSampleRate(),
			    false);
		    // Get AudioInputStream that will be decoded by underlying
		    // VorbisSPI
		    this.decodedStream = AudioSystem.getAudioInputStream(this.decodedFormat, this.stream);
		}
	    } catch (final Exception e) {
		// Do nothing
	    }
	    try (SourceDataLine line = Music.getLine(this.decodedFormat)) {
		if (line != null) {
		    try {
			final byte[] data = new byte[4096];
			// Start
			line.start();
			int nBytesRead = 0;
			while (nBytesRead != -1) {
			    nBytesRead = this.decodedStream.read(data, 0, data.length);
			    if (nBytesRead != -1) {
				line.write(data, 0, nBytesRead);
			    }
			    if (this.stop) {
				break;
			    }
			}
			// Stop
			line.drain();
			line.stop();
		    } catch (final IOException io) {
			// Do nothing
		    } finally {
			// Stop
			line.drain();
			line.stop();
		    }
		}
	    } catch (final LineUnavailableException lue) {
		// Do nothing
	    }
	}
    }

    private static SourceDataLine getLine(final AudioFormat audioFormat) throws LineUnavailableException {
	SourceDataLine res = null;
	final DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	res = (SourceDataLine) AudioSystem.getLine(info);
	res.open(audioFormat);
	return res;
    }

    public void stopLoop() {
	this.stop = true;
    }
}