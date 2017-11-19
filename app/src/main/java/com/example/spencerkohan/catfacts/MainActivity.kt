package com.example.spencerkohan.catfacts

import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.os.Bundle
import android.widget.SeekBar

class MainActivity : AppCompatActivity(), DataProvider.Observer, SeekBar.OnSeekBarChangeListener {

    private var facts : ArrayList<FactModel> = ArrayList()

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter
    private lateinit var dataProvider: DataProvider

    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()

    private var seekBarMin : Int = 20
    private var seekBarMAX : Int = 200

    fun charactersForProgress(value: Int): Int {
        val range = (seekBarMAX - seekBarMin).toFloat()
        val min = seekBarMin.toFloat()
        val p : Float = value.toFloat() / 100.0f
        return ((p * range) + min).toInt()
    }

    fun progressForCharacters(value: Int): Int {
        val range = (seekBarMAX - seekBarMin).toFloat()
        val min = seekBarMin.toFloat()
        val v = value.toFloat() - min
        return ( v * (100.0f/range)).toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        adapter = RecyclerAdapter(facts, this)
        recyclerView.adapter = adapter

        dataProvider = DataProvider(applicationContext, this)

        setupScrollListener()

        lengthText.text = "Max fact length: ${dataProvider.factLength}"
        seekBar.setOnSeekBarChangeListener(this)
        seekBar.progress = progressForCharacters(dataProvider.factLength)

    }

    override fun onStart() {
        super.onStart()
        dataProvider.fetchNextPage()
    }

    override fun didReceiveData(newFacts:List<FactModel>) {
        facts.addAll(newFacts)
        adapter.notifyItemInserted(facts.size)
    }

    private fun setupScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView!!.layoutManager.itemCount
                if(totalItemCount == lastVisibleItemPosition + 1) {
                    dataProvider.fetchNextPage()
                }
            }
        })
    }

    override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) {
        lengthText.text = "Max fact length: ${charactersForProgress(progress)}"
    }

    override fun onStartTrackingTouch(bar: SeekBar?) {

    }

    override fun onStopTrackingTouch(bar: SeekBar?) {
        dataProvider.factLength = charactersForProgress(this.seekBar.progress)
        this.facts.clear()
        adapter.notifyDataSetChanged()
        dataProvider.fetchNextPage()
    }

}
