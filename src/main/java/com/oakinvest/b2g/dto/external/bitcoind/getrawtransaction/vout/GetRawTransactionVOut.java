package com.oakinvest.b2g.dto.external.bitcoind.getrawtransaction.vout;

import com.oakinvest.b2g.dto.external.bitcoind.getrawtransaction.vout.scriptpubkey.GetRawTransactionScriptPubKey;

/**
 * vout.
 * Created by straumat on 01/09/16.
 */
public class GetRawTransactionVOut {

	/**
	 * The value in BTC.
	 */
	private float value;

	/**
	 * Index.
	 */
	private long n;

	/**
	 * ScriptPubKey.
	 */
	private GetRawTransactionScriptPubKey scriptPubKey;

	/**
	 * Getter de la propriété value.
	 *
	 * @return value
	 */
	public final float getValue() {
		return value;
	}

	/**
	 * Setter de la propriété value.
	 *
	 * @param newValue the value to set
	 */
	public final void setValue(final float newValue) {
		value = newValue;
	}

	/**
	 * Getter de la propriété n.
	 *
	 * @return n
	 */
	public final long getN() {
		return n;
	}

	/**
	 * Setter de la propriété n.
	 *
	 * @param newIndex the n to set
	 */
	public final void setN(final long newIndex) {
		n = newIndex;
	}

	/**
	 * Getter de la propriété scriptPubKey.
	 *
	 * @return scriptPubKey
	 */
	public final GetRawTransactionScriptPubKey getScriptPubKey() {
		return scriptPubKey;
	}

	/**
	 * Setter de la propriété scriptPubKey.
	 *
	 * @param newScriptPubKey the scriptPubKey to set
	 */
	public final void setScriptPubKey(final GetRawTransactionScriptPubKey newScriptPubKey) {
		scriptPubKey = newScriptPubKey;
	}
}
