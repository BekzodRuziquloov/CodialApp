package space.beka.newcodialapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import space.beka.newcodialapp.adapters.GroupsAdapter
import space.beka.newcodialapp.databinding.DialogDeleteBinding
import space.beka.newcodialapp.databinding.DialogEditGroupsBinding
import space.beka.newcodialapp.databinding.FragmentItem2Binding
import space.beka.newcodialapp.db.MyDBHelper
import space.beka.newcodialapp.models.Groups
import space.beka.newcodialapp.models.Mentors
import space.beka.newcodialapp.utils.MyObject
import space.beka.newcodialapp.utils.MyObject.courses
import space.beka.newcodialapp.utils.MyObject.mentors


class Item2Fragment : Fragment() {
    private lateinit var dialog: AlertDialog
    lateinit var myDBHelper: MyDBHelper
    lateinit var binding: FragmentItem2Binding
    lateinit var bindingDialog: DialogEditGroupsBinding
    lateinit var groupsAdapter: GroupsAdapter
    lateinit var bindingDelete: DialogDeleteBinding
    lateinit var dialogDelete: AlertDialog
    lateinit var arrayListMentors: ArrayList<Mentors>
    lateinit var arrayListMentorsString: ArrayList<String>
    lateinit var arrayListTime: ArrayList<String>
    lateinit var arrayListDay: ArrayList<String>
    lateinit var arrayAdapterTimes: ArrayAdapter<String>
    lateinit var arrayAdapterDays: ArrayAdapter<String>
    lateinit var arrayAdapterMentors: ArrayAdapter<String>
    var booleanAntiBag = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItem2Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadData()
        binding.recyclerViewGroups2.adapter = groupsAdapter
    }

    private fun loadData() {
        myDBHelper = MyDBHelper(requireActivity())
        var arrayListGroups = myDBHelper.readGroups("1", requireActivity())
        val arrayList = ArrayList<Groups>()
        for (i in arrayListGroups) {
            if (i.courses!!.id == courses.id) {
                arrayList.add(i)
            }
        }
        arrayListGroups = arrayList
        groupsAdapter = GroupsAdapter(requireActivity(),
            arrayListGroups,
            object : GroupsAdapter.RVClickGroups {
                override fun readGroup(groups: Groups) {
                    MyObject.groups = groups
                    findNavController().navigate(R.id.showGroupsFragment)
                }

                override fun editGroups(groups: Groups, position: Int) {
                    if (booleanAntiBag) {
                        loadDataDialog(groups)
                        buildDialog(groups)
                        booleanAntiBag = false
                    }
                }

                override fun deleteGroups(groups: Groups) {
                    if (booleanAntiBag) {
                        buildDialogDelete(groups)
                        booleanAntiBag = false
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun buildDialog(groups: Groups) {
        val alertDialog = AlertDialog.Builder(activity)
        bindingDialog.edtGroupsName.setText(groups.name)
        bindingDialog.spinnerMentors.setText("${groups.mentors!!.name} ${groups.mentors!!.surname}")
        bindingDialog.spinnerTimes.setText(groups.times)
        bindingDialog.spinnerDays.setText(groups.days)
        bindingDialog.spinnerTimes.setAdapter(arrayAdapterTimes)
        bindingDialog.spinnerDays.setAdapter(arrayAdapterDays)
        bindingDialog.spinnerMentors.setAdapter(arrayAdapterMentors)


        bindingDialog.spinnerMentors.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    mentors = arrayListMentors[position]
                }
            }

        bindingDialog.tvCancel.setOnClickListener {
            dialog.cancel()
        }

        bindingDialog.tvSave.setOnClickListener {
            if (bindingDialog.edtGroupsName.text.toString().trim()
                    .isNotEmpty() && bindingDialog.spinnerMentors.text.toString().trim()
                    .isNotEmpty() && bindingDialog.spinnerTimes.text.toString().trim().isNotEmpty()
                && bindingDialog.spinnerDays.text.toString().trim().isNotEmpty()
            ) {
                val groupID = groups.id
                val groupName = bindingDialog.edtGroupsName.text.toString().trim()
                val groupMentor = mentors
                val groupTime = bindingDialog.spinnerTimes.text.toString().trim()
                val groupDay = bindingDialog.spinnerDays.text.toString().trim()
                val groupCourses = courses
                val open = groups.open
                val groupsAdd = Groups(
                    groupID, groupName, groupMentor, groupTime, groupDay, groupCourses, open
                )

                myDBHelper.updateGroups(groupsAdd, requireActivity())
                onResume()

                dialog.cancel()
            }
        }

        alertDialog.setOnCancelListener {
            booleanAntiBag = true
        }

        alertDialog.setView(bindingDialog.root)
        dialog = alertDialog.create()
        dialog.window!!.attributes.windowAnimations = R.style.MyAnimation
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun loadDataDialog(groups: Groups) {
        bindingDialog = DialogEditGroupsBinding.inflate(layoutInflater)
        arrayListMentors = ArrayList()
        arrayListMentorsString = ArrayList()
        arrayListTime = ArrayList()
        arrayListDay = ArrayList()
        mentors = groups.mentors!!

        arrayListTime.add("10:00 - 12:00")
        arrayListTime.add("12:00 - 14:00")
        arrayListTime.add("14:00 - 16:00")
        arrayListTime.add("16:00 - 18:00")
        arrayListTime.add("18:00 - 20:00")
        arrayListDay.add("Monday/Wednesday/Friday")
        arrayListDay.add("Tuesday/Thursday/Saturday")

        arrayListMentors = myDBHelper.getAllMentorsByID(courses.id!!)
        for (i in 0 until arrayListMentors.size) {
            arrayListMentorsString.add("${arrayListMentors[i].name!!} ${arrayListMentors[i].surname!!}")
        }

        arrayAdapterTimes = ArrayAdapter(requireActivity(), R.layout.item_spinner, arrayListTime)
        arrayAdapterDays = ArrayAdapter(requireActivity(), R.layout.item_spinner, arrayListDay)
        arrayAdapterMentors =
            ArrayAdapter(requireActivity(), R.layout.item_spinner, arrayListMentorsString)
    }

    @SuppressLint("SetTextI18n")
    private fun buildDialogDelete(groups: Groups) {
        val alertDialog = AlertDialog.Builder(activity)
        bindingDelete = DialogDeleteBinding.inflate(layoutInflater)
        bindingDelete.tvDescription.text =
            "Do you want to delete this group? , if you delete a group, the students associated with it will also be deleted!"

        bindingDelete.tvCancel.setOnClickListener {
            dialogDelete.cancel()
        }

        bindingDelete.tvDelete.setOnClickListener {
            val boolean = myDBHelper.deleteGroups(groups)
            if (boolean) {
                Toast.makeText(requireActivity(), "Successfully Deleted!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireActivity(), "Failed to Delete", Toast.LENGTH_SHORT).show()
            }
            dialogDelete.cancel()
            onResume()
        }

        alertDialog.setOnCancelListener {
            booleanAntiBag = true
        }

        alertDialog.setView(bindingDelete.root)
        dialogDelete = alertDialog.create()
        dialogDelete.window!!.attributes.windowAnimations = R.style.MyAnimation
        dialogDelete.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogDelete.show()
    }
}