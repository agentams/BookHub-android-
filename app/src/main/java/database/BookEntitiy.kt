package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="books")
data class BookEntitiy(
    @PrimaryKey val book_id:Int,
    @ColumnInfo(name="book_name") val bookname:String,
    @ColumnInfo(name="book_auth")val bookauth:String,
    @ColumnInfo(name="book_price")val bookprice:String,
    @ColumnInfo(name="book_rate")val bookrate:String,
    @ColumnInfo(name="book_decs") val bookdecs:String,
    @ColumnInfo(name="book_img") val bookimg:String
)


