package com.ScalableTeam.chat.app.GroupChat;

import com.ScalableTeam.chat.app.MyCommand;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AddMemberCommand implements MyCommand {
    @Override
    public Object execute(Map<String, Object> groupChatDetails) {
        try {
            String groupChatId = (String) groupChatDetails.get("groupId");
            String memberId = (String) groupChatDetails.get("memberId");

            final Firestore database = FirestoreClient.getFirestore();
            DocumentReference docRef = database.collection("GroupChats").document(groupChatId);

            ApiFuture<WriteResult> arrayUnion = docRef.update("users", FieldValue.arrayUnion(memberId));
            System.out.println("Update time : " + arrayUnion.get());

            DocumentReference updatedDocumentReference = database.collection("GroupChats").document(groupChatId);

            return updatedDocumentReference.get().get().getData();
        } catch (Exception e) {
            System.out.println(e);
            return "Internal Server Error";
        }
    }
}