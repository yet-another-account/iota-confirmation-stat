package eukaryote.iota.confstat;

public enum Status {
	/**
	 * Transaction has inclusion in latest milestone
	 */
	CONFIRMED("Confirmed"),
	
	/**
	 * Transaction waiting to be reviewed and referenced via new transaction
	 */
	PENDING("Pending"),
	
	/**
	 * Double spent failed transaction and will never be confirmed
	 */
	DOUBLESPEND("Failed - Double Spent"),
	
	/**
	 * Depth too low in network and needs to be reattached
	 */
	DEPTHLOW("Failed - Reattach Needed"),
	
	/**
	 * Transaction has invalid trytes
	 */
	INVALID("Failed - Invalid");
	
	String name;
	
	Status(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
