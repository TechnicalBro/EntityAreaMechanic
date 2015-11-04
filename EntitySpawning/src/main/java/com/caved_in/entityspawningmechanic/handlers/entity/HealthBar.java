package com.caved_in.entityspawningmechanic.handlers.entity;

public enum HealthBar {
	ONE("█         "),
	TWO("██        "),
	THREE("███       "),
	FOUR("████      "),
	FIVE("█████     "),
	SIX("██████    "),
	SEVEN("███████   "),
	EIGHT("████████  "),
	NINE("█████████ "),
	TEN("██████████");

	private final String healthBar;

	HealthBar(String healthBar) {
		this.healthBar = healthBar;
	}

	@Override
	public String toString() {
		return this.healthBar;
	}

	public static String getHealthBar(int barAmount) {
		int generateAmount = (barAmount > 10 ? 10 : barAmount);
		switch (generateAmount) {
			case 10:
				return TEN.toString();
			case 9:
				return NINE.toString();
			case 8:
				return EIGHT.toString();
			case 7:
				return SEVEN.toString();
			case 6:
				return SIX.toString();
			case 5:
				return FIVE.toString();
			case 4:
				return FOUR.toString();
			case 3:
				return THREE.toString();
			case 2:
				return TWO.toString();
			case 1:
				return ONE.toString();
			default:
				return "";
		}
	}

	public static String getHealthBarChar() {
		return "█";
	}

}
