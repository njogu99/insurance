package com.vehicle.webserver;

import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.Vault.Page;
import net.corda.core.node.services.vault.FieldInfo;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.node.services.vault.QueryCriteriaUtils;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.r3.corda.lib.tokens.contracts.states.FungibleToken;
import com.r3.corda.lib.tokens.contracts.types.TokenType;
import com.r3.corda.lib.tokens.workflows.utilities.QueryUtilitiesKt;

/**
 * Define your API endpoints here.
 */
@RestController
@RequestMapping("/app") // The paths for HTTP requests are relative to this base path.
public class Controller {
    private final CordaRPCOps proxy;
    private final static Logger logger = LoggerFactory.getLogger(Controller.class);

    public Controller(NodeRPCConnection rpc) {
        this.proxy = rpc.proxy;
    }

    @GetMapping(value = "/templateendpoint", produces = "text/plain")
    private String templateendpoint() {
        return "Define an endpoint here.";
    }
    
    @GetMapping(value = "/getAllTokens")
	public String getContractsData()
			throws NoSuchFieldException, SecurityException {
		QueryCriteria generalCriteria = new QueryCriteria.VaultQueryCriteria(Vault.StateStatus.UNCONSUMED);
		CordaX500Name name = new CordaX500Name("PartyA", "US", "US");
		Set<Party> liParty = proxy.partiesFromName("PartyA", false);
		final Party gpoParty = liParty.iterator().next();
		
		
		
		  TokenType token = new TokenType("V-Claims",0);
		QueryCriteria isserCriteria=QueryUtilitiesKt.tokenAmountWithIssuerCriteria(token, gpoParty);
		
		
		QueryCriteria criteria = generalCriteria.and(isserCriteria);

		
	      

		Page<FungibleToken> fing= proxy.vaultQueryByCriteria(criteria,FungibleToken.class);

		

		return String.valueOf(fing.getStates().get(0).getState().getData().getAmount().getQuantity());
	}
    
    
}

