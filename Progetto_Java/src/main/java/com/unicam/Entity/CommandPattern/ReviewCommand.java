package com.unicam.Entity.CommandPattern;

import com.unicam.Entity.BuilderPattern.ReviewBuilder;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Review;
import com.unicam.Entity.User;
import com.unicam.Service.Content.MediaService;
import com.unicam.Service.Content.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ReviewCommand implements Command {

    private Review review;
    private ReviewBuilder builder;
    private ReviewService reviewService;

    public ReviewCommand(String title, String description, User author, InterestPoint reference, List<MultipartFile> fileUploaded, ReviewService reviewService, MediaService mediaService) throws IOException {
        this.reviewService = reviewService;
        this.builder = new ReviewBuilder(mediaService);
        this.builder.buildTitle(title.toUpperCase(Locale.ROOT));
        this.builder.buildDescription(description);
        this.builder.buildReference(reference);
        this.builder.buildAuthor(author);
        this.builder.buildStatus(ContentStatus.APPROVED);
        this.builder.buildMunicipality(author.getVisitedMunicipality());
        if(fileUploaded != null){
            this.builder.buildFile(fileUploaded);
        }
        this.review = builder.result();
    }

    @Override
    public void execute() {
        this.reviewService.add(review);
    }
}
