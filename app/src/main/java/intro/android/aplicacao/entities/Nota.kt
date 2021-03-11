package intro.android.aplicacao.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nota_tabela")

class Nota (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "titulo") val titulo: String,
    @ColumnInfo(name = "conteudo") val conteudo: String
)

