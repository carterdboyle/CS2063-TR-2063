package ca.unb.mobiledev.tr2063drumsequencer.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.unb.mobiledev.tr2063drumsequencer.entity.SoundbankItem;

@Dao
public interface SoundbankItemDao {
    @Query("SELECT * FROM soundbank_item_table ORDER BY id ASC")
    LiveData<List<SoundbankItem>> listAllItems();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SoundbankItem item);

    @Delete
    void delete(SoundbankItem item);

    @Query("SELECT COUNT(id) FROM soundbank_item_table")
    LiveData<Integer> countItems();
}
