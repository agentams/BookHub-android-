package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ams.bookhub.R
import com.squareup.picasso.Picasso
import database.BookEntitiy

class favrecadapt(val context :Context,val bookList:List<BookEntitiy>) :RecyclerView.Adapter<favrecadapt.Favviewholder>(){
  
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Favviewholder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_fav,parent,false)
        return Favviewholder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size

    }

    override fun onBindViewHolder(holder: Favviewholder, position: Int) {
       val book=bookList[position]
        holder.bookname.text = book.bookname
        holder.bookauth.text = book.bookauth

        holder.bookprice.text = book.bookprice
        holder.bookrating.text = book.bookrate
        Picasso.get().load(book.bookimg).error(R.drawable.ic_launcher_foreground).into(holder.imgofbook)
    }
    class Favviewholder(view:View):RecyclerView.ViewHolder(view)
    {
        val bookname: TextView =view.findViewById(R.id.txtFavBookTitle)
        val bookauth: TextView =view.findViewById(R.id.txtFavBookAuthor)
        val bookprice: TextView =view.findViewById(R.id.txtFavBookPrice)
        val bookrating: TextView =view.findViewById(R.id.txtFavBookRating)
        val imgofbook: ImageView =view.findViewById(R.id.imgFavBookImage)
        val llcontent: LinearLayout = view.findViewById(R.id.llFavContent)
    }

}