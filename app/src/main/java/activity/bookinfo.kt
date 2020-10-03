package activity

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.ams.bookhub.R
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import database.BookData
import database.BookEntitiy
import kotlinx.android.synthetic.main.recdashboard.*
import model.Book
import org.json.JSONObject
import util.ConnectionManager
import java.lang.Exception

class bookinfo : AppCompatActivity() {
    lateinit var bookname: TextView
    lateinit var bookauth: TextView
    lateinit var bookprice: TextView
    lateinit var bookrate: TextView
    lateinit var bookimg: ImageView
    lateinit var bookdecs: TextView
    lateinit var addtofav: Button
    lateinit var progresbar: ProgressBar
    lateinit var prolayout: RelativeLayout

    lateinit var toolbar: Toolbar

    var bookID: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookinfo)
        bookname = findViewById(R.id.txt1)
        bookauth = findViewById(R.id.txt2)
        bookprice = findViewById(R.id.txt3)
        bookrate = findViewById(R.id.bookrate)
        bookimg = findViewById(R.id.imgBookImage)
        bookdecs = findViewById(R.id.txtbookdec)
        addtofav = findViewById(R.id.addtofavbtn)
        progresbar = findViewById(R.id.progbar)
        progresbar.visibility = View.VISIBLE
        prolayout = findViewById(R.id.prlayout)
        prolayout.visibility = View.VISIBLE


        toolbar = findViewById(R.id.toolid)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"

        if (intent != null) {
            bookID = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(this@bookinfo, "some error occurred", Toast.LENGTH_LONG).show()
        }
        if (bookID == "100") {
            finish()
            Toast.makeText(this@bookinfo, "some error occurred", Toast.LENGTH_LONG).show()
        }
        val queue = Volley.newRequestQueue(this@bookinfo)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookID)
        if (ConnectionManager().checkconnectivity(this@bookinfo)) {
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    try {

                        val success = it.getBoolean("success")
                        if (success) {
                            prolayout.visibility = View.GONE
                            val bookJsonObject = it.getJSONObject("book_data")
                            val imgurl=bookJsonObject.getString("image")
                            Picasso.get().load(bookJsonObject.getString("image"))
                                .error(R.drawable.book)
                                .into(imgBookImage)
                            bookname.text = bookJsonObject.getString("name")
                            bookauth.text = bookJsonObject.getString("author")
                            bookprice.text = bookJsonObject.getString("price")
                            bookrate.text = bookJsonObject.getString("rating")
                            bookdecs.text = bookJsonObject.getString("description")

                            val bookentity=BookEntitiy(
                                bookID?.toInt()as Int,
                                bookname.text.toString(),
                                bookauth.text.toString(),
                                bookprice.text.toString(),
                                bookrate.text.toString(),
                                bookdecs.text.toString(),
                                imgurl
                            )

                            val checkfav=dbasynctask(applicationContext,bookentity,1).execute()
                            val isfav= checkfav.get()
                            if(isfav){
                                addtofav.text="Remove from fav"
                                val favcolor=ContextCompat.getColor(applicationContext,R.color.favcolor)
                                addtofav.setBackgroundColor(favcolor)

                            }
                            else
                            {
                                addtofav.text="Add to fav"
                                val favcolor=ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                addtofav.setBackgroundColor(favcolor)

                            }
                            addtofav.setOnClickListener{

                                if(!dbasynctask(applicationContext,bookentity,1).execute().get()){
                                    val async=dbasynctask(applicationContext,bookentity,2).execute()
                                    val result=async.get()
                                    if(result)
                                    {
                                        Toast.makeText( this@bookinfo,"added to fav",Toast.LENGTH_LONG).show()
                                        addtofav.text="Remove from fav"
                                        val favcolor=ContextCompat.getColor(applicationContext,R.color.favcolor)
                                        addtofav.setBackgroundColor(favcolor)

                                    }
                                    else{
                                        Toast.makeText( this@bookinfo,"Error",Toast.LENGTH_LONG).show()
                                    }
                                }
                                else{
                                    val async=dbasynctask(applicationContext,bookentity,3).execute()
                                    val result=async.get()
                                    if(result)
                                    {
                                        Toast.makeText( this@bookinfo,"Removed  from  fav",Toast.LENGTH_LONG).show()
                                        addtofav.text="add to fav"
                                        val favcolor=ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                        addtofav.setBackgroundColor(favcolor)

                                    }
                                    else{
                                        Toast.makeText( this@bookinfo,"Error",Toast.LENGTH_LONG).show()
                                    }
                                }
                            }



                        } else {
                            Toast.makeText(this@bookinfo, "some error occured", Toast.LENGTH_LONG)
                                .show()

                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@bookinfo, "some error occured", Toast.LENGTH_LONG)
                            .show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@bookinfo, "Volley error occured", Toast.LENGTH_LONG).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["content_type"] = "application/json"
                        headers["token"] = "c5133deb06fcbf"
                        return headers

                    }
                }
            queue.add(jsonRequest)
        } else {
            val dialog = AlertDialog.Builder(this@bookinfo)
            dialog.setTitle("ERROR")
            dialog.setMessage("device not connected to internet")
            dialog.setPositiveButton("OPEN SETTINGS") { text, listner ->
                val settingintent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingintent)
                finish()
            }.show()
            dialog.setNegativeButton("EXIT") { text, listner ->
                ActivityCompat.finishAffinity(this@bookinfo)
            }
            dialog.create()
            dialog.show()

        }

    }
    class dbasynctask(val context: Context,val bookEntitiy: BookEntitiy,val mode:Int):AsyncTask<Void,Void,Boolean>(){
        /*
        mode1 check db is book is fav or not
        mode2  add to fav
        mode3 remove from fav
         */

        val db=Room.databaseBuilder(context,BookData::class.java,"books-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
         when(mode){
             1 ->{
                 val book:BookEntitiy?=db.bookDao().getBookbyid(bookEntitiy.book_id.toString())
                 db.close()
                 return book!=null

             }
             2 ->{
                 db.bookDao().insertBook(bookEntitiy)
                 db.close()
                 return true
             }
             3 ->{
                 db.bookDao().deleteBook(bookEntitiy)
                 db.close()
                 return true

             }

         }
            return false
        }

    }
}