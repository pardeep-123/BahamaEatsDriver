package com.bahamaeatsdriver.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bahamaeatsdriver.adapter.OrderDetailsAddOnsQuantiytAdapter
import com.bahamaeatsdriver.adapter.OrderDetailsQuantiytAfterOrderCompleteAdapter
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.helper.others.Helper
import com.bahamaeatsdriver.model_class.add_on_list.AddOnsCustomModel
import com.bahamaeatsdriver.model_class.change_ride_status.Body
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_complete_ride_details.*
import java.lang.Math.*
import java.text.DecimalFormat
import kotlin.math.sin

class CompleteRideDetailsActivity : AppCompatActivity(), View.OnClickListener {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_ride_details)
        if (intent.getSerializableExtra("data") != null) {
            val orderData = intent.getSerializableExtra("data") as Body
            Helper.showErrorAlertWithoutTitle(this, "Ride completed successfully.")
            if (orderData.restaurant.latitude.isNotEmpty() && orderData.restaurant.longitude.isNotEmpty() && orderData.userAddress.latitude.toDouble() != 0.0 && orderData.userAddress.longitude.toDouble() != 0.0) {
                val restaurantToUser = distance(
                    orderData.restaurant.latitude.toDouble(),
                    orderData.restaurant.longitude.toDouble(),
                    orderData.userAddress.latitude.toDouble(),
                    orderData.userAddress.longitude.toDouble()
                )
                val finalDistanceNew: String = java.lang.String.valueOf(DecimalFormat("##").format(restaurantToUser))
                tv_totalDistance.text = "$finalDistanceNew mi"
            }
            /**
             * paymentMethod-1 for suncash
             * paymentMethod-2 for paypal
             * paymentMethod-4 for kanoo
             * paymentMethod-5 for Atlantic
             * paymentMethod-6 for IsLand Pay
             * paymentMethod-7 for be_wallet
             * paymentMethod-8 for Loyalty Bonus
             */
            when (orderData.order.paymentMethod) {
                1 -> {
                    tv_paymentMode.text = "Payment Mode: Suncash"
                }
                2 -> {
                    tv_paymentMode.text = "Payment Mode: Paypal"
                }
                4 -> {
                    tv_paymentMode.text = "Payment Mode: Kanoo"
                }
                5 -> {
                    tv_paymentMode.text = "Payment Mode: Atlantic"
                }
                6 -> {
                    tv_paymentMode.text = "Payment Mode: IsLand Pay"
                }
                7 -> {
                    tv_paymentMode.text = "Payment Mode: " + getString(R.string.be_wallet)
                }
                8 -> {
                    tv_paymentMode.text = "Payment Mode: " + getString(R.string.loyalty_bonus)
                }
                9 -> {
                    tv_paymentMode.text = "Payment Mode: " + getString(R.string.referral_reward)
                }
            }
            tv_totalAmount.text = "$" + Helper.roundOffDecimalNew(orderData.order.netAmount.toFloat())
            tv_restaurantName.text = orderData.restaurant.address
            val houseNumber = orderData.userAddress.completeAddress
            val streetName = if (orderData.userAddress.streetName.isNotEmpty()) "/" + orderData.userAddress.streetName else ""
            val landmark = if (orderData.userAddress.deliveryInstructions.isNotEmpty()) "\n" + orderData.userAddress.deliveryInstructions else ""
            val userAddres = if (orderData.userAddress.address.isNotEmpty()) "\n" + orderData.userAddress.address else ""
            val finalAddress = houseNumber + streetName + landmark + userAddres
            tv_userOrderAddress.text = finalAddress
            tv_deliveryAmount.text = "$" + orderData.order.deliveryFee
//            tv_deliveryAmount.text = "$" + orderData.order.userDeliveryFee
            if (orderData.order.deliveryFee.isNotEmpty()&&orderData.order.deliveryFee!="0"&&orderData.order.deliveryFee!="0.0") {
                ll_deliverChargesRoot.visibility = View.VISIBLE
                ll_deliverChargesRootView.visibility = View.VISIBLE
            } else {
                ll_deliverChargesRoot.visibility = View.GONE
                ll_deliverChargesRootView.visibility = View.GONE
            }

            tv_vatAmount.text = "$" + orderData.order.tax
            if (orderData.order.tax.isNotEmpty()&&orderData.order.tax!="0"&&orderData.order.tax!="0.0") {
                ll_vatRoot.visibility = View.VISIBLE
                ll_vatRootView.visibility = View.VISIBLE
            } else {
                ll_vatRoot.visibility = View.GONE
                ll_vatRootView.visibility = View.GONE
            }
            tv_serviceFeesAmount.text = "$" + orderData.order.serviceFee
            if (orderData.order.serviceFee != 0.0) {
                ll_serviceRoot.visibility = View.VISIBLE
                ll_serviceRootView.visibility = View.VISIBLE
            } else {
                ll_serviceRoot.visibility = View.GONE
                ll_serviceRootView.visibility = View.GONE
            }
            tv_vatLabel.text = "VAT(" + orderData.order.taxPercentage + "%)"
            tv_tip.text = "$" + orderData.order.tip
            if (orderData.order.tip.isNotEmpty()&&orderData.order.tip!="0"&&orderData.order.tip!="0.0") {
                ll_tipRoot.visibility = View.VISIBLE
                ll_tipRootView.visibility = View.VISIBLE
            } else {
                ll_tipRoot.visibility = View.GONE
                ll_tipRootView.visibility = View.GONE
            }
            tv_cartFee.text = "$" + orderData.order.cartFee
            if (orderData.order.cartFee.isNotEmpty()&&orderData.order.cartFee!="0.0"&&orderData.order.cartFee!="0") {
                ll_cartFeeRoot.visibility = View.VISIBLE
                ll_cartFeeRootView.visibility = View.VISIBLE
            } else {
                ll_cartFeeRoot.visibility = View.GONE
                ll_cartFeeRootView.visibility = View.GONE
            }
            tv_promo.text = "-$" + orderData.order.promoDiscount
            if (orderData.order.promoDiscount.isNotEmpty()&&orderData.order.promoDiscount!="0.0"&&orderData.order.promoDiscount!="0") {
                ll_promoRoot.visibility = View.VISIBLE
                ll_promoRootView.visibility = View.VISIBLE
            } else {
                ll_promoRoot.visibility = View.GONE
                ll_promoRootView.visibility = View.GONE
            }
            tv_totalAmountWithAll.text = "$" + Helper.roundOffDecimalNew(orderData.order.netAmount.toFloat())
            Glide.with(this).load(orderData.restaurant.image).placeholder(R.drawable.placeholder_rectangle).into(iv_restaurantImage)
            val adapterItemQuantity = OrderDetailsQuantiytAfterOrderCompleteAdapter(this, orderData.order.orderDetails)
            rv_orderItems.adapter = adapterItemQuantity
            val listAddOnList = ArrayList<AddOnsCustomModel>()
            var count = 0
            for (i in orderData.order.orderDetails.indices) {
                for (j in orderData.order.orderDetails[i].categories.indices) {
                    for (k in 0 until orderData.order.orderDetails[i].categories[j].addOnArray.size) {
                        val addOnMOdel = AddOnsCustomModel(
                            orderData.order.orderDetails[i].categories[j].addOnArray[k].addon,
                            orderData.order.orderDetails[i].categories[j].addOnArray[k].price,
                            orderData.order.orderDetails[i].quantity
                        )
                        listAddOnList.add(count, addOnMOdel)
                        count++
                    }
                }
            }

            if (listAddOnList != null && listAddOnList.isNotEmpty()) {
                ll_addOnsLabel.visibility = View.VISIBLE
                val adapterItemAddOnsQuantity1 = OrderDetailsAddOnsQuantiytAdapter(this, listAddOnList)
                rv_orderAddOnsItems.adapter = adapterItemAddOnsQuantity1
            } else {
                ll_addOnsLabel.visibility = View.GONE
            }
        }
        btn_done.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_done -> {
                finish()
            }
        }
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (sin(deg2rad(lat1))
                * sin(deg2rad(lat2))
                + (kotlin.math.cos(deg2rad(lat1))
                * kotlin.math.cos(deg2rad(lat2))
                * kotlin.math.cos(deg2rad(theta))))
        dist = kotlin.math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / PI
    }
}