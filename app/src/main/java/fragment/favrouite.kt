package fragment

import adapter.favrecadapt
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.ams.bookhub.R
import database.BookData
import database.BookEntitiy


class favrouite : Fragment() {
    lateinit var recfav:RecyclerView
    lateinit var prolay:RelativeLayout
    lateinit var probar:ProgressBar
    lateinit var layoutmanager:RecyclerView.LayoutManager
    lateinit var recycleradpt:favrecadapt
    var dbBooklist= listOf<BookEntitiy>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favrouite, container, false)
 recfav=view.findViewById(R.id.favrecfrag)
        prolay=view.findViewById(R.id.profavlay)
        probar=view.findViewById(R.id.probarfav)


        layoutmanager=GridLayoutManager(activity as Context,2)

        dbBooklist=retfav(activity as Context).execute().get()

        if(activity != null){
            prolay.visibility=View.GONE
            recycleradpt= favrecadapt(activity as Context,dbBooklist)
            recfav.adapter=recycleradpt
            recfav.layoutManager=layoutmanager
        }
        return view


    }
    class retfav(val context: Context):AsyncTask<Void,Void,List<BookEntitiy>>(){
        override fun doInBackground(vararg params: Void?): List<BookEntitiy> {
            val db=Room.databaseBuilder(context,BookData::class.java,"books-db").build()
            return db.bookDao().getAllBook()
        }

    }
}