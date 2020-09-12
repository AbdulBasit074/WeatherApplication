package com.example.weather.Room_Data_Base

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey






@Entity(tableName = "Hours_Model")
data class Hours_Model(



    /**ALL ATTRIBUTES OF ENTITY **/

    @PrimaryKey
     var id: String,
    @ColumnInfo(name = "temperature")
    var temperature: String,
    @ColumnInfo(name = "day")
    var day: String,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "time")
    var time: String,
    @ColumnInfo(name = "city")
    var city: String,
    @ColumnInfo(name = "humadity")
    var humadity: String,
    @ColumnInfo(name = "wind_speed")
    var wind_speed: String,
    @ColumnInfo(name = "wind_degree")
    var wind_degree: String,
    @ColumnInfo(name = "weather")
    var weather: String,
    @ColumnInfo(name = "pressure")
    var pressure: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "icon")
    var icon: String,
    @ColumnInfo(name = "sea_level")
    var sea_level: String,
    @ColumnInfo(name = "grand_level")
    var grand_level: String

    )
