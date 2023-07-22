package com.example.contact_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions


@AndroidEntryPoint
class ContactsFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var contactViewModel: ContactViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerViewContacts: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        progressBar = view.findViewById(R.id.progressBar)
        recyclerViewContacts = view.findViewById(R.id.recyclerViewContacts)
        recyclerViewContacts.layoutManager = LinearLayoutManager(requireContext())

        contactViewModel = ViewModelProvider(this)[ContactViewModel::class.java]

        requestContactsPermission()

        return view
    }


    private fun requestContactsPermission() {
        val permission = android.Manifest.permission.READ_CONTACTS
        if (EasyPermissions.hasPermissions(requireContext(), permission)) {
            fetchContacts()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to your contacts to fetch them.",
                Companion.READ_CONTACTS_PERMISSION_CODE,
                permission
            )
        }
    }


    private fun fetchContacts() {
        progressBar.visibility = ProgressBar.VISIBLE
        contactViewModel.fetchContacts()

        contactViewModel.contactsLiveData.observe(viewLifecycleOwner, Observer { contacts ->
            progressBar.visibility = ProgressBar.GONE
            recyclerViewContacts.visibility = RecyclerView.VISIBLE

            val adapter = ContactsAdapter(contacts)
            recyclerViewContacts.adapter = adapter
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == READ_CONTACTS_PERMISSION_CODE) {
            fetchContacts()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == READ_CONTACTS_PERMISSION_CODE) {
            progressBar.visibility = ProgressBar.GONE
            recyclerViewContacts.visibility = RecyclerView.GONE
        }
    }

    companion object {
        private const val READ_CONTACTS_PERMISSION_CODE = 101
    }
}
