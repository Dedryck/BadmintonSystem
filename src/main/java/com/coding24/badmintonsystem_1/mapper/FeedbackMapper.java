package com.coding24.badmintonsystem_1.mapper;

import com.coding24.badmintonsystem_1.entity.Feedback;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FeedbackMapper {

    @Select("SELECT * FROM feedback WHERE FeedbackID = #{feedbackID}")
    Feedback findById(@Param("feedbackID") Integer feedbackID);

    @Select("SELECT * FROM feedback")
    List<Feedback> findAll();

    @Insert("INSERT INTO feedback (UserID, Username, Comments, Rating, FeedbackDate) VALUES (#{userID}, #{username}, #{comments}, #{rating}, #{feedbackDate})")
    @Options(useGeneratedKeys = true, keyProperty = "feedbackID", keyColumn = "FeedbackID")
    void insertFeedback(Feedback feedback);

    @Update("UPDATE feedback SET UserID = #{userID}, Username = #{username}, Comments = #{comments}, Rating = #{rating}, FeedbackDate = #{feedbackDate} WHERE FeedbackID = #{feedbackID}")
    void updateFeedback(Feedback feedback);

    @Delete("DELETE FROM feedback WHERE FeedbackID = #{feedbackID}")
    void deleteFeedback(@Param("feedbackID") Integer feedbackID);
}
