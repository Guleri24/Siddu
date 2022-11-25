package com.guleri24.siddu;

import com.guleri24.siddu.apilayer.macaroon.APILayerMacaroonManager;
import com.guleri24.siddu.applicationlayer.attestation.Attestation;
import com.guleri24.siddu.applicationlayer.attestation.NamespaceAttestation;
import com.guleri24.siddu.applicationlayer.attestation.issuer.IssuerPartAttestation;
import com.guleri24.siddu.applicationlayer.attestation.issuer.IssuerPartNamespaceAttestation;
import com.guleri24.siddu.applicationlayer.attestation.policy.PolicyRight;
import com.guleri24.siddu.applicationlayer.attestation.policy.RTreePolicy;
import com.guleri24.siddu.applicationlayer.proof.ProofObject;
import com.guleri24.siddu.applicationlayer.revocation.RevocationCommitment;
import com.guleri24.siddu.applicationlayer.revocation.RevocationObject;
import com.guleri24.siddu.applicationlayer.revocation.RevocationSecret;
import com.guleri24.siddu.encryptionlayer.entities.EntityIdentifier;
import com.guleri24.siddu.encryptionlayer.entities.PrivateEntityIdentifier;
import com.guleri24.siddu.encryptionlayer.entities.PublicEntityIdentifier;
import com.guleri24.siddu.storagelayer.StorageElementIdentifier;
import com.guleri24.siddu.storagelayer.map.MultiMappedStorageLayer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Demo {
    private final static String GREEN = "\u001B[32m";
    private final static String CYAN = "\u001B[1;36m";
    private final static String BLUE = "\u001B[34m";

    private final static String RESET = "\u001B[0m";

    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        OperationTime time = new OperationTime();
        FileWriter resultWriter = new FileWriter("results.txt", true);
        resultWriter.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");

        // Generating the storage layer.
        System.out.println("Running...");

        time.start();
        var storageLayer = new MultiMappedStorageLayer();
        time.end();
        resultWriter.write(String.format("Generating the storage layer took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Storage Layer Setup Time: " + time.getDuration() + "ms" + RESET);

        time.start();
        var macaroonManager = new APILayerMacaroonManager();
        time.end();
        resultWriter.write(String.format("Generating the macaroon manager took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Macaroon Manager Setup Time: " + time.getDuration() + "ms" + RESET);

        // Generating encryption keys for the cloud storage service providers.

        time.start();
        var cloudA = EntityIdentifier.generateEntityIdentifierPair("cloudA");
        var cloudB = EntityIdentifier.generateEntityIdentifierPair("cloudB");
        var cloudC = EntityIdentifier.generateEntityIdentifierPair("cloudC");
        var cloudD = EntityIdentifier.generateEntityIdentifierPair("cloudD");
        time.end();
        resultWriter.write(String.format("Generating the encryption keys for the cloud storage service providers took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Cloud Encryption Key Generation Time: " + time.getDuration() + "ms" + RESET);

        time.start();
        List<Pair<String, Pair<PrivateEntityIdentifier, PublicEntityIdentifier>>> clouds = new ArrayList<>();
        clouds.add(new ImmutablePair<>("cloudA", cloudA));
        clouds.add(new ImmutablePair<>("cloudB", cloudB));
        clouds.add(new ImmutablePair<>("cloudC", cloudC));
        clouds.add(new ImmutablePair<>("cloudD", cloudD));
        time.end();
        resultWriter.write(String.format("Generating the list of cloud storage service providers took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Clouds List Setup Time: " + time.getDuration() + "ms" + RESET);


        // Generating encryption keys for the users.
        time.start();
        var userA = EntityIdentifier.generateEntityIdentifierPair("userA");
        var userB = EntityIdentifier.generateEntityIdentifierPair("userB");
        var userC = EntityIdentifier.generateEntityIdentifierPair("userC");
        var userD = EntityIdentifier.generateEntityIdentifierPair("userD");
        time.end();
        resultWriter.write(String.format("Generating the encryption keys for the users took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "User Encryption Key Generation Time: " + time.getDuration() + "ms" + RESET);

        time.start();
        List<Pair<String, Pair<PrivateEntityIdentifier, PublicEntityIdentifier>>> users = new ArrayList<>();
        users.add(new ImmutablePair<>("userA", userA));
        users.add(new ImmutablePair<>("userB", userB));
        users.add(new ImmutablePair<>("userC", userC));
        users.add(new ImmutablePair<>("userD", userD));
        time.end();
        resultWriter.write(String.format("Generating the list of users took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Users List Setup Time: " + time.getDuration() + "ms" + RESET);

        time.start();
        List<Pair<String, Pair<PrivateEntityIdentifier, PublicEntityIdentifier>>> allEntities = new ArrayList<>();
        allEntities.addAll(clouds);
        allEntities.addAll(users);
        for (var entity : allEntities) {
            System.out.printf("%s%s%s%n%sPublic EC key:%s\t%s%n%sPrivate EC key:%s\t%s%n%sIBE parameters:%s\t%s%n%sStorage layer identifier first attestation in personal queue:%s\t%s%n%n",
                    BLUE, entity.getLeft(), RESET,
                    GREEN, RESET, entity.getRight().getLeft().getRSAIdentifier(),
                    GREEN, RESET, entity.getRight().getRight().getRSAIdentifier(),
                    GREEN, RESET, entity.getRight().getLeft().getIBEIdentifier(),
                    GREEN, RESET, entity.getRight().getRight().getNamespaceServiceProviderEmailAddressUserConcatenation());
        }
        time.end();
        resultWriter.write(String.format("Printing the list of all entities took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "All Entities List Setup Time: " + time.getDuration() + "ms" + RESET);

        time.start();
        Map<PublicEntityIdentifier, StorageElementIdentifier> queueStorageElementIdentifiers = new HashMap<>();
        queueStorageElementIdentifiers.put(userA.getRight(), new StorageElementIdentifier(userA.getRight().getNamespaceServiceProviderEmailAddressUserConcatenation()));
        queueStorageElementIdentifiers.put(userB.getRight(), new StorageElementIdentifier(userB.getRight().getNamespaceServiceProviderEmailAddressUserConcatenation()));
        queueStorageElementIdentifiers.put(userC.getRight(), new StorageElementIdentifier(userC.getRight().getNamespaceServiceProviderEmailAddressUserConcatenation()));
        time.end();
        resultWriter.write(String.format("Generating the storage element identifiers for the personal queues took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Queue Storage Element Identifiers Setup Time: " + time.getDuration() + "ms" + RESET);

        // Registering the users to the respective cloud storage service providers.
        time.start();
        NamespaceAttestation namespaceAttestationUserC = null;
        for (var i = 0; i < 3; i++) {
            var user = users.get(i);
            var cloud = clouds.get(i);
            var issuerPartNamespaceAttestation = new IssuerPartNamespaceAttestation(
                    cloud.getRight().getLeft(), cloud.getRight().getRight(), user.getRight().getRight(), new RevocationCommitment(new RevocationSecret()),
                    new RTreePolicy(PolicyRight.WRITE, user.getLeft()), new InetSocketAddress("localhost", 8000));
            var newNextStorageElementIdentifierPersonalQueue = new StorageElementIdentifier();
            queueStorageElementIdentifiers.put(user.getRight().getRight(), newNextStorageElementIdentifierPersonalQueue);
            var namespaceAttestation = new NamespaceAttestation(issuerPartNamespaceAttestation,
                    new RevocationCommitment(new RevocationSecret()), newNextStorageElementIdentifierPersonalQueue,
                    user.getRight().getRight(), user.getRight().getLeft());
            storageLayer.put(namespaceAttestation);
            if (i == 2) namespaceAttestationUserC = namespaceAttestation;
            System.out.printf("%nFirst attestation in personal queue of %s%s%s:\t%s%n", BLUE, user.getLeft(), RESET, namespaceAttestation);
        }
        time.end();
        resultWriter.write(String.format("Registering the users to the respective cloud storage service providers took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Registering the users to the respective cloud storage service providers: " + time.getDuration() + "ms" + RESET);
        System.out.println();

        // Generating the necessary attestations for the demo.
        time.start();
        var policy = new RTreePolicy(PolicyRight.WRITE, "userA", "file");
        List<Pair<Pair<PrivateEntityIdentifier, PublicEntityIdentifier>, Pair<PrivateEntityIdentifier, PublicEntityIdentifier>>>
                issuersReceiversAttestationsDemo = new ArrayList<>();
        issuersReceiversAttestationsDemo.add(new ImmutablePair<>(userA, userB));
        issuersReceiversAttestationsDemo.add(new ImmutablePair<>(userA, userC));
        issuersReceiversAttestationsDemo.add(new ImmutablePair<>(userB, userC));
        List<String> entityNames = new ArrayList<>(Arrays.asList("userA", "userB", "userA", "userC", "userB", "userC"));
        List<Attestation> generatedAttestationsForDemo = new ArrayList<>();
        List<RevocationSecret> revocationSecrets = new ArrayList<>();
        for (var issuerReceiverCombination : issuersReceiversAttestationsDemo) {
            var issuer = issuerReceiverCombination.getLeft();
            var receiver = issuerReceiverCombination.getRight();
            var issuerPartAttestation = new IssuerPartAttestation(issuer.getLeft(), issuer.getRight(),
                    receiver.getRight(), new RevocationCommitment(new RevocationSecret()), policy);
            var newNextStorageElementIdentifierPersonalQueue = new StorageElementIdentifier();
            var oldNextStorageLayerIdentifier = queueStorageElementIdentifiers.get(receiver.getRight());
            queueStorageElementIdentifiers.put(receiver.getRight(), newNextStorageElementIdentifierPersonalQueue);
            var lastRevocationSecret = new RevocationSecret();
            var attestation = new Attestation(oldNextStorageLayerIdentifier, issuerPartAttestation,
                    new RevocationCommitment(lastRevocationSecret),
                    newNextStorageElementIdentifierPersonalQueue, receiver.getLeft());
            storageLayer.put(attestation);
            revocationSecrets.add(lastRevocationSecret);
            generatedAttestationsForDemo.add(attestation);
            System.out.printf("%sAttestation (%s --> %s):%s\t%s%n", BLUE, entityNames.remove(0), entityNames.remove(0), RESET, attestation);
        }
        time.end();
        resultWriter.write(String.format("Generating the necessary attestations for the demo took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Generating the necessary attestations for the demo: " + time.getDuration() + "ms" + RESET);

        // Personal queues
        time.start();
        entityNames.add("userA");
        entityNames.add("userB");
        entityNames.add("userC");
        for (var i = 0; i < 3; i++) {
            var personalQueue = storageLayer.getPersonalQueueUser(users.get(i).getRight().getRight());
            System.out.printf("%n%sPersonal queue of %s:%s%n", BLUE, entityNames.remove(0), RESET);
            while (true) {
                try {
                    System.out.printf("%sNext attestation found:%s\t%s%n", BLUE, RESET, personalQueue.next());
                } catch (IllegalArgumentException e) {
                    break;
                }
            }
        }
        time.end();
        resultWriter.write(String.format("Retrieving the personal queues took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Personal queues: " + time.getDuration() + "ms" + RESET);


        // Obtain the first ephemeral AES key of the namespace attestation of user C.
        time.start();
        var aesEncryptionInformationSegmentNamespaceAttestationUserC =
                namespaceAttestationUserC.getFirstLayer().getAesEncryptionInformationSegment();
        var aesKeys = aesEncryptionInformationSegmentNamespaceAttestationUserC.decrypt(userC.getLeft(), new RTreePolicy(PolicyRight.WRITE, "userC"));
        var firstEphemeralAESKeyNamespaceAttestationUserC = aesKeys.getAesKeyInformation().getLeft();
        aesKeys = generatedAttestationsForDemo.get(2).getFirstLayer().getAesEncryptionInformationSegment()
                .decrypt(userC.getLeft(), policy);
        time.end();
        resultWriter.write(String.format("Obtaining the first ephemeral AES key of the namespace attestation of user C took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Obtain the first ephemeral AES key of the namespace attestation of user C: " + time.getDuration() + "ms" + RESET);

        // Generating the first proof object.
        time.start();
        System.out.printf("%n%sGenerating first variant of the proof object (userA --> userB --> userC):%s%n", BLUE, RESET);
        Logger.getLogger(ProofObject.class.getName()).setLevel(Level.ALL);
        var proofObjectOne = ProofObject.generateProofObject(generatedAttestationsForDemo.get(2), aesKeys.getAesKeyInformation().getLeft(),
                aesKeys.getAesKeyInformation().getRight(), firstEphemeralAESKeyNamespaceAttestationUserC, storageLayer);
        System.out.printf("%sResulting proof object:%s%n%s%n", BLUE, RESET, proofObjectOne);
        var macaroonOne = macaroonManager.registerPolicy(proofObjectOne.verify(storageLayer));
        System.out.printf("%sResulting macaroon:%s%n%s%n%n", BLUE, RESET, macaroonOne);
        time.end();
        resultWriter.write(String.format("Generating the first proof object took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Generating the first proof object: " + time.getDuration() + "ms" + RESET);

        // Generating the second proof object.
        time.start();
        aesKeys = generatedAttestationsForDemo.get(1).getFirstLayer().getAesEncryptionInformationSegment()
                .decrypt(userC.getLeft(), policy);
        System.out.printf("%sGenerating second variant of the proof object (userA --> userC):%s%n", BLUE, RESET);
        var proofObjectTwo = ProofObject.generateProofObject(generatedAttestationsForDemo.get(1),
                aesKeys.getAesKeyInformation().getLeft(), aesKeys.getAesKeyInformation().getRight(), firstEphemeralAESKeyNamespaceAttestationUserC, storageLayer);
        System.out.printf("%sResulting proof object:%s%n%s%n", BLUE, RESET, proofObjectTwo);
        var macaroonTwo = macaroonManager.registerPolicy(proofObjectTwo.verify(storageLayer));
        System.out.printf("%sResulting macaroon:%s%n%s%n%n", BLUE, RESET, macaroonTwo);
        time.end();
        resultWriter.write(String.format("Generating the second proof object took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Generating the second proof object: " + time.getDuration() + "ms" + RESET);

        // Revoking the attestation user A --> user B
        time.start();
        var revocationObject = new RevocationObject(new RevocationCommitment(revocationSecrets.get(0)), revocationSecrets.get(0));
        System.out.printf("%sRevoking the attestation (userA --> userB):%s%n%s%n%n", BLUE, RESET, revocationObject);
        storageLayer.put(revocationObject);
        time.end();
        resultWriter.write(String.format("Revoking the attestation (userA --> userB) took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Revoking the attestation user A --> user B: " + time.getDuration() + "ms" + RESET);

        // Generating the second proof object.
        time.start();
        aesKeys = generatedAttestationsForDemo.get(1).getFirstLayer().getAesEncryptionInformationSegment()
                .decrypt(userC.getLeft(), policy);
        System.out.printf("%sGenerating second variant of the proof object (userA --> userC):%s%n", BLUE, RESET);
        proofObjectTwo = ProofObject.generateProofObject(generatedAttestationsForDemo.get(1),
                aesKeys.getAesKeyInformation().getLeft(), aesKeys.getAesKeyInformation().getRight(), firstEphemeralAESKeyNamespaceAttestationUserC, storageLayer);
        System.out.printf("%sResulting proof object:%s%n%s%n", BLUE, RESET, proofObjectTwo);
        macaroonTwo = macaroonManager.registerPolicy(proofObjectTwo.verify(storageLayer));
        System.out.printf("%sResulting macaroon:%s%n%s%n%n", BLUE, RESET, macaroonTwo);
        time.end();
        resultWriter.write(String.format("Generating the second proof object took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Generating the second proof object: " + time.getDuration() + "ms" + RESET);

        // Generating the first proof object.
        time.start();
        System.out.printf("%n%sGenerating first variant of the proof object (userA --> userB --> userC):%s%n", BLUE, RESET);
        Logger.getLogger(ProofObject.class.getName()).setLevel(Level.ALL);
        aesKeys = generatedAttestationsForDemo.get(2).getFirstLayer().getAesEncryptionInformationSegment()
                .decrypt(userC.getLeft(), policy);
        try {
            proofObjectOne = ProofObject.generateProofObject(generatedAttestationsForDemo.get(2), aesKeys.getAesKeyInformation().getLeft(),
                    aesKeys.getAesKeyInformation().getRight(), firstEphemeralAESKeyNamespaceAttestationUserC, storageLayer);
            System.out.printf("%sResulting proof object:%s%n%s%n", BLUE, RESET, proofObjectOne);
            macaroonOne = macaroonManager.registerPolicy(proofObjectOne.verify(storageLayer));
            System.out.printf("%sResulting macaroon:%s%n%s%n%n", BLUE, RESET, macaroonOne);
        } catch (Exception e) {
            System.out.printf("%sAn exception is thrown... as expected!%s%n", GREEN, RESET);
            e.printStackTrace();
        }
        time.end();
        resultWriter.write(String.format("Generating the first proof object took %d ms.%n", time.getDuration()));
        System.out.println(CYAN + "Generating the first proof object: " + time.getDuration() + "ms" + RESET);

        resultWriter.write("Done\n");
        resultWriter.close();
    }
}