package db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by root on 08/11/18.
 */
@Dao
public interface FloraDao {
    @Query("SELECT * FROM flora")
    List<Flora> getAll();

    @Query("SELECT * FROM flora LIMIT 50")
    List<Flora> getLimit50();

    @Query("SELECT * FROM flora LIMIT 50 OFFSET :limit")
    List<Flora> getContinueLimit(int limit);

    //Example Custum Query
    @Query("SELECT * FROM flora WHERE instr(lower(nama_flora), lower(:namaFlora)) > 0 LIMIT 50")
    List<Flora> findByNamaFlora(String namaFlora);

    @Query("SELECT * FROM flora WHERE instr(lower(nama_latin), lower(:namaLatin)) > 0 LIMIT 50")
    List<Flora> findByNamaLatin(String namaLatin);

    @Insert
    void insertAll(List<Flora> flora);
//
//    @Delete
//    public void deleteUsers(Flora... users);
}
