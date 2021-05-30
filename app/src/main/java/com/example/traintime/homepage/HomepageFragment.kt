package com.example.traintime.homepage

import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traintime.R
import com.example.traintime.databinding.FragmentHomepageBinding
import com.example.traintime.dialog.RequestPickerDialogFragment
import com.example.traintime.util.PaddingDecoration

class HomepageFragment : Fragment() {
    private lateinit var viewModel: HomepageViewModel
    private lateinit var adapter: TimetableRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(
            this, HomepageViewModel.Factory(requireNotNull(activity).application)
        ).get(HomepageViewModel::class.java)

        val binding = FragmentHomepageBinding.inflate(inflater, container, false)
        val requestList = binding.requestList

        adapter = TimetableRequestAdapter()
        adapter.setListener {
            val bundle = bundleOf("fromTo" to "${it.fromStationID}-${it.toStationID}")
            findNavController().navigate(R.id.action_homepageFragment_to_timetableFragment, bundle)
        }
        requestList.adapter = adapter

        val padding =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics)
                .toInt()
        requestList.addItemDecoration(PaddingDecoration(padding))
        ItemTouchHelper(RequestCallback(adapter)).attachToRecyclerView(requestList)

        viewModel.timetableRequests.observe(viewLifecycleOwner) { requests ->
            val oldCount = adapter.itemCount
            adapter.requests = requests
            if (oldCount != 0 && oldCount < adapter.itemCount) {
                (requestList.layoutManager as LinearLayoutManager)
                    .scrollToPositionWithOffset(adapter.itemCount - 1, padding)
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_homepage, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                adapter.changeMode()
                item.setIcon(if (adapter.isEditMode) R.drawable.ic_check else R.drawable.ic_edit)
                if (!adapter.isEditMode)
                    viewModel.saveTimetableRequests(adapter.requests)
                true
            }
            R.id.action_add -> {
                if (!adapter.isEditMode) {
                    val dialog = RequestPickerDialogFragment()
                    dialog.setListener {
                        when {
                            it.fromStationID == it.toStationID -> {
                                showToast("起終點重複")
                            }
                            adapter.requests.indexOfFirst { r ->
                                r.fromStationID == it.fromStationID
                                        && r.toStationID == it.toStationID
                            } != -1 -> {
                                showToast("既存路線")
                            }
                            else -> {
                                dialog.dismiss()
                                it.listOrder = adapter.itemCount
                                viewModel.insertTimetableRequest(it)
                            }
                        }
                    }
                    dialog.show(parentFragmentManager, "RequestPickerDialogFragment")
                    true
                } else {
                    false
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }
}