package com.example.weather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.example.weather.Room_Data_Base.Hours_Model
import kotlinx.android.synthetic.main.list_row.view.*






class List_Adobter(context: Context,var resource: Int,var  objects: ArrayList<Hours_Model>) :
    ArrayAdapter<Hours_Model>(context, resource, objects) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        /*** Setting Value On single Row*/

        val inflator:LayoutInflater = LayoutInflater.from(context)
        val view = inflator.inflate(resource,null)
        view.date_.setText(objects[position].date)
        view.time_.setText(objects[position].time+"Hr("+objects[position].day+")")
        view.temperature_.setText(objects[position].temperature+"\u2103")
        view.weather_.setText(objects[position].weather)
        view.humadity_.setText(objects[position].humadity+"%")
        view.pressure_.setText(objects[position].pressure+"hPa")
        view.description_.setText(objects[position].description)
        view.winddegree_.setText(objects[position].wind_degree+"\u00B0")
        view.windspeed_.setText(objects[position].wind_speed+"meter/sec")
        view.sea_level_.setText(objects[position].sea_level+"hPa")
        view.grandlevel_.setText(objects[position].grand_level+"hPa")

        var icon:Int = R.drawable.refresh
        when(objects[position].icon)
        {
            "01d"->
                icon = R.drawable.o1d
            "01n"->
                icon = R.drawable.o1n
            "02d","02n"->
                icon = R.drawable.o2d
            "03d","03n"->
                icon = R.drawable.o3d
            "04d","04n"->
                icon = R.drawable.o4d
            "09d","09n"->
                icon = R.drawable.o9d
            "10d","10n"->
                icon = R.drawable.l0d
            "11d","11n"->
                icon = R.drawable.lld
            "13d","13n"->
                icon = R.drawable.l3d
            "50d","50n"->
                icon = R.drawable.l3d


        }

        Glide.with(context.applicationContext).load(icon).into(view.icon_)
        return view;

    }



}