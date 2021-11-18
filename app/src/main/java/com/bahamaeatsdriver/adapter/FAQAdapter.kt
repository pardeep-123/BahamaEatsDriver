package com.bahamaeatsdriver.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.FaqActivity
import com.bahamaeatsdriver.model_class.faqs.Body
import kotlinx.android.synthetic.main.faq_row_layout.view.*


class FAQAdapter(var context: FaqActivity, var faqList: List<Body>) :
    RecyclerView.Adapter<FAQAdapter.MyViewHolder>() {
    var templist: List<Body>

    init {
        this.templist = faqList
    }

    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val rootLayout: RelativeLayout = itemView.findViewById(R.id.root_layout)
        val dropDownArrow: ImageView = itemView.findViewById(R.id.expand_arrow)
        val linearRoot: LinearLayout = itemView.findViewById(R.id.linear_root4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutView =
            LayoutInflater.from(parent.context).inflate(R.layout.faq_row_layout, parent, false)
        return MyViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /***
         * Set Data over views
         */
        holder.itemView.tv_title.text = faqList[position].question
        holder.itemView.tv_description.text = faqList[position].answer

        if (faqList[position].isSelected == 0) {
            holder.dropDownArrow.setImageResource(R.drawable.arrowside)
            holder.linearRoot.visibility = View.GONE
        } else {
            holder.dropDownArrow.setImageResource(R.drawable.arrowup)
            holder.linearRoot.visibility = View.VISIBLE
        }
        holder.rootLayout.setOnClickListener {
            if (faqList[position].isSelected == 0)
                faqList[position].isSelected = 1
            else
                faqList[position].isSelected = 0
            notifyDataSetChanged()
        }

    }

    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase()
        val nList: MutableList<Body> =
            ArrayList<Body>()
        if (charText.length == 0) {
            nList.addAll(templist)
        } else {
            for (wp in templist) {
                if (wp.answer.toLowerCase()
                        .contains(charText.toLowerCase()) || wp.question.toLowerCase()
                        .contains(charText.toLowerCase())
                ) {
                    nList.add(wp)
                }
            }
        }
        faqList = nList
        notifyDataSetChanged()
        context.updateViews(faqList as MutableList<Body>)
    }

}