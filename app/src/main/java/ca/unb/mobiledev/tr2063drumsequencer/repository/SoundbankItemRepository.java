package ca.unb.mobiledev.tr2063drumsequencer.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import ca.unb.mobiledev.tr2063drumsequencer.dao.SoundbankItemDao;
import ca.unb.mobiledev.tr2063drumsequencer.db.AppDatabase;
import ca.unb.mobiledev.tr2063drumsequencer.entity.SoundbankItem;

public class SoundbankItemRepository {
    private final SoundbankItemDao itemDao;
    private final String TAG = "REPO";

    public SoundbankItemRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        itemDao = db.itemDao();
    }

    public LiveData<List<SoundbankItem>> listAllItems() {
        Future<LiveData<List<SoundbankItem>>> items = null;
        items = AppDatabase.databaseWriterExecutor.submit(new Callable<LiveData<List<SoundbankItem>>>() {
            @Override
            public LiveData<List<SoundbankItem>> call() throws Exception {
                return itemDao.listAllItems();
            }
        });
        try {
            return items.get();
        }
        catch (Exception e) {return null; }

    }

    public void insertRecord(final SoundbankItem item) {
        AppDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() { itemDao.insert(item);}
        });
    }

    public void deleteRecord(final SoundbankItem item) {
        AppDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() { itemDao.delete(item);}
        });
    }

    public LiveData<Integer> countItems() {
        Future<LiveData<Integer>> count = AppDatabase.databaseWriterExecutor.
                submit(new Callable<LiveData<Integer>>() {
            @Override
            public LiveData<Integer> call() throws Exception {
                return itemDao.countItems();
            }
        });
        try {
            return count.get();
        }
        catch (Exception e) {return null; }
    }
}
