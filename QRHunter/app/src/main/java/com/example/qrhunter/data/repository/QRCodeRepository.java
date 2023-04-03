package com.example.qrhunter.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.utils.QRCodeUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
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
    public void addQRCodeToPlayer(QRCode qrCode, String playerId, byte[] savedPhoto, RepositoryCallback<String> repositoryCallback) {
        PlayerRepository playerRepository = new PlayerRepository();

        // Check whether qr code exists.
        doesQRCodeExist(qrCode, existingQRCode -> {
            if (existingQRCode == null) {
                qrCode.setUnique();
                // If qr code doesn't exist, add a new document to Firestore
                String photoPath;
                ArrayList<String> photos = new ArrayList<>();
                String qrCodeId = qrCode.getId();

                if (savedPhoto == null) {
                    photoPath = null;
                } else {
                    photoPath = "photos/" + qrCodeId + "/" + playerId + ".jpg";
                    photos.add(photoPath);
                    qrCode.setPhotos(photos);
                }

                // Create new QR Code document
                Map<String, Object> qrCodeHashMap = QRCodeUtil.convertQRCodeToHashmap(qrCode);
                db.collection("qrCodes").document(qrCodeId).set(qrCodeHashMap).addOnCompleteListener(task -> {
                    playerRepository.addScoreToPlayer(playerId, qrCode.getScore());
                });

                // Upload photo to Firebase Storage
                playerRepository.uploadPhoto(savedPhoto, qrCodeId, playerId);

                repositoryCallback.onSuccess("QR Code successfully added!");
            } else {
                qrCode.setUnique();
                // If qr code exists, update the existing document in Firestore
                String photoPath;
                ArrayList<String> existingPhotos = qrCode.getPhotos();
                if (savedPhoto == null) {
                    photoPath = null;
                } else {
                    photoPath = "photos/" + existingQRCode.getId() + "/" + playerId + ".jpg";
                    existingPhotos.add(photoPath);
                    qrCode.setPhotos(existingPhotos);
                }

                playerRepository.uploadPhoto(savedPhoto, existingQRCode.getId(), playerId);

                //Update player count
                db.collection("qrCodes").document(existingQRCode.getId())
                        .update("playerIds", FieldValue.arrayUnion(playerId));

                // Update location
                if (!qrCode.getLocations().isEmpty()) {
                    db.collection("qrCodes").document(existingQRCode.getId())
                            .update("locations", FieldValue.arrayUnion(qrCode.getLocations().get(0)));
                }

                // Update photos
                if (!qrCode.getPhotos().isEmpty()) {
                    db.collection("qrCodes").document(existingQRCode.getId())
                            .update("photos", FieldValue.arrayUnion(qrCode.getPhotos().get(0)));
                }

                if (!existingQRCode.getPlayerIds().contains(playerId)) {
                    playerRepository.addScoreToPlayer(playerId, qrCode.getScore());
                    repositoryCallback.onSuccess("QR Code successfully added!");
                } else {
                    repositoryCallback.onSuccess("QR Code already scanned.");
                }
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

        String photoPath = "photos/" + qrCodeId + "/" + playerId + ".jpg";

        // Update qr code document, remove player id. photo path and location.
        db.collection("qrCodes").document(qrCodeId)
                .update("playerIds", FieldValue.arrayRemove(playerId));


        getQRCode(qrCodeId, qrCode1 -> {
            if (!qrCode1.getLocations().isEmpty()) {
                db.collection("qrCodes").document(qrCodeId)
                        .update("locations", FieldValue.arrayRemove(qrCode1.getLocations().get(0)));
            }
            if (!qrCode1.getPhotos().isEmpty()) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference deleteReference = storageReference.child("photos").child(qrCodeId).child(playerId);

                deleteReference.delete().addOnSuccessListener(aVoid -> {
                    Log.d("QRCodeRepository", "Photo deleted successfully");
                }).addOnFailureListener(e -> {
                    Log.d("QRCodeRepository", "Photo deletion failed");
                });
                db.collection("qrCodes").document(qrCodeId)
                        .update("photos", FieldValue.arrayRemove(photoPath)).addOnSuccessListener(task -> {
                            Log.d("QRCodeRepository", "Photo path removed successfully");
                        }).addOnFailureListener(e -> {
                            Log.d("QRCodeRepository", "Photo path removal failed");
                        });
            }
            qrCode1.setUnique();
        });


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
    public void doesQRCodeExist(QRCode qrCode, RepositoryCallback<QRCode> repositoryCallback) {
        Query qrCodesQuery = db.collection("qrCodes").whereEqualTo("hash", qrCode.getHash());


        qrCodesQuery.get().addOnCompleteListener(task -> {
            //If qr code doesn't exist
            if (task.isSuccessful() && task.getResult().isEmpty()) {
                repositoryCallback.onSuccess(null);
            } else {
                Boolean doesExist = false;
                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                    QRCode queriedQRCode = QRCodeUtil.convertDocumentToQRCode(documentSnapshot);

                    if (qrCode.getLocations().isEmpty() && queriedQRCode.getLocations().isEmpty()) {
                        doesExist = true;
                        repositoryCallback.onSuccess(queriedQRCode);
                        break;
                    } else if (qrCode.getLocations().size() > 0 && queriedQRCode.getLocations().size() > 0) {
                        doesExist = true;
                        repositoryCallback.onSuccess(queriedQRCode);
                        break;
                    }
                }

                if (!doesExist)
                    repositoryCallback.onSuccess(null);

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

    /**
     * Gets a list of QRCodes from firestore
     *
     * @return LiveData of a list of QRCode objects
     */
    public LiveData<List<QRCode>> getQRCodeList() {
        MutableLiveData<List<QRCode>> QRCodesLiveData = new MutableLiveData<>();
        CollectionReference QRCodesRef = db.collection("qrCodes");
        QRCodesRef.addSnapshotListener((queryDocumentSnapshots, error) -> {
            List<QRCode> QRCodes = new ArrayList<>();
            assert queryDocumentSnapshots != null;
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                QRCodes.add(QRCodeUtil.convertDocumentToQRCode(document));
            }
            QRCodesLiveData.setValue(QRCodes);
        });
        return QRCodesLiveData;
    }

    public void addCommentId(String qrCodeId, String commentId) {
        db.collection("qrCodes").document(qrCodeId)
                .update("commentIds", FieldValue.arrayUnion(commentId));
    }
}
