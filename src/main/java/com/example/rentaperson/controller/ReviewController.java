package com.example.rentaperson.controller;


import com.example.rentaperson.dto.ApiResponse;
import com.example.rentaperson.dto.Rate;
import com.example.rentaperson.model.Review;
import com.example.rentaperson.model.Skill;
import com.example.rentaperson.model.User;
import com.example.rentaperson.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Text;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/viewAll")
    public ResponseEntity<List> getReviews(){
        List<Review> reviews=reviewService.getAllReview();
        return ResponseEntity.status(200).body(reviews);
    }

    @PostMapping("/addReview/{username}")
    public ResponseEntity<ApiResponse> addReview(@PathVariable String username,@RequestBody @Valid Review review,@AuthenticationPrincipal User user){
        if(reviewService.postReview(user,username,review))
        return ResponseEntity.status(201).body(new ApiResponse("review added !",201));
        else
            return ResponseEntity.status(400).body(new ApiResponse("Try the service before you judge !",400));

    }

    @PutMapping("/update")
    public ResponseEntity updateReview(@RequestBody Review review){
        reviewService.updateReview(review);
        return ResponseEntity.status(200).body("Review update");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteReview( @PathVariable Integer id){
        reviewService.deleteReview(id);
        return ResponseEntity.status(201).body(new ApiResponse("Review deleted !",201));
    }

    @GetMapping("/reviewByPerson/{username}")
    public ResponseEntity<List> getPersonReviews(@PathVariable String username){
        List<Rate> reviews=reviewService.getPersonReviews(username);
        return ResponseEntity.status(200).body(reviews);
    }

    @GetMapping("/aveOfPerson/{username}")
    public ResponseEntity<String> getPersonAve(@PathVariable String username){
        return ResponseEntity.status(200).body(reviewService.ave(username));
    }


}
