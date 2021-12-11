package com.example.easysplit;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TripMemRetrival {

    HashMap<String, ArrayList<Double>> members = new HashMap<String, ArrayList<Double>>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TripMemRetrival() {

    }

    public HashMap<String, ArrayList<Double>> retrieve(String tripId) {

        members.put("haha@gmail.com", new ArrayList<Double>());
        ArrayList<Double> pp = members.get("haha@gmail.com");
        pp.add(1.0);
        pp.add(2.0);
        members.put("haha@gmail.com", pp);

        //tripId="temptrip";

        DocumentReference ref = FirebaseFirestore.getInstance().collection("Trips").document(tripId);

        List<String> mailIds = new ArrayList<String>();

        db.collection("Trips").document(tripId).collection("memberlist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //list = new ArrayList<>();
                    //String eventsid="";
                    //int i=0;
                    //Toast.makeText(EventsPage.this,"before for" ,Toast.LENGTH_SHORT).show();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        mailIds.add(document.getId());
                        //eventsid=eventsid+list.get(i);
                        //Toast.makeText(EventsPage.this,"in for" ,Toast.LENGTH_LONG).show();
                        // i++;
                    }
                    //  Toast.makeText(EventsPage.this,"after for" ,Toast.LENGTH_LONG).show();
                    //  Toast.makeText(EventsPage.this,eventsid ,Toast.LENGTH_LONG).show();
                    // eventId=list.get(0);
                    //String heh= list.get(0)+list.get(1)+list.get(2);
                    //Log.d(TAG, list.toString());
                    //  Toast.makeText(EventsPage.this,heh ,Toast.LENGTH_LONG).show();
                    // Toast.makeText(EventsPage.this,list.get(0) ,Toast.LENGTH_LONG).show();
                    for (int i = 0; i < mailIds.size(); i++) {
                        String tripId = "temptrip";
                        DocumentReference ref2 = FirebaseFirestore.getInstance().collection("Trips").document(tripId).collection("memberlist").document(mailIds.get(i));
                        String currentMailId = mailIds.get(i);
                        ref2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (documentSnapshot.exists()) {
                                    try {

                                        //EvenTester event1 = documentSnapshot.toObject(EvenTester.class);
                                        //Toast.makeText(EventsPage.this,list.get(0) ,Toast.LENGTH_LONG).show();
                                        //  Toast.makeText(EventsPage.this, "Name: " + event1.getName()+"bill"+event1.getBill()+ "\n" , Toast.LENGTH_SHORT).show();
                                        //+ "cb: " + event1.getBill()+ "pb: "+event1.paidBy.get("Yutika")
                                        //+ "pb: "+event1.paidBy.get("Yutika").toString()
                                        String paid = documentSnapshot.getString("paid");
                                        String toPay = documentSnapshot.getString("toPay");
                                        members.put(currentMailId, new ArrayList<Double>());
                                        ArrayList<Double> pp = members.get(currentMailId);
                                        pp.add(Double.parseDouble(paid));
                                        pp.add(Double.parseDouble(toPay));
                                        members.put(currentMailId, pp);

                                    } catch (Exception e) {
                                        // Toast.makeText(EventsPage.this, "NAy banla object", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    //Toast.makeText(EventsPage.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(EventsPage.this, "Error!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                ;
            }

        });
        return members;
    }

    public void updatetoPay(String trip_id, String mailId, String toPay) {

        DocumentReference ref = FirebaseFirestore.getInstance().collection("Trips").document(trip_id).collection("memberlist").document(mailId);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    try {

                        String past_toPay = documentSnapshot.getString("toPay");
                        double update_toPay_num= Double.parseDouble(past_toPay)+Double.parseDouble(toPay);
                        String update_toPay= String.valueOf(update_toPay_num);
                        ref.update("toPay", update_toPay);
                    }catch (Exception e){

                    }
                }

            }
        });
    }

    public void updatePaid(String trip_id, String mailId, String paid) {


        DocumentReference ref = FirebaseFirestore.getInstance().collection("Trips").document(trip_id).collection("memberlist").document(mailId);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    try {

                        String past_paid = documentSnapshot.getString("paid");
                        double update_paid_num= Double.parseDouble(past_paid)+Double.parseDouble(paid);
                        String update_paid= String.valueOf(update_paid_num);
                        ref.update("paid", update_paid);
                    }catch (Exception e){

                    }
                }

            }
        });
    }
}