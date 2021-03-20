package com.vehicle.contract;

import java.util.List;

import com.vehicle.state.VehicleTokenActivityState;
import static net.corda.core.contracts.ContractsDSL.requireThat;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

public class VehicleTokenActivityContract implements Contract {
	public static final String ID = "com.vehicle.contract.VehicleTokenActivityContract";
	@Override
	public void verify(LedgerTransaction tx) throws IllegalArgumentException {
		List<VehicleTokenActivityState> inpStates=tx.inputsOfType(VehicleTokenActivityState.class);
		requireThat(require ->{
			 require.using("There is must be zero input State" , inpStates.size()<1);
			 require.using("There is must be one output State of type VehicleTokenApplication State" , tx.getOutputs().get(0).getData() instanceof VehicleTokenActivityState);
			 return require;
		 });
		

	}
	public interface Commands extends CommandData {
		class Create implements Commands {
		}
	}
	

}
