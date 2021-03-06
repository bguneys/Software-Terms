package com.bguneys.myapplication.drinkstoknow.database;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class DataRepository {

    //fields
    private ItemDao mItemDao;
    private LiveData<List<Item>> mItemList;
    private LiveData<List<Item>> mFavouriteItemList;
    private LiveData<List<Item>> mItemListByGroup;
    private static volatile DataRepository sInstance = null;

    public static DataRepository getInstance(Context context) {

        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(context);
                }
            }
        }

        return sInstance;
    }

    private DataRepository(Context context) {
        ItemDatabase itemDatabase = ItemDatabase.getInstance(context);
        mItemDao = itemDatabase.getItemDao();
        mItemList = mItemDao.getItemList();
    }

    public LiveData<List<Item>> getFavouriteItemList() {
        mFavouriteItemList = mItemDao.getFavouriteItemList();
        return mFavouriteItemList;
    }

    public LiveData<List<Item>> getItemListByGroup(String group) {
        mItemListByGroup = mItemDao.getItemListByGroup(group);
        return  mItemListByGroup;
    }

    public LiveData<Item> getItemWithId(int id) {
        return mItemDao.getItemWithId(id);
    }

    public LiveData<List<Item>> getItemsWithName(String name) {
        return mItemDao.getItemsWithName(name);
    }

    @Nullable
    public Item getRandomItem() {
        try {
            return (Item) ItemDatabase.databaseExecutor.submit(new Callable() {
                public final Object call() {
                    return mItemDao.getRandomItem();
                }
            }).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public Item getNextItemWithId(final int id) {
        try {
            return (Item) ItemDatabase.databaseExecutor.submit(new Callable() {
                public final Object call() {
                    return mItemDao.getNextItemWithId(id);
                }
            }).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void updateFavorite(final Item item) {
        ItemDatabase.databaseExecutor.execute(new Runnable() {

            public final void run() {
                mItemDao.updateFavorite(item);
            }
        });
    }
}
