package space.beka.newcodialapp.utils

import android.app.Activity
import androidx.navigation.NavController
import space.beka.newcodialapp.adapters.CoursesAdapter
import space.beka.newcodialapp.databinding.FragmentCourseBinding
import space.beka.newcodialapp.db.MyDBHelper
import space.beka.newcodialapp.models.Courses


class ReadCourses(
    private val activity: Activity,
    private val binding: FragmentCourseBinding,
    val findNavController: NavController
) {
    private lateinit var arrayListCourses: ArrayList<Courses>
    private lateinit var coursesAdapter: CoursesAdapter
    private lateinit var myDBHelper: MyDBHelper

    fun readCourses() {
        loadData()
        coursesAdapter = CoursesAdapter(arrayListCourses, object : CoursesAdapter.RVClickCourses {
            override fun onClick(courses: Courses) {
                MyObject.courses = courses
                findNavController.navigate(MyObject.navigationID)
            }
        })
        binding.recyclerCourses.adapter = coursesAdapter
    }

    private fun loadData() {
        arrayListCourses = ArrayList()
        myDBHelper = MyDBHelper(activity)
        arrayListCourses = myDBHelper.readCourses()
    }
}