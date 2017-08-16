package eukaryote.iota.confstat;

import java.util.ArrayList;
import java.util.List;

import jota.IotaAPI;
import jota.dto.response.GetBalancesResponse;
import jota.dto.response.GetBundleResponse;
import jota.dto.response.GetInclusionStateResponse;
import jota.error.ArgumentException;
import jota.error.InvalidBundleException;
import jota.error.InvalidSignatureException;
import jota.error.NoNodeInfoException;
import jota.model.Transaction;

public class ConfirmationStat {
	IotaAPI api;
	
	// empty transaction array
	private static final Transaction[] txnarr = {};

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
		} catch (NumberFormatException e) {
			
			// doublespent bundles somehow throw numberformatexception
			return Status.DOUBLESPEND;
		}
		
		// check for doublespending
		
		List<Transaction> inputs = new ArrayList<>(bdl.getTransactions().size());
		
		for (Transaction t : bdl.getTransactions()) {
			// we only want inputs
			if (t.getValue() >= 0)
				continue;
			
			inputs.add(t);
		}
		
		// look up address balances and compare
		
		Transaction[] inparray = inputs.toArray(txnarr);
		String[] addresses = new String[inparray.length];
		String[] txnhashes = new String[inparray.length];
		boolean[] inclusion = api.getLatestInclusion(txnhashes).getStates();
		
		for (int i = 0; i < inparray.length; i++) {
			addresses[i] = inparray[i].getAddress();
			txnhashes[i] = inparray[i].getHash();
		}
		
		GetBalancesResponse balances = api.getBalances(1, addresses);
		String[] balstrs = balances.getBalances();
		
		
		for (int i = 0; i < balstrs.length; i++) {
			String balance = balstrs[i];
			long bal = Long.parseLong(balance);
			
			// if not confirmed, we ignore
			if (inclusion[i] == false)
				continue;
			
			// invert value because input balance is negative
			if (bal < -inparray[i].getValue()) {
				return Status.DOUBLESPEND;
			}
		}
		
		return Status.PENDING;
	}
}
