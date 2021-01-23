package com.arvatel.baam

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback


class QRScannerFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var mainView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.fragment_q_r_scanner, container, false)
        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                saveQR(it.text)
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
        requestPermissions()
    }

    private fun saveQR(qr : String?){

        val data = qr?.substringAfter('#')

        (activity as InterfaceQRSaver).setSession(data?.substringBefore('-'))
        (activity as InterfaceQRSaver).setSecretCode(data?.substringAfter('-'))

        Toast.makeText(activity, "Session: ${(activity as InterfaceQRSaver).getSession()}, " +
                "SecretCode: ${(activity as InterfaceQRSaver).getSecretCode()}", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions((activity as Activity), permissionList, MY_PERMISSION_REQUEST_CODE)
    }

    companion object{
        private val permissionList = arrayOf(Manifest.permission.CAMERA)
        private const val MY_PERMISSION_REQUEST_CODE = 1001
    }
}