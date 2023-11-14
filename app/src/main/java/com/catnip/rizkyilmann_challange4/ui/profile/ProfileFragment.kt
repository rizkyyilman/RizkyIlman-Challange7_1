package com.catnip.rizkyilmann_challange4.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.catnip.rizkyilmann_challange4.R
import com.catnip.rizkyilmann_challange4.data.repository.UserRepositoryImpl
import com.catnip.rizkyilmann_challange4.databinding.FragmentProfileBinding
import com.catnip.rizkyilmann_challange4.network.auth.FirebaseAuthDataSourceImpl
import com.catnip.rizkyilmann_challange4.ui.loginactivity.LoginActivity
import com.catnip.rizkyilmann_challange4.utils.GenericViewModelFactory
import com.catnip.rizkyilmann_challange4.utils.proceedWhen
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }
    private fun createViewModel(): ProfileViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        return ProfileViewModel(repo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inisialisasi binding
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupForm() // Panggil setupForm() setelah binding diinisialisasi
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showUserData()
        // checkIfUserLogin()
        setClickListeners()
        observeData()
    }

    private fun checkIfUserLogin() {
        if (!viewModel.isUserLoggedIn()) {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        activity?.finish() // Optional, menutup aktivitas sebelumnya jika belum login
    }

    private fun setClickListeners() {
        binding.btnChangeProfile.setOnClickListener {
            if (checkNameValidation()) {
                changeProfileData()
            }
        }
        binding.tvChangePwd.setOnClickListener {
            requestChangePassword()
        }
        binding.tvLogout.setOnClickListener {
            doLogout()
        }
    }

    private fun requestChangePassword() {
        viewModel.createChangePwdRequest()
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage("Change password request sent to your email: ${viewModel.getCurrentUser()?.email} Please check your inbox or spam")
            .setPositiveButton("Okay") { dialog, id ->
                // Tindakan yang akan diambil saat tombol Okay ditekan
            }
            .create()
        dialog.show()
    }

    private fun doLogout() {
        val dialog = AlertDialog.Builder(requireContext()).setMessage("Do you want to logout ?")
            .setPositiveButton(
                "Yes"
            ) { dialog, id ->
                viewModel.doLogout()
                navigateToLogin()
            }
            .setNegativeButton(
                "No"
            ) { dialog, id ->
                // no-op , do nothing
            }.create()
        dialog.show()
    }

    private fun changeProfileData() {
        val fullName = binding.layoutForm.etName.text.toString().trim()
        viewModel.updateFullName(fullName)
    }

    private fun checkNameValidation(): Boolean {
        val fullName = binding.layoutForm.etName.text.toString().trim()
        return if (fullName.isEmpty()) {
            binding.layoutForm.tilName.isErrorEnabled = true
            binding.layoutForm.tilName.error = getString(R.string.text_error_name_cannot_empty)
            false
        } else {
            binding.layoutForm.tilName.isErrorEnabled = false
            true
        }
    }

    private fun observeData() {
        viewModel.changeProfileResult.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnChangeProfile.isVisible = true
                    Toast.makeText(requireContext(), "Change Profile data Success !", Toast.LENGTH_SHORT).show()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnChangeProfile.isVisible = true
                    Toast.makeText(requireContext(), "Change Profile data Failed !", Toast.LENGTH_SHORT).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnChangeProfile.isVisible = false
                }
            )
        }
    }

    private fun setupForm() {
        binding.layoutForm.tilName.isVisible = true
        binding.layoutForm.tilEmail.isVisible = true
        binding.layoutForm.etEmail.isEnabled = false
    }

    private fun showUserData() {
        viewModel.getCurrentUser()?.let {
            binding.layoutForm.etName.setText(it.fullName)
            binding.layoutForm.etEmail.setText(it.email)
            binding.ivProfilePict.load(it.photoUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_user)
                error(R.drawable.ic_user)
                transformations(CircleCropTransformation())
            }
        }
    }
}
