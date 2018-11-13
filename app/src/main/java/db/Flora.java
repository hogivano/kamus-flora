package db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by root on 08/11/18.
 */
@Entity
public class Flora implements Serializable{
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "nama_flora")
    String namaFlora;
    @ColumnInfo(name = "nama_latin")
    String namaLatin;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNamaFlora() {
        return namaFlora;
    }
    public void setNamaFlora(String namaFlora) {
        this.namaFlora = namaFlora;
    }

    public String getNamaLatin() {
        return namaLatin;
    }

    public void setNamaLatin(String namaLatin) {
        this.namaLatin = namaLatin;
    }
}
