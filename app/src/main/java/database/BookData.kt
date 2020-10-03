package database


import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [BookEntitiy::class],version = 1)
abstract  class BookData:RoomDatabase() {
    abstract  fun bookDao():bookdao
}