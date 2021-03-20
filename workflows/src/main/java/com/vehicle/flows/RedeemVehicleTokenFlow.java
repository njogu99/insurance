package com.vehicle.flows;

import java.util.ArrayList;

import com.r3.corda.lib.tokens.contracts.states.FungibleToken;
import com.r3.corda.lib.tokens.contracts.types.TokenType;
import com.r3.corda.lib.tokens.workflows.flows.rpc.RedeemFungibleTokens;
import com.r3.corda.lib.tokens.workflows.utilities.QueryUtilitiesKt;

import co.paralleluniverse.fibers.Suspendable;
import jdk.nashorn.internal.parser.Token;
import net.corda.core.contracts.Amount;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.Party;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.Vault.Page;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.ProgressTracker;


@StartableByRPC
public class RedeemVehicleTokenFlow extends FlowLogic<SignedTransaction> {

	private final ProgressTracker progressTracker = new ProgressTracker();
	private final String tokenName;
	//private final TokenType VehicleToken;
	private final Party issuerName;
	private final Long quantity;

	@Override
	public ProgressTracker getProgressTracker() {
		return progressTracker;
	}

	public RedeemVehicleTokenFlow(String tokenName, Party issuerName, Long quantity) {
		//VehicleToken = vehicleToken;

		this.tokenName = tokenName;
		this.issuerName = issuerName;
		this.quantity = quantity;
	}

	public Long getQuantity() {
		return quantity;
	}

	public String getTokenName() {
		return tokenName;
	}
	

	public Party getIssuerName() {
		return issuerName;
	}

	@Override
	@Suspendable
	public SignedTransaction call() throws FlowException {
		QueryCriteria generalCriteria = new QueryCriteria.VaultQueryCriteria(Vault.StateStatus.UNCONSUMED);

		//VehicleToken = new TokenType(VehicleToken, 0);
		TokenType token = new TokenType(tokenName, 0);
		QueryCriteria holderCriteria = QueryUtilitiesKt.tokenAmountWithIssuerCriteria(token, issuerName);
		
		QueryCriteria criteria = generalCriteria.and(holderCriteria);

		Page<FungibleToken> fing = getServiceHub().getVaultService().queryBy(FungibleToken.class, criteria);
		//

		
		System.out.println(fing.getStates().get(0).getState().getData().getAmount().getQuantity());

		return subFlow(new RedeemFungibleTokens(new Amount(quantity, token), issuerName, new ArrayList<Party>(), criteria));
	}

}
