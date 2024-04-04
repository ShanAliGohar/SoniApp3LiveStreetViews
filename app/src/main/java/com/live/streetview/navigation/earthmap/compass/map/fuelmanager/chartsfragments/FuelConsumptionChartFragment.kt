package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.chartsfragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.live.streetview.navigation.earthmap.compass.map.databinding.FragmentFuelConsumptionChartBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.chart.ChartsActivity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@RequiresApi(Build.VERSION_CODES.O)
class FuelConsumptionChartFragment : Fragment() {

    private lateinit var binding: FragmentFuelConsumptionChartBinding
    private lateinit var mileageLogDao: MileageLogDao

    private var vehiclePosition: Int? = null

    //  val data: MutableLiveData<List<ValueDataEntry>> = MutableLiveData<List<ValueDataEntry>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFuelConsumptionChartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mileageLogDao = MileageLogDatabase.getDatabase(requireContext()).mileageDao()


        vehiclePosition = ChartsActivity.getSelectedPosition()


        fuelPriceChart()


    }

    private fun fuelPriceChart() {
        // val dateFormatter = DateTimeFormatter.ofPattern("D MMMM, yyyy", Locale.ENGLISH)
        //-------------------------------
        lifecycleScope.launch {
            val list = withContext(Dispatchers.IO) { mileageLogDao.getAllMileageLogData2() }
            Log.d("TAG", "fuelPriceChart4545: " + list.size)

            val filteredList = mutableListOf<MileageLogEntity>()

            VehicleDatabase.getDatabase(requireContext()).vehicleDao().getAllVehicleData()
                .observe(viewLifecycleOwner) {

                    if (vehiclePosition == 0) {
                        setChart(list)
                        pieChart(list)
                    } else {
                        val selectedVehicleName = it[vehiclePosition!! -1].id

                        for (item in list) {
                            if (item.carId == selectedVehicleName) {
                                filteredList.add(item)
                            }
                        }

                        setChart(filteredList)
                        pieChart(filteredList)

                    }
                }

        }

    }

    private fun setChart(list: List<MileageLogEntity>) {

        val array = ArrayList<Int>()
        val categories = ArrayList<String>()

        for (i in list.indices) {
            array.add(list[i].price)
            categories.add(list[i].date)
        }


        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .title("Fuel Prices")
            .subtitle("Fuel price with filling date")
            .dataLabelsEnabled(true)
            .categories(categories.toTypedArray())
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Price")
                        .data(array.toTypedArray())
                )
            )
        //The chart view object calls the instance object of AAChartModel and draws the final graphic
        binding.aaChartView.aa_drawChartWithChartModel(aaChartModel)

    }


    private fun pieChart(list: List<MileageLogEntity>) {


        val array = ArrayList<Int>()
        val categories = ArrayList<String>()

        for (i in list.indices) {
            array.add(list[i].price)
            categories.add(list[i].date)
        }


        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Pie)
            .title("Fuel Prices")
            .subtitle("Fuel prices with filling date and cost")
            .dataLabelsEnabled(true)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Filling Data")
                        .innerSize("20%") // Adjust the innerSize as per your preference
                        .data(
                            // Create data array for the pie chart
                            categories.zip(array).map { (date, cost) ->
                                arrayOf(date, cost)
                            }.toTypedArray()
                        )
                )
            )

// The chart view object calls the instance object of AAChartModel and draws the final graphic
        binding.aaChartViewPie.aa_drawChartWithChartModel(aaChartModel)

    }

}