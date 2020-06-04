package com.himanshu.whatsappclone.activities

import android.content.Context
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.himanshu.whatsappclone.R
import com.himanshu.whatsappclone.adapters.UserListAdapter
import com.himanshu.whatsappclone.models.UserObject
import com.himanshu.whatsappclone.utils.Iso2Phone

class FindUserActivity : AppCompatActivity() {

    private lateinit var mUserListRecyclerView : RecyclerView
    private lateinit var mUserListAdapter : RecyclerView.Adapter<*>
    private lateinit var mUserListLayoutManager: RecyclerView.LayoutManager
    private lateinit var mUserList: ArrayList <UserObject>
    private lateinit var mContactList : ArrayList <UserObject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_user)

        mUserList = ArrayList()
        mContactList = ArrayList()
        initializeRecyclerView()

        getContactList()
    }

    private fun getContactList() {

        val phonePrefix : String = getCountryPhonePrefix()

        var contacts : Cursor? = contentResolver.query (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        if (contacts != null) {
            while (contacts.moveToNext()) {
                val name : String = contacts.getString (contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var phone : String = contacts.getString (contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                phone = phone.replace (" ", "")
                phone = phone.replace ("-", "")
                phone = phone.replace ("(", "")
                phone = phone.replace (")", "")

                if (phone[0] != '+') {
                    phone = phonePrefix + phone
                }

                val contact = UserObject(name, phone)
                mContactList.add (contact)
                getUserDetails (contact)
            }
        }
    }

    private fun getUserDetails(contact: UserObject) {
        val userDB : DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
        val query : Query = userDB.orderByChild("phone").equalTo(contact.phone)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var phone = ""
                    var name = ""

                    for (childSnapShot : DataSnapshot in dataSnapshot.children) {
                        if (childSnapShot.child ("phone").value != null) {
                            phone = childSnapShot.child ("Phone").value.toString()
                        }
                        if (childSnapShot.child ("name").value != null) {
                            name = childSnapShot.child ("Phone").value.toString()
                        }

                        val user : UserObject = UserObject(name, phone)
                        mUserList.add (user)
                        mUserListAdapter.notifyDataSetChanged()
                        return
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getCountryPhonePrefix (): String {
        var iso = ""
        val telephonyManager : TelephonyManager = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (telephonyManager.networkCountryIso != null) {
            if (telephonyManager.networkCountryIso.toString().isNotEmpty()) {
                iso = telephonyManager.networkCountryIso.toString()
            }
        }
        return Iso2Phone.getPhone(iso)!!
    }

    private fun initializeRecyclerView() {
        mUserListRecyclerView = findViewById(R.id.recycler_view_user_list)
        mUserListRecyclerView.isNestedScrollingEnabled = false
        mUserListRecyclerView.setHasFixedSize(false)

        mUserListLayoutManager = LinearLayoutManager (applicationContext, RecyclerView.VERTICAL, false)
        mUserListRecyclerView.layoutManager = mUserListLayoutManager

        mUserListAdapter = UserListAdapter (mUserList)
        mUserListRecyclerView.adapter = mUserListAdapter
    }
}
