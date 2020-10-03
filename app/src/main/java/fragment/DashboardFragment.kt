package fragment

import adapter.DashboardRecycler
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ams.bookhub.R
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_dashboard.*
import model.Book
import org.json.JSONException
import util.ConnectionManager
import java.lang.Exception
import java.util.*
import java.util.Arrays.sort
import java.util.Collections.sort
import kotlin.Comparator
import kotlin.collections.HashMap


class DashboardFragment : Fragment() {
    lateinit var recdash:RecyclerView
    lateinit var  layoutmanager:RecyclerView.LayoutManager
   // lateinit var btncheck:Button
     var bookInfoList=arrayListOf<Book>()
    lateinit var progressbar:ProgressBar
    lateinit var progresslayout:RelativeLayout
   /* val bookInfoList = arrayListOf(
        "book1",
        "book2",
        "book3",
        "book4",
        "book5",
        "book6",
        "book7",
        "book8",
        "book9",
        "book10"
    )*/
    lateinit var recadapt:DashboardRecycler
    var ratecompartor= Comparator<Book>{book1,book2->
        if(book1.bookrate.compareTo(book2.bookrate,true)==0)
        {
            book1.bookname.compareTo(book2.bookname,true)

        }else
        {
            book1.bookrate.compareTo(book2.bookrate,true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


      val view= inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)
        recdash=view.findViewById(R.id.recview)
       // btncheck=view.findViewById(R.id.chkbtn)



        progressbar=view.findViewById(R.id.probar)
        progresslayout=view.findViewById(R.id.prolayout)
        progresslayout.visibility=View.VISIBLE



      /*  btncheck.setOnClickListener{
            if (ConnectionManager().checkconnectivity(activity as Context)) {
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("sucessfull")
                dialog.setMessage("device connected to internet")
                dialog.setPositiveButton("ok") { text, listner ->
                }.show()
                dialog.setNegativeButton("cancle") { text, listner ->
                }.show()
            }
        }*/
        layoutmanager=LinearLayoutManager(activity)


        val queue = Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v1/book/fetch_books/"


        if(ConnectionManager().checkconnectivity(activity as Context)){
            val jsonObjectRequest= object : JsonObjectRequest(Request.Method.GET,url,null,Response.Listener{


                //code for response
                try{
                    progresslayout.visibility=View.GONE
                    val success= it.getBoolean("success")
                    if(success)
                    {
                        val data=it.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val bookJsonObject=data.getJSONObject(i)
                            val bookObject=
                                Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")
                                )
                            bookInfoList.add(bookObject)
                            recadapt= DashboardRecycler(activity as Context,bookInfoList)
                            recdash.adapter=recadapt
                            recdash.layoutManager=layoutmanager
                            /*recdash.addItemDecoration(
                                DividerItemDecoration(recdash.context,
                                    (layoutmanager as LinearLayoutManager).orientation
                                )
                            )*/
                        }
                    }
                    else
                    {
                        Toast.makeText(activity as Context,"some error occured",Toast.LENGTH_LONG).show()
                    }
                }catch (e:JSONException){
                    Toast.makeText(activity as Context,"some error occured while fetching data from server!!!!",Toast.LENGTH_LONG).show()
                }


            },Response.ErrorListener {
                //code for error
                println("response is $it")
                Toast.makeText(activity as Context,"Volley error occured!!!",Toast.LENGTH_LONG).show()
            }){
                override  fun getHeaders():MutableMap<String,String>{
                    val headers = HashMap<String,String>()
                    headers["content_type"]="application/json"
                    headers["token"]="c5133deb06fcbf"
                    return headers
                }

            }
            queue.add(jsonObjectRequest)

        }
       else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("ERROR")
            dialog.setMessage("device not connected to internet")
            dialog.setPositiveButton("OPEN SETTINGS") { text, listner ->
                val settingintent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingintent)
                activity?.finish()
            }.show()
            dialog.setNegativeButton("EXIT") { text, listner ->
                ActivityCompat.finishAffinity(activity as Activity)
            }.show()
        }

        return view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.sort,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item?.itemId
        if(id==R.id.sort)
        {
            Collections.sort(bookInfoList,ratecompartor)
            bookInfoList.reverse()
        }
        recadapt.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

}