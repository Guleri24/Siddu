package com.guleri24.siddu;

import com.guleri24.siddu.apilayer.macaroon.APILayerMacaroon;
import com.guleri24.siddu.apilayer.macaroon.APILayerMacaroonManager;
import com.guleri24.siddu.applicationlayer.attestation.Attestation;
import com.guleri24.siddu.applicationlayer.attestation.issuer.IssuerPartAttestation;
import com.guleri24.siddu.applicationlayer.attestation.policy.PolicyRight;
import com.guleri24.siddu.applicationlayer.attestation.policy.RTreePolicy;
import com.guleri24.siddu.applicationlayer.proof.ProofObject;
import com.guleri24.siddu.applicationlayer.revocation.RevocationCommitment;
import com.guleri24.siddu.encryptionlayer.entities.EntityIdentifier;
import com.guleri24.siddu.encryptionlayer.schemes.AESCipherEncryptedSegment;
import com.guleri24.siddu.shared.serialization.ExportableUtils;
import com.guleri24.siddu.storagelayer.StorageElementIdentifier;

import java.io.FileWriter;
import java.io.IOException;

public class DissertationDemo {
    private final static String CYAN = "\u001B[1;36m";
    private final static String RESET = "\u001B[0m";

    public static void main(String[] args) throws IOException {
        OperationTime time = new OperationTime();
        FileWriter fileWriter;
        FileWriter resultWriter = new FileWriter("results.txt", true);
        resultWriter.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");

        System.out.println("Invitations...");
        time.start();
        fileWriter = new FileWriter("invitation_objects.txt");
        for (int i = 8; i <= 100; i++) {
            RTreePolicy rTreePolicy = new RTreePolicy(PolicyRight.READ, "A".repeat(i - 7));
            var entityPair = EntityIdentifier.generateEntityIdentifierPair("");
            IssuerPartAttestation issuerPartAttestation = new IssuerPartAttestation(entityPair.getKey(), entityPair.getValue(), entityPair.getRight(), new RevocationCommitment(), rTreePolicy);
            byte[] serialized = ExportableUtils.serialize(issuerPartAttestation);
            fileWriter.write(String.format("%s\t%s%n", i, serialized.length));
        }
        fileWriter.close();
        time.end();
        resultWriter.write(String.format("Invitations: %s ms.%n", time.getDuration()));
        System.out.println(CYAN + "Invitations: " + time.getDuration() + "ms" + RESET);

        System.out.println("Attestations...");
        time.start();
        fileWriter = new FileWriter("attestation_objects.txt");
        for (int i = 8; i <= 100; i++) {
            RTreePolicy rTreePolicy = new RTreePolicy(PolicyRight.READ, "A".repeat(i - 7));
            var entityPair = EntityIdentifier.generateEntityIdentifierPair("");
            IssuerPartAttestation issuerPartAttestation = new IssuerPartAttestation(entityPair.getKey(), entityPair.getValue(), entityPair.getRight(), new RevocationCommitment(), rTreePolicy);
            Attestation attestation = new Attestation(new StorageElementIdentifier(), issuerPartAttestation, new RevocationCommitment(), new StorageElementIdentifier(), entityPair.getKey());
            byte[] serialized = ExportableUtils.serialize(attestation);
            fileWriter.write(String.format("%s\t%s%n", i, serialized.length));
        }
        time.end();
        resultWriter.write(String.format("Attestations: %s ms.%n", time.getDuration()));
        System.out.println(CYAN + "Attestations: " + time.getDuration() + "ms" + RESET);
        fileWriter.close();

        System.out.println("Proof objects...");
        time.start();
        fileWriter = new FileWriter("proof_objects.txt");
        for (int i = 1; i <= 1000; i++) {
            String[] aesKeys = new String[i];
            StorageElementIdentifier[] storageElementIdentifiers = new StorageElementIdentifier[i];
            for (int j = 0; j < i; j++) {
                aesKeys[j] = AESCipherEncryptedSegment.generateAESKey();
                storageElementIdentifiers[j] = new StorageElementIdentifier();
            }
            ProofObject proofObject = new ProofObject(storageElementIdentifiers, aesKeys, "");
            byte[] serialized = ExportableUtils.serialize(proofObject);
            fileWriter.write(String.format("%s\t%s%n", i, serialized.length));
        }
        time.end();
        resultWriter.write(String.format("Proof objects: %s ms.%n", time.getDuration()));
        System.out.println(CYAN + "Proof objects: " + time.getDuration() + "ms" + RESET);
        fileWriter.close();

        System.out.println("Macaroon objects...");
        time.start();
        APILayerMacaroonManager apiLayerMacaroonManager = new APILayerMacaroonManager();
        fileWriter = new FileWriter("macaroon_objects.txt");
        for (int i = 8; i <= 1000; i++) {
            RTreePolicy rTreePolicy = new RTreePolicy(PolicyRight.READ, "A".repeat(i - 7));
            APILayerMacaroon macaroon = apiLayerMacaroonManager.registerPolicy(rTreePolicy);
            byte[] serialized = ExportableUtils.serialize(macaroon);
            fileWriter.write(String.format("%s\t%s%n", i, serialized.length));
        }
        time.end();
        resultWriter.write(String.format("Macaroon objects: %s ms.%n", time.getDuration()));
        System.out.println(CYAN + "Macaroon objects: " + time.getDuration() + "ms" + RESET);
        fileWriter.close();

        resultWriter.write("Done\n");
        resultWriter.close();
        System.out.println("Done.");
    }
}