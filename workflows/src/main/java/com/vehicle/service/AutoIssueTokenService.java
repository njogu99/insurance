package com.vehicle.service;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.vehicle.flows.VehicleTokenIssuanceFlow;
import com.vehicle.state.VehicleTokenActivityState;

import net.corda.core.identity.Party;
import net.corda.core.messaging.DataFeed;
import net.corda.core.node.AppServiceHub;

import net.corda.core.node.services.CordaService;
import net.corda.core.node.services.Vault.Page;
import net.corda.core.node.services.Vault.Update;
import net.corda.core.serialization.SingletonSerializeAsToken;
import rx.Observer;

@CordaService
public class AutoIssueTokenService extends SingletonSerializeAsToken {
	private AppServiceHub serviceHub;

	public AutoIssueTokenService(AppServiceHub serviceHub) {
		this.serviceHub = serviceHub;
		// code ran at service creation / node startup
		init();
	}

	private void init() {

		issueTokens();

	}

	private void issueTokens() {
		Party ourIdentity = serviceHub.getMyInfo().getLegalIdentities().get(0);
		Optional<Party> adminParty = serviceHub.getIdentityService().partiesFromName("Sanlam", true).stream()
				.findFirst();

		if (ourIdentity.equals(adminParty.get())) {
			DataFeed<Page<VehicleTokenActivityState>, Update<VehicleTokenActivityState>> dataFeed = serviceHub
					.getVaultService().trackBy(VehicleTokenActivityState.class);

			Observer<Update<VehicleTokenActivityState>> observer = new Observer<Update<VehicleTokenActivityState>>() {

				@Override
				public void onCompleted() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onError(Throwable e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onNext(Update<VehicleTokenActivityState> t) {

					processState(t);
				}

			};
			dataFeed.getUpdates().subscribe(observer);
		}

		// dataFeed.getUpdates().subscribe(s -> processState(s));

	}

	private void processState(Update<VehicleTokenActivityState> updates) {
		// LoyalityActivityState state;
		updates.getProduced().forEach(message -> {
			VehicleTokenActivityState state = message.getState().getData();
			ExecutorService executor = Executors.newSingleThreadExecutor();

			if (state.getVehicleValue() > 50000) {
				executor.submit(() -> {

					serviceHub
							.startFlow(new VehicleTokenIssuanceFlow("V-Claims", new Long(state.getVehicleValue()), state.getOwnedBy()));
				});
			}

		});
		// return state;
	}
}
