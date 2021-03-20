package com.vehicle.state;

import java.util.List;

import com.vehicle.contract.VehicleTokenContract;
import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType;
import com.r3.corda.lib.tokens.contracts.types.IssuedTokenType;

import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.Party;

@BelongsToContract(VehicleTokenContract.class)
public class VehicleTokenState extends EvolvableTokenType{

	private final Party maintainer;
    private final UniqueIdentifier uniqueIdentifier;
    private final int fractionDigits;
	


	@Override
	public UniqueIdentifier getLinearId() {
		
		return null;
	}

	public VehicleTokenState(Party maintainer, UniqueIdentifier uniqueIdentifier, int fractionDigits) {
		
		this.maintainer = maintainer;
		this.uniqueIdentifier = uniqueIdentifier;
		this.fractionDigits = fractionDigits;
	}

	public Party getMaintainer() {
		return maintainer;
	}

	public UniqueIdentifier getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	@Override
	public int getFractionDigits() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Party> getMaintainers() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
