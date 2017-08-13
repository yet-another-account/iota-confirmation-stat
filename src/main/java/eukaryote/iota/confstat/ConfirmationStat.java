package eukaryote.iota.confstat;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jota.IotaAPI;
import jota.dto.response.GetBundleResponse;
import jota.dto.response.GetInclusionStateResponse;
import jota.error.ArgumentException;
import jota.error.InvalidBundleException;
import jota.error.InvalidSignatureException;
import jota.error.NoNodeInfoException;
import jota.model.Bundle;
import jota.model.Transaction;

public class ConfirmationStat {
	IotaAPI api;

	public ConfirmationStat(IotaAPI api) {
		this.api = api;
	}

	public Status statusOf(String txnhash) throws NoNodeInfoException {
		return statusOf(api.getTransactionsObjects(new String[] { txnhash }).get(0));
	}

	public Status statusOf(Transaction txn) throws NoNodeInfoException {
		// transaction persistence field
		if (txn.getPersistence() != null && txn.getPersistence().booleanValue() == true)
			return Status.CONFIRMED;
		
		// check inclusion on latest milestone
		
		GetInclusionStateResponse incl = api.getLatestInclusion(new String[] { txn.getHash() });
		
		if (incl.getStates()[0])
			return Status.CONFIRMED;
				
		GetBundleResponse bdl;
		try {
			bdl = api.getBundle(txn.getBundle());
		} catch (ArgumentException | InvalidBundleException | InvalidSignatureException e) {
			
			// bad bundle?
			return Status.INVALID;
		}
		
		return Status.PENDING;
	}
}
