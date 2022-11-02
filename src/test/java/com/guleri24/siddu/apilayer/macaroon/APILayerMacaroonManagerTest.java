package com.guleri24.siddu.apilayer.macaroon;

import com.guleri24.siddu.applicationlayer.attestation.policy.PolicyRight;
import com.guleri24.siddu.applicationlayer.attestation.policy.RTreePolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class APILayerMacaroonManagerTest {

    @Test
    void registerPolicy() {
        APILayerMacaroonManager apiLayerMacaroonManager = new APILayerMacaroonManager();

        RTreePolicy rTreePolicy = new RTreePolicy(PolicyRight.WRITE, "A", "B", "C");
        APILayerMacaroon macaroon = apiLayerMacaroonManager.registerPolicy(rTreePolicy);

        assertEquals(rTreePolicy, apiLayerMacaroonManager.returnVerifiedPolicy(macaroon));

        APILayerMacaroon unregisteredMacaroon = new APILayerMacaroon("secret", "publicIdentifier",
                new RTreePolicy(PolicyRight.WRITE, "A", "B"));
        assertThrows(IllegalArgumentException.class, () -> apiLayerMacaroonManager.returnVerifiedPolicy(unregisteredMacaroon));
    }
}