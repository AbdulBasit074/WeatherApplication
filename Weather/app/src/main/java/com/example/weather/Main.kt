package com.example.weather

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.example.weather.Http_Singalton_Volley.NetwokCalls_Sigalton
import com.example.weather.Room_Data_Base.Hours_Model
import com.example.weather.Room_Data_Base.Room_Data_Base
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.main.*
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Main : AppCompatActivity() {


    /*** DECLARATION*/
    private lateinit var arrayList: ArrayList<Hours_Model>

    /*** COROUTINES TO RUN BACKGROUND PROCESS*/
    private var scope1 = CoroutineScope(Dispatchers.IO)
    private var scope2 = CoroutineScope(Dispatchers.IO)
    private var scope3 = CoroutineScope(Dispatchers.IO)

    /*** FOR LOCATION*/
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationAvalaible: LocationManager
    private lateinit var longitude: String
    private lateinit var latituede: String

    /*** Room DataBase*/
    private lateinit var roomDa: Room_Data_Base

    /*** OTHERS*/
    private lateinit var aDobter: List_Adobter
    private lateinit var list: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        initilze()

        /*** Check Data in Room DataBase*/
        checKDataBaseData(roomDa)


        /*** On Update Click ***/
        refresh.setOnClickListener(View.OnClickListener {
            getNEwUPDATE()
        })

}
    private fun initilze() {
        arrayList = ArrayList()            //Initilize ArrayList
        list = findViewById(R.id.show) //List View
        aDobter =
            List_Adobter(this, R.layout.list_row, arrayList) //Set aDobter with layout and ArrayList
        list.adapter = aDobter       //set adobter to the
        locationClient =
            LocationServices.getFusedLocationProviderClient(this)//Get Google Service Location Client Initilization
        roomDa =
            Room_Data_Base.GetRoomDataBaseInstance(this) as Room_Data_Base //Get Roomdatebase Instance

    }

        /*** NEW_UPDATE***/
    private fun getNEwUPDATE()
    {
        /*** Checking Location Permission For App*/
        when(ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED){

            true->
            {
                /*** Permission is Allow Then Checking Device LOCATION (GPS) Status*/
                    when(locationAvailable())
                    {

                        true->
                        {
                            Glide.with(applicationContext).load(R.drawable.refresh).into(refresh)//Loading gif
                            fetchingLocation()
                        }
                        else->
                        { /*** Dialog to enable or not device location*/
                                           onGPSdIALOG() }
                    }

            }
            else->
            {
                /*** Permission is Not-Allow Request for Permission
                 * Request For Fine Location else of COARSE LOCAIION*/
                  ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 20)
            }

        }
    }


    /************* GETTING LOCATION THROUGH GOOGLE PLAY SERVICE CLIENT*****************************/
    private fun fetchingLocation() {

            var Request_Location = LocationRequest()
            Request_Location.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            Request_Location.interval = 0 //Time interval
            Request_Location.fastestInterval = 0 //Fastest response interval
            Request_Location.numUpdates = 1 //Number of  update in time interval

            //Google location Client GET LOCATION UPDATE  THROUGH CALLBACK Method
            locationClient.requestLocationUpdates(Request_Location,callBackLocation, Looper.myLooper())
    }


    /******* After Getting Location call back method On LocationResult Call ***********************/

    private var callBackLocation =object : LocationCallback()
    {
        override fun onLocationResult(loc: LocationResult?) {
            latituede =   loc?.lastLocation?.latitude.toString()
            longitude =   loc?.lastLocation?.longitude.toString()
            fetchingWeatherDetail()
        }
    }

    /************************* FETCHING DATA FROM OPEN WEATHER API ********************************/

    private fun fetchingWeatherDetail() {

        /*** RESPONSE urL **/
        val urL:String = "https://api.openweathermap.org/data/2.5/forecast?&units=metric&lat="+latituede+"&lon="+longitude+"&APPID=12a49f88d84b2c365e153a65fb644cee"

        /*** HTTP REQUEST THROUGH VOLLEY LIBRARY ***/
        val jSONoBJ = JsonObjectRequest(Request.Method.POST,urL,null, Response.Listener { response ->
            try {

                 arrayList.removeAll(arrayList)
                 var singleDetail:JSONObject
                 var gETMAIN:JSONObject
                 var gETWEATHERARRAY:JSONArray
                 var gETWIND:JSONObject
                 var gETCITYOBJ:JSONObject
                 var mAinArray:JSONArray
                 var date1 :String
                 var obj:Hours_Model
                 var dateString:String
                 var date:String
                 var date_time:String
                 var time:String
                /**** DETAIL FOR ALL ATTRIBUTE FROM JSON RESPONSE ***/
                gETCITYOBJ  =  response.getJSONObject("city")

                mAinArray   = response.getJSONArray("list")

                for (i in 0 until mAinArray.length()-1)
                {

                    singleDetail = mAinArray.getJSONObject(i)
                    gETWEATHERARRAY    =    singleDetail.getJSONArray("weather")

                    /*** Getting main objects that contains some attribtes(Temperature detail)and store in objects attribute****/
                    gETMAIN     =     singleDetail.getJSONObject("main")

                    /*** Getting Winds Detail From Object*******/
                    gETWIND    =     singleDetail.getJSONObject("wind")

                    /*** Getting Date and time Detail From Object*****/
                    date_time = singleDetail.get("dt_txt").toString()
                     date = date_time.split(" ")[0].trim()
                      time = date_time.split(" ")[1].trim()

                    /***The Receiving date format is in GMT we want to get locale GMT**/
                     dateString      =  date+"T"+time+".747Z" // considered GMT
                      date1          =  getLocalStringFormat(dateString)



                    /** SAVE DATA IN OBJECT AND SAVE OBJECT IN ARRAY LIST ***/

                    obj = Hours_Model(singleDetail.get("dt").toString()
                        ,gETMAIN.get("temp").toString(),//Temperature
                        date1.split(" ")[0],//day
                        date1.split(" ")[1],//date
                        date1.split(" ")[2],//time
                        gETCITYOBJ.getString("name"),//city name
                        gETMAIN.get("humidity").toString(),//humadity
                        gETWIND.getString("speed"),// wind speed
                        gETWIND.getString("deg"),//wind degree
                        gETWEATHERARRAY.getJSONObject(0).get("main").toString(),// weather main
                        gETMAIN.get("pressure").toString(),//pressure
                        gETWEATHERARRAY.getJSONObject(0).get("description").toString(),//weater description
                        gETWEATHERARRAY.getJSONObject(0).get("icon").toString(),//icon
                        gETMAIN.get("sea_level").toString(),//sea level
                        gETMAIN.get("grnd_level").toString())// grand level

                        arrayList.add(obj)//Saving objects in array

                }

                /********* UPDATE ROOM DATABASE AND ARRAY ADOBTER TO REFRESH LIST ******/
                scope2.launch {
                    onNewUpdate(arrayList,roomDa)
                }
            }
            catch (e: JSONException)
            {
                Glide.with(this).load(R.drawable.ic_refresh_black_24dp).into(refresh)
                alertDialongHandling("Error",R.drawable.ic_error_black_24dp,"Try Again")
            }

        }, Response.ErrorListener { error ->

            when(error)
            {

                is NetworkError ,is NoConnectionError  ->
                {
                    alertDialongHandling("Error",R.drawable.ic_error_black_24dp,"No Internet Connection")

                }
                is TimeoutError->
                {
                    alertDialongHandling("Time Out",R.drawable.ic_update_black_24dp,"Try Again")
                }
                is ServerError->
                {
                    alertDialongHandling("Server Error",R.drawable.ic_update_black_24dp,"Try Again")
                }
                else->
                {
                    alertDialongHandling("Error",R.drawable.ic_update_black_24dp,"Try Again")
                }


            }
            Glide.with(this).load(R.drawable.ic_refresh_black_24dp).into(refresh)
        })


        /*** PASS REQUEST TO REQUEST QUEUE OF VOLLEY ***/
        NetwokCalls_Sigalton.Get_Singalton_Instance(this).AddtoNetworkRequestQ(jSONoBJ)

    }


    /*** DEVICE LOCATION STATUS FUNCTION*/
    private fun locationAvailable(): Boolean////////////GPS STATUS FUNCTION///////////////////////
    {
        locationAvalaible = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationAvalaible.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /***************ON PERMISSION RESULT ********/
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,grantResults: IntArray)
    {
        when(requestCode)
        {
            20->
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    getNEwUPDATE()
                }
        }
    }

    /************ ON ACTIVITY RESULT ********/
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode)
        {
            21->
                when(locationAvalaible .isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    true->
                    {
                        getNEwUPDATE()//Call to get data
                    }
                    else->
                    {
                        Toast.makeText(this, "Unable to update", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
    /*** HANDLE ALL ALERT DIALOG*******/

    private fun alertDialongHandling(title:String,icon:Int,msg:String)
    {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setIcon(icon)
            .setPositiveButton("Ok"){_,_ -> }.show()
    }


    /*** REQUEST TO ENABLE LOCATION*/
    private fun onGPSdIALOG()
    {

        /*** Allert Dialog to Enable device Location*/
        AlertDialog.Builder(this)
            .setTitle("Enable")
            .setMessage("Google Play Service Location")
            .setIcon(R.drawable.ic_location_on_black_24dp)
            .setPositiveButton("Ok"){_,_ ->
                /*** Call Implicit activity to enable device location*/
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),21)
            }
            .setNegativeButton("No Thanks"){_,_ ->
                Toast.makeText(this, "No", Toast.LENGTH_LONG).show()
            }.show()
    }




    /*** GET GMT ON LOCAL BASE CONVERSION FROM STRING TO DATE***/
  private fun getDateLocalTimeStamp(dat:String): Date? {
       val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.getDefault())
       dateFormat.timeZone = TimeZone.getTimeZone("GMT")
       try
       { return dateFormat.parse(dat) }
       catch (e: Exception) { return null }
   }

    /*** WILL PROVICE PROPER LOCAL GMT IN STRING FORMATE ***/
   private fun getLocalStringFormat(dat1:String): String {
       var cDate:Date = getDateLocalTimeStamp(dat1) as Date
        var dateFormat = SimpleDateFormat("EEE dd/MMM/yyyy HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault()
        return dateFormat.format(cDate)
    }



    /**************************************ALL COROUTINES FUNCTION*********************************/

    private fun checKDataBaseData(ref: Room_Data_Base) {
        /*** Check data in Room data base through backgroud process*/
        scope1.launch {
            checkPreviousUpdate(ref)
        }

    }
    private suspend fun checkPreviousUpdate(ref:Room_Data_Base) {

        /*** DATABase Is Empty If Condition will Run Other Wise Else*/

        if (ref.Hour_DAO_INTERFACE().ReadALL().isEmpty())
        {
            /*** RoomData Base is Empty then Request for Fetching new update from main thread*/
            withContext(Dispatchers.Main)
            {
                getNEwUPDATE()
            }
        }
        else
        {
            scope3.async {

                /*** Read Data from Room Database and Store Data in ArrayList and Notify to aDobter to update List*/

                var s = readAllData(ref)
                withContext(Dispatchers.Main)
                {

                    arrayList.addAll(s);//List to ArrayList
                    city?.setText(arrayList.get(0).city) //Set Last Update City Name
                    day?.setText(arrayList.get(0).day.toUpperCase()) //Set the Last update Day
                    aDobter.notifyDataSetChanged()

                }

            }
        }

    }

    private  fun readAllData(ref: Room_Data_Base):List<Hours_Model>
    {
        return ref.Hour_DAO_INTERFACE().ReadALL()
    }
/************************************ UPDATE ROOM DATA BASE *************************************/
private suspend fun onNewUpdate(ArtList:ArrayList<Hours_Model>,ref:Room_Data_Base)
{
    var ss:List<Hours_Model> = ArtList


        if(ref.Hour_DAO_INTERFACE().ReadALL().isEmpty())
        {
            /*** IF ROOM DATABASE(Hours_Model Table) is Empty then inseart all new data*/
            ref.Hour_DAO_INTERFACE().InsertAll(ss)
            withContext(Dispatchers.Main)
            {
                city.setText(arrayList.get(0).city)
                day.setText(arrayList.get(0).day.toUpperCase()) //Set the Last update Day
                Glide.with(applicationContext).load(R.drawable.ic_refresh_black_24dp).into(refresh)
                aDobter.notifyDataSetChanged()
            }
        }
        else
        {

            /*** DELETE ALL PREVIOUS RECORDS AND UPDATE WITH NEW RECORDS*/
            val One = scope3.async(Dispatchers.IO) { deleteAll(ref) }
                if(One.await())
                {
                    ref.Hour_DAO_INTERFACE().InsertAll(ss)
                    withContext(Dispatchers.Main){
                        Toast.makeText(applicationContext, "Updated", Toast.LENGTH_LONG).show()
                        city.setText(arrayList.get(0).city)
                        day.setText(arrayList.get(0).day.toUpperCase()) //Set the Last update Day
                        Glide.with(applicationContext).load(R.drawable.ic_refresh_black_24dp).into(refresh)
                        aDobter.notifyDataSetChanged()
                    }
                }
        }
    }
    /*** DELETE ALL DETAIL FROM Hour_Model Table **/
    private fun  deleteAll(ref: Room_Data_Base):Boolean
    {
        ref.Hour_DAO_INTERFACE().DeleteALL()
        return true
    }


}