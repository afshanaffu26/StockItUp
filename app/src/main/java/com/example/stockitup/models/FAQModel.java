package com.example.stockitup.models;

/**
 * Model file for FAQ
 */
public class FAQModel {
    private String question;
    private String answer;

    /**
     * Non - parameterized constructor
     */
    public FAQModel() {
    }

    /**
     * Constructor to initialize question, answer of FAQ
     * @param question FAQ question
     * @param answer FAQ answer
     */
    public FAQModel(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    /**
     * Getter method to get question of FAQ
     * @return question of FAQ
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Setter method to set the question for a FAQ
     * @param question the question to be set to a FAQ.
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Getter method to get question of FAQ
     * @return question of FAQ
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Setter method to set the answer for a FAQ
     * @param answer the answer to be set to a FAQ.
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }
}