# HealthCoinsApplication
Sample Corda Application on Token SDK  based on Health coin use case .

*CorDapp Nodes:*

* `Admin`:  This party issues fungible tokens to the employee when employee logs thier activity.
* `Employee`: This party initially log thier  health activity like running ,cycling along with calories burned on that activity .Once employee log their activity they recieve fungible tokens that they redeem with partner merchants(HealthCoin) from the Admin.
* `Merchant`:This party recieve's token in exchange of an equivalant off ledger thing ,the merchant can then redeem these tokens on their side.
* `Notary`: notary node to check double-spend of input states then verify and sign final transaction.


**This CorDapp example business-logic flows as below:**

* 1. The `Employee` party logs the `Activity` state of type `HealthCoinActivityState` on ledger.
* 2. There would be Corda Service running  in the background on the `Admin` Node which would listen to HealthCoinActivityState created by `Employee`  and for every `HealthCoinActivityState` it would issue `FungibleToken` with Token Name as `HealthCoin`. 
* 3. The `Employee` party can then review and view the HealthCoins issued by `Admin` and redeem those by transfering those to the `Merchant` party.
* 4. The `Merchant` party - offline review and validate the `HealthCoins` data received along with `FungibleToken` state. If everything is good, he can initiate the `RedeemHealthCoinFlow` flow to redeem the tokens . 

### Running the Health Coin Application.

First  clone the source code from the git repository.

    git clone https://github.com/amitpitambare/HealthCoinApplication.git
    cd HealthCoinApplication
   

Once you have cloned   the repository, you should open it with Eclipse/STS/IntelliJ. 
You can `deployNodes` to create four nodes (Admin.Employee,Merchant and Notary):

    ./gradlew clean deployNodes
    ./build/nodes/runnodes

## Interacting with the CorDapp via Corda Shell

Use relevant node's Shell console to initiate the flows.

**Let's start flows from each Corda node's shell console step-by-step:**

**1.** To create `HealthCoinActivityState` - run below command on `Employee` node's console:
```console
flow start HealthActivityLoggerFlow completedBy: Employee ,activityName: "Running" , caloriesBurned: 2000
```
To see state created run below command:
```console
run vaultQuery contractStateType: com.healthcoin.state.HealthCoinActivityState
```
Now we have on ledger Health Activity of type HealthCoinActivityState.
    
**2.** Service running on background will  initiate HealthCoinIssuanceFlow which will create Tokens with token name as "HealthCoin" and quantity based on the Calories burned in the health activity and it will create `FungibleToken` of type .

To see `FungibleToken` state created run below command again:
```console    
run vaultQuery contractStateType: com.r3.corda.lib.tokens.contracts.states.FungibleToken
```    
You can see the FungibleToken  data and **copy** the `tokenName` *field value* of it and save it with you as we required it in next step.
If the Employee wants to redeem this token it can do it by transfering this to the merchant.

**3.** The `Employee` party can transfer any quantity of tokens that the 'Employee' is holder of.  
Running below command on party `Employee` node's console:
```console
flow start HealthCoinTransferFlow tokenName: "HealthCoin" ,holderName: "Employee" , quantity: 20 ,recipient: Merchant"
```
This flow will transfer the `FungibleToken`  to `Merchant`.

**4.** The `Merchant` party can redeem the Tokens he holds:
```console
flow start RedeemHealthCoinFlow tokenName: "HealthCoin" ,issuerName: Admin , quantity: 20 "
```

If we check and query the states we can find that the tokens are consumed and not available now.
```console    
run vaultQuery contractStateType: com.r3.corda.lib.tokens.contracts.states.FungibleToken
``` 

# Future Scope

* Make use of Corda Accounts in `Employee` Node so that every employee's data is logically segregated in ledger.
* Create a Custom Token type so that we can custom token properties.





