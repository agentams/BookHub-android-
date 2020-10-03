package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface bookdao
{
    @Insert
    fun insertBook(bookEntity:BookEntitiy)
    @Delete
    fun deleteBook(bookEntity:BookEntitiy)

    @Query("SELECT*FROM books")
    fun getAllBook():List<BookEntitiy>

    @Query("SELECT*FROM books WHERE book_id=:bookID")
    fun getBookbyid(bookID:String):BookEntitiy
}