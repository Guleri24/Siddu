# Siddu authorization framework
A framework which focus on authorization over a distributed environment with Zero Trust policy

## Requirements
* Java17+
* gradle (optional as gradle wrapper is included)
 
## Build
* `./gradlew build` - it will create a *.jar file
* `java -jar *.jar` - it will run the project and give the relavent output

## Output
* Generating the storage layer took %d ms.%n --time ms
* Storage Layer Setup Time: --time ms
* Generating the macaroon manager took %d ms.%n --time ms
* Macaroon Manager Setup Time: --time ms
* Generating the encryption keys for the cloud storage service providers took %d ms.%n --time ms
* Cloud Encryption Key Generation Time: --time ms
* Generating the list of cloud storage service providers took %d ms.%n --time ms
* Clouds List Setup Time: --time ms
* Generating the encryption keys for the users took %d ms.%n --time ms
* User Encryption Key Generation Time: --time ms
* Generating the list of users took %d ms.%n --time ms
* Users List Setup Time: --time ms
* --entityLeft, \ 
    Public EC key: --entityRightLeft_GetRSAIdentifier, \
    Private EC key: --entityRightRight_GetRSAIdentifier, \
    IBE parameters: --entityRightLeft_getIBEIdentifier, \
    Storage layer identifier first attestation in personal queue: --entityRightRight_GetNamespaceServiceProviderEmailAddressUserConcatenation
* Printing the list of all entities took %d ms.%n --time ms
* All Entities List Setup Time: --time ms
* Generating the storage element identifiers for the personal queues took %d ms.%n --time ms
* Queue Storage Element Identifiers Setup Time: --time ms
* For Loop:: First attestation in personal queue of %s%s%s:\t%s%n --time ms
* Registering the users to the respective cloud storage service providers took %d ms.%n --time ms
* Registering the users to the respective cloud storage service providers: --time ms
* Attestation (--entityNames --> --entityNames): --attestation
* Generating the necessary attestations for the demo took %d ms.%n --time ms
* Generating the necessary attestations for the demo: --time ms
* Personal queue of --entityNames
* While Loop:: Next attestation found: --personalQueue_next
* Retrieving the personal queues took %d ms.%n --time ms
* Personal queues: --time ms
* 

