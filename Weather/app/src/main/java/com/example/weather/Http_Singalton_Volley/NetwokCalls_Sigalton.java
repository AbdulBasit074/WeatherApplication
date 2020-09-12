package com.example.weather.Http_Singalton_Volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/*** Volley Http Network calls Handling Class **/

public class NetwokCalls_Sigalton {



    /**Single Instance (Object) will Handle all Network calls **/
    private static NetwokCalls_Sigalton Singalton_Instance;

    /**All Request will be in Request Queqe**/
    private RequestQueue Network_Calls_Request_Q;

    /**Context **/
    private static Context Context_weatherUpdate;


    /**Constructor which will create Request Que**/
    private NetwokCalls_Sigalton(Context context)
    {
        Context_weatherUpdate = context ;
        Get_Network_Request_Q();//request que will create
    }
    /** Initilize Instance (Object) Once which will manage All network calls**/
    public static synchronized NetwokCalls_Sigalton  Get_Singalton_Instance(Context c)
    {

        if(Singalton_Instance==null)
        {
            Singalton_Instance = new NetwokCalls_Sigalton(c);
        }
        return Singalton_Instance;
    }

    /**Call from by Constructor it will Create Request Queue Once And All request will Collect in this Queue**/
    private RequestQueue Get_Network_Request_Q()
    {
        if(Network_Calls_Request_Q==null)
        {
            Network_Calls_Request_Q = Volley.newRequestQueue(Context_weatherUpdate);
        }

        return Network_Calls_Request_Q;
    }
    /**Function Add Request In RequestQueue**/
    public void AddtoNetworkRequestQ(Request request)
    {
        Network_Calls_Request_Q.add(request);
    }


}
