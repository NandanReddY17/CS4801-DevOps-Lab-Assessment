
package com.university;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {

    @Test
    void shouldRegisterStudentForValidCourse() {
        Student student = new Student("S101", "Anita");
        Course course = new Course("CS301", "Data Structures", 4, 30, true, null);
        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));
        assertTrue(student.isRegistered("CS301"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectInactiveCourse() {
        Student student = new Student("S101", "Anita");
        Course course = new Course("CS301", "Data Structures", 4, 30, false, null);
        RegistrationService service = new RegistrationService();

        assertFalse(service.register(student, course));
        assertFalse(student.isRegistered("CS301"));
    }

    @Test
    void shouldRejectMissingPrerequisite() {
        Student student = new Student("S101", "Anita");
        Course course = new Course("CS401", "Advanced Algorithms", 4, 30, true, "CS301");
        RegistrationService service = new RegistrationService();

        assertFalse(service.register(student, course));
        assertFalse(student.isRegistered("CS401"));
    }

    @Test
    void shouldAllowRegistrationWhenPrerequisiteIsCompleted() {
        Student student = new Student("S101", "Anita");
        student.addCompletedPrerequisite("CS301");
        Course course = new Course("CS401", "Advanced Algorithms", 4, 30, true, "CS301");
        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));
        assertTrue(student.isRegistered("CS401"));
    }

    @Test
    void shouldRejectDuplicateRegistration() {
        Student student = new Student("S101", "Anita");
        Course course = new Course("CS301", "Data Structures", 4, 30, true, null);
        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));
        assertFalse(service.register(student, course));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectRegistrationWhenCreditLimitIsExceeded() {
        Student student = new Student("S101", "Anita");

        student.addRegistration("CS101", 4);
        student.addRegistration("CS102", 4);
        student.addRegistration("CS201", 4);
        student.addRegistration("CS202", 4);
        student.addRegistration("CS203", 4);

        Course course = new Course("CS301", "Data Structures", 4, 30, true, null);
        RegistrationService service = new RegistrationService();

        assertFalse(service.register(student, course));
        assertFalse(student.isRegistered("CS301"));
    }

    @Test
    void shouldAcceptCoursesWithOneAndSixCredits() {
        RegistrationService service = new RegistrationService();

        Student student1 = new Student("S201", "Ravi");
        Course oneCredit = new Course("CS101", "Course One", 1, 10, true, null);

        assertTrue(service.register(student1, oneCredit));
        assertEquals(1, student1.getTotalCredits());

        Student student2 = new Student("S202", "Priya");
        Course sixCredits = new Course("CS106", "Course Six", 6, 10, true, null);

        assertTrue(service.register(student2, sixCredits));
        assertEquals(6, student2.getTotalCredits());
    }

    @Test
    void shouldRejectInvalidCourseCreditsWithoutChangingState() {
        int[] invalidCredits = {0, 7, -1};

        for (int credits : invalidCredits) {
            Student student = new Student("S301", "Anita");
            String courseCode = "CS" + (credits < 0 ? "NEG" : credits);
            Course course = new Course(
                    courseCode, "Invalid Credit Course", credits, 10, true, null);

            RegistrationService service = new RegistrationService();

            assertFalse(service.register(student, course));
            assertFalse(student.isRegistered(courseCode));
            assertEquals(0, student.getTotalCredits());
            assertEquals(0, course.getCurrentEnrollment());
        }
    }

    @Test
    void shouldRejectFullCourseWithoutChangingState() {
        Student student = new Student("S401", "Kiran");
        Course course = new Course("CS401", "Full Course", 3, 1, true, null);
        course.incrementEnrollment();

        RegistrationService service = new RegistrationService();

        assertFalse(service.register(student, course));
        assertFalse(student.isRegistered("CS401"));
        assertEquals(0, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldAllowRegistrationWithOneSeatRemaining() {
        Student student = new Student("S402", "Meena");
        Course course = new Course("CS402", "Almost Full", 3, 2, true, null);
        course.incrementEnrollment();

        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));
        assertTrue(student.isRegistered("CS402"));
        assertEquals(3, student.getTotalCredits());
        assertEquals(2, course.getCurrentEnrollment());
    }
}
