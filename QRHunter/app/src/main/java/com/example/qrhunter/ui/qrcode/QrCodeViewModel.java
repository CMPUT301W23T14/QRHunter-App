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

public class QrCodeViewModel extends ViewModel {
    private final MutableLiveData<QRCode> qrCode = new MutableLiveData<>(null);
    private final MutableLiveData<Integer> scannedBy = new MutableLiveData<>(0);
    private final MutableLiveData<ArrayList<Comment>> comments = new MutableLiveData<>(new ArrayList<>());
    private QRCodeRepository qrCodeRepository = new QRCodeRepository();
    private CommentRepository commentRepository = new CommentRepository();

    public LiveData<QRCode> getQRCode(String qrCodeId) {
        qrCodeRepository.getQRCode(qrCodeId, qrCode -> {
            this.qrCode.setValue(qrCode);
        });

        return this.qrCode;
    }

    public LiveData<Integer> getScannedBy(QRCode qrCode) {
        qrCodeRepository.getScannedBy(qrCode.getId(), amountScannedBy -> {
            this.scannedBy.setValue(amountScannedBy);
        });

        return this.scannedBy;
    }

    public LiveData<ArrayList<Comment>> getComments(QRCode qrCode) {
        commentRepository.getComments(qrCode.getCommentIds(), comments -> {
            this.comments.setValue(comments);
        });

        return this.comments;
    }

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

    public String getAddress(QRCode qrCode, Context context){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(qrCode.getLocations().get(0).getLatitude(), qrCode.getLocations().get(0).getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        }
        catch (Exception e){
            return "None, None, None";
        }
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String postalCode = addresses.get(0).getPostalCode();
        if(city == null && postalCode == null) {
            return String.format("%s", address);
        }
        else{
            return String.format("%s, %s, %s", address, city, postalCode);
        }
    }
}