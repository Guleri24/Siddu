package com.guleri24.siddu.storagelayer.map;

import com.guleri24.siddu.applicationlayer.attestation.Attestation;
import com.guleri24.siddu.storagelayer.StorageElement;
import com.guleri24.siddu.storagelayer.StorageElementIdentifier;
import com.guleri24.siddu.storagelayer.StorageLayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class representing a {@link HashMap} implementation of the {@link StorageLayer} interface.
 *
 * @implSpec The decision was made to implement the {@link StorageLayer} with a {@link HashMap},
 * since the used Kademlia library could not support the size of the {@link Attestation} instances.
 * Since the multi-value DHT is considered a building block for this thesis,
 * the decision was made to just replace it with a {@link HashMap}.
 *              TODO: maybe try to fix this?
 */
public class MultiMappedStorageLayer implements StorageLayer {

    private final static Map<StorageElementIdentifier, Set<StorageElement>> storedElements =
            Collections.synchronizedMap(new HashMap<>());

    @Override
    public void put(@NotNull StorageElement newElement) {
        if (!storedElements.containsKey(newElement.getStorageLayerIdentifier()))
            storedElements.put(newElement.getStorageLayerIdentifier(), Collections.synchronizedSet(new HashSet<>()));
        storedElements.get(newElement.getStorageLayerIdentifier()).add(newElement);
    }

    @Override
    public Set<StorageElement> retrieve(@NotNull StorageElementIdentifier identifier) {
        return storedElements.getOrDefault(identifier, new HashSet<>());
    }
}
