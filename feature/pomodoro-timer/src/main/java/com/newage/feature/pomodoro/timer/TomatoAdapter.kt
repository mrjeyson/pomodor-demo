package com.newage.feature.pomodoro.timer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timers.stopwatch.feature.pomodoro.timer.R

/**
 * Created by Janak on 27/11/23.
 */
class TomatoAdapter(var count: Int) : RecyclerView.Adapter<TomatoAdapter.TomatoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TomatoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pomodoro_completed_item_layout, parent, false)
        return TomatoHolder(view)
    }

    override fun getItemCount(): Int {
        return count
    }

    override fun onBindViewHolder(holder: TomatoHolder, position: Int) {
        // No implementation
    }

    class TomatoHolder(view: View) : RecyclerView.ViewHolder(view)
}
