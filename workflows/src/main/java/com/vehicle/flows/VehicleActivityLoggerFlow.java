package com.vehicle.flows;

import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.vehicle.contract.VehicleTokenActivityContract;
import com.vehicle.state.VehicleTokenActivityState;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.Command;
import net.corda.core.flows.FinalityFlow;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

@StartableByRPC
public class VehicleActivityLoggerFlow extends FlowLogic<SignedTransaction> {
	private final ProgressTracker progressTracker = new ProgressTracker();

	private final Party ownedBy;
	private final Party insurance;
	private final String numberPlate;
	private final Long vehicleValue;

	public VehicleActivityLoggerFlow(Party ownedBy, Party insurance, String numberPlate, Long vehicleValue) {

		this.ownedBy = ownedBy;
		this.insurance = insurance;
		this.numberPlate = numberPlate;
		this.vehicleValue = vehicleValue;
	}

	public Party getOwnedBy() {
		return ownedBy;
	}
	public Party getInsurance() {
		return insurance;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public Long getVehicleValue() {
		return vehicleValue;
	}
	
	@Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }
	@Override
	@Suspendable
	public SignedTransaction call() throws FlowException {
		
		  final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

          // Stage 1.
        //  progressTracker.setCurrentStep(GENERATING_TRANSACTION);
          // Generate an unsigned transaction.
		  
		  Optional<Party> admin = getServiceHub().getIdentityService().partiesFromName("Sanlam", true).stream().findFirst();
          Party me = getOurIdentity();

		VehicleTokenActivityState activityState = new VehicleTokenActivityState(me,insurance ,numberPlate,vehicleValue );
         
          final Command<VehicleTokenActivityContract.Commands.Create> txCommand = new Command<>(
                  new VehicleTokenActivityContract.Commands.Create(),
                  ImmutableList.of(activityState.getOwnedBy().getOwningKey()));
         
          final TransactionBuilder txBuilder = new TransactionBuilder(notary)
                  .addOutputState(activityState, VehicleTokenActivityContract.ID)
                  .addCommand(txCommand);

          // Stage 2.
         
          // Verify that the transaction is valid.
          txBuilder.verify(getServiceHub());

          // Stage 3.
         
          // Sign the transaction.
          final SignedTransaction fullySignedTx = getServiceHub().signInitialTransaction(txBuilder);

      
         final SignedTransaction finalTx= subFlow(new FinalityFlow(fullySignedTx));
         subFlow(new ReportToInsuranceFlow.SendToAdminFlow(admin.get(), finalTx));

        return finalTx;
	}

}
