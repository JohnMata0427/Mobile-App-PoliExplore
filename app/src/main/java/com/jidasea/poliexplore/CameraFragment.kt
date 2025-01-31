package com.jidasea.poliexplore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.ar.core.*
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException
import com.google.ar.sceneform.ArSceneView
import java.io.IOException

class CameraFragment : Fragment() {
    private lateinit var arSceneView: ArSceneView
    private lateinit var session: Session
    private lateinit var augmentedImageDatabase: AugmentedImageDatabase

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
        val fragmentTitleTextView: TextView? = activity?.findViewById(R.id.fragment_title)
        fragmentTitleTextView?.text = "Cámara de Realidad Aumentada"

        // Inicializar ARCore
        initializeARCore(fragmentTitleTextView)
    }

    private fun initializeARCore(fragmentTitleTextView: TextView?) {
        // Verificar si ARCore es compatible
        val availability = ArCoreApk.getInstance().checkAvailability(requireContext())
        if (availability.isSupported) {
            try {
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
                                    showCardView(augmentedImage.centerPose)
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
                fragmentTitleTextView?.text = "ARCore no está instalado. Por favor, instálalo desde Google Play Store."
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            // Mostrar un mensaje de error si ARCore no es compatible
            fragmentTitleTextView?.text = "ARCore no es compatible con este dispositivo"
        }
    }

    private fun showCardView(pose: Pose) {
        // Mostrar un CardView con información del edificio
        val cardView = layoutInflater.inflate(R.layout.cardview_edificio, null) as androidx.cardview.widget.CardView
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        requireActivity().addContentView(cardView, layoutParams)
    }

    private fun hideCardView() {
        // Ocultar el CardView
        requireActivity().findViewById<androidx.cardview.widget.CardView>(R.id.cardViewEdificio)?.visibility = View.GONE
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