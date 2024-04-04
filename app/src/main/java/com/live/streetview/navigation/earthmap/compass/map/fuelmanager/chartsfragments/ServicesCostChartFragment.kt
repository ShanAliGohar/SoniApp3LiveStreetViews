package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.chartsfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartStackingType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.FragmentServicesCostChartBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.chart.ChartsActivity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.costs.CostDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.costs.CostEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDatabase
import kotlinx.coroutines.launch
import kotlin.math.cos


class ServicesCostChartFragment : Fragment() {

    private lateinit var binding: FragmentServicesCostChartBinding

    private lateinit var costDao: CostDao

    private var vehiclePosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServicesCostChartBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        vehiclePosition = ChartsActivity.getSelectedPosition()

        costDao = MileageLogDatabase.getDatabase(requireContext()).costDao()


        lifecycleScope.launch {
           costDao.getAllCosts().observe(viewLifecycleOwner){list ->

               val filteredList = mutableListOf<CostEntity>()

               VehicleDatabase.getDatabase(requireContext()).vehicleDao().getAllVehicleData()
                   .observe(viewLifecycleOwner) {

                       if (vehiclePosition == 0) {
                               setChart(list)
                               pieChart(list)
                       } else {

                           val selectedVehicleName = it[vehiclePosition!! -1].vehicleName
                           if (list != null) {
                               for (item in list) {
                                   if (item.car == selectedVehicleName) {
                                       filteredList.add(item)
                                   }
                               }
                           }

                           setChart(filteredList)
                           pieChart(filteredList)
                       }

                   }


           }


        }


    }


    private fun setChart(list: List<CostEntity>) {
        val categories = list.map { it.selectDate }.toTypedArray()

        val seriesData = list.map { costEntity ->
            val costValue = costEntity.totalCost.toInt()
            val serviceType = costEntity.service
            mapOf("name" to serviceType, "y" to costValue)
        }

        val aaChartModel: AAChartModel =
            AAChartModel().chartType(AAChartType.Bar).title("Services Costs")
                .subtitle("Services costs with date").stacking(AAChartStackingType.Normal)
                .dataLabelsEnabled(true).categories(categories).series(
                    arrayOf(
                        AASeriesElement().name("Service Types").data(seriesData.toTypedArray())
                    )
                )

        binding.anyChartView2.aa_drawChartWithChartModel(aaChartModel)
    }

    private fun pieChart(list: List<CostEntity>) {
        val serviceCostMap = HashMap<String, Int>()

        // Group costs by service type
        for (costEntity in list) {
            val serviceType = costEntity.service
            val cost = costEntity.totalCost.toInt()

            if (serviceCostMap.containsKey(serviceType)) {
                serviceCostMap[serviceType] = serviceCostMap[serviceType]!! + cost
            } else {
                serviceCostMap[serviceType] = cost
            }
        }

        // Create a list of maps for pie chart data
        val seriesData = serviceCostMap.map { (serviceType, cost) ->
            mapOf("name" to serviceType, "y" to cost)
        }

        val aaChartModel: AAChartModel =
            AAChartModel().chartType(AAChartType.Pie).title("Service Costs")
                .subtitle("Cost distribution by service type").dataLabelsEnabled(true).series(
                    arrayOf(
                        AASeriesElement().name("Costs")
                            .data(seriesData.toTypedArray()) // Spread the list elements
                    )
                )

        // The chart view object calls the instance object of AAChartModel and draws the final graphic
        binding.aaChartViewPie.aa_drawChartWithChartModel(aaChartModel)
    }



}