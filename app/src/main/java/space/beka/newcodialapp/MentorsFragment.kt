package space.beka.newcodialapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import space.beka.newcodialapp.databinding.FragmentMentorsBinding
import space.beka.newcodialapp.utils.MyObject
import space.beka.newcodialapp.utils.MyObject.courses
import space.beka.newcodialapp.utils.ReadMentors


class MentorsFragment : Fragment() {
    lateinit var binding: FragmentMentorsBinding
    private lateinit var showMentors: ReadMentors
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        loadData()
        showWindow()
        setOnClick()

        return binding.root
    }

    private fun setOnClick() {
        binding.lyAdd.setOnClickListener {
            findNavController().navigate(R.id.action_mentorsFragment_to_addMentorsFragment)
        }
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadData() {
        binding = FragmentMentorsBinding.inflate(layoutInflater)
        showMentors = ReadMentors(requireActivity(), binding)
    }

    private fun showWindow() {
        binding.tvCoursesName.text = courses.name
        showMentors.readMentors()
    }

}