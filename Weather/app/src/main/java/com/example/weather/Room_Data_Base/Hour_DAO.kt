package com.example.weather.Room_Data_Base


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface Hour_DAO {


/**that will run Querrey and return All record in Hours_model Entity**/
@Query("SELECT * FROM Hours_Model")
fun ReadALL():List<Hours_Model>


/** that will run Querrey and Delete All record in Hours_model Entity**/
@Query("DELETE FROM Hours_Model")
fun DeleteALL()


/**that will Insert List of new Hours_model object in Hours_model Entity**/
@Insert(onConflict = OnConflictStrategy.REPLACE)
fun InsertAll(list:List<Hours_Model>)


}