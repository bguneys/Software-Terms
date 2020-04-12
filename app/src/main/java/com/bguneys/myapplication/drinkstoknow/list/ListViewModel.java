package com.bguneys.myapplication.drinkstoknow.list;

import com.bguneys.myapplication.drinkstoknow.database.DataRepository;
import com.bguneys.myapplication.drinkstoknow.database.Item;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListViewModel extends ViewModel {

    private final DataRepository mRepository;
    private LiveData<List<Item>> mItemList;
    private LiveData<List<Item>> mFavouriteItemList;
    private final MutableLiveData<Boolean> mIsListFavourite = new MutableLiveData<>(false);

    public ListViewModel(DataRepository dataRepository) {
        mRepository = dataRepository;
        mItemList = mRepository.getItemList();
    }

    public LiveData<List<Item>> getItemList() {
        return mItemList;
    }

    public LiveData<List<Item>> getFavouriteItemList() {
        mFavouriteItemList = mRepository.getFavouriteItemList();
        return mFavouriteItemList;
    }

    public void insert(Item item) {
        mRepository.insert(item);
    }

    public LiveData<Boolean> isListFavourite() {
        return mIsListFavourite;
    }

    public void toggleFavouriteList() {
        Boolean isFavorite = mIsListFavourite.getValue();

        if (isFavorite != null) {
            mIsListFavourite.setValue(!isFavorite);
        }
    }

}
