package com.university;

public class RegistrationService {

    private static final int MAX_CREDITS = 20;

    public boolean register(Student student, Course course) {

        // Reject inactive courses
        if (!course.isActive()) {
            return false;
        }

        // Only courses with 1 to 6 credits are allowed
        int credits = course.getCredits();
        if (credits < 1 || credits > 6) {
            return false;
        }

        // Reject registration when the course is full
        if (course.getCurrentEnrollment() >= course.getCapacity()) {
            return false;
        }

        // Reject duplicate registration
        if (student.isRegistered(course.getCourseCode())) {
            return false;
        }

        // Validate prerequisite
        String prerequisite = course.getPrerequisite();
        if (prerequisite != null
                && !prerequisite.isBlank()
                && !student.hasCompletedPrerequisite(prerequisite)) {
            return false;
        }

        // Validate student's total credit limit
        if (student.getTotalCredits() + credits > MAX_CREDITS) {
            return false;
        }

        // Update state only after every validation succeeds
        student.addRegistration(course.getCourseCode(), credits);
        course.incrementEnrollment();

        return true;
    }
}