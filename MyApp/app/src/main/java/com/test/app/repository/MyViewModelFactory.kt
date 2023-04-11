package com.test.app.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

import com.c1ctech.mvvmwithnetworksource.viewmodel.MainViewModel
import com.test.app.viewmodel.ChooseLocationViewModel
import com.test.app.viewmodel.QrCodeForFirebase
import com.test.app.viewmodel.QrCodeInfoGetViewModel
import com.test.app.viewmodel.ShowLocationDataViewModel

class MyViewModelFactory constructor(private val repository: MainRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
         if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
          return  MainViewModel(this.repository) as T
        }


        else if (modelClass.isAssignableFrom(ChooseLocationViewModel::class.java)) {
              return  ChooseLocationViewModel(this.repository) as T
            }
         else if (modelClass.isAssignableFrom(ShowLocationDataViewModel::class.java)) {
             return  ShowLocationDataViewModel(this.repository) as T
         }
         else if (modelClass.isAssignableFrom(QrCodeInfoGetViewModel::class.java)) {
             return  QrCodeInfoGetViewModel(this.repository) as T
         }
         else if (modelClass.isAssignableFrom(QrCodeForFirebase::class.java)) {
             return  QrCodeForFirebase(this.repository) as T
         }
             else
            {
               return  throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
