package com.example.traintime.timetable

import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traintime.R
import com.example.traintime.databinding.FragmentTimetableBinding
import com.example.traintime.util.PaddingDecoration
import com.example.traintime.util.now

class TimetableFragment : Fragment() {
    private lateinit var viewModel: TimetableViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(
            this, TimetableViewModel.Factory(requireNotNull(activity).application)
        ).get(TimetableViewModel::class.java)

        arguments?.getString("fromTo")?.let {
            val (from, to) = it.split("-")
            viewModel.loadData(from, to)
        }

        val binding = FragmentTimetableBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val itemPadding =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, resources.displayMetrics)
                .toInt()

        val timetable = binding.timetable
        timetable.adapter = TrainInfoAdapter()
        timetable.addItemDecoration(PaddingDecoration(itemPadding))

        viewModel.trainInfos.observe(viewLifecycleOwner) { trainInfos ->
            (timetable.adapter as TrainInfoAdapter).submitList(trainInfos) {
                val now = now()
                trainInfos.indexOfFirst { it.startingTime >= now }.let {
                    val currentPosition = if (it != -1) it else trainInfos.size - 1
                    (timetable.layoutManager as LinearLayoutManager)
                        .scrollToPositionWithOffset(currentPosition, itemPadding)
                }
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_timetable, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                arguments?.getString("fromTo")?.let {
                    val (from, to) = it.split("-")
                    viewModel.loadData(from, to, true)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}