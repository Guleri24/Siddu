package com.guleri24.siddu.storagelayer.dht;

import com.guleri24.siddu.applicationlayer.revocation.RevocationCommitment;
import com.guleri24.siddu.applicationlayer.revocation.RevocationObject;
import com.guleri24.siddu.applicationlayer.revocation.RevocationSecret;
import com.guleri24.siddu.encryptionlayer.entities.EntityIdentifier;
import com.guleri24.siddu.encryptionlayer.entities.PrivateEntityIdentifier;
import com.guleri24.siddu.encryptionlayer.entities.PublicEntityIdentifier;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DHTStorageLayerTest {

    private final Pair<PrivateEntityIdentifier, PublicEntityIdentifier> userOne =
            EntityIdentifier.generateEntityIdentifierPair("testOne");
    private final Pair<PrivateEntityIdentifier, PublicEntityIdentifier> userTwo =
            EntityIdentifier.generateEntityIdentifierPair("testTwo");

    private final DHTStorageLayer dhtStorageLayerOne = new DHTStorageLayer(userOne.getRight(), 5878);
    private final DHTStorageLayer getDhtStorageLayerTwo = new DHTStorageLayer(userTwo.getRight(), 5879, dhtStorageLayerOne);

    DHTStorageLayerTest() throws IOException {
    }

    @Test
    void retrieve() throws IOException, InterruptedException {
        var revocationSecret = new RevocationSecret();
        var revocationCommitment = new RevocationCommitment(revocationSecret);
        var revocationObject = new RevocationObject(revocationCommitment, revocationSecret);

        dhtStorageLayerOne.put(revocationObject);
        var retrieved = getDhtStorageLayerTwo.retrieve(revocationCommitment);
        assertEquals(1, retrieved.size());
        retrieved.forEach(storageElement -> Assertions.assertEquals(revocationCommitment, storageElement.getStorageLayerIdentifier()));
    }
}