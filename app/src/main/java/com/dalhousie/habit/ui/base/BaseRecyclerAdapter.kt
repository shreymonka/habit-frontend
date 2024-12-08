package com.dalhousie.habit.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dalhousie.habit.BR

/**
 * Base recycler adapter for all recycler adapters.
 */
abstract class BaseRecyclerAdapter<T> :
    RecyclerView.Adapter<BaseRecyclerAdapter<T>.RecyclerHolder>() {
    /**
     * Inner class to set recycler view holder
     *
     * @param viewDataBinding   The [ViewDataBinding] instance
     */
    inner class RecyclerHolder(
        private val viewDataBinding: ViewDataBinding
    ) : RecyclerView.ViewHolder(viewDataBinding.root), View.OnClickListener {
        /**
         * Bind the holder with [data]
         *
         * @param data  The data to bind
         */
        fun bind(data: T) {
            viewDataBinding.setVariable(BR.data, data)
            viewDataBinding.setVariable(BR.clickHandler, this)
            setDataForListItem(viewDataBinding, data, adapterPosition)
            viewDataBinding.executePendingBindings()
        }

        override fun onClick(v: View?) {
            onItemClick(v, adapterPosition)
        }
    }

    /**
     * Base class for [DiffUtil] to calculate difference between old and new list
     *
     * @param oldList   The old list
     * @param newList   The new list
     */
    inner class BaseDiffUtil(
        private val oldList: List<T>,
        private val newList: List<T>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsSame(oldList[oldItemPosition], newList[newItemPosition])

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areContentsSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    protected val arrayList: MutableList<T> = mutableListOf()
    private val previousArrayList = ArrayList<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getLayoutIdForType(viewType),
                parent,
                false
            )
        return RecyclerHolder(binding)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun getItemViewType(position: Int): Int = ITEM_TYPE_NORMAL

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    /**
     * Get the layout id for required [viewType]
     *
     * @param viewType  The view type to get layout id
     *
     * @return The layout id of the required view type
     */
    abstract fun getLayoutIdForType(viewType: Int): Int

    /**
     * Callback on item clicked
     *
     * @param view      The view that is clicked
     * @param position  The position of the [view] clicked
     */
    protected open fun onItemClick(view: View?, position: Int) { }

    /**
     * Compares [firstItem] with [secondItem]
     *
     * @param firstItem     First item
     * @param secondItem    Second item
     *
     * @return A [Boolean] representing true if both items are same, otherwise false
     */
    abstract fun areItemsSame(firstItem: T, secondItem: T): Boolean

    /**
     * Compares the contents of the [firstItem] with [secondItem]
     *
     * @param firstItem     First item
     * @param secondItem    Second item
     *
     * @return A [Boolean] representing true if contents of both items are same, otherwise false
     */
    protected open fun areContentsSame(firstItem: T, secondItem: T): Boolean =
        firstItem == secondItem

    /**
     * Set the list item with [data] at [position]
     *
     * @param binding   The [ViewDataBinding] instance
     * @param data      The data to set
     * @param position  The position at which [data] should be added
     */
    protected open fun setDataForListItem(
        binding: ViewDataBinding,
        data: T,
        position: Int
    ) { }

    /**
     * Add the [item]
     *
     * @param item  The item to add
     */
    fun addItem(item: T) =
        setItems(arrayList.apply { add(item) })

    /**
     * Add all the items in recycler view
     *
     * @param newList   The [List] to add
     */
    fun addAllItem(newList: List<T>) =
        setItems(arrayList.apply { addAll(newList) })

    /**
     * Remove the [item]
     *
     * @param item  The item to remove
     */
    fun removeItem(item: T) =
        setItems(arrayList.apply { remove(item) })

    /**
     * Remove all the items
     */
    fun removeAllItems() = setItems(listOf())

    /**
     * Update the item at [index]
     *
     * @param item  New item to set
     * @param index Index of the [item]
     */
    fun updateItemAt(index: Int, item: T) {
        arrayList[index] = item
        notifyItemChanged(index)
    }

    /**
     * Sets the list in recycler view
     *
     * @param newList   The [List] to set
     */
    fun setItems(newList: List<T>) {
        if (arrayList.size >= previousArrayList.size) {
            previousArrayList.clear()
            previousArrayList.addAll(arrayList)
        }
        DiffUtil.calculateDiff(BaseDiffUtil(arrayList, newList)).apply {
            arrayList.clear()
            arrayList.addAll(newList)
            dispatchUpdatesTo(this@BaseRecyclerAdapter)
        }
    }

    companion object {
        const val ITEM_TYPE_NORMAL = 1
    }
}