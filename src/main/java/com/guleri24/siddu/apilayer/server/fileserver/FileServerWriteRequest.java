package com.guleri24.siddu.apilayer.server.fileserver;

import com.guleri24.siddu.applicationlayer.attestation.policy.PolicyRight;
import com.guleri24.siddu.applicationlayer.attestation.policy.RTreePolicy;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract class representing a {@link FileServerRequest} to write to specific resources.
 */
public abstract class FileServerWriteRequest extends FileServerRequest {

    /**
     * Constructor for the {@link FileServerWriteRequest} class.
     *
     * @param resourceLocation The location of the specific resources.
     */
    protected FileServerWriteRequest(@NotNull String[] resourceLocation) {
        super(resourceLocation);
    }


    @Override
    public boolean coveredByPolicy(@NotNull RTreePolicy policy) {
        var generatedPolicy = new RTreePolicy(PolicyRight.WRITE, getResourceLocation());
        return policy.coversRTreePolicy(generatedPolicy);
    }
}
