package sounds;

/**
 * Written by Nicholas Cercos
 * Created on Dec 10 2023
 **/
public enum Sound {

	// Music
	MENU("menu", true),
	LVL_1("level_1", true),
	LVL_2("level_2", true),

	// Effects
	LVL_COMPLETE("level_complete"),
	GAME_OVER("game_over"),
	JUMP("jump"),
	ROLL("roll"),
	ATTACK("attack", 3),
	HURT("hurt"),
	DEATH("death"),
	GEM("gem"),
	HEAL("heal"),
	SPEED("speed"),
	DEBUFF("debuff"),
	WATER("water"),
	CRATE_OPEN("crate_open"),
	BRANCH("branch"),


	// UI
	CLICK("click"),
	PAUSE("pause"),
	UNPAUSE("unpause");

	private final String fileName;
	private final int variants;
	private final boolean song;

	Sound(String fileName, boolean song, int variants) {
		this.fileName = fileName;
		this.song = song;
		this.variants = variants;
	}

	Sound(String fileName, boolean song) {
		this.fileName = fileName;
		this.song = song;
		variants = 1;
	}

	Sound(String fileName, int variants) {
		this.fileName = fileName;
		this.variants = variants;
		song = false;
	}

	Sound(String fileName) {
		this.fileName = fileName;
		this.song = false;
		variants = 1;
	}

	public String getFileName() {
		return fileName;
	}

	/**
	 * @return True if there are multiple sounds under this name.
	 * Ex: attack_1, attack_2, attack_3, etc, etc.
	 */
	public boolean isVariable() {
		return variants > 1;
	}

	public int getVariants() {
		return variants;
	}

	public boolean isSong() {
		return song;
	}
}
