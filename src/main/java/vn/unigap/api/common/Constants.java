package vn.unigap.api.common;

public class Constants {
    // Email validation messages
    public static final String VALIDATE_EMAIL_NOT_EMPTY = "Email is required";
    public static final String VALIDATE_EMAIL_INVALID = "Email is invalid";
    public static final String VALIDATE_EMAIL_SIZE = "Email must be between 3 and 255 characters";

    // Name validation messages
    public static final String VALIDATE_NAME_NOT_EMPTY = "Name is required";
    public static final String VALIDATE_NAME_SIZE = "Name must be between 3 and 255 characters";
    public static final String VALIDATE_NAME_INVALID = "Name is invalid";

    // Province validation messages
    public static final String VALIDATE_PROVINCE_NOT_EMPTY = "Province is required";
    public static final String VALIDATE_PROVINCE_INVALID = "Province is invalid";

    // Description validation messages
    public static final String VALIDATE_DESCRIPTION_SIZE = "Description must be between 3 and 500 characters";
    public static final String VALIDATE_DESCRIPTION_INVALID = "Description is invalid";

    // Title validation messages
    public static final String VALIDATE_TITLE_NOT_EMPTY = "Title is required";
    public static final String VALIDATE_TITLE_SIZE = "Title must be between 3 and 255 characters";

    // Quantity validation messages
    public static final String VALIDATE_QUANTITY_NOT_EMPTY = "Quantity is required";
    public static final String VALIDATE_QUANTITY_POSITIVE = "Quantity must be positive";

    // Salary validation messages
    public static final String VALIDATE_SALARY_NOT_EMPTY = "Salary is required";
    public static final String VALIDATE_SALARY_POSITIVE = "Salary must be positive";

    // Date validation messages
    public static final String VALIDATE_EXPIRED_AT_NOT_EMPTY = "Expiration date is required";
    public static final String VALIDATE_EXPIRED_AT_FUTURE = "Expiration date must be in the future";
    public static final String VALIDATE_BIRTHDAY_INVALID = "Birthday is invalid";

    // Address validation messages
    public static final String VALIDATE_ADDRESS_NOT_EMPTY = "Address is required";
    public static final String VALIDATE_ADDRESS_SIZE = "Address must be between 3 and 500 characters";

    // Career objective validation messages
    public static final String VALIDATE_CAREER_OBJ_NOT_EMPTY = "Career objective is required";
    public static final String VALIDATE_CAREER_OBJ_SIZE = "Career objective must be between 3 and 500 characters";

    // Field validation messages
    public static final String VALIDATE_FIELDS_NOT_EMPTY = "At least one field is required";
    public static final String VALIDATE_PROVINCES_NOT_EMPTY = "At least one province is required";

    // Resource messages
    public static final String EMPLOYER_NOT_FOUND = "Employer not found with id: ";
    public static final String JOB_NOT_FOUND = "Job not found with id: ";
    public static final String SEEKER_NOT_FOUND = "Seeker not found with id: ";
    public static final String RESUME_NOT_FOUND = "Resume not found with id: ";
    public static final String JOB_FIELD_NOT_FOUND = "Job field not found with id: ";
    public static final String JOB_PROVINCE_NOT_FOUND = "Job province not found with id: ";
    public static final String JOB_APPLICATION_NOT_FOUND = "Job application not found with id: ";

    // Duplicate resource messages
    public static final String DUPLICATE_EMAIL = "Email already exists: ";
    public static final String DUPLICATE_APPLICATION = "You have already applied for this job";

    private Constants() {
        // Private constructor to prevent instantiation
    }
}
