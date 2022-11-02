package com.guleri24.siddu.applicationlayer.attestation.issuer;

import com.guleri24.siddu.applicationlayer.attestation.NamespaceAttestation;
import com.guleri24.siddu.applicationlayer.attestation.policy.RTreePolicy;
import com.guleri24.siddu.applicationlayer.revocation.RevocationCommitment;
import com.guleri24.siddu.encryptionlayer.entities.PrivateEntityIdentifier;
import com.guleri24.siddu.encryptionlayer.entities.PublicEntityIdentifier;
import com.guleri24.siddu.encryptionlayer.schemes.ECCipherEncryptedSegment;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.util.Objects;

/**
 * Class representing an {@link IssuerPartAttestation} for the {@link NamespaceAttestation}
 * instances.
 */
public class IssuerPartNamespaceAttestation extends IssuerPartAttestation {

    private final InetSocketAddress referenceAPILayer;

    /**
     * The constructor of the {@link IssuerPartNamespaceAttestation}.
     *
     * @param privateEntityIdentifierIssuer  The {@link PrivateEntityIdentifier} of the issuer of this {@link IssuerPartAttestation}.
     * @param publicEntityIdentifierIssuer   The {@link PublicEntityIdentifier} of the issuer of this {@link IssuerPartAttestation}.
     * @param publicEntityIdentifierReceiver The {@link PublicEntityIdentifier} of the receiver of this {@link IssuerPartAttestation}.
     * @param revocationCommitment           The {@link RevocationCommitment} of the issuer for the attestation.
     * @param rTreePolicy                    The {@link RTreePolicy} for this attestation.
     * @param empiricalECKeyPair             The empirical EC {@link KeyPair} for this attestation.
     * @param referenceAPILayer              The reference to the API layer for this attestation.
     * @throws IllegalArgumentException If an invalid key was provided for the encryption schemes used during the construction process.
     */
    public IssuerPartNamespaceAttestation(@NotNull PrivateEntityIdentifier privateEntityIdentifierIssuer,
                                          @NotNull PublicEntityIdentifier publicEntityIdentifierIssuer,
                                          @NotNull PublicEntityIdentifier publicEntityIdentifierReceiver,
                                          @NotNull RevocationCommitment revocationCommitment,
                                          @NotNull RTreePolicy rTreePolicy,
                                          @NotNull KeyPair empiricalECKeyPair,
                                          @NotNull InetSocketAddress referenceAPILayer) throws IllegalArgumentException {
        super(privateEntityIdentifierIssuer, publicEntityIdentifierIssuer, publicEntityIdentifierReceiver,
                revocationCommitment, rTreePolicy, empiricalECKeyPair);
        this.referenceAPILayer = referenceAPILayer;
        updateSignature(empiricalECKeyPair.getPublic());
    }

    /**
     * The constructor of the {@link IssuerPartNamespaceAttestation}.
     *
     * @param privateEntityIdentifierIssuer  The {@link PrivateEntityIdentifier} of the issuer of this {@link IssuerPartAttestation}.
     * @param publicEntityIdentifierIssuer   The {@link PublicEntityIdentifier} of the issuer of this {@link IssuerPartAttestation}.
     * @param publicEntityIdentifierReceiver The {@link PublicEntityIdentifier} of the receiver of this {@link IssuerPartAttestation}.
     * @param revocationCommitment           The {@link RevocationCommitment} of the issuer for the attestation.
     * @param rTreePolicy                    The {@link RTreePolicy} for this attestation.
     * @param referenceAPILayer              The reference to the API layer for this attestation.
     * @throws IllegalArgumentException If an invalid key was provided for the encryption schemes used during the construction process.
     */
    public IssuerPartNamespaceAttestation(@NotNull PrivateEntityIdentifier privateEntityIdentifierIssuer,
                                          @NotNull PublicEntityIdentifier publicEntityIdentifierIssuer,
                                          @NotNull PublicEntityIdentifier publicEntityIdentifierReceiver,
                                          @NotNull RevocationCommitment revocationCommitment,
                                          @NotNull RTreePolicy rTreePolicy,
                                          @NotNull InetSocketAddress referenceAPILayer) throws IllegalArgumentException {
        this(privateEntityIdentifierIssuer, publicEntityIdentifierIssuer, publicEntityIdentifierReceiver, revocationCommitment, rTreePolicy,
                ECCipherEncryptedSegment.generateKeyPair(), referenceAPILayer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IssuerPartNamespaceAttestation that = (IssuerPartNamespaceAttestation) o;
        return referenceAPILayer.equals(that.referenceAPILayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), referenceAPILayer);
    }
}
