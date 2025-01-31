package com.jidasea.poliexplore

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.ar.core.*
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException
import com.google.ar.sceneform.ArSceneView
import java.io.IOException

class CameraFragment : Fragment() {
    private lateinit var arSceneView: ArSceneView
    private lateinit var session: Session
    private lateinit var augmentedImageDatabase: AugmentedImageDatabase
    private lateinit var fragmentTitleTextView: TextView

    private val requestMultiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.CAMERA] == true) {
            // Permiso de cámara concedido
            initializeARCore()
        } else {
            // Permiso de cámara denegado
            fragmentTitleTextView.text = "Permiso de cámara denegado. No se puede usar ARCore."
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(android.Manifest.permission.CAMERA)
        }

        if (permissionsToRequest.isNotEmpty())
            requestMultiplePermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        else initializeARCore()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        arSceneView = view.findViewById(R.id.ar_scene_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentTitleTextView = activity?.findViewById<TextView>(R.id.fragment_title)!!
        fragmentTitleTextView.text = "Cámara de Realidad Aumentada"

        if (ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permiso ya concedido, inicializar ARCore
            initializeARCore()
        } else {
            // Solicitar permiso de la cámara
            requestMultiplePermissionsLauncher.launch(arrayOf(android.Manifest.permission.CAMERA))
        }
    }

    private fun initializeARCore() {
        // Verificar si ARCore es compatible
        val availability = ArCoreApk.getInstance().checkAvailability(requireContext())
        if (availability.isSupported) {
            try {
                showCardView(null)
                // Crear una sesión de ARCore
                session = Session(requireContext())

                // Crear la base de datos de imágenes aumentadas
                augmentedImageDatabase = AugmentedImageDatabase(session)

                // Agregar imágenes a la base de datos
                try {
                    val inputStream = requireContext().assets.open("edificio.jpg")
                    val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                    augmentedImageDatabase.addImage("edificio", bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                // Configurar ARCore para usar la base de datos de imágenes
                val config = Config(session)
                config.augmentedImageDatabase = augmentedImageDatabase
                config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE // Set the correct UpdateMode
                session.configure(config)

                // Configurar el ArSceneView
                arSceneView.setupSession(session)

                // Escuchar actualizaciones de ARCore
                arSceneView.scene.addOnUpdateListener {
                    val frame = arSceneView.arFrame
                    val updatedAugmentedImages = frame?.getUpdatedTrackables(AugmentedImage::class.java)

                    updatedAugmentedImages?.forEach { augmentedImage ->
                        when (augmentedImage.trackingState) {
                            TrackingState.TRACKING -> {
                                if (augmentedImage.name == "edificio") {
                                    // La imagen "edificio" fue detectada
                                    showCardView(null)
                                }
                            }
                            else -> {
                                // La imagen ya no se detecta
                                hideCardView()
                            }
                        }
                    }
                }
            } catch (e: UnavailableArcoreNotInstalledException) {
                // ARCore no está instalado, mostrar un mensaje al usuario
                fragmentTitleTextView.text = "ARCore no está instalado. Por favor, instálalo desde Google Play Store."
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            // Mostrar un mensaje de error si ARCore no es compatible
            fragmentTitleTextView.text = "ARCore no es compatible con este dispositivo"
        }
    }

    private fun showCardView(pose: Pose?) {
        // Mostrar un CardView con información del edificio
        val cardView = layoutInflater.inflate(R.layout.cardview_edificio, null)
        val navigateButton = cardView.findViewById<Button>(R.id.navigateButton)
        cardView.id = R.id.cardViewEdificio // Asegúrate de que el ID coincide con el del diseño

        // Configurar los LayoutParams para centrar el CardView
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }

        navigateButton.setOnClickListener {
            val fragment = BuildingDetailsFragment.newInstance(21)
        }

        // Agregar el CardView al FrameLayout
        (requireActivity().findViewById<ViewGroup>(android.R.id.content) as FrameLayout).addView(cardView, layoutParams)
        cardView.visibility = View.VISIBLE // Asegúrate de que la visibilidad está configurada a VISIBLE
    }

    private fun hideCardView() {
        // Ocultar el CardView
        (requireActivity().findViewById<ViewGroup>(android.R.id.content) as FrameLayout).removeAllViews()
    }

    override fun onResume() {
        super.onResume()
        if (ArCoreApk.getInstance().checkAvailability(requireContext()).isSupported) {
            arSceneView.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        arSceneView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        arSceneView.destroy()
    }
}