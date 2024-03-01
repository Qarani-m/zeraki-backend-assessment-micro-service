package com.mkarani.courses;

import com.mkarani.courses.entity.CourseEntity;

import java.util.ArrayList;
import java.util.List;

public class Tester {

    private static void somethin(Object param){
        System.out.println(param.toString());


    }
    public static void main(String[] args) {
        List<CourseEntity> entities = new ArrayList<>();
        entities.add(
                new CourseEntity(3L, "EB3", "Comp",3)
        );
        somethin("A String");
        somethin(2L);
        somethin(entities);
    }
}
