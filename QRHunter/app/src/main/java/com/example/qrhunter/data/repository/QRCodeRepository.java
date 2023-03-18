package com.example.qrhunter.data.repository;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.utils.QRCodeUtil;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * A repository class for containing any data access and business logic related to QR Codes
 */
public class QRCodeRepository extends DataRepository {
    /**
     * Create a QR Code document in Firestore and add appropriate score to player
     *
     * @param playerId The id of the player
     * @param qrCode   The id of the qrCode
     */
    public void addQRCodeToPlayer(QRCode qrCode, String playerId) {
        PlayerRepository playerRepository = new PlayerRepository();

        // Check whether qr code exists.
        doesQRCodeExist(qrCode, documentSnapshot -> {
            if (documentSnapshot == null) {
                // If qr code doesn't exist, add a new document to Firestore
                Map<String, Object> qrCodeHashMap = QRCodeUtil.convertQRCodeToHashmap(qrCode);

                db.collection("qrCodes").add(qrCodeHashMap).addOnCompleteListener(task -> {
                    playerRepository.addScoreToPlayer(playerId, qrCode.getScore());
                });
            } else {
                //Update qr code document
                db.collection("qrCodes").document(documentSnapshot.getId())
                        .update("playerIds", FieldValue.arrayUnion(playerId));
                playerRepository.addScoreToPlayer(playerId, qrCode.getScore());
            }
        });
    }

    /**
     * Remove a QR Code document in Firestore and reduce appropriate score from player
     *
     * @param playerId The id of the player
     * @param qrCodeId The id of the qrCode
     */
    public void removeQRCodeFromPlayer(String qrCodeId, String playerId) {
        PlayerRepository playerRepository = new PlayerRepository();

        // Update qr code document
        db.collection("qrCodes").document(qrCodeId)
                .update("playerIds", FieldValue.arrayRemove(playerId));

        // update player document (reduce score).
        db.collection("qrCodes").document(qrCodeId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                double scoreChange = task.getResult().getDouble("score");
                playerRepository.addScoreToPlayer(playerId, -(scoreChange));
            }
        });

    }

    /**
     * Get a qr code document in Firestore.
     * The criteria for equivalence is based on https://eclass.srv.ualberta.ca/mod/forum/discuss.php?d=2203362#p5702650
     * Where a QRCode with location will be treated as a different QRCode with location even if their hash is the same.
     * Returns A QRCode document reference in the callback if qr code exist in the callback.
     *
     * @param qrCode The qr code to be checked against
     */
    public void doesQRCodeExist(QRCode qrCode, RepositoryCallback<DocumentSnapshot> repositoryCallback) {
        Query qrCodesQuery = db.collection("qrCodes").whereEqualTo("hash", qrCode.getHash());
        boolean hasLocation = (qrCode.getLocation().latitude != 0 && qrCode.getLocation().longitude != 0);

        qrCodesQuery.get().addOnCompleteListener(task -> {
            //If qr code doesn't exist
            if (task.isSuccessful() && task.getResult().isEmpty()) {
                repositoryCallback.onSuccess(null);
            } else {
                DocumentSnapshot result = task.getResult().getDocuments().get(0);

                // If qr code exist, check whether the location is the same
                if (hasLocation && (double) result.get("latitude") != 0 && (double) result.get("longitude") != 0) {
                    repositoryCallback.onSuccess(result);
                } else if (!hasLocation && (double) result.get("latitude") == 0 && (double) result.get("longitude") == 0) {
                    repositoryCallback.onSuccess(result);
                } else {
                    repositoryCallback.onSuccess(null);
                }

            }
        });
    }

    /**
     * Gets a qr code given the id of the qr code
     * Returns a QR Code object in the callback
     *
     * @param qrCodeId The id of the qr code
     */
    public void getQRCode(String qrCodeId, RepositoryCallback<QRCode> repositoryCallback) {
        db.collection("qrCodes").document(qrCodeId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        QRCode result = QRCodeUtil.convertDocumentToQRCode(task.getResult());
                        repositoryCallback.onSuccess(result);
                    }
                });
    }

    /**
     * Gets list of qr codes that a player has scanned
     * Returns A list of qr code that the player has scanned in the callback
     *
     * @param player The player to be checked against
     */
    public void getScannedQRCodes(Player player, RepositoryCallback<ArrayList<QRCode>> repositoryCallback) {
        db.collection("qrCodes").whereArrayContains("playerIds", player.getId())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        ArrayList<QRCode> result = new ArrayList<>();

                        for (QueryDocumentSnapshot qrCodeDocument : task.getResult()) {
                            result.add(QRCodeUtil.convertDocumentToQRCode(qrCodeDocument));
                        }

                        repositoryCallback.onSuccess(result);
                    }
                });
    }


    /**
     * Get the number of the people who scanned the qr code.
     * Returns the amount of people who scanned in the callback
     *
     * @param qrCodeId The qr code to be checked
     */
    public void getScannedBy(String qrCodeId, RepositoryCallback<Integer> repositoryCallback) {
        db.collection("qrCodes").document(qrCodeId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        int result = ((ArrayList<String>) task.getResult().get("playerIds")).size();
                        repositoryCallback.onSuccess(result);
                    }
                });
    }
}
