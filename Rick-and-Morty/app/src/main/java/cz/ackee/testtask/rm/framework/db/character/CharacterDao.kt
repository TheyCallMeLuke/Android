package cz.ackee.testtask.rm.framework.db.character

import androidx.room.*

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character")
    fun getAll(): List<CharacterDbEntity>

    @Query("SELECT EXISTS(SELECT * FROM character WHERE id = :id)")
    fun exists(id: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(characterDbEntity: CharacterDbEntity)

    @Delete
    fun delete(characterDbEntity: CharacterDbEntity)
}