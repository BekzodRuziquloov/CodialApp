package space.beka.newcodialapp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import space.beka.newcodialapp.databinding.FragmentEditStudentBinding
import space.beka.newcodialapp.db.MyDBHelper
import space.beka.newcodialapp.models.Students
import space.beka.newcodialapp.utils.MyObject
import java.time.LocalDate

class EditStudentFragment : Fragment() {
    private lateinit var myDBHelper: MyDBHelper
    private lateinit var binding: FragmentEditStudentBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditStudentBinding.inflate(layoutInflater)
        loadData()

        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imageSave.setOnClickListener {
            if (MyObject.editStudents) {
                editStudents()
            } else {
                addStudents()
            }
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addStudents() {
        val name = binding.edtStudentName.text.toString().trim()
        val surname = binding.edtStudentSurname.text.toString().trim()
        val number = binding.edtStudentNumber.text.toString().trim()
        val date = LocalDate.now().toString()
        val students = Students(name, surname, number, date, MyObject.groups)

        if (name.isNotEmpty() && surname.isNotEmpty() && number.isNotEmpty() && date.isNotEmpty()) {
            myDBHelper.createStudents(students, requireActivity())
            binding.edtStudentName.text!!.clear()
            binding.edtStudentSurname.text!!.clear()
            binding.edtStudentNumber.text!!.clear()
        }
    }

    private fun editStudents() {
        val name = binding.edtStudentName.text.toString().trim()
        val surname = binding.edtStudentSurname.text.toString().trim()
        val number = binding.edtStudentNumber.text.toString().trim()
        val students =
            Students(
                MyObject.students.id,
                name,
                surname,
                number,
                MyObject.students.day,
                MyObject.groups
            )
        myDBHelper.updateStudents(students, requireActivity())
        findNavController().popBackStack()
    }

    private fun loadData() {
        binding = FragmentEditStudentBinding.inflate(layoutInflater)
        myDBHelper = MyDBHelper(requireActivity())
        if (MyObject.editStudents) {
            binding.tvAllCourses.text = "EDIT STUDENTS"
            binding.imageSave.setImageResource(R.drawable.ic_baseline_edit_24)
            binding.edtStudentName.setText(MyObject.students.name)
            binding.edtStudentSurname.setText(MyObject.students.surname)
            binding.edtStudentNumber.setText(MyObject.students.number)
        }

    }
}