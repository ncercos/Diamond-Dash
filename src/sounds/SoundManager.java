package sounds;

import game.Game;
import levels.Level;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Written by Nicholas Cercos
 * Created on Dec 10 2023
 **/
public class SoundManager {

	private final Map<Sound, List<Clip>> songs, sfx;
	private Clip currentSong;

	private float volume;
	private boolean songMute, sfxMute;

	public SoundManager() {
		songs = new HashMap<>();
		sfx = new HashMap<>();
		loadSounds();
		playSong(Sound.MENU);
	}

	/**
	 * Loads all sounds for the game.
	 */
	public void loadSounds() {
		for(Sound sound : Sound.values()) {
			List<Clip> clips = getClips(sound);
			if(sound.isSong()) songs.put(sound, clips);
			else {
				sfx.put(sound, clips);
				updateSFXVolume();
			}
		}
	}

	// Play Sounds

	/**
	 * Rewinds an audio clip to the start and
	 * starts playing it.
	 *
	 * @param clip The audio clip to be played.
	 * @param loop Whether the audio should repeat infinitely.
	 */
	private void playSound(Clip clip, boolean loop) {
		clip.setMicrosecondPosition(0);
		if(loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
		else     clip.start();
	}

	/**
	 * Stops the current song and plays a new one.
	 *
	 * @param song The song to be played.
	 */
	public void playSong(Sound song) {
		Clip clip = getSongs(song).get(0);
		if(clip == null)return;

		stopSong();
		currentSong = clip;
		updateSongVolume();
		playSound(clip, true);
	}

	/**
	 * Plays a sound effect. Variable sounds will
	 * be randomly chosen and played as well.
	 *
	 * @param sfx The sound effect to be played.
	 */
	public void playSFX(Sound sfx) {
		List<Clip> clips = getEffects(sfx);
		if(clips.isEmpty())return;
		int randomSFX = ThreadLocalRandom.current().nextInt(clips.size());
		playSound(clips.get(randomSFX), false);
	}

	// Volume

	/**
	 * Updates the gain (volume) for a given audio clip.
	 * #setControl() does not have a range from 0-1, so we must translate it.
	 *
	 * @param clip The audio clip to be played.
	 */
	private void updateSoundVolume(Clip clip, boolean music) {
		if(clip == null)return;
		FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = ((range * volume) + gainControl.getMinimum()) - (music ? 15 : 0);
		if(gain < -80) gain = -80;
		gainControl.setValue(gain);
	}

	private void updateSongVolume() {
		updateSoundVolume(currentSong, true);
	}

	private void updateSFXVolume() {
		sfx.values().forEach(clips -> clips.forEach(c -> updateSoundVolume(c, false)));
	}

	// Mute

	/**
	 * Mutes / Un-mutes all given audio clips.
	 *
	 * @param sounds The audio clips to modify.
	 * @param value  Whether audio should be muted or un-muted.
	 */
	private void toggleMute(Collection<List<Clip>> sounds, boolean value) {
		sounds.forEach(s -> s.forEach(clip -> {
			BooleanControl booleanControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(value);
		}));
	}

	public void toggleSongMute() {
		this.songMute = !songMute;
		toggleMute(songs.values(), songMute);
	}

	public void toggleSFXMute() {
		this.sfxMute = !sfxMute;
		toggleMute(sfx.values(), sfxMute);
	}

	// Utilities

	public void setVolume(float volume) {
		this.volume = volume;
		updateSongVolume();
		updateSFXVolume();
	}

	public void startSong() {
		if(currentSong == null || currentSong.isActive())return;
		playSound(currentSong, true);
	}

	public void stopSong() {
		if(currentSong != null && currentSong.isActive())
			currentSong.stop();
	}

	/**
	 * Stops a specific sfx sound from playing.
	 *
	 * @param sound The sound that is being played.
	 */
	public void stopSFX(Sound sound) {
		List<Clip> clips = getEffects(sound);
		for(Clip c : clips) {
			if(c.isActive())
				c.stop();
		}
	}

	public void setLevelSong(Level level) {
		if(level.getId() % 2 == 0)
			playSong(Sound.LVL_1);
		else playSong(Sound.LVL_2);
	}

	/**
	 * Get an audio file clip to be played.
	 *
	 * @param name The name of the sound.
	 * @return A clip that contains the audio.
	 */
	private Clip getClip(String name) {
		File file = new File(Game.RESOURCE_URL + "sounds/" + name + ".wav");
		AudioInputStream audio;

		try {
			audio = AudioSystem.getAudioInputStream(file);
			Clip c = AudioSystem.getClip();
			c.open(audio);
			return c;
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get all audio clips for a sound.
	 *
	 * @param sound The specific sound to be obtained.
	 * @return An array of audio clips for a sound.
	 */
	private List<Clip> getClips(Sound sound) {
		List<Clip> clips = new ArrayList<>();
		for(int i = 0; i < sound.getVariants(); i++)
			clips.add(getClip(sound.getFileName() + (sound.isVariable() ? "_" + (i + 1) : "")));
		return clips;
	}

	private List<Clip> getSongs(Sound sound) {
		return songs.getOrDefault(sound, null);
	}

	private List<Clip> getEffects(Sound sound) {
		return sfx.getOrDefault(sound, null);
	}
}
