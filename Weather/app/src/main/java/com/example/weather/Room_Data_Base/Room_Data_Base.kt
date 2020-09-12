package com.example.weather.Room_Data_Base


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [Hours_Model::class],version = 1)
abstract class  Room_Data_Base: RoomDatabase(){

    /*** Interface value to access Interface Operation*/
    abstract fun Hour_DAO_INTERFACE():Hour_DAO


    /*** This Companion Object work as Static in Kotlin it will Follow as Sigalton Design Pattern to Create Instance of Database*/

    companion object{
        private var instance:Room_Data_Base?=null
        fun GetRoomDataBaseInstance(context:Context):RoomDatabase?{

            if(instance == null)
            {
                synchronized(Room_Data_Base::class)
                {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        Room_Data_Base::class.java,
                        "WEATHER"
                    ).build()
                }
            }
            return instance

        }

    }

}