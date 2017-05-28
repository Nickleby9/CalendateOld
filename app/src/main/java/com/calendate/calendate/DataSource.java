package com.calendate.calendate;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hilay on 28-מאי-2017.
 */

public class DataSource {

    static ArrayList<Item> items = new ArrayList<>();


    public interface OnDataArrivedListener {
        void onDataArrived(ArrayList<Item> items, Exception e);
    }

    public static void fetchItems(final OnDataArrivedListener listener){
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //import database
                for (int i = 0; i < 10; i++) {
                    Item item = new Item("Example number " + i);
                    listener.onDataArrived(items,null);
                    items.add(item);
                }
            }
        });
        thread.start();
    }

    public static ArrayList<Item> getItems() {
        return items;
    }
}
