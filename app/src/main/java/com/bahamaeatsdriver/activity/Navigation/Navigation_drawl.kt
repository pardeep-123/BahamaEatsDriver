package com.bahamaeatsdriver.activity.Navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Documentation
import com.bahamaeatsdriver.activity.Home_Page
import com.bahamaeatsdriver.activity.PaymentStatsActivity
import com.bahamaeatsdriver.activity.driver_profile.My_Profile_Activity
import com.bahamaeatsdriver.activity.login_register.Login_Activity

/**
 * A simple [Fragment] subclass.
 */
class Navigation_drawl : Fragment() {
    private var views: View? = null
    private var drawer_layout: DrawerLayout? = null
    private var mUserLearnedDrawer = false
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var LL_deliveries: LinearLayout? = null
    private var homelayout: LinearLayout? = null
    private var LL_paymentstatus: LinearLayout? = null
    private var LL_support: LinearLayout? = null
    private var LL_settings: LinearLayout? = null
    private var ll_refer_earn: LinearLayout? = null
    private var LL_logout: LinearLayout? = null
    private var ll_Documents: LinearLayout? = null
    private var ll_wallet: LinearLayout? = null
    private var LL_TandC: LinearLayout? = null
    private val mFromSavedInstanceState = false
    private var temp = 0
    private var Relativ_profile: RelativeLayout? = null
    private var instanceiNterface: closeDrawer? = null

    interface closeDrawer {
        fun close()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        instanceiNterface = context as closeDrawer
    }

    var builder: AlertDialog.Builder? = null
    var Relative_Online: RelativeLayout? = null
    var Relative_offline: RelativeLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_navigation_drawl, container, false)
        LL_deliveries = views!!.findViewById(R.id.LL_deliveries)
        LL_support = views!!.findViewById(R.id.LL_support)
        LL_settings = views!!.findViewById(R.id.LL_settings)
        ll_refer_earn = views!!.findViewById(R.id.ll_refer_earn)
        LL_logout = views!!.findViewById(R.id.LL_logout)
        ll_Documents = views!!.findViewById(R.id.ll_Documents)
        ll_wallet = views!!.findViewById(R.id.ll_wallet)
        homelayout = views!!.findViewById(R.id.homelayout)
        Relativ_profile = views!!.findViewById(R.id.Relativ_profile)
        LL_TandC = views!!.findViewById(R.id.LL_TandC)
        LL_paymentstatus = views!!.findViewById(R.id.LL_paymentstatus)
        builder = AlertDialog.Builder(requireActivity())
        LL_deliveries!!.setOnClickListener {

        }
        LL_paymentstatus!!.setOnClickListener {
            temp = 1
            startActivity(Intent(activity, PaymentStatsActivity::class.java))
        }
        homelayout!!.setOnClickListener {
            if (temp == 0) {
                instanceiNterface!!.close()
            } else {
                temp = 0
                startActivity(Intent(activity, Home_Page::class.java))
            }
        }
        LL_support!!.setOnClickListener {
            temp = 1
            startActivity(Intent(activity, Contactus_Activity::class.java))
        }
        LL_TandC!!.setOnClickListener {
            temp = 1
            startActivity(Intent(activity, TermAnd_Conditions::class.java))
        }
        LL_settings!!.setOnClickListener {
            temp = 1
            startActivity(Intent(activity, SettingsActivity::class.java))
        }
        ll_refer_earn!!.setOnClickListener {
            temp = 1
            startActivity(Intent(activity, Refer_And_Earn::class.java))
        }
        LL_logout!!.setOnClickListener(View.OnClickListener {
            temp = 1
            builder!!.setMessage("Logout").setTitle("Logout")

            //Setting message manually and performing action on button click
            builder!!.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    startActivity(
                        Intent(
                            activity,
                            Login_Activity::class.java
                        )
                    )
                }
                .setNegativeButton("No") { dialog, id -> //  Action for 'NO' Button
                    dialog.cancel()
                }
            //Creating dialog box
            val alert = builder!!.create()
            //Setting the title manually
            alert.show()
        })
        ll_Documents!!.setOnClickListener(View.OnClickListener {
            temp = 1
            startActivity(Intent(activity, Documentation::class.java))
        })
        ll_wallet!!.setOnClickListener(View.OnClickListener {
            temp = 1
            startActivity(Intent(activity, My_Wallet_Activity::class.java))
        })
        Relativ_profile!!.setOnClickListener(View.OnClickListener {
            temp = 1
            startActivity(Intent(activity, My_Profile_Activity::class.java))
        })
        return view
    }

    fun setUp(fragmentId: Int, drawerLayout: DrawerLayout?) {
        /*var viewss = activity!!.findViewById(fragmentId)
        drawer_layout = drawerLayout
        mDrawerToggle = object : ActionBarDrawerToggle(activity, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true
                    saveToPreferences(activity, KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer.toString() + "")
                }
                activity!!.invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                activity!!.invalidateOptionsMenu()
                //  toolbar.clearAnimation();
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                //toolbar.setAlpha(1 - slideOffset / 2);
            }
        }
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            drawer_layout!!.openDrawer(view)
        }
        drawer_layout!!.setDrawerListener(mDrawerToggle)
        drawer_layout!!.post { mDrawerToggle.syncState() }*/
    }

    companion object {
        const val PREF_FILE_NAME = "testpref"
        const val KEY_USER_LEARNED_DRAWER = "user_learned_drawer"
        fun saveToPreferences(
            context: Context?,
            preferencesName: String?,
            preferencesValue: String?
        ) {
            val sharedPreferences =
                context!!.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(preferencesName, preferencesValue)
            editor.commit()
        }

        fun readFromPreference(
            context: Context,
            preferencesName: String?,
            defaultValue: String?
        ): String? {
            val sharedPreferences =
                context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(preferencesName, defaultValue)
        }
    }
}