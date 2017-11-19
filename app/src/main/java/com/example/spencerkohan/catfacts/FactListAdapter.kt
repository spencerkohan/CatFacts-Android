package com.example.spencerkohan.catfacts
import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.cell_layout.view.*


class RecyclerAdapter(private val facts: ArrayList<FactModel>, activity: Activity) : RecyclerView.Adapter<RecyclerAdapter.FactHolder>(), View.OnClickListener {

    private val activityContext: Activity = activity

    override fun getItemCount() = facts.size

    override fun onBindViewHolder(holder: RecyclerAdapter.FactHolder, position: Int) {
        val fact = facts[position]
        holder.setFact(fact.fact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.FactHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellView = layoutInflater.inflate(R.layout.cell_layout, null)
        cellView.setOnClickListener(this)
        return FactHolder(cellView)
    }

    override fun onClick(v: View) {
        val fact = v.contentText.text
        val intent = Intent(android.content.Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Cat Fact")
        intent.putExtra(android.content.Intent.EXTRA_TEXT, fact)
        activityContext.startActivity(intent)
    }

    class FactHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun setFact(fact: String) {
            view.contentText.text = fact
        }
    }

}