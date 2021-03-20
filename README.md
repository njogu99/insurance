# Insurance Token Application
Sample Corda Application on Token SDK  based on insurance use case .

*CorDapp Nodes:*

* `Sanlam`:  This party issues fungible tokens to the customer.
* `Customer`: This party logs vehicle info and receives tokens based on car value.
* `Garage`:This party recieve's token in exchange of an equivalant off ledger thing ,the garage can then redeem these tokens on their side.
* `CIPA`: notary node to check double-spend of input states then verify and sign final transaction.
