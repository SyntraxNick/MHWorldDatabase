package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemLocation
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemReward
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import kotlinx.android.synthetic.main.cell_icon_verbose_label_text.view.*
import kotlinx.android.synthetic.main.listitem_reward.view.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction


/**
 * Renders list items for item location information
 */
class ItemLocationAdapterDelegate : SimpleListDelegate<ItemLocation, View>() {
    override fun isForViewType(obj: Any) = obj is ItemLocation

    override fun onCreateView(parent: ViewGroup): View {
        // todo: refactor listitem_reward into a general view
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_reward, parent, false)
    }

    override fun bindView(view: View, data: ItemLocation) {
        view.reward_name.text = view.resources.getString(R.string.location_area, data.data.area)
        view.reward_stack.text = "x ${data.data.stack}"
        view.reward_percent.text = "${data.data.percentage}%"
    }
}

/**
 * Used to display the monsters that a particular item can come from.
 * This is the "reverse" of MonsterRewardAdapterDelegate
 */
class MonsterRewardSourceAdapterDelegate: SimpleListDelegate<ItemReward, View>() {
    override fun isForViewType(obj: Any) = obj is ItemReward

    override fun onCreateView(parent: ViewGroup): View {
        // todo: decide if we want to make this into a standalone view class or not
        // an alternative option is to upgrade the normal one to support sublabels and sub-values
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.cell_icon_verbose_label_text, parent, false)
    }

    override fun bindView(view: View, data: ItemReward) {
        // Returns LR/HR depending on the rank
        val rankStr = view.resources.getString(when (data.rank) {
            Rank.LOW -> R.string.low_rank_short
            Rank.HIGH -> R.string.high_rank_short
        })

        // The condition alongside the rank
        val source = view.resources.getString(
                R.string.item_source_reward_condition, rankStr, data.condition_name)

        view.icon.setImageDrawable(view.assetLoader.loadIconFor(data.monster))
        view.label_text.text = data.monster.name
        view.sublabel_text.text = source
        view.value_text.text = view.resources.getString(R.string.percentage, data.percentage)
        view.subvalue_text.text = view.resources.getString(R.string.quantity, data.stack)
    }
}

class CraftResult(
        val name: String?,
        val value: String?,
        val icon: Drawable?,
        val clickFn: (v: View) -> Unit
)

interface CraftResultBinder {
    fun build(ctx: Context): CraftResult
}

fun createCraftBinder(fn: (Context) -> CraftResult): CraftResultBinder {
    return object: CraftResultBinder {
        override fun build(ctx: Context) = fn(ctx)
    }
}

/**
 * An adapter delegate that renders for any simple craft result object.
 * The objects rendered must be CraftResultBinders, which provide callback functions
 * to general certain pieces of information.
 */
class ItemCraftResultAdapterDelegate: SimpleListDelegate<CraftResultBinder, IconLabelTextCell>() {
    override fun isForViewType(obj: Any) = obj is CraftResultBinder

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(view: IconLabelTextCell, data: CraftResultBinder) {
        val result = data.build(view.context)
        view.setLeftIconDrawable(result.icon)
        view.setLabelText(result.name)
        view.setValueText(result.value)

        view.setOnClickListener { result.clickFn(view) }
    }

}