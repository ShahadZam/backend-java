package com.example.rentaperson.service;


import com.example.rentaperson.dto.Rate;
import com.example.rentaperson.dto.UserBody;
import com.example.rentaperson.model.Appointment;
import com.example.rentaperson.model.Review;
import com.example.rentaperson.model.User;
import com.example.rentaperson.repository.AppointmentRepository;
import com.example.rentaperson.repository.ReviewRepository;
import com.example.rentaperson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public List<Review> getAllReview(){return reviewRepository.findAll();}

    public List<Review> findReviewByUserId(Integer id){return reviewRepository.findReviewByUserId(id);}

    public List<Review> findReviewByPersonId(Integer id){return reviewRepository.findReviewByPersonId(id);}

    public List<Review> findReviewByRate(Integer rate){return reviewRepository.findReviewByRate(rate);}

    public boolean postReview(User user, String username, Review review){
        User person=userRepository.findUsersByUsername(username);
        if(person==null)
            return false;
        Appointment appointment=appointmentRepository.
                findAppointmentByUserIdAndPersonIdAndConfirm(user.getId(),person.getId(),true);
        if(appointment==null)
            return false;
        review.setUserId(user.getId());
        review.setPersonId(person.getId());
        reviewRepository.save(review);
        return true;
    }

    public void updateReview(Review review) {
        Review review1=reviewRepository.getById(review.getId());
        review1.setMessage(review.getMessage());
        review1.setRate(review.getRate());
        reviewRepository.save(review1);
    }

    public void deleteReview(Integer id) {
        Review review=reviewRepository.getById(id);
        reviewRepository.delete(review);
    }

    public List<Rate> getPersonReviews(String username) {
        User person=userRepository.findUsersByUsername(username);
        return formatList(reviewRepository.findReviewByPersonId(person.getId()));
    }

    public String ave(String username){
        List<Rate> rates=getPersonReviews(username);
        Integer count=rates.size();
        if(count==0)
            return "There is no reviews for"+ username;
        Double ave= Double.valueOf(0);
        for (int i = 0; i <rates.size() ; i++) {
            ave=ave+rates.get(i).getRate();
        }
        ave=ave/count;
        return "average rate of "+username+": "+ave;
    }

    public List<Rate> formatList(List<Review> reviews) {
        List<Rate> rate =new ArrayList<>();
        for (Review review : reviews) {
            rate.add(new Rate(review.getMessage(), review.getRate()));
        }
        return rate;
    }
}
