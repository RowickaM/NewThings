package com.rowicka.newthings.recyclerViewSection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rowicka.newthings.databinding.ActivityRecyclerViewSectionBinding
import com.rowicka.newthings.recyclerViewSection.adapter.BaseListAdapter
import com.rowicka.newthings.recyclerViewSection.model.BinItem
import com.rowicka.newthings.recyclerViewSection.model.BinType
import java.time.LocalDateTime

class RecyclerViewSectionActivity : AppCompatActivity() {
    val list = arrayListOf<BinItem>()

    private var _binding: ActivityRecyclerViewSectionBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityRecyclerViewSectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list.addAll(
            arrayListOf(
                BinItem(
                    0,
                    "Produkt 1",
                    LocalDateTime.of(2021, 4, 25, 0, 0, 0),
                    1
                ),
                BinItem(
                    1,
                    "Produkt 2",
                    LocalDateTime.of(2021, 4, 26, 0, 0, 0),
                    2
                ),
                BinItem(
                    2,
                    "Produkt 3",
                    LocalDateTime.of(2021, 4, 26, 0, 0, 0),
                    1
                ),
                BinItem(
                    3,
                    "Produkt 4",
                    LocalDateTime.of(2021, 4, 27, 0, 0, 0),
                    5
                ),
                BinItem(
                    4,
                    "Produkt 5",
                    LocalDateTime.of(2021, 4, 25, 0, 0, 0),
                    10
                ),
            )
        )

        binding.list.layoutManager = LinearLayoutManager(this)
        val adapter = BaseListAdapter()
        binding.list.adapter = adapter

        adapter.setList(createDateBinItem(list))
    }

    private fun createDateBinItem(items: List<BinItem>): ArrayList<BinType> {

        // Wrap data in list items
        val binItems = items.map { BinType.Bin(it) }.sortedBy { it.value.removedAt }

        val binWithDateHeader = arrayListOf<BinType>()

        // Loop through the fruit list and add headers where we need them
        var currentHeader: LocalDateTime? = null
        binItems.forEach { bin ->
            bin.value.let {
                if (it.removedAt != currentHeader) {
                    binWithDateHeader.add(BinType.Date(it.removedAt))
                    currentHeader = it.removedAt
                }
            }
            binWithDateHeader.add(bin)
        }
        return binWithDateHeader
    }
}