package adapter

import activity.bookinfo
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ams.bookhub.R
import com.squareup.picasso.Picasso
import java.util.ArrayList
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import model.Book


class DashboardRecycler (val context: Context , val itemList:ArrayList<Book>): RecyclerView.Adapter<DashboardRecycler.DashboardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recdashboard,parent,false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
      return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.bookname.text = book.bookname
        holder.bookauth.text = book.bookauth
        holder.bookprice.text = book.bookprice
        holder.bookrating.text = book.bookrate
      //  holder.imgofbook.setImageResource(book.bookimg)
        Picasso.get().load(book.bookimg).error(R.drawable.ic_launcher_foreground).into(holder.imgofbook)
        holder.llcontent.setOnClickListener {
            val intent= Intent(context,bookinfo::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
            //Toast.makeText(context, "clicked on ${holder.textView.text}", Toast.LENGTH_LONG).show()
    }

    }
    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val bookname:TextView=view.findViewById(R.id.txtBookName)
        val bookauth:TextView=view.findViewById(R.id.txtBookAuthor)
        val bookprice:TextView=view.findViewById(R.id.txtBookPrice)
        val bookrating:TextView=view.findViewById(R.id.txtBookRating)
        val imgofbook:ImageView=view.findViewById(R.id.imgBookImage)
        val llcontent:LinearLayout= view.findViewById(R.id.llayout)

    }

}