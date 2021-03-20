package com.vehicle.flows;

import java.util.ArrayList;

import com.r3.corda.lib.tokens.contracts.states.FungibleToken;
import com.r3.corda.lib.tokens.contracts.types.TokenType;
import com.r3.corda.lib.tokens.workflows.flows.rpc.MoveFungibleTokens;
import com.r3.corda.lib.tokens.workflows.types.PartyAndAmount;
import com.r3.corda.lib.tokens.workflows.utilities.QueryUtilitiesKt;

import co.paralleluniverse.fibers.Suspendable;
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
public class VehicleTokenTransferFlow extends FlowLogic<SignedTransaction> {
	 private final ProgressTracker progressTracker = new ProgressTracker();
	private final String tokenName;
	private final Party holderName;
	private final Long quantity;
	private final Party recipient;
	
	
	public Party getRecipient() {
		return recipient;
	}


	public String getTokenName() {
		return tokenName;
	}


	public Long getQuantity() {
		return quantity;
	}



	public Party getHolderName() {
		return holderName;
	}


	@Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

	@Override
	@Suspendable
	public SignedTransaction call() throws FlowException {
	
		QueryCriteria generalCriteria = new QueryCriteria.VaultQueryCriteria(Vault.StateStatus.UNCONSUMED);
		
		  TokenType token = new TokenType(tokenName,0);
		QueryCriteria isserCriteria=QueryUtilitiesKt.heldTokenAmountCriteria(token, holderName);
		
		QueryCriteria criteria = generalCriteria.and(isserCriteria);

		
      

		Page<FungibleToken> fing= getServiceHub().getVaultService().queryBy(FungibleToken.class,criteria);
		System.out.println(fing.getStates().get(0).getState().getData().getAmount().getQuantity());
		
		return subFlow(new MoveFungibleTokens(new PartyAndAmount(recipient,new Amount(quantity, token)),new ArrayList<Party>(),criteria,holderName));
	}


	public VehicleTokenTransferFlow(String tokenName, Party holderName, Long quantity, Party recipient) {
		
		this.tokenName = tokenName;
		this.holderName = holderName;
		this.quantity = quantity;
		this.recipient = recipient;
	}


	
	
	

}
