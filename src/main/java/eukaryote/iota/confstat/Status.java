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
	DOUBLESPENT("Double Spent (Reattach Confirmed)"),
	
	/**
	 * Double spent failed transaction and will never be confirmed
	 */
	BADSIG("Failed - Bad Signature"),
	
	
	/**
	 * Transaction has invalid trytes
	 */
	INVALID("Failed - Invalid Trytes");
	
	String name;
	
	Status(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
