package com.guleri24.siddu.applicationlayer.attestation;

import com.guleri24.siddu.applicationlayer.attestation.issuer.IssuerPartAttestation;
import com.guleri24.siddu.applicationlayer.attestation.issuer.IssuerPartNamespaceAttestation;
import com.guleri24.siddu.applicationlayer.attestation.policy.PolicyRight;
import com.guleri24.siddu.applicationlayer.attestation.policy.RTreePolicy;
import com.guleri24.siddu.applicationlayer.revocation.RevocationCommitment;
import com.guleri24.siddu.applicationlayer.revocation.RevocationSecret;
import com.guleri24.siddu.encryptionlayer.entities.EntityIdentifier;
import com.guleri24.siddu.encryptionlayer.entities.PrivateEntityIdentifier;
import com.guleri24.siddu.encryptionlayer.entities.PublicEntityIdentifier;
import com.guleri24.siddu.storagelayer.StorageElementIdentifier;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AttestationTest {

    Pair<PrivateEntityIdentifier, PublicEntityIdentifier> issuerIdentifiers = EntityIdentifier.generateEntityIdentifierPair("");
    Pair<PrivateEntityIdentifier, PublicEntityIdentifier> receiverIdentifiers = EntityIdentifier.generateEntityIdentifierPair("");
    RevocationCommitment revocationCommitment = new RevocationCommitment(new RevocationSecret());
    RTreePolicy rTreePolicy = new RTreePolicy(PolicyRight.WRITE, "A", "B", "C");
    InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 5678);
    IssuerPartAttestation issuerPartAttestation = new IssuerPartNamespaceAttestation(
            issuerIdentifiers.getLeft(), issuerIdentifiers.getRight(), receiverIdentifiers.getRight(),
            revocationCommitment, rTreePolicy, inetSocketAddress);
    Attestation attestation = new Attestation(storageElementIdentifier, issuerPartAttestation, revocationCommitment,
            storageElementIdentifier, receiverIdentifiers.getLeft());
    StorageElementIdentifier storageElementIdentifier = new StorageElementIdentifier("test");

    @Test
    void isValid() {
        assertTrue(attestation.isValid(receiverIdentifiers.getLeft(), receiverIdentifiers.getRight(), issuerIdentifiers.getRight(),
                rTreePolicy));

        /*
        Why the following part of this test:
        In Java: serializing & deserializing an object ==> different memory allocation, which means the hashes
        of the objects may be different if the Attestation class is incorrectly implemented.

        P.S.: Yes it took me a lot of time to fix this...
         */
        byte[] serializedAttestation = SerializationUtils.serialize(attestation);
        Attestation deserializedAttestation = SerializationUtils.deserialize(serializedAttestation);
        assertTrue(deserializedAttestation.isValid(receiverIdentifiers.getLeft(), receiverIdentifiers.getRight(),
                issuerIdentifiers.getRight(), rTreePolicy));
    }
}