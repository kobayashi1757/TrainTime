package com.example.traintime.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import com.example.traintime.database.TimetableRequest
import com.example.traintime.databinding.ListItemTimetableRequestBinding

class TimetableRequestAdapter :
        RecyclerView.Adapter<TimetableRequestAdapter.ViewHolder>() {
    class ViewHolder (val binding: ListItemTimetableRequestBinding) :
            RecyclerView.ViewHolder(binding.root)

    private var _requests = mutableListOf<TimetableRequest>()
    var requests : List<TimetableRequest>
        get() = _requests
        set(value) {
            _requests = value.toMutableList()
            notifyDataSetChanged()
        }

    private var mListener: ((TimetableRequest) -> Unit)? = null

    private var _isEditMode = false
    val isEditMode: Boolean
        get() = _isEditMode

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTimetableRequestBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = _requests[position]
        val binding = holder.binding
        binding.isEditMode = _isEditMode
        binding.request = request
        binding.slashView.setOnClickListener {
            mListener?.invoke(request)
        }
        binding.deleteButton.setOnClickListener {
            _requests.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }
        binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return _requests.size
    }

    fun setListener(listener: (TimetableRequest) -> Unit) {
        mListener = listener
    }

    fun changeMode() {
        _isEditMode = !_isEditMode
        notifyDataSetChanged()
    }

    fun swap(from: Int, to: Int) {
        val tmp = _requests[from]
        _requests[from] = _requests[to]
        _requests[to] = tmp
        notifyItemMoved(from, to)
    }
}

class RequestCallback(private val adapter: TimetableRequestAdapter): ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(UP or DOWN, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        adapter.swap(from, to)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun isLongPressDragEnabled(): Boolean {
        return adapter.isEditMode
    }
}