package eukaryote.iota.confstat;

public enum Status {
	/**
	 * Transaction has inclusion in latest milestone
	 */
	CONFIRMED,
	
	/**
	 * Transaction waiting to be reviewed and referenced via new transaction
	 */
	PENDING,
	
	/**
	 * Double spent failed transaction and will never be confirmed
	 */
	DOUBLESPEND,
	
	/**
	 * Depth too low in network and needs to be reattached
	 */
	DEPTHLOW
}
