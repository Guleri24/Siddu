package com.guleri24.siddu.applicationlayer.attestation.issuer;

import com.guleri24.siddu.applicationlayer.attestation.policy.PolicyRight;
import com.guleri24.siddu.applicationlayer.attestation.policy.RTreePolicy;
import com.guleri24.siddu.applicationlayer.revocation.RevocationCommitment;
import com.guleri24.siddu.applicationlayer.revocation.RevocationSecret;
import com.guleri24.siddu.encryptionlayer.entities.EntityIdentifier;
import com.guleri24.siddu.encryptionlayer.entities.PrivateEntityIdentifier;
import com.guleri24.siddu.encryptionlayer.entities.PublicEntityIdentifier;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IssuerPartAttestationTest {

    Pair<PrivateEntityIdentifier, PublicEntityIdentifier> issuerIdentifiers = EntityIdentifier.generateEntityIdentifierPair("");
    Pair<PrivateEntityIdentifier, PublicEntityIdentifier> receiverIdentifiers = EntityIdentifier.generateEntityIdentifierPair("");
    String ibeIdentifierAESEncryptionInformation = "test";
    RevocationCommitment revocationCommitment = new RevocationCommitment(new RevocationSecret());
    RTreePolicy rTreePolicy = new RTreePolicy(PolicyRight.WRITE, "A", "B", "C");
    IssuerPartAttestation issuerPartAttestation = new IssuerPartAttestation(issuerIdentifiers.getLeft(),
            issuerIdentifiers.getRight(), receiverIdentifiers.getRight(),
            revocationCommitment, rTreePolicy);

    @Test
    void hasValidSignature() {
        assertTrue(issuerPartAttestation.hasValidSignature(receiverIdentifiers.getLeft(), issuerIdentifiers.getRight(),
                rTreePolicy));

        /*
        Why the following part of this test:
        In Java: serializing & deserializing an object ==> different memory allocation, which means the hashes
        of the objects may be different if the IssuerPartAttestation class is incorrectly implemented.

        P.S.: Yes it took me a lot of time to fix this...
         */
        byte[] serializedIssuerPartAttestation = SerializationUtils.serialize(issuerPartAttestation);
        IssuerPartAttestation deserializedIssuerPartAttestation = SerializationUtils.deserialize(serializedIssuerPartAttestation);
        assertTrue(deserializedIssuerPartAttestation.hasValidSignature(receiverIdentifiers.getLeft(), issuerIdentifiers.getRight(),
                rTreePolicy));
    }
}