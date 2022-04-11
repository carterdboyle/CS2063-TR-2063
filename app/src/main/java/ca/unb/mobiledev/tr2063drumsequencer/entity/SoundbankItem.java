package ca.unb.mobiledev.tr2063drumsequencer.entity;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "soundbank_item_table")
public class SoundbankItem {

    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String res1;
    String res2;
    String res3;
    String res4;

    public int getId() { return id; }

    public String getName() {return name; }

    public String getRes1() {return res1; }
    public String getRes2() {return res2; }
    public String getRes3() {return res3; }
    public String getRes4() {return res4; }

    public void setId(int id) {this.id = id; }
    public void setName(String name) {this.name = name; }
    public void setRes1(String res1) {this.res1 = res1; }
    public void setRes2(String res2) {this.res2 = res2; }
    public void setRes3(String res3) {this.res3 = res3; }
    public void setRes4(String res4) {this.res4 = res4; }

}
