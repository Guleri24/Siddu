package com.guleri24.siddu.applicationlayer.proof;

import com.guleri24.siddu.applicationlayer.attestation.Attestation;
import com.guleri24.siddu.applicationlayer.attestation.NamespaceAttestation;
import com.guleri24.siddu.applicationlayer.attestation.issuer.AESEncryptionInformationSegmentAttestation;
import com.guleri24.siddu.applicationlayer.attestation.issuer.IssuerPartAttestation;
import com.guleri24.siddu.applicationlayer.attestation.issuer.IssuerPartNamespaceAttestation;
import com.guleri24.siddu.applicationlayer.attestation.policy.PolicyRight;
import com.guleri24.siddu.applicationlayer.attestation.policy.RTreePolicy;
import com.guleri24.siddu.applicationlayer.revocation.RevocationCommitment;
import com.guleri24.siddu.applicationlayer.revocation.RevocationSecret;
import com.guleri24.siddu.encryptionlayer.entities.EntityIdentifier;
import com.guleri24.siddu.encryptionlayer.entities.PrivateEntityIdentifier;
import com.guleri24.siddu.encryptionlayer.schemes.IBEDecryptableSegment;
import com.guleri24.siddu.storagelayer.StorageElementIdentifier;
import com.guleri24.siddu.storagelayer.StorageLayer;
import com.guleri24.siddu.storagelayer.dht.DHTStorageLayer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProofObjectTest {

    @Test
    /**
     * One big test case, which basically tests the entire framework except for the API and the storage layers:
     * 1)   Constructs a chain of attestations (cloud storage service provider --(namespace attestation)--> user A
     *          --(attestation)--> user B --(attestation)--> user C at publishes the constructed objects in the
     *          {@link StorageLayer}.
     * 2)   Asks user C to construct a proof object for the chain of attestations.
     * 3)   Verifies the proof object.
     * 4)   Checks if the proof object can still be verified if one of the attestations is revoked.
     */
    void test() throws IOException, CloneNotSupportedException {
        long counter;

        // 1) Generate three users and a cloud storage service provider.
        counter = System.currentTimeMillis();
        var cloudStorageProvider = EntityIdentifier.generateEntityIdentifierPair("cloudStorageServiceProvider");
        var userA = EntityIdentifier.generateEntityIdentifierPair("userA");
        var userB = EntityIdentifier.generateEntityIdentifierPair("userB");
        var userC = EntityIdentifier.generateEntityIdentifierPair("userC");
        System.out.printf("Entity identifiers generated (time needed: %s milliseconds).%n", System.currentTimeMillis() - counter);
        System.out.println("The generate PublicEntityIdentifiers:");
        System.out.printf("Cloud:\t%s%n", cloudStorageProvider.getRight());
        System.out.printf("userA:\t%s%n", userA.getRight());
        System.out.printf("userB:\t%s%n", userB.getRight());
        System.out.printf("userC:\t%s%n", userC.getRight());


        // 2) Generate the policies for each (namespace) attestation.
        counter = System.currentTimeMillis();
        var namespacePolicyUserA = new RTreePolicy(PolicyRight.WRITE, "A");
        var namespacePolicyUserB = new RTreePolicy(PolicyRight.WRITE, "B");
        var namespacePolicyUserC = new RTreePolicy(PolicyRight.WRITE, "C");

        var userAToUserBPolicy = new RTreePolicy(namespacePolicyUserA, PolicyRight.READ, "B");
        var userBToUserCPolicy = new RTreePolicy(userAToUserBPolicy, PolicyRight.WRITE, "C");

        System.out.printf("Policies generated (time needed: %s milliseconds).%n", System.currentTimeMillis() - counter);

        // 3) Generate the storage identifiers for each (namespace) attestation.
        counter = System.currentTimeMillis();
        var namespaceAttestationStorageIdentifierUserA = new StorageElementIdentifier(userA.getRight().getNamespaceServiceProviderEmailAddressUserConcatenation());
        var namespaceAttestationStorageIdentifierUserB = new StorageElementIdentifier(userB.getRight().getNamespaceServiceProviderEmailAddressUserConcatenation());
        var namespaceAttestationStorageIdentifierUserC = new StorageElementIdentifier(userC.getRight().getNamespaceServiceProviderEmailAddressUserConcatenation());

        var shareAttestationStorageIdentifier = new StorageElementIdentifier("userAtoUserB");
        var delegateAttestationStorageIdentifier = new StorageElementIdentifier("userBtoUserC");

        System.out.printf("Storage identifiers generated (time needed: %s milliseconds).%n", System.currentTimeMillis() - counter);

        // 4) Generate the revocation commitments.
        counter = System.currentTimeMillis();
        var serviceProviderUserARevocationSecret = new RevocationSecret("serviceProviderUserARevocationSecret");
        var serviceProviderUserBRevocationSecret = new RevocationSecret("serviceProviderUserBRevocationSecret");
        var serviceProviderUserCRevocationSecret = new RevocationSecret("serviceProviderUserCRevocationSecret");

        var userANamespaceAttestationRevocationSecret = new RevocationSecret("userANamespaceAttestationRevocationSecret");
        var userBNamespaceAttestationRevocationSecret = new RevocationSecret("userBNamespaceAttestationRevocationSecret");
        var userCNamespaceAttestationRevocationSecret = new RevocationSecret("userCNamespaceAttestationRevocationSecret");

        var userAShareAttestationRevocationSecret = new RevocationSecret("userAShareAttestationRevocationSecret");
        var userBShareAttestationRevocationSecret = new RevocationSecret("userBShareAttestationRevocationSecret");

        var userBDelegationAttestationRevocationSecret = new RevocationSecret("userBDelegationAttestationRevocationSecret");
        var userCDelegationAttestationRevocationSecret = new RevocationSecret("userCDelegationAttestationRevocationSecret");

        var serviceProviderUserARevocationCommitment = new RevocationCommitment(serviceProviderUserARevocationSecret);
        var serviceProviderUserBRevocationCommitment = new RevocationCommitment(serviceProviderUserBRevocationSecret);
        var serviceProviderUserCRevocationCommitment = new RevocationCommitment(serviceProviderUserCRevocationSecret);

        var userANamespaceAttestationRevocationCommitment = new RevocationCommitment(userANamespaceAttestationRevocationSecret);
        var userBNamespaceAttestationRevocationCommitment = new RevocationCommitment(userBNamespaceAttestationRevocationSecret);
        var userCNamespaceAttestationRevocationCommitment = new RevocationCommitment(userCNamespaceAttestationRevocationSecret);

        var userAShareAttestationRevocationCommitment = new RevocationCommitment(userAShareAttestationRevocationSecret);
        var userBShareAttestationRevocationCommitment = new RevocationCommitment(userBShareAttestationRevocationSecret);

        var userBDelegationAttestationRevocationCommitment = new RevocationCommitment(userBDelegationAttestationRevocationSecret);
        var userCDelegationAttestationRevocationCommitment = new RevocationCommitment(userCDelegationAttestationRevocationSecret);

        System.out.printf("Revocation secrets and commitments generated (time needed: %s milliseconds).%n", System.currentTimeMillis() - counter);

        // 5) For simplicity reasons: only use one InetSocketAddress.
        var referenceAPILayer = new InetSocketAddress("localhost", 5678);

        // 6) Create the issuer's part of the attestations.
        counter = System.currentTimeMillis();
        var serviceProviderPartNamespaceAttestationUserA = new IssuerPartNamespaceAttestation(cloudStorageProvider.getLeft(), cloudStorageProvider.getRight(), userA.getRight(), serviceProviderUserARevocationCommitment, namespacePolicyUserA, referenceAPILayer);
        var serviceProviderPartNamespaceAttestationUserB = new IssuerPartNamespaceAttestation(cloudStorageProvider.getLeft(), cloudStorageProvider.getRight(), userB.getRight(), serviceProviderUserBRevocationCommitment, namespacePolicyUserB, referenceAPILayer);
        var serviceProviderPartNamespaceAttestationUserC = new IssuerPartNamespaceAttestation(cloudStorageProvider.getLeft(), cloudStorageProvider.getRight(), userC.getRight(), serviceProviderUserCRevocationCommitment, namespacePolicyUserC, referenceAPILayer);
        var userAPartShareAttestation = new IssuerPartAttestation(userA.getLeft(), userA.getRight(), userB.getRight(), userAShareAttestationRevocationCommitment, userAToUserBPolicy);
        var userBPartDelegationAttestation = new IssuerPartAttestation(userB.getLeft(), userB.getRight(), userC.getRight(), userBDelegationAttestationRevocationCommitment, userBToUserCPolicy);
        System.out.printf("Issuer's parts generated (time needed: %s milliseconds).%n", System.currentTimeMillis() - counter);

        // 7) create the attestations.
        counter = System.currentTimeMillis();
        var namespaceAttestationA = new NamespaceAttestation(serviceProviderPartNamespaceAttestationUserA, userANamespaceAttestationRevocationCommitment, namespaceAttestationStorageIdentifierUserA, userA.getRight(), userA.getLeft());
        var namespaceAttestationB = new NamespaceAttestation(serviceProviderPartNamespaceAttestationUserB, userBNamespaceAttestationRevocationCommitment, shareAttestationStorageIdentifier, userB.getRight(), userB.getLeft());
        var namespaceAttestationC = new NamespaceAttestation(serviceProviderPartNamespaceAttestationUserC, userCNamespaceAttestationRevocationCommitment, delegateAttestationStorageIdentifier, userC.getRight(), userC.getLeft());
        var shareAttestation = new Attestation(shareAttestationStorageIdentifier, userAPartShareAttestation, userBShareAttestationRevocationCommitment, namespaceAttestationStorageIdentifierUserB, userB.getLeft());
        var delegateAttestation = new Attestation(delegateAttestationStorageIdentifier, userBPartDelegationAttestation, userCDelegationAttestationRevocationCommitment, namespaceAttestationStorageIdentifierUserC, userC.getLeft());
        System.out.printf("Attestations generated (time needed: %s milliseconds).%n", System.currentTimeMillis() - counter);

        // 8) Initialized the storage layer.
        counter = System.currentTimeMillis();
        var storageLayerUserA = new DHTStorageLayer(userA.getRight(), 5678);
        var storageLayerUserB = new DHTStorageLayer(userB.getRight(), 5679, storageLayerUserA);
        var storageLayerUserC = new DHTStorageLayer(userC.getRight(), 5680, storageLayerUserA, storageLayerUserB);
        System.out.printf("DHTStorageLayers initialized (time needed: %s milliseconds).%n", System.currentTimeMillis() - counter);

        counter = System.currentTimeMillis();
        storageLayerUserA.put(namespaceAttestationA);
        storageLayerUserB.put(namespaceAttestationB);
        storageLayerUserC.put(namespaceAttestationC);
        storageLayerUserB.put(shareAttestation);
        storageLayerUserC.put(delegateAttestation);
        System.out.printf("Generated Attestations stored in the DHT (time needed: %s milliseconds).%n", System.currentTimeMillis() - counter);


        // 9) Generate the proof object manually.
        counter = System.currentTimeMillis();
        Attestation[] attestationsForProofObject = new Attestation[]{namespaceAttestationA, shareAttestation, delegateAttestation, namespaceAttestationC};
        RTreePolicy[] attestationPolicies = new RTreePolicy[]{namespacePolicyUserA, userAToUserBPolicy, userBToUserCPolicy, namespacePolicyUserC};
        PrivateEntityIdentifier[] privateEntityIdentifiersReceivers = new PrivateEntityIdentifier[]{userA.getLeft(), userB.getLeft(), userC.getLeft(), userC.getLeft()};
        StorageElementIdentifier[] storageElementIdentifiersForProofObject = new StorageElementIdentifier[4];
        String[] firstAESKeysForProofObject = new String[4];
        String[] secondAESKeysForProofObject = new String[4];
        for (int i = 0; i < attestationsForProofObject.length; i++) {
            IBEDecryptableSegment<AESEncryptionInformationSegmentAttestation> encryptedAESEncryptionInformationSegment = attestationsForProofObject[i].getFirstLayer().getAesEncryptionInformationSegment();
            AESEncryptionInformationSegmentAttestation aesEncryptionInformationSegment = encryptedAESEncryptionInformationSegment.decrypt(privateEntityIdentifiersReceivers[i], attestationPolicies[i]);
            var aesKeys = aesEncryptionInformationSegment.getAesKeyInformation();

            storageElementIdentifiersForProofObject[i] = attestationsForProofObject[i].getStorageLayerIdentifier();
            firstAESKeysForProofObject[i] = aesKeys.getLeft();
            secondAESKeysForProofObject[i] = aesKeys.getRight();
        }
        var proof = new ProofObject(Arrays.copyOfRange(storageElementIdentifiersForProofObject, 0, storageElementIdentifiersForProofObject.length - 1), Arrays.copyOfRange(firstAESKeysForProofObject, 0, storageElementIdentifiersForProofObject.length - 1), firstAESKeysForProofObject[3]);
        System.out.printf("Proof object manually generated (time needed: %s milliseconds).%n", System.currentTimeMillis() - counter);


        // 10) Generate the proof automatically.
        counter = System.currentTimeMillis();
        RTreePolicy policyToProve = new RTreePolicy(PolicyRight.READ, "A", "B", "C");
        var automaticallyConstructedProof = ProofObject.generateProofObject(delegateAttestation, firstAESKeysForProofObject[2], secondAESKeysForProofObject[2], firstAESKeysForProofObject[3], storageLayerUserC);
        System.out.printf("Proof object automatically generated (time needed: %s milliseconds).%n", System.currentTimeMillis() - counter);
        assertEquals(proof, automaticallyConstructedProof);

        // 11) Verify the proof object.
        counter = System.currentTimeMillis();
        var provenPolicy = proof.verify(storageLayerUserA);
        System.out.printf("Proof object verified (time needed: %s milliseconds).%n", System.currentTimeMillis() - counter);
        assertEquals(policyToProve, provenPolicy);

        // 12) Revoke the share Attestation.
        counter = System.currentTimeMillis();
        userAShareAttestationRevocationSecret.revealInStorageLayer(storageLayerUserA);
        System.out.printf("One revocation secret revealed (time needed: %s milliseconds).%n", System.currentTimeMillis() - counter);

        // 13) Check if the proof object can still be verified.
        counter = System.currentTimeMillis();
        assertThrows(IllegalArgumentException.class, () -> proof.verify(storageLayerUserA));
        System.out.printf("Invalid proof object successfully detected (time needed: %s milliseconds).%n", System.currentTimeMillis() - counter);
    }
}