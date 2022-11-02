package com.guleri24.siddu.applicationlayer.proof;

import com.guleri24.siddu.applicationlayer.attestation.Attestation;
import com.guleri24.siddu.applicationlayer.attestation.NamespaceAttestation;
import com.guleri24.siddu.applicationlayer.attestation.issuer.IssuerPartAttestation;
import com.guleri24.siddu.applicationlayer.attestation.issuer.IssuerPartNamespaceAttestation;
import com.guleri24.siddu.applicationlayer.attestation.policy.PolicyRight;
import com.guleri24.siddu.applicationlayer.attestation.policy.RTreePolicy;
import com.guleri24.siddu.applicationlayer.revocation.RevocationCommitment;
import com.guleri24.siddu.encryptionlayer.entities.EntityIdentifier;
import com.guleri24.siddu.encryptionlayer.entities.PrivateEntityIdentifier;
import com.guleri24.siddu.encryptionlayer.entities.PublicEntityIdentifier;
import com.guleri24.siddu.storagelayer.StorageElementIdentifier;
import com.guleri24.siddu.storagelayer.StorageLayer;
import com.guleri24.siddu.storagelayer.map.MultiMappedStorageLayer;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DisproofObjectTest {

    @Test
    void verify() throws IOException {
        Pair<PrivateEntityIdentifier, PublicEntityIdentifier> userA = EntityIdentifier.generateEntityIdentifierPair("");
        Pair<PrivateEntityIdentifier, PublicEntityIdentifier> userB = EntityIdentifier.generateEntityIdentifierPair("");

        RevocationCommitment revocationCommitment = new RevocationCommitment(); // Not important here.
        RTreePolicy rTreePolicyNamespaceAttestationUserA = new RTreePolicy(PolicyRight.WRITE, "A");
        RTreePolicy rTreePolicyNamespaceAttestationUserB = new RTreePolicy(PolicyRight.WRITE, "B");
        StorageElementIdentifier storageElementIdentifierNextElementPersonalQueueUserA = new StorageElementIdentifier();
        StorageElementIdentifier storageElementIdentifierNextElementPersonalQueueUserB = new StorageElementIdentifier();
        IssuerPartNamespaceAttestation issuerPartNamespaceAttestationUserA = new IssuerPartNamespaceAttestation(userA.getLeft(),
                userA.getRight(), userA.getRight(), revocationCommitment, rTreePolicyNamespaceAttestationUserA, new InetSocketAddress("localhost", 0)); // Must arguments are not important here.
        IssuerPartNamespaceAttestation issuerPartNamespaceAttestationUserB = new IssuerPartNamespaceAttestation(userB.getLeft(),
                userB.getRight(), userB.getRight(), revocationCommitment, rTreePolicyNamespaceAttestationUserB, new InetSocketAddress("localhost", 0));
        NamespaceAttestation namespaceAttestationUserA = new NamespaceAttestation(issuerPartNamespaceAttestationUserA,
                revocationCommitment, storageElementIdentifierNextElementPersonalQueueUserA, userA.getRight(), userA.getLeft());
        NamespaceAttestation namespaceAttestationUserB = new NamespaceAttestation(issuerPartNamespaceAttestationUserB,
                revocationCommitment, storageElementIdentifierNextElementPersonalQueueUserB, userB.getRight(), userB.getLeft());

        StorageLayer storageLayer = new MultiMappedStorageLayer();
        storageLayer.put(namespaceAttestationUserA);
        storageLayer.put(namespaceAttestationUserB);

        RTreePolicy policyToDisprove = new RTreePolicy(PolicyRight.READ, "B", "A");
        DisproofObject disproofObject = new DisproofObject(userA.getRight(), userA.getLeft(), policyToDisprove);
        disproofObject.verify(storageLayer);

        IssuerPartAttestation issuerPartAttestationThatRuinsEverything = new IssuerPartAttestation(userB.getLeft(),
                userB.getRight(), userA.getRight(), revocationCommitment, new RTreePolicy(PolicyRight.WRITE, "B", "A"));
        Attestation attestationThatRuinsEverything = new Attestation(storageElementIdentifierNextElementPersonalQueueUserA,
                issuerPartAttestationThatRuinsEverything, revocationCommitment, storageElementIdentifierNextElementPersonalQueueUserA,
                userA.getLeft());
        storageLayer.put(attestationThatRuinsEverything);
        assertThrows(IllegalStateException.class, () -> disproofObject.verify(storageLayer));
    }
}