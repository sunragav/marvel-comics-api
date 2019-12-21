package com.sunragav.indiecampers.feature_home.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.sunragav.indiecampers.feature_home.ui.recyclerview.viewholders.ComicsViewHolder
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import org.hamcrest.Description
import org.hamcrest.Matcher


class CustomMatchers {


    companion object {

        fun atPositionWithComis_Id_And_Title(position: Int, comics:ComicsEntity): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description) {
                    description.appendText("has item at position $position: ")
                    // itemMatcher.describeTo(description)
                }

                override fun matchesSafely(item: RecyclerView): Boolean {
                    val viewHolder: ComicsViewHolder =
                        item.findViewHolderForAdapterPosition(position) as ComicsViewHolder
                    return comics.id == viewHolder.binding.tvId.text &&
                            comics.title == viewHolder.binding.tvTitle.text
                }
            }
        }
    }
}