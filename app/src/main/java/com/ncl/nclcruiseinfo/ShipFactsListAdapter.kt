package com.ncl.nclcruiseinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ncl.nclcruiseinfo.databinding.ItemCruiseShipBinding
import com.ncl.nclcruiseinfo.databinding.ItemSectionTitleBinding
import com.ncl.nclcruiseinfo.databinding.ItemShipFactsBinding
import com.ncl.nclcruiseinfo.databinding.ItemWhatsincludedBinding
import java.util.*
import kotlin.collections.ArrayList

class ShipFactsListAdapter(
    private val cruiseInfo: CruiseShipInfo
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<ViewType>()

    init {

        items.add(ViewType.SectionTitle("Ship Facts"))

        val facts = with(cruiseInfo){
            listOf(
                ViewType.ShipFact("Crew", shipFacts.crew),
                ViewType.ShipFact("Cruise Speed", shipFacts.cruiseSpeed),
                ViewType.ShipFact("Draft", shipFacts.draft),
                ViewType.ShipFact("Engines", shipFacts.engines),
                ViewType.ShipFact("Gross Register Tonnage", shipFacts.grossRegisterTonnage),
                ViewType.ShipFact("Inaugural Date", shipFacts.inauguralDate),
                ViewType.ShipFact("Max Beam", shipFacts.maxBeam),
                ViewType.ShipFact("Overall Length", shipFacts.overallLength),
                ViewType.ShipFact("Passenger Capacity", shipFacts.passengerCapacity),
                ViewType.ShipFact("Remodeled Date", shipFacts.remodeledDate ?: "")
            )
        }
        items.addAll(facts)

        items.add(ViewType.SectionTitle("What's Included"))
        val whatsIncluded = cruiseInfo.whatsIncluded.map { ViewType.WhatsIncluded(it) }
        items.addAll(whatsIncluded)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflateLayout = LayoutInflater.from(parent.context)
        return when(viewType){
            SECTION_TITLE -> SectionTitleViewHolder(ItemSectionTitleBinding.inflate(inflateLayout, parent, false))
            SHIP_FACT -> ShipFactViewHolder(ItemShipFactsBinding.inflate(inflateLayout, parent, false))
            WHATS_INCLUDED -> WhatsIncludedViewHolder(ItemWhatsincludedBinding.inflate(inflateLayout, parent, false))
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when(holder){
            is SectionTitleViewHolder -> (item as? ViewType.SectionTitle)?.let { holder.bind(it.title) }
            is ShipFactViewHolder -> (item as? ViewType.ShipFact)?.let { holder.bind(it.title, it.value) }
            is WhatsIncludedViewHolder -> (item as? ViewType.WhatsIncluded)?.let { holder.bind(it.value) }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when(items[position]){
            is ViewType.SectionTitle -> SECTION_TITLE
            is ViewType.ShipFact -> SHIP_FACT
            is ViewType.WhatsIncluded -> WHATS_INCLUDED
        }
    }

    inner class SectionTitleViewHolder(private val itemBinding: ItemSectionTitleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(title: String) {
            itemBinding.titleLabel.text = title
        }
    }

    inner class ShipFactViewHolder(private val itemBinding: ItemShipFactsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(title: String, value: String) {
            itemBinding.titleLabel.text = title
            itemBinding.valueLabel.text = value
        }
    }

    inner class WhatsIncludedViewHolder(private val itemBinding: ItemWhatsincludedBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(value: String) {
            itemBinding.valueLabel.text = value
        }
    }

    sealed class ViewType {
        class SectionTitle(val title: String): ViewType()
        class ShipFact(val title: String, val value: String): ViewType()
        class WhatsIncluded(val value: String): ViewType()
    }

    companion object{
        const val SECTION_TITLE = 0
        const val SHIP_FACT = 1
        const val WHATS_INCLUDED = 2
    }

}

