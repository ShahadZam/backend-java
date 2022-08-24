package com.example.rentaperson.service;

import com.example.rentaperson.dto.PostAppointment;
import com.example.rentaperson.dto.UserBody;
import com.example.rentaperson.model.Appointment;
import com.example.rentaperson.model.User;
import com.example.rentaperson.repository.AppointmentRepository;
import com.example.rentaperson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    private final SendEmailService service;




    public List<Appointment> getAll(){return appointmentRepository.findAll();}

    public void post(PostAppointment ap, Integer id){
        User person=userRepository.findUsersByUsername(ap.getUsername());
        Appointment appointment1=new Appointment(null,id,person.getId(),ap.getLocation(),ap.getHours(),person.getPricePerHour()*ap.getHours(),false);
        appointmentRepository.save(appointment1);
    }

    public List viewYourAppointments(User user){
        if(user.getRole().equals("PERSON")) {
            return appointmentRepository.findAppointmentByPersonId((user.getId()));
        }
        else{
            return formatList(appointmentRepository.findAppointmentByUserId((user.getId())));
        }
    }
    public void confirm(User person,Integer id){
        Appointment appointment= appointmentRepository.
                findAppointmentByPersonIdAndIdAndConfirm(person.getId(),id,false);
        User user=userRepository.getById(appointment.getUserId());


        appointment.setConfirm(true);
        String title="-Rent a person- Confirm your appointment";
        String body="Your Appointment with "+person.getUsername()+" confirmed\n" +
                "location: "+appointment.getLocation()+"\n" +
                "hours: "+appointment.getHours()+"\n" +
                "total: "+appointment.getTotal()+"\n Have fun!";

        service.sendEmail(user.getEmail(),title,body);
        appointmentRepository.save(appointment);
        //send an email?
    }


    public void updateApp(Appointment appointment) {
        Appointment newA=appointmentRepository.getById(appointment.getId());
        newA.setLocation(appointment.getLocation());
        newA.setConfirm(appointment.isConfirm());
        appointmentRepository.save(appointment);
    }

    public void deleteApp(Integer id) {
        Appointment appointment=appointmentRepository.getReferenceById(id);
        appointmentRepository.delete(appointment);
    }

    public List viewConfirm(User user){
        if(user.getRole().equals("PERSON")){
            return appointmentRepository.findAppointmentByPersonIdAndConfirm(user.getId(),true);
        }
        else{
            return formatList(appointmentRepository.findAppointmentByUserIdAndConfirm(user.getId(),true));
        }
    }

    public List<PostAppointment> formatList(List<Appointment> app) {
        List<PostAppointment> appBody =new ArrayList<>();
        User user;
        for (Appointment value : app) {
            user=userRepository.getById(value.getPersonId());
                appBody.add(new PostAppointment(user.getUsername(), value.getLocation(),value.getHours(), value.getTotal(),value.isConfirm()));
        }
        return appBody;
    }

}
