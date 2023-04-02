package com.example.qrhunter.ui.qrcode;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Comment;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.data.repository.CommentRepository;
import com.example.qrhunter.data.repository.QRCodeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The view model for the QR code screen. This class updates itself when data changes.
 * Gets the QR code with the specified ID.
 * @param qrCodeId The ID of the QR code to get.
 * @return The QR code to be returned.
 */

public class QrCodeViewModel extends ViewModel {
    private final MutableLiveData<QRCode> qrCode = new MutableLiveData<>(null);
    private final MutableLiveData<Integer> scannedBy = new MutableLiveData<>(0);
    private final MutableLiveData<ArrayList<Comment>> comments = new MutableLiveData<>(new ArrayList<>());
    private QRCodeRepository qrCodeRepository = new QRCodeRepository();
    private CommentRepository commentRepository = new CommentRepository();
    
    /**
     * Gets the QR code with the specified ID.
     *
     * @param qrCodeId The ID of the QR code to get.
     * @return The QR code to be returned.
     */
    public LiveData<QRCode> getQRCode(String qrCodeId) {
        qrCodeRepository.getQRCode(qrCodeId, qrCode -> {
            this.qrCode.setValue(qrCode);
        });

        return this.qrCode;
    }
    
    /**
     * Gets the number of times the specified QR code has been scanned.
     *
     * @param qrCode The QR code to get the scan count for.
     * @return The number of times the specified QR code has been scanned
     */
    public LiveData<Integer> getScannedBy(QRCode qrCode) {
        qrCodeRepository.getScannedBy(qrCode.getId(), amountScannedBy -> {
            this.scannedBy.setValue(amountScannedBy);
        });

        return this.scannedBy;
    }
    
    /**
     * Gets the list of comments associated with the specified QR code.
     *
     * @param qrCode The QR code to get the comments for.
     * @return The list of comments associated with the QR code
     */
    public LiveData<ArrayList<Comment>> getComments(QRCode qrCode) {
        commentRepository.getComments(qrCode.getCommentIds(), comments -> {
            this.comments.setValue(comments);
        });

        return this.comments;
    }
    
    /**
     * Adds a comment to the specified QR code.
     *
     * @param qrCode The QR code to add the comment to.
     * @param comment The comment to add.
     */
    public void addComment(QRCode qrCode, Comment comment) {
        commentRepository.addComment(qrCode.getId(), comment, commentId -> {
            // Update the comment variable in ViewModel
            ArrayList<Comment> oldComments = this.comments.getValue();
            oldComments.add(comment);
            this.comments.setValue(oldComments);

            // Update the qr code document in Firestore to include this comment
            qrCodeRepository.addCommentId(qrCode.getId(), commentId);
        });
    }
    
    /**
     * Returns an ArrayList of string addresses based on the location coordinates stored in the QR code.
     * Uses the Geocoder class to retrieve the address information.
     * @param qrCode The QRCode object containing the location coordinates.
     * @param context The context of the current state of the application.
     * @return An ArrayList of string addresses based on the location coordinates stored in the QR code.
     */
    public ArrayList<String> getAddress(QRCode qrCode, Context context){
        Geocoder geocoder;
        List<Address> addresses;
        ArrayList<String> stringAddresses = new ArrayList<>();
        geocoder = new Geocoder(context, Locale.getDefault());
        for(int i = 0; i < qrCode.getLocations().size(); i++){
            try {
                addresses = geocoder.getFromLocation(qrCode.getLocations().get(i).getLatitude(), qrCode.getLocations().get(0).getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0);
                stringAddresses.add(String.format("%s", address));
            } catch (Exception e) {
                stringAddresses.add("");
            }
        }
        return stringAddresses;
    }
}
