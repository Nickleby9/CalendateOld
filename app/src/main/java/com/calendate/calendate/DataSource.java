package com.calendate.calendate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Hilay on 28-מאי-2017.
 */

public class DataSource {



    static ArrayList<EventItem> EventItems = new ArrayList<>();


    public interface OnDataArrivedListener {
        void onDataArrived(ArrayList<EventItem> EventItems, Exception e);
    }

    public static void fetchItems(final OnDataArrivedListener listener){
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //import database
                DatabaseReference ref = mDatabase.getReference(user.getEmail());

//                for (int i = 0; i < 10; i++) {
//                    EventItem EventItem = new EventItem("Example number " + i);
//                    listener.onDataArrived(EventItems,null);
//                    EventItems.add(EventItem);
//                }
            }
        });
        thread.start();
    }

    public static ArrayList<EventItem> getEventItems() {
        return EventItems;
    }
}
