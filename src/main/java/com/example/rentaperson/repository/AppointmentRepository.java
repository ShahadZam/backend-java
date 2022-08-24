package com.example.rentaperson.repository;

import com.example.rentaperson.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {

    List<Appointment> findAppointmentByPersonId(Integer id);

    List<Appointment> findAppointmentByPersonIdAndConfirm(Integer id,boolean c);

    Appointment findAppointmentByUserIdAndPersonIdAndConfirm(Integer Uid,Integer Pid,boolean c);


    List<Appointment> findAppointmentByUserIdAndConfirm(Integer id,boolean c);


    List<Appointment> findAppointmentByUserId(Integer id);

    Appointment findAppointmentByPersonIdAndIdAndConfirm(Integer pid,Integer id,boolean c);

    Appointment findAppointmentById(Integer id);


}
