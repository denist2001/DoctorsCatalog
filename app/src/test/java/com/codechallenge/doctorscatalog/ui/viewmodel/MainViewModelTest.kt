package com.codechallenge.doctorscatalog.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.codechallenge.doctorscatalog.data.model.presentation.Doctor
import com.codechallenge.doctorscatalog.domain.Repository
import com.codechallenge.doctorscatalog.ui.home.MainViewModel
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: Repository

    @Mock
    lateinit var observer: Observer<List<Doctor>>

    @Captor
    private lateinit var viewModelCaptor: ArgumentCaptor<List<Doctor>>

    lateinit var subject: MainViewModel
    lateinit var doctor1: Doctor
    lateinit var doctor2: Doctor
    lateinit var doctor3: Doctor
    lateinit var doctor4: Doctor

    @Before
    fun setUp() {
        subject = MainViewModel(repository)
        subject.visitedDoctorLiveData.observeForever(observer)
        doctor1 = Doctor("id1", "name1", "address1", "picture1", 1F)
        doctor2 = Doctor("id2", "name2", "address2", "picture2", 2F)
        doctor3 = Doctor("id3", "name3", "address3", "picture3", 3F)
        doctor4 = Doctor("id4", "name4", "address4", "picture4", 4F)
    }

    @Test
    fun `check if live data emit value after saving`() {
        subject.addLastVisitedDoctor(doctor1)
        viewModelCaptor.apply {
            verify(observer, only()).onChanged(capture())
            assertEquals(1, allValues.size)
            assertEquals(doctor1.id, allValues[0][0].id)
        }
    }

    @Test
    fun `check if live data emit value it the saving order`() {
        subject.addLastVisitedDoctor(doctor1)
        subject.addLastVisitedDoctor(doctor2)
        subject.addLastVisitedDoctor(doctor3)
        viewModelCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(3, allValues.size)
            assertEquals(3, value.size)
            assertEquals(doctor1.id, value[0].id)
            assertEquals(doctor2.id, value[1].id)
            assertEquals(doctor3.id, value[2].id)
        }
    }

    @Test
    fun `when comes 4th item should shift values`() {
        subject.addLastVisitedDoctor(doctor1)
        subject.addLastVisitedDoctor(doctor2)
        subject.addLastVisitedDoctor(doctor3)
        subject.addLastVisitedDoctor(doctor4)
        viewModelCaptor.apply {
            verify(observer, times(4)).onChanged(capture())
            assertEquals(4, allValues.size)
            assertEquals(3, value.size)
            assertEquals(doctor2.id, value[0].id)
            assertEquals(doctor3.id, value[1].id)
            assertEquals(doctor4.id, value[2].id)
        }
    }
}