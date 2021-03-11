package intro.android.aplicacao.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import intro.android.aplicacao.dao.NotaDao
import intro.android.aplicacao.entities.Nota
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Nota::class), version = 1, exportSchema = false)
public abstract class NotaDB : RoomDatabase() {

    abstract fun notaDao(): NotaDao

    companion object{
        @Volatile
        private var INSTANCE: NotaDB? = null

        fun getDataBase(context: Context, scope: CoroutineScope): NotaDB{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, NotaDB::class.java, "notas_database")
                    //.fallbackToDestructiveMigration()
                    .addCallback(NotaDatabaseCallback(scope))
                    .build()
                INSTANCE =instance
                return instance
            }
        }
    }

    private class NotaDatabaseCallback(private  val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database -> scope.launch {
                var  notaDao = database.notaDao()
            } }
        }
    }
}