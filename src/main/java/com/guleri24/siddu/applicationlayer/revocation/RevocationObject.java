package com.guleri24.siddu.applicationlayer.revocation;

import com.google.common.hash.Hashing;
import com.guleri24.siddu.shared.serialization.ExportableUtils;
import com.guleri24.siddu.storagelayer.StorageElement;
import com.guleri24.siddu.storagelayer.StorageElementIdentifier;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Class representing a {@link StorageElement} which encapsulates a revealed {@link RevocationSecret}.
 */
public class RevocationObject extends StorageElement {

    private final RevocationSecret revealedSecret;

    /**
     * Constructor for the {@link RevocationObject} class.
     *
     * @param identifier     The {@link StorageElementIdentifier} for this {@link StorageElement}.
     * @param revealedSecret The revealed {@link RevocationSecret}.
     */
    public RevocationObject(@NotNull StorageElementIdentifier identifier,
                            @NotNull RevocationSecret revealedSecret) {
        super(identifier);
        this.revealedSecret = revealedSecret;
    }

    @NotNull
    public static RevocationObject deserialize(@NotNull ByteBuffer byteBuffer) throws IOException {
        byte[] identifierAsByteArray = new byte[byteBuffer.getInt()];
        byteBuffer.get(identifierAsByteArray);
        StorageElementIdentifier storageElementIdentifier = ExportableUtils.deserialize(identifierAsByteArray, RevocationCommitment.class);

        byte[] revealedSecretAsByteArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(revealedSecretAsByteArray);
        RevocationSecret revealedSecret = ExportableUtils.deserialize(revealedSecretAsByteArray, RevocationSecret.class);

        return new RevocationObject(storageElementIdentifier, revealedSecret);
    }

    /**
     * Getter for the revealed {@link RevocationSecret}.
     *
     * @return The revealed {@link RevocationSecret}.
     */
    public RevocationSecret getRevealedSecret() {
        return revealedSecret;
    }

    /**
     * Method to check if the revealed secret actually corresponds with the {@link RevocationCommitment}
     * which was used as {@link StorageElementIdentifier} for this {@link StorageElement}.
     *
     * @return True if the revealed secret actually corresponds with the {@link RevocationCommitment}
     * used for the constructor of this instance; false otherwise.
     */
    public boolean isValid() {
        String reconstructedRevocationCommitment = Hashing.sha512().hashString(revealedSecret.getSecret(), StandardCharsets.UTF_8)
                .toString();
        return reconstructedRevocationCommitment.equals(getStorageLayerIdentifier().getIdentifier());
    }

    @Override
    public String toString() {
        return "RevocationObject{" +
                "revocationCommitment=" + getStorageLayerIdentifier() +
                ", revealedSecret=" + revealedSecret +
                '}';
    }

    @Override
    public byte[] serialize() throws IOException {
        byte[] storageElementSerializationResult = super.serialize();
        byte[] revealedSecretSerializationResult = ExportableUtils.serialize(revealedSecret);

        ByteBuffer byteBuffer = ByteBuffer.allocate(storageElementSerializationResult.length + revealedSecretSerializationResult.length + 4);
        byteBuffer.putInt(storageElementSerializationResult.length);
        byteBuffer.put(storageElementSerializationResult);
        byteBuffer.put(revealedSecretSerializationResult);

        return byteBuffer.array();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RevocationObject that)) return false;

        return getRevealedSecret().equals(that.getRevealedSecret());
    }

    @Override
    public int hashCode() {
        return getRevealedSecret().hashCode();
    }
}
