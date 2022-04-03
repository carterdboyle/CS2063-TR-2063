package ca.unb.mobiledev.tr2063drumsequencer.ui;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ca.unb.mobiledev.tr2063drumsequencer.entity.SoundbankItem;
import ca.unb.mobiledev.tr2063drumsequencer.repository.SoundbankItemRepository;

public class SoundbankItemViewModel extends AndroidViewModel {
    private final SoundbankItemRepository itemRepository;
    private LiveData<List<SoundbankItem>> items;

    public SoundbankItemViewModel(@NonNull Application application) {
        super(application);
        itemRepository = new SoundbankItemRepository(application);
    }

    // TODO
    //  Add mapping calls between the UI and Database

    public LiveData<List<SoundbankItem>> listAllItems() {
        return itemRepository.listAllItems();
    }

    public void insert (String name, String res1, String res2, String res3, String res4) {
        SoundbankItem item = new SoundbankItem();
        item.setName(name);
        item.setRes1(res1);
        item.setRes2(res2);
        item.setRes3(res3);
        item.setRes4(res4);

        itemRepository.insertRecord(item);
    }

    public void delete (SoundbankItem item) {
        itemRepository.deleteRecord(item);
    }

    public LiveData<Integer> countItems() {
        return itemRepository.countItems();
    }

}
