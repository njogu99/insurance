package com.vehicle.state;

import java.util.Arrays;
import java.util.List;

import com.vehicle.contract.VehicleTokenActivityContract;

import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;


@BelongsToContract(VehicleTokenActivityContract.class)
public class VehicleTokenActivityState implements ContractState {
	private final Party ownedBy;
	private final Party insurance;
	private final String numberPlate;
	private final Long vehicleValue;



	public VehicleTokenActivityState(Party ownedBy, Party insurance, String numberPlate, Long vehicleValue) {
		
		this.ownedBy = ownedBy;
		this.insurance = insurance;
		this.numberPlate = numberPlate;
		this.vehicleValue = vehicleValue;
	}

	public Party getOwnedBy() {
		return ownedBy;
	}

	public Party getInsurance() {return insurance; }

	public String getNumberPlate() {
		return numberPlate;
	}

	public Long getVehicleValue() {
		return vehicleValue;
	}

	

	@Override
	public List<AbstractParty> getParticipants() {
	
		return Arrays.asList(ownedBy);
	}

}
