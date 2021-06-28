package com.dicoding.githubuser.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.githubuser.alarm.AlarmReceiver
import com.dicoding.githubuser.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    companion object {
        const val PREFS_NAME = "setting_pref"
        private const val REMINDER = "reminder"
    }

    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var _binding: FragmentSettingBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alarmReceiver = AlarmReceiver()
        mSharedPreferences =
            requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        binding.swReminder.isChecked = mSharedPreferences.getBoolean(REMINDER, false)
        binding.swReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                save(isChecked)
                alarmReceiver.setReminderAlarm(
                    requireActivity(),
                    AlarmReceiver.TYPE_REPEATING,
                    "09:00",
                    "Let's see your favorite user!"
                )
            } else {
                alarmReceiver.cancelReminderAlarm(requireActivity())
            }
        }
    }

    private fun save(value: Boolean) {
        val sp = mSharedPreferences.edit()
        sp.putBoolean(REMINDER, value)
        sp.apply()
    }
}