package com.jidasea.poliexplore.ui.camera
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.ar.sceneform.ux.ArFragment
import com.jidasea.poliexplore.databinding.FragmentCameraBinding

class CameraFragment: Fragment() {
    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var arFragment: ArFragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cameraViewModel =
            ViewModelProvider(this).get(CameraViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_camera, container, false)
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar el fragmento de AR
        arFragment = childFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment

        // Aquí puedes añadir tus configuraciones y funcionalidades de ARCore
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
        }
    }
}