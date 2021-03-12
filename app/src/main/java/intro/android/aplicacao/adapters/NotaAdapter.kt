package intro.android.aplicacao.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import intro.android.aplicacao.R
import intro.android.aplicacao.entities.Nota

class NotaAdapter internal  constructor(context: Context) :
RecyclerView.Adapter<NotaAdapter.NotaViewHolder>(){

    private val  inflater: LayoutInflater = LayoutInflater.from(context)
    private  var notas = emptyList<Nota>()

    class NotaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val notaTitulo: TextView = itemView.findViewById(R.id.titulo)
        val notaConteudo: TextView = itemView.findViewById(R.id.conteudo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaAdapter.NotaViewHolder {
        val  itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NotaViewHolder(itemView)
    }

    override fun getItemCount() = notas.size


    override fun onBindViewHolder(holder: NotaAdapter.NotaViewHolder, position: Int) {
        val current = notas[position]
        holder.notaTitulo.text = current.titulo
        holder.notaConteudo.text = current.conteudo
    }

    internal fun setNotas(notas: List<Nota>){
        this.notas = notas
        notifyDataSetChanged()
    }

}