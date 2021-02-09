package com.arvatel.baam

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import kotlinx.android.synthetic.main.fragment_q_r_scanner.view.*


class QRScannerFragment : Fragment(), InterfaceResponseCallback {

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

//                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()

                saveQR(it.text)

                val controller = BaamApiController(this)
                controller.start()
                controller.submitChallenge((activity as InterfaceDataSaver).getCookie(),
                                (activity as InterfaceDataSaver).getSession(),
                                (activity as InterfaceDataSaver).getSecretCode())
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        view.button2.setOnClickListener {
            (activity as InterfaceSendRequest).sendRequest()
        }
        requestPermissions()
    }

    private fun saveQR(qr: String?){

        val data = qr?.substringAfter('#')

        (activity as InterfaceDataSaver).setSession(data?.substringBefore('-'))
        (activity as InterfaceDataSaver).setSecretCode(data?.substringAfter('-'))

//        Toast.makeText(activity, "Session: ${(activity as InterfaceDataSaver).getSession()}, " +
//                "SecretCode: ${(activity as InterfaceDataSaver).getSecretCode()}", Toast.LENGTH_LONG).show()
    }

    fun goToWeb(){
        Navigation.findNavController(mainView).navigate(R.id.action_QRScanner_to_webViewFragment)
    }

    fun checkCode(code : Int){
        if (code == CODE_OK)
            Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show()
        if (code == CODE_REDIRECT)
            goToWeb()
        if (code == CODE_ERROR)
            Toast.makeText(activity, "Try again", Toast.LENGTH_LONG).show()
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
        var CODE_REDIRECT = 300
        var CODE_OK = 200
        var CODE_ERROR = 400
    }

    override fun responseCallBack(responseCode: Int) {
        checkCode(responseCode)
    }
}