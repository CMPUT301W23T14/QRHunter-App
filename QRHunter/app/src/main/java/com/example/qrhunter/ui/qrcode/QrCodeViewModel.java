package com.example.qrhunter.ui.qrcode;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Comment;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.data.repository.CommentRepository;
import com.example.qrhunter.data.repository.QRCodeRepository;

import java.util.ArrayList;

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
}