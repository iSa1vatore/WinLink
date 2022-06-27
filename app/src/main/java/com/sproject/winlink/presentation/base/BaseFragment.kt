package com.sproject.winlink.presentation.base

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>(
    layoutID: Int
) : Fragment(layoutID), SensorEventListener {

    private var _binding: VB? = null
    protected lateinit var vm: VM private set

    val binding get() = _binding!!

    private val type = (javaClass.genericSuperclass as ParameterizedType)

    @Suppress("UNCHECKED_CAST")
    private val classVB = type.actualTypeArguments[0] as Class<VB>

    @Suppress("UNCHECKED_CAST")
    private val classVM = type.actualTypeArguments[1] as Class<VM>

    private val inflateMethod = classVB.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )

    private var mSensorManager: SensorManager? = null

    private var mGyroscope: Sensor? = null
    private var mGyroscopeEnabled: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        @Suppress("UNCHECKED_CAST")
        _binding = inflateMethod.invoke(null, inflater, container, false) as VB
        vm = createViewModelLazy(classVM.kotlin, { viewModelStore }).value

        mSensorManager = requireContext()
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mGyroscope = mSensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        if (mGyroscopeEnabled) registerGyroscopeListener()
    }

    override fun onPause() {
        super.onPause()

        unregisterSensorsListener()
    }

    open fun onGyroscopeChanged(
        x: Float = 0f,
        y: Float = 0f,
        z: Float = 0f
    ) = Unit

    override fun onSensorChanged(event: SensorEvent) {
        val sensorName: String = event.sensor!!.stringType

        if (sensorName == Sensor.STRING_TYPE_GYROSCOPE) {
            onGyroscopeChanged(event.values[0], event.values[1], event.values[2])
        }
    }

    private fun registerGyroscopeListener() {
        mSensorManager?.registerListener(
            this,
            mGyroscope,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    private fun unregisterSensorsListener() {
        mSensorManager?.unregisterListener(this)
    }

    fun enableGyroscopeListener() {
        mGyroscopeEnabled = true

        registerGyroscopeListener()
    }

    fun disableGyroscopeListener() {
        mGyroscopeEnabled = false

        unregisterSensorsListener()
    }

    open fun setupViews() {}

    open fun setupListeners() {}

    open fun setupObservers() {}

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
